package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.AggregateItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeClickingListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.LayoutVisualizator;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers.BioVisualizerEdgeRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;

/**
 * Class that handles the node clicks.<br>
 * Left click will provide information on the nodes, and right
 * click will show a menu with fix/unfix and merge/split operations 
 * for the graph nodes. The changes in the graph will also change the 
 * layout container.
 * 
 * @author Noronha
 *
 */
public class BioVisualizerNodeInfoClickControl extends ControlAdapter{
	
	public Set<NodeClickingListener> listeners;
	private LayoutVisualizator lViz;
	private Graph g;
	private Display d;
	private Visualization vis;
	
	private VisualItem nodeVisualDummy;
	public Node nodeSourceDummy;
	public Edge edgeDummy;
	private boolean creatingEdge = false;

//	private VisualItem clickedItem;
	
	private Point2D mousePosition = new Point2D.Double();
	private int tempx;
	private int tempy;
	
	public BioVisualizerNodeInfoClickControl(LayoutVisualizator lviz){
		super();
		this.listeners = new HashSet<NodeClickingListener>();
		this.lViz = lviz;
		this.vis = lviz.getVis();
		this.g = (Graph) vis.getSourceData("graph");
		this.d = vis.getDisplay(0);
		
		createDummy();
//		removeNodeDummy();
	}
	
	public void reset(){
		super.setEnabled(false);
		removeAllDummies();
		removeAllListeners();
		this.lViz = null;
		this.vis = null;
		this.g = null;
		this.d = null;
		
		
	}
	
	public void addNodeClickListener(NodeClickingListener listener){
		this.listeners.add(listener);
	}
	
	public void notifyNodeClicked(String id){
		for(NodeClickingListener l : listeners) l.nodeClicked(id);
	}
	
	public void notifyReactionClicked(String id){
		for(NodeClickingListener l : listeners) l.reactionClicked(id);
	}
	
	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		
		if(SwingUtilities.isRightMouseButton(e)){

			if (creatingEdge) {
				stopEdgeCreation();
				return;
			}else
				handleRightClick(item, e);
		}
		else if(SwingUtilities.isLeftMouseButton(e)){

			if (creatingEdge) {

				if(item instanceof NodeItem){
					
					Node n = (Node)item.getSourceTuple();
					String nodeType = n.getString(LayoutUtils.TYPE);
					String sourceType = edgeDummy.getSourceNode().getString(LayoutUtils.TYPE);
					
					if(!(nodeType.equals(LayoutUtils.REA_T) || nodeType.equals(LayoutUtils.INFO))){

						Node sourceNode = edgeDummy.getSourceNode();
						Node targetNode = (Node)item.getSourceTuple();
						VisualItem sourceNodeVI = vis.getVisualItem(LayoutUtils.NODES, sourceNode);
						Double x = (sourceNodeVI.getX() + item.getX()) / 2;
						Double y = (sourceNodeVI.getY() + item.getY()) / 2;
						stopEdgeCreation();
						if(validForMerge(sourceNode, targetNode))
							lViz.mergeNodes(sourceNode, targetNode, x , y );
					}
					else if(sourceType.equals(LayoutUtils.REA_T) && nodeType.equals(LayoutUtils.REA_T)){
						
						Node sourceNode = edgeDummy.getSourceNode();
						Node targetNode = (Node)item.getSourceTuple();
						VisualItem sourceNodeVI = vis.getVisualItem(LayoutUtils.NODES, sourceNode);
						Double x = (sourceNodeVI.getX() + item.getX()) / 2;
						Double y = (sourceNodeVI.getY() + item.getY()) / 2;
						stopEdgeCreation();
						if(validReactionForMerge(sourceNode, targetNode))
							lViz.mergeReactions(sourceNode, targetNode, x , y );
					}
					else{
						stopEdgeCreation();
					}
				}
				else
					stopEdgeCreation();


			} else if (item instanceof NodeItem) {

				Node n = (Node)item.getSourceTuple();

				String nodeType = n.getString(LayoutUtils.TYPE);
				if(nodeType.equalsIgnoreCase(LayoutUtils.REA_T)){
					String id = n.getString(LayoutUtils.ID);
					notifyReactionClicked(id);
				}
				else if(nodeType.equalsIgnoreCase(LayoutUtils.MET_T) || nodeType.equalsIgnoreCase(LayoutUtils.HUBS)){
					String id = n.getString(LayoutUtils.ID);
					notifyNodeClicked(id);
				}
			}
			
		}
	} 
	
	private boolean validReactionForMerge(Node sourceNode, Node targetNode) {
		
		IReactionLay source = lViz.getReactions().get(sourceNode.getString(LayoutUtils.ID));
		IReactionLay target = lViz.getReactions().get(targetNode.getString(LayoutUtils.ID));
		
		if(source.getProducts().size()!=target.getProducts().size()) return false;
		if(source.getReactants().size()!=target.getReactants().size()) return false;
		for(String s : source.getProducts().keySet()){
			if(!target.getProducts().containsKey(s)) return false;
		}
		for(String s : source.getReactants().keySet()){
			if(!target.getReactants().containsKey(s)) return false;
		}
		
		return true;
		
	}

	/**
	 * Function that handles the right click of a node.
	 * @param visualItem - item clicked
	 * @param e - mouse event
	 */
	private void handleRightClick(final VisualItem visualItem, final MouseEvent e)
	{
		if (visualItem instanceof prefuse.visual.tuple.TableNodeItem)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()

				{
					JPopupMenu menu = getRightClickMenu(visualItem);
					menu.setInvoker(e.getComponent());
					menu.setLocation(sumPoints(e.getComponent().getLocationOnScreen(), e.getPoint()));
					menu.setEnabled(true);
					menu.setVisible(true);
				}
			});
		}
	}
	
	/**
	 * Builds the right click pop-up menu.
	 * Each menu item is built in a separate function.
	 * @param visualItem
	 * @return pop-up menu
	 */
	private JPopupMenu getRightClickMenu(VisualItem visualItem)
	{
		JPopupMenu rightClickPopupMenu = new JPopupMenu();

		rightClickPopupMenu.add(buildFixUnfixMenu(visualItem));
		rightClickPopupMenu.add(new JSeparator());
		rightClickPopupMenu.add(buildFixTypeMenu(visualItem.getString(LayoutUtils.TYPE)));
		rightClickPopupMenu.add(buildUnfixTypeMenu(visualItem.getString(LayoutUtils.TYPE)));
		
		boolean hasConnections = ((Node) visualItem.getSourceTuple()).getDegree() > 1;
		String type = visualItem.getString(LayoutUtils.TYPE);

		boolean isValidNode = !type.equals(LayoutUtils.INFO) && !type.equals(LayoutUtils.REA_T);
		
		
		if(hasConnections && isValidNode){
			rightClickPopupMenu.add(new JSeparator());
			rightClickPopupMenu.add(replicateNodeMenu((Node) visualItem.getSourceTuple()));
		}
		else if(type.equals(LayoutUtils.REA_T)){
		
			String rId = ((Node) visualItem.getSourceTuple()).getString(LayoutUtils.ID);
			IReactionLay r = lViz.getLayoutContainer().getReactions().get(rId);
			
			rightClickPopupMenu.add(new JSeparator());
			rightClickPopupMenu.add(mergeReactionMenu(visualItem));
			if(r.getIDs().size()>1){
				
				rightClickPopupMenu.add(unfoldReactionNodeMenu((Node) visualItem.getSourceTuple()));
			}
			rightClickPopupMenu.add(replicateReactionNodeMenu((Node) visualItem.getSourceTuple()));
			rightClickPopupMenu.add(new JSeparator());
			rightClickPopupMenu.add(removeReactionNodeMenu((Node) visualItem.getSourceTuple()));
		}

		
		if(isValidNode){
			if(!hasConnections)
				rightClickPopupMenu.add(new JSeparator());
			rightClickPopupMenu.add(mergeNodeMenu(visualItem));
		}
		else if(type.equals(LayoutUtils.ID)){
			String rId = ((Node) visualItem.getSourceTuple()).getString(LayoutUtils.ID);
			IReactionLay r = lViz.getLayoutContainer().getReactions().get(rId);
			if(!(r.getIDs().size()>1))
				rightClickPopupMenu.add(new JSeparator());
			rightClickPopupMenu.add(mergeNodeMenu(visualItem));
		}
		
		
		if(isValidForTypeChange((Node) visualItem.getSourceTuple())){
			rightClickPopupMenu.add(new JSeparator());
			rightClickPopupMenu.add(changeTypeMenu(visualItem));
		}
			
		return rightClickPopupMenu;
	}
	
	

	private JMenuItem removeReactionNodeMenu(final Node sourceTuple) {
		
		JMenuItem item = new JMenuItem("Remove reaction Node");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lViz.removeReaction(sourceTuple);
			}
		});
		
		return item;
	}

	private JMenuItem replicateReactionNodeMenu(final Node node) {
		JMenuItem item = new JMenuItem("Replicate Reaction");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					
				
				lViz.replicateReaction(node);
				
			}
		});
		
		return item;
	}
	
	private JMenuItem unfoldReactionNodeMenu(final Node node) {
		JMenuItem item = new JMenuItem("Unfold Reaction");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					
				
				lViz.unfoldReaction(node);
				
			}
		});
		
		return item;
	}

	private JMenuItem changeTypeMenu(final VisualItem visualItem) {
		
		String menu = "";
		if(visualItem.getString(LayoutUtils.TYPE).equals(LayoutUtils.MET_T)){
			menu = "Convert node to currency";
		}
		else if(visualItem.getString(LayoutUtils.TYPE).equals(LayoutUtils.HUBS))
			menu = "Convert node to metabolite";
		
		JMenuItem item = new JMenuItem(menu);
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(visualItem.getString(LayoutUtils.TYPE).equals(LayoutUtils.MET_T))
					lViz.changeTypeToCurrency(visualItem);
				else if(visualItem.getString(LayoutUtils.TYPE).equals(LayoutUtils.HUBS))
					lViz.changeTypeToMetabolite(visualItem);
			}
		});

		return item;
	}

	private boolean isValidForTypeChange(Node sourceTuple) {
		if(sourceTuple.getString(LayoutUtils.TYPE).equalsIgnoreCase(LayoutUtils.MET_T)||sourceTuple.getString(LayoutUtils.TYPE).equalsIgnoreCase(LayoutUtils.HUBS))
			return true;
		else
			return false;
	}

	/**
	 * Builds the menu item to merge nodes.
	 * @param vitem
	 * @return Menu item
	 */
	private JMenuItem mergeNodeMenu(final VisualItem vitem) {
		
		JMenuItem item = new JMenuItem("Merge Node");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vitem.setBoolean(LayoutUtils.ISFIXED, true);
				vitem.setFixed(true);
				lViz.updatePositionInILayout(vitem, true);
				creatingEdge = true;
				createTemporaryEdgeFromSourceToDummy(vitem);
				lViz.notifyListeners();
			}
		});
		
		return item;
	}
	
	private JMenuItem mergeReactionMenu(final VisualItem vitem) {
		
		JMenuItem item = new JMenuItem("Merge Reaction");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vitem.setBoolean(LayoutUtils.ISFIXED, true);
				vitem.setFixed(true);
				lViz.updatePositionInILayout(vitem, true);
				creatingEdge = true;
				createTemporaryEdgeFromSourceToDummy(vitem);
				lViz.notifyListeners();
			}
		});
		return item;
	}
	
	/**
	 * Builds the menu item to replicate nodes.
	 * @param node
	 * @return Menu item
	 */
	private JMenuItem replicateNodeMenu(final Node node) {
		
		JMenuItem item = new JMenuItem("Replicate Node");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					
				
				lViz.replicateNode(node);
				
			}
		});
		
		return item;
	}

	/**
	 * Builds the menu to fix all nodes of a given type.
	 * @param visualItemType
	 * @return Menu item
	 */
	private JMenuItem buildUnfixTypeMenu(final String visualItemType) {
		
		JMenuItem item = new JMenuItem("Unfix all nodes of this type");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lViz.updateFixedNodesByType(visualItemType, false);
				
			}
		});
		return item;
	}
	
	/**
	 * Builds the menu to fix all nodes of a given type.
	 * @param visualItemType
	 * @return Menu item
	 */
	private JMenuItem buildFixTypeMenu(final String visualItemType) {
		
		JMenuItem item = new JMenuItem("Fix all nodes of this type");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lViz.updateFixedNodesByType(visualItemType, true);
				
			}
		});
		return item;
	}
	
	/**
	 * Builds the Fix/Unfix node menu. If the node is fixed it
	 * will be unfixed and vice-versa.
	 * @param visualItem
	 * @return Menu item
	 */
	private JMenuItem buildFixUnfixMenu(final VisualItem visualItem) {
		
		
		String label;
		
		if(visualItem.getBoolean(LayoutUtils.ISFIXED))
			label = "Unfix Node";
		else
			label = "Fix Node";
		
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(visualItem.getBoolean(LayoutUtils.ISFIXED)){
					visualItem.setBoolean(LayoutUtils.ISFIXED, false);
					visualItem.setFixed(false);
					lViz.updatePositionInILayout(visualItem, true);
				}
				else{
					visualItem.setBoolean(LayoutUtils.ISFIXED, true);
					visualItem.setFixed(true);
					lViz.updatePositionInILayout(visualItem, true);
				}
			}
		});
		return item;
	}
	
	// ---------------------------------------------
	// --- methods for edgeCreation
	// ---------------------------------------------
	
	/**
	 * Stops edge creation - being used to
	 * select two nodes to merge.
	 */
	private void stopEdgeCreation() {
		creatingEdge = false;
		removeEdgeDummy();
	}

	/**
	 * Removes all dummies, the node and the two edges. Additionally sets the 
	 * variables who stored a reference to these dummies to null.
	 */
	private void removeAllDummies() {
		
		
		removeEdgeDummy();
		removeNodeDummy();
		
//		if (nodeSourceDummy != null) g.removeNode(nodeSourceDummy);
//		edgeDummy = null;
//		nodeSourceDummy = null;
//		nodeVisualDummy = null;
	}
	
	/**
	 * Removes all edge dummies, if the references stored to these dummies are
	 * not null. Additionally sets the references to these dummies to null.
	 */
	private void removeEdgeDummy() {
		if (edgeDummy != null) {
			g.removeEdge(edgeDummy);
			edgeDummy = null;
		}
	}

	private VisualItem createDummy() {
		
		//create the dummy node for the creatingEdge mode
		nodeSourceDummy = g.addNode();
		nodeSourceDummy.set(LayoutUtils.LABEL, "");
		
		/** ILAYOUT STUFF */
		nodeSourceDummy.set(LayoutUtils.ID, LayoutUtils.DUMMY);
		nodeSourceDummy.set(LayoutUtils.TYPE, LayoutUtils.MET_T);
		/**/
		
		nodeVisualDummy = vis.getVisualItem(LayoutUtils.NODES, nodeSourceDummy);
		nodeVisualDummy.setSize(0.0);
		nodeVisualDummy.setVisible(false);
		
		nodeSourceDummy.set(LayoutUtils.ISFIXED, true);
		
		nodeSourceDummy.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
		nodeSourceDummy.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);
		/*
		 * initially set the dummy's location. upon mouseMove events, we 
		 * will do that there. otherwise, the dummy would appear on top 
		 * left position until the mouse moves
		 */
		double x = d.getBounds().getCenterX();
		double y = d.getBounds().getCenterY();
		mousePosition.setLocation(x, y);
		nodeVisualDummy.setX(mousePosition.getX());
		nodeVisualDummy.setY(mousePosition.getY());
		return nodeVisualDummy;
	}
	
	private void removeNodeDummy() {
		if(nodeSourceDummy!=null){
			g.removeNode(nodeSourceDummy);
			
		}
		nodeSourceDummy = null;
		nodeVisualDummy = null;
	}


	private void createTemporaryEdgeFromSourceToDummy(Node source) {
		
		System.out.println("Edge Para o merge");
		if (edgeDummy == null) {
			edgeDummy = g.addEdge(source, nodeSourceDummy);
			
			/**Biovisualizer stuff */
			
			edgeDummy.set(LayoutUtils.ID, LayoutUtils.DUMMY);
			edgeDummy.setString(LayoutUtils.LABEL, LayoutUtils.DUMMY);
			edgeDummy.setDouble(LayoutUtils.ARROWSIZE, 1);

			edgeDummy.setString(LayoutUtils.TYPE, LayoutUtils.DUMMY);
			edgeDummy.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
			
			edgeDummy.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
			edgeDummy.setBoolean(LayoutUtils.HAS_THICKNESS, false);
			/**/
			
		} 
	} 

//	private void createTemporaryEdgeFromDummyToTarget(Node target) {
//		if (edgeDummy == null) {
//			edgeDummy = g.addEdge((Node)nodeVisualDummy.getSourceTuple(), target);
//
//			/**Biovisualizer stuff */
//
//			edgeDummy.set(LayoutUtils.ID, LayoutUtils.DUMMY);
//			edgeDummy.setString(LayoutUtils.LABEL, LayoutUtils.DUMMY);
//			edgeDummy.setDouble(LayoutUtils.ARROWSIZE, 1);
//
//			edgeDummy.setString(LayoutUtils.TYPE, LayoutUtils.DUMMY);
//			edgeDummy.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
//
//			/**/
//		} 
//	} 

	/**
	 * @param source the item to use as source for the dummy edge
	 */
	private void createTemporaryEdgeFromSourceToDummy(VisualItem source) {
		createTemporaryEdgeFromSourceToDummy((Node)source.getSourceTuple());
	}

//	/**
//	 * @param target the item to use as target for the dummy edge
//	 */
//	private void createTemporaryEdgeFromDummyToTarget(VisualItem target) {
//		createTemporaryEdgeFromDummyToTarget((Node)target.getSourceTuple());
//	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//necessary, if you have no dummy and this ControlAdapter is running 
		if (nodeVisualDummy == null) return;
		// update the coordinates of the dummy-node to the mouselocation so the tempEdge is drawn to the mouselocation too 
		d.getAbsoluteCoordinate(e.getPoint(), mousePosition);
		nodeVisualDummy.setX(mousePosition.getX());
		nodeVisualDummy.setY(mousePosition.getY());
	}

	
	/**
	 * Checks if two nodes are valid for merge. The type must be the same
	 * and the metabolic ids set must be exactly the same.
	 * @param sourceNode
	 * @param targetNode
	 * @return validity of the merge
	 */
	private boolean validForMerge(Node sourceNode, Node targetNode) {
		
		boolean isValidMerge = true;
		
		if(sourceNode == targetNode)
			return false;
		
		Map<String, INodeLay> nodes = lViz.getLayoutContainer().getNodes();
		String sourceId = sourceNode.getString(LayoutUtils.ID);
		String targetId = targetNode.getString(LayoutUtils.ID);
		
		if(!nodes.get(sourceId).getNodeType().equals(nodes.get(targetId).getNodeType()))
			isValidMerge = false;
		
		if(CollectionUtils.getSetDiferenceValues(nodes.get(sourceId).getIds(), nodes.get(targetId).getIds()).size()>0)
			isValidMerge = false;
		
		return isValidMerge;
	}
	
	/**
	 * Sumps coordinates of two points
	 * @param a
	 * @param b
	 * @return result point
	 */
	private Point sumPoints(Point a, Point b){
		
		double x = a.getX() + b.getX();
		double y = a.getY() + b.getY();
		Point res = new Point();
		res.setLocation(x, y);
		return res;
	}
	
	/**
	 * only necessary if edge-creation is used together with aggregates and
	 * the edge should move on when mousepointer moves within an aggregate
	 */
	@Override
	public void itemMoved(VisualItem item, MouseEvent e) {
		if (item instanceof AggregateItem) 
			mouseMoved(e);
		
//		if(item instanceof NodeItem && item.getString(LayoutUtils.TYPE)!="DUMMY")
//			updatePositionInILayout(item);
	}
	
	@Override
	public void itemReleased(VisualItem item, MouseEvent e) {
		
		
		TupleSet focusSet = lViz.getVis().getFocusGroup(LayoutUtils.SELECTED);
		
		
		Iterator iter = focusSet.tuples();
		while (iter.hasNext()) {
			VisualItem movedItem = (VisualItem) iter.next();
			
			lViz.updatePositionInILayout(movedItem, false);

		}
		
		
		if(tempx!= e.getX() || tempy!=e.getY())
			lViz.notifyListeners();
//		System.out.println("Released\t" +e.getX() + "\t" + e.getY());
	}


    public void itemPressed(VisualItem item, MouseEvent e) {
    	
    	tempx = e.getX();
    	tempy = e.getY();
//    	System.out.println("Pressed\t" +e.getX() + "\t" + e.getY());
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (creatingEdge) {
			stopEdgeCreation();
			return;
		}
		if (SwingUtilities.isRightMouseButton(e)) {
			if (creatingEdge) stopEdgeCreation();
		}
	}
	
	public void removeAllListeners(){
		listeners.clear();
	}
	

}
