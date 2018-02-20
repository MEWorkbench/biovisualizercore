package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.swing.JPanel;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.activity.ActivityManager;
import prefuse.controls.Control;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.expression.AndPredicate;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.demos.TreeMap.LabelLayout;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.PolygonRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphicsLib;
import prefuse.util.PrefuseLib;
import prefuse.util.display.DisplayLib;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ChangeLayoutListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeClickingListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions.VisibleElementsManagementAction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls.BVAggregateDragControl;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls.BVRubberBandSelect;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls.BioVisualizerHighlightControl;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls.BioVisualizerNodeInfoClickControl;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls.ColorShapeManager;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.layout.BVAggregateLayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.layout.BioVisualizerLayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.VisualizationProperties;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers.BioVisualizerConvEdgeRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers.BioVisualizerEdgeRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers.BioVisualizerShapeRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.Constants;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.OptVisualExtensions;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.MapUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * This is the class where the {@link Visualization} is built and the {@link Display} generated.<br>
 * A graph visualization is built from an {@link ILayout}. The
 * method generateVisualization returns a JPanel with the drawn graph.
 * @author Alberto Noronha
 * 
 */
public class LayoutVisualizator {

	private double maxX = Double.NEGATIVE_INFINITY;
	private double minX = Double.POSITIVE_INFINITY;
	private double maxY = Double.NEGATIVE_INFINITY;
	private double minY = Double.POSITIVE_INFINITY;

	private Visualization vis;
	
	private Display display;
	
	private ILayout layoutContainer;
	
	private Map<String, IReactionLay> reactions;
	
	/**
	 * temporary collection to expose node access
	 */
	//uniqueid -> node
	public Map<String, Node> nodes = new HashMap<>();
	//string -> unique id
	//e.g., C00001 -> 4385674868wyerusdff8
	public Map<String, String> somethingToUniqueId = new HashMap<> ();
	
	Graph met_graph;

	protected VisibleElementsManagementAction visibleManager;
	
	private Set<ChangeLayoutListener> listeners;
	
	private ColorShapeManager colorShapeManager;
	private BioVisualizerLayout biovisLayout;
	
	private AggregateTable at;
	private BioVisualizerNodeInfoClickControl nodeClickControl;
	
	private List<Control> displayControlers;
	
	public LayoutVisualizator(ILayout layoutContainer, ColorShapeManager cshapeManager){
		this.layoutContainer = layoutContainer;	 
		reactions = layoutContainer.getReactions();
		listeners = new HashSet<ChangeLayoutListener>();
		this.colorShapeManager = cshapeManager;
	}
	
//	public LayoutVisualizator(ILayout lc, boolean isClassic){
//		this(lc);
//		if(isClassic) this.colorShapeManager = new ColorShapeManager(
//				ColorLib.rgb(200,255,200), ColorLib.rgb(255, 255, 200),ColorLib.rgb(200, 200, 255),
//				ColorLib.rgb(0, 0, 0), ColorLib.rgb(0, 0, 0),ColorLib.rgb(0, 0, 0));
//	}
//
//	
//	public LayoutVisualizator(ILayout layoutContainer, int metColor, int hubColor, int infoColor, int labelMetColor, int labelReactColor, int labelInfoColor){
//		
//		this.layoutContainer = layoutContainer;	 
//		reactions = layoutContainer.getReactions();
//		listeners = new HashSet<ChangeLayoutListener>();
//		this.colorShapeManager = new ColorShapeManager(metColor,hubColor, infoColor, labelReactColor, labelMetColor, labelInfoColor);
//	}
	
	public void setColorShapeManager(ColorShapeManager colorShapeManager) {
		this.colorShapeManager = colorShapeManager;
	}

	
	/**
	 * Gets the {@link Visualization} object
	 * @return visualization object
	 */
	public Visualization getVis() {
		return vis;
	}

	/**
	 * Gets the {@link Display}
	 * @return gets the display - used to add Controls
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Sets the {@link Display}
	 * @param display
	 */
	public void setDisplay(Display display) {
		this.display = display;
	}

	/**
	 * Set the {@link Visualization}
	 * @param visualization
	 */
	public void setVis(Visualization vis) {
		this.vis = vis;
	}

	/**
	 * Gets the layout container - {@link ILayout} - of the visualization
	 * @return Layout Container
	 */
	public ILayout getLayoutContainer() {
		return layoutContainer;
	}

	/**
	 * Sets the layout container - {@link ILayout} - for the visualization
	 * @param layoutContainer
	 */
	public void setLayoutContainer(ILayout layoutContainer) {
		this.layoutContainer = layoutContainer;
	}

	/**
	 * Gets the map of reactions ({@link IReactionLay}) of the layout container - {@link ILayout}
	 * @return map of reaction Id to IReactionLay
	 */
	public Map<String, IReactionLay> getReactions() {
		return reactions;
	}

	/**
	 * Sets the map of reactions ({@link IReactionLay}) of the layout container - {@link ILayout}
	 * @param reactions
	 */
	public void setReactions(Map<String, IReactionLay> reactions) {
		this.reactions = reactions;
	}
	
	public AggregateTable getAT(){
		return at;
	}

	/**
	 * Builds the {@link Graph} from the container ({@link ILayout})
	 * The graph is composed of a table that allows the storage of the information.
	 * Nodes and Edges share this table.
	 * 
	 * Each reaction is a {@link Node} that connects the Reactants and the Products.
	 * 
	 * @return Graph
	 */
	public Graph makeMetGraph(){
		/**
		 * Creates a graph from the given Reactions
		 * First Create the graph table
		 */
		met_graph = new Graph(true);
		Map<String, Node> nodes = new HashMap<String, Node>();
		
		met_graph.addColumn(LayoutUtils.LABEL, String.class);
		met_graph.addColumn(LayoutUtils.ID, String.class);
		
		met_graph.addColumn(VisualItem.SHAPE, int.class);
		met_graph.addColumn(LayoutUtils.TYPE, String.class);
		met_graph.addColumn(EdgeRenderer.EDGE_TYPE, int.class);
		met_graph.addColumn(LayoutUtils.ARROWSIZE, double.class);
		met_graph.addColumn(OptVisualExtensions.VISUAL_HEIGHT, double.class);
		met_graph.addColumn(OptVisualExtensions.VISUAL_WIDTH, double.class);
		
		met_graph.addColumn(VisualItem.FILLCOLOR, int.class);
		met_graph.addColumn(VisualItem.FIXED, boolean.class);

		met_graph.addColumn(LayoutUtils.ISFIXED, boolean.class);
		met_graph.addColumn(LayoutUtils.MY_X, double.class);
		met_graph.addColumn(LayoutUtils.MY_Y, double.class);
		
		//Special colors and shapes for overlap purposes
		met_graph.addColumn(LayoutUtils.HAS_SPECIAL_COLOR, boolean.class);
		met_graph.addColumn(LayoutUtils.SPECIAL_COLOR, int.class);
		met_graph.addColumn(LayoutUtils.HAS_SPECIAL_SHAPE, boolean.class);
		met_graph.addColumn(LayoutUtils.SPECIAL_SHAPE, int.class);
		
		met_graph.addColumn(LayoutUtils.HAS_THICKNESS, boolean.class);
		met_graph.addColumn(LayoutUtils.THICKNESS, double.class);
		
		met_graph.addColumn(LayoutUtils.IS_REACTANT, boolean.class);
		met_graph.addColumn(BioVisualizerConvEdgeRenderer.MY_EDGE_TYPE, String.class);
		
		/**
		 *  For all reactions
		 */
		for(String rId : this.reactions.keySet()){
			
			String label = reactions.get(rId).getLabel();
			String uniqueId = reactions.get(rId).getUniqueId();
			Node r1 = met_graph.addNode();
			
			r1.setString(LayoutUtils.LABEL, label);
			r1.setString(LayoutUtils.ID, uniqueId);
			r1.setString(LayoutUtils.TYPE, LayoutUtils.REA_T);
			r1.setInt(VisualItem.SHAPE, Constants.SHAPE_RECTANGLE);
			r1.setDouble(OptVisualExtensions.VISUAL_HEIGHT,  LayoutUtils.reactionNodeSize);
			r1.setDouble(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.reactionNodeSize);
			
			r1.setString(BioVisualizerConvEdgeRenderer.MY_EDGE_TYPE, BioVisualizerConvEdgeRenderer.EDGE_DRAW_TYPE);
			
			
			if(reactions.get(rId).getX()!=null && reactions.get(rId)!=null){
				r1.setBoolean(LayoutUtils.ISFIXED, true);
				r1.setDouble(LayoutUtils.MY_X, reactions.get(rId).getX());
				r1.setDouble(LayoutUtils.MY_Y, reactions.get(rId).getY());
				if(reactions.get(rId).getX()>maxX) maxX = reactions.get(rId).getX();
				if(reactions.get(rId).getX()<minX) minX = reactions.get(rId).getX();
				if(reactions.get(rId).getY()>maxY) maxY = reactions.get(rId).getY();
				if(reactions.get(rId).getY()<minY) minY = reactions.get(rId).getY();
			}

			r1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
			r1.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);
			
			Map<String, INodeLay> metabolites = reactions.get(rId).getReactants();

			/**
			 * Reactants
			 */
			for(String metId : metabolites.keySet()){
			
				Node n1 = null;

				if(!nodes.containsKey(metId)){
					
					INodeLay met = metabolites.get(metId);
					double dim2 = LayoutUtils.defaultMetaboliteWidth;
					if(met.getNodeType().equals(NodeTypeLay.CURRENCY)) dim2 = LayoutUtils.defaultNodeHeight;
					n1 = buildNode(met, LayoutUtils.defaultNodeHeight, dim2);
					nodes.put(metId,n1);
					
				
				}
				else{
					n1 = nodes.get(metId);
				}
				
				Edge e1 = null;
				e1 = met_graph.addEdge(r1, n1);
				
				if(metabolites.get(metId).getNodeType().equals(NodeTypeLay.CURRENCY)){
					e1.setString(LayoutUtils.TYPE,LayoutUtils.LINK_HUBS);
				}
				else
					e1.setString(LayoutUtils.TYPE, LayoutUtils.LINK_CONVERTION);

					e1.setDouble(LayoutUtils.ARROWSIZE, 2);

				if(reactions.get(rId).isReversible()){
					e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);	 
				}
				else{
					e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
				}

				e1.setString(LayoutUtils.ID, rId);
				e1.setBoolean(LayoutUtils.IS_REACTANT, true);
				e1.setString(LayoutUtils.LABEL, label);
				
				//No special color for edge for now!
				e1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				e1.setBoolean(LayoutUtils.HAS_THICKNESS, false);
			}

			/**
			 * Products
			 */
			metabolites = reactions.get(rId).getProducts();
			for(String metId : metabolites.keySet()){
				
				Node n1 = null;
				if(!nodes.containsKey(metId)){

					INodeLay met = metabolites.get(metId);
					double dim2 = LayoutUtils.defaultMetaboliteWidth;
					
					if(met.getNodeType().equals(NodeTypeLay.CURRENCY)) dim2 = LayoutUtils.defaultNodeHeight;
					n1 = buildNode(met, LayoutUtils.defaultNodeHeight, dim2);
					
					nodes.put(metId,n1);
					
				}
				else{
					n1 = nodes.get(metId);
				}

				Edge e1 = null;
				e1 = met_graph.addEdge(r1, n1); 
				if(metabolites.get(metId).getNodeType().equals(NodeTypeLay.CURRENCY)){
					e1.setString(LayoutUtils.TYPE,LayoutUtils.LINK_HUBS);
				}
				else
					e1.setString(LayoutUtils.TYPE, LayoutUtils.LINK_CONVERTION);

					e1.setDouble(LayoutUtils.ARROWSIZE, 2);
				
				e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);
				
				e1.setString(LayoutUtils.LABEL, label);
				e1.setString(LayoutUtils.ID, rId);
				e1.setBoolean(LayoutUtils.IS_REACTANT, false);
				
				//No special color for edge for now!
				e1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				e1.setBoolean(LayoutUtils.HAS_THICKNESS, false);

			}

			/**
			 * Infos
			 */
			Map<String, INodeLay> infos = reactions.get(rId).infos();
			for(String infoId : infos.keySet()){
				Node n1 = null;
				INodeLay info = infos.get(infoId);

				n1 = buildNode(info, LayoutUtils.defaultInfoHeight, LayoutUtils.defaultInfoWidth);
			
				nodes.put(infoId,n1);
				
				Edge e1 = null;
				e1 = met_graph.addEdge(r1, n1);
				e1.setDouble(LayoutUtils.ARROWSIZE, 1);

				e1.setString(LayoutUtils.TYPE, LayoutUtils.LINK_INFO);
				e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
				
				e1.setString(LayoutUtils.ID, rId);
				e1.setString(LayoutUtils.LABEL, label);
				
				//No special color for edge for now!
				e1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				e1.setBoolean(LayoutUtils.HAS_THICKNESS, false);
				
			}

		}

		return met_graph;
		
	}

	/**
	 * Builds a {@link Node} for the {@link Graph} - may have specific sizes.
	 * @param met - {@link INodeLay} from the {@link ILayout}
	 * @param xdim - xSize
	 * @param dim2 - ySize
	 * @return {@link Node}
	 */
	private Node buildNode(INodeLay met, double xdim, double dim2) {

		Node n1 = met_graph.addNode();
		n1.setString(LayoutUtils.LABEL, met.getLabel());
		n1.setString(LayoutUtils.ID, met.getUniqueId());
		
		if(met.getNodeType()!=null){
			n1.setString(LayoutUtils.TYPE, LayoutUtils.getNodeType(met.getNodeType()));
		}
		else{
			n1.setString(LayoutUtils.TYPE, LayoutUtils.INFO);
		}
		
			n1.setDouble(OptVisualExtensions.VISUAL_HEIGHT, new Double(xdim));
			n1.setDouble(OptVisualExtensions.VISUAL_WIDTH, new Double(dim2)); 

		if(met.getX()!=null && met.getY()!= null){
			n1.setBoolean(LayoutUtils.ISFIXED, true);
			n1.setDouble(LayoutUtils.MY_X, met.getX());
			n1.setDouble(LayoutUtils.MY_Y, met.getY());
			if(met.getX()>maxX) maxX = met.getX();
			if(met.getX()<minX) minX = met.getX();
			if(met.getY()>maxY) maxY = met.getY();
			if(met.getY()<minY) minY = met.getY();
		}
		
		
		//No special color for edge for now!
		n1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
		n1.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);
		
		return n1;
	}

	/**
	 * This function generates the visualization using the prefuse.
	 * Returns a {@link JPanel} to use in the GUI.
	 * 
	 * The panel is just the graph visualization and some actions added.<br>
	 * The layout is the {@link ForceDirectedLayout}, with some of the nodes fixed.
	 * The fixation of the nodes is made if the layoutcontainer ({@link ILayout}) has specific 
	 * coordinates for the given nodes.<br>
	 * 
	 * Labels for the nodes are defined by decorators. Everytime visual/shape/coloring
	 * actions are made this must be dealt with.<br>
	 * 
	 * Nodes and edges shapes are rendered by shape renders.
	 * Actions are defined to deal with the coloring of the nodes and edges.<br>
	 * 
	 * Current actions are defined by default at this step.<br>
	 * 
	 * The initial positioning of the graph is being calculated by determining the minimum and maximum
	 * x and y coordinates for the nodes (this may not work correctly if any positions 
	 * are defined in the layout container.
	 * 
	 * @return {@link JPanel} with the visualization
	 */
	public JPanel generateVisualization(){

		Graph g = makeMetGraph();
		vis = new Visualization();
		vis.addGraph("graph", g, null, PrefuseLib.getVisualItemSchema(), PrefuseLib.getVisualItemSchema());

		vis.setInteractive(LayoutUtils.NODES, null, true);
		vis.setInteractive(LayoutUtils.EDGES, null, false);
		
		
		at = vis.addAggregates(LayoutUtils.AGGR);
        at.addColumn(VisualItem.POLYGON, float[].class);
        at.addColumn("id", int.class);
     
		
		/**
		 * Setting the renders
		 */
		Renderer nodeR = new BioVisualizerShapeRenderer(/*25,25*0.7*/);
		DefaultRendererFactory drf = new DefaultRendererFactory();
		drf.setDefaultRenderer(nodeR);
		
		BioVisualizerConvEdgeRenderer edgeR = new BioVisualizerConvEdgeRenderer(Constants.EDGE_TYPE_CURVE, vis);

		drf.add(new InGroupPredicate(LayoutUtils.EDGES), edgeR);
		
		drf.add(new InGroupPredicate("nodes_deco"), new LabelRenderer(LayoutUtils.LABEL));
		
		LabelRenderer reactionlabelRenderer = new LabelRenderer(LayoutUtils.LABEL);
		reactionlabelRenderer.setVerticalAlignment(Constants.TOP);
		
		drf.add(new InGroupPredicate("reactionNodes_deco"), reactionlabelRenderer);
		
		
		LabelRenderer infoLabelsRenderer = new LabelRenderer(LayoutUtils.LABEL);
		drf.add(new InGroupPredicate("inforNodes_deco"), infoLabelsRenderer);
		
		LabelRenderer hubsLabelsRenderer = new LabelRenderer(LayoutUtils.LABEL);
		drf.add(new InGroupPredicate("hubNodes_deco"), hubsLabelsRenderer);
		
		
		//AGGREGATES
		// draw aggregates as polygons with curved edges
        Renderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
        ((PolygonRenderer)polyR).setCurveSlack(0.15f);
        // draw aggregates as polygons with curved edges
        drf.add("ingroup('aggregates')", polyR);
        
		vis.setRendererFactory(drf);

		/**
		 * Actions
		 */
		
		
		/** Color and Shape action */
		colorShapeManager.setVisualization(vis);
		
		colorShapeManager.runDecorators();
		
		ActionList edgeColoring = colorShapeManager.getEdgeColoring();
		ActionList nodeColoring = colorShapeManager.getNodesColoring();
//		ActionList edgeShapes = colorShapeManager.getEdgeShaping();
		
		vis.putAction(LayoutUtils.default_nodeColoring, nodeColoring);
		vis.run(LayoutUtils.default_nodeColoring);
		
//		vis.putAction(LayoutUtils.default_EdgeSizeAction,edgeShapes);
//		vis.run(LayoutUtils.default_EdgeSizeAction);
		
		vis.putAction(LayoutUtils.default_EdgeColorAction, edgeColoring);
		vis.run(LayoutUtils.default_EdgeColorAction);
		
		/** Animated Layout */
		
		boolean isHuge = false;
		if((layoutContainer.getNodes().size() + layoutContainer.getReactions().size())>1500) isHuge = true;
		
		visibleManager = new VisibleElementsManagementAction(vis);
		
		ActionList layout = new ActionList(Activity.INFINITY);
		
		double vel = 0.5;
		
		this.biovisLayout = new BioVisualizerLayout(LayoutUtils.GRAPH, !isHuge, vel);
		layout.add(biovisLayout);

		layout.add(new LabelLayout("nodes_deco"));
		layout.add(new LabelLayout("hubNodes_deco"));
		layout.add(new LabelLayout("reactionNodes_deco"));
		layout.add(new LabelLayout("inforNodes_deco"));
		layout.add(new RepaintAction());
		layout.add(new BVAggregateLayout(LayoutUtils.AGGR));
		layout.add(visibleManager);

		//	      Create the focus group
		TupleSet selectedItems = new DefaultTupleSet(); 
		vis.addFocusGroup(LayoutUtils.SELECTED, selectedItems);
		
		TupleSet inPath = new DefaultTupleSet(); 
		vis.addFocusGroup(LayoutUtils.INPATHWAY, inPath);
		
		
		//	      Create the rubber band object for rendering on screen
		Table rubberBandTable = new Table();
		rubberBandTable.addColumn(VisualItem.POLYGON, float[].class);      
		rubberBandTable.addRow();
		vis.add(LayoutUtils.RUBBER_BAND, rubberBandTable);       
		

		//	      render the rubber band with the default polygon renderer
		Renderer rubberBandRenderer = new PolygonRenderer(Constants.POLY_TYPE_LINE);
		drf.add(new InGroupPredicate(LayoutUtils.RUBBER_BAND), rubberBandRenderer);


		vis.putAction(LayoutUtils.default_layout_action, layout);
		vis.putAction(LayoutUtils.default_repaint_action, new RepaintAction());

		display = new Display(vis);
//		display.setSize(null);
		
		addControlersToDisplay();
		display.setHighQuality(true);
//		display.panTo(new Point(0,0));
		
		Point pm = tryToPanToDimensions();
		
		JPanel panel = new JPanel();
		GridLayout thisLayout1 = new GridLayout(0,1);
		panel.setLayout(thisLayout1);
		panel.add(display);
		vis.run(LayoutUtils.default_layout_action);
		
		return panel;
	}
	
	public Point tryToPanToDimensions() {
		Point pm = new Point(0, 0);
		if(this.maxX != Double.NEGATIVE_INFINITY && this.minX != Double.POSITIVE_INFINITY
				&& this.maxY != Double.NEGATIVE_INFINITY && this.minY != Double.POSITIVE_INFINITY){
			
			
			int minx = (int) minX;
			int maxx = (int) maxX;
			int miny = (int) minY;
			int maxy = (int) maxY;
			
			int medX = (int) ((maxX - minX) / 2);
			int medY = (int) ((maxY - minY) / 2);
			Rectangle2D r2d = new Rectangle(minx, miny, maxx-minx, maxy-miny);
			
			GraphicsLib.expand(r2d, 50 + (int)(1/display.getScale()));
			pm = new Point(medX, medY);
			biovisLayout.setInitialPos((float)pm.getX(), (float)pm.getY());
			DisplayLib.fitViewToBounds(display, r2d ,pm, 0);
			
		}else{
			Rectangle2D r2d = new Rectangle(-200, -200, 400, 400);
			
			GraphicsLib.expand(r2d, 50 + (int)(1/display.getScale()));
			DisplayLib.fitViewToBounds(display, r2d ,pm, 0);
			
		}
		return pm;
		
	}

	public JPanel generateVisualizationTest(){

		Graph g = makeMetGraph();
		vis = new Visualization();
		vis.addGraph("graph", g, null, PrefuseLib.getVisualItemSchema(), PrefuseLib.getVisualItemSchema());

		vis.setInteractive(LayoutUtils.NODES, null, true);
		vis.setInteractive(LayoutUtils.EDGES, null, false);
		
		
		at = vis.addAggregates(LayoutUtils.AGGR);
        at.addColumn(VisualItem.POLYGON, float[].class);
        at.addColumn("id", int.class);
		
		/**
		 * Setting the renders
		 */
		Renderer nodeR = new BioVisualizerShapeRenderer(/*25,25*0.7*/);
		DefaultRendererFactory drf = new DefaultRendererFactory();
		drf.setDefaultRenderer(nodeR);
		
		BioVisualizerConvEdgeRenderer edgeR = new BioVisualizerConvEdgeRenderer(Constants.EDGE_TYPE_CURVE, vis);

		drf.add(new InGroupPredicate(LayoutUtils.EDGES), edgeR);
		
		drf.add(new InGroupPredicate("nodes_deco"), new LabelRenderer(LayoutUtils.LABEL));
		
		LabelRenderer reactionlabelRenderer = new LabelRenderer(LayoutUtils.LABEL);
		reactionlabelRenderer.setVerticalAlignment(Constants.TOP);
		
		drf.add(new InGroupPredicate("reactionNodes_deco"), reactionlabelRenderer);
		
		
		LabelRenderer infoLabelsRenderer = new LabelRenderer(LayoutUtils.LABEL);
		drf.add(new InGroupPredicate("inforNodes_deco"), infoLabelsRenderer);
		
		LabelRenderer hubsLabelsRenderer = new LabelRenderer(LayoutUtils.LABEL);
		drf.add(new InGroupPredicate("hubNodes_deco"), hubsLabelsRenderer);
		
		
		//AGGREGATES
		// draw aggregates as polygons with curved edges
        Renderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
        ((PolygonRenderer)polyR).setCurveSlack(0.15f);
        // draw aggregates as polygons with curved edges
        drf.add("ingroup('aggregates')", polyR);
        
		vis.setRendererFactory(drf);

		/**
		 * Actions
		 */
		
		
		/** Color and Shape action */
		colorShapeManager.setVisualization(vis);
		
		colorShapeManager.runDecorators();
		
		ActionList edgeColoring = colorShapeManager.getEdgeColoring();
		ActionList nodeColoring = colorShapeManager.getNodesColoring();
//		ActionList edgeShapes = colorShapeManager.getEdgeShaping();
		
		vis.putAction(LayoutUtils.default_nodeColoring, nodeColoring);
		vis.run(LayoutUtils.default_nodeColoring);
		
//		vis.putAction(LayoutUtils.default_EdgeSizeAction,edgeShapes);
//		vis.run(LayoutUtils.default_EdgeSizeAction);
		
		vis.putAction(LayoutUtils.default_EdgeColorAction, edgeColoring);
		vis.run(LayoutUtils.default_EdgeColorAction);
		
		/** Animated Layout */
		
		boolean isHuge = false;
//		if((layoutContainer.getNodes().size() + layoutContainer.getReactions().size())>1500) isHuge = true;
		
		visibleManager = new VisibleElementsManagementAction(vis);
		
		ActionList layout = new ActionList(Activity.INFINITY);
		
		double vel = 0.035;
//		if(isHuge) vel = 0.5;
		
		this.biovisLayout = new BioVisualizerLayout(LayoutUtils.GRAPH, !isHuge, vel);
		layout.add(biovisLayout);

		layout.add(new LabelLayout("nodes_deco"));
		layout.add(new LabelLayout("hubNodes_deco"));
		layout.add(new LabelLayout("reactionNodes_deco"));
		layout.add(new LabelLayout("inforNodes_deco"));
		layout.add(new RepaintAction());
		layout.add(new BVAggregateLayout(LayoutUtils.AGGR));
		layout.add(visibleManager);

		//	      Create the focus group
//		TupleSet selectedItems = new DefaultTupleSet(); 
//		vis.addFocusGroup(LayoutUtils.SELECTED, selectedItems);
//		
//		TupleSet inPath = new DefaultTupleSet(); 
//		vis.addFocusGroup(LayoutUtils.INPATHWAY, inPath);
//		
//		
//		//	      Create the rubber band object for rendering on screen
		Table rubberBandTable = new Table();
		rubberBandTable.addColumn(VisualItem.POLYGON, float[].class);      
		rubberBandTable.addRow();
		vis.add(LayoutUtils.RUBBER_BAND, rubberBandTable);
		

		//	      render the rubber band with the default polygon renderer
//		Renderer rubberBandRenderer = new PolygonRenderer(Constants.POLY_TYPE_LINE);
//		drf.add(new InGroupPredicate(LayoutUtils.RUBBER_BAND), rubberBandRenderer);


		vis.putAction(LayoutUtils.default_layout_action, layout);
		vis.putAction(LayoutUtils.default_repaint_action, new RepaintAction());

		display = new Display(vis);
//		display.setSize(null);
		
		addControlersToDisplay();
		display.setHighQuality(true);
		display.panTo(new Point(0,0));
		
		
		JPanel panel = new JPanel();
		GridLayout thisLayout1 = new GridLayout(0,1);
		panel.setLayout(thisLayout1);
		panel.add(display);
		vis.run(LayoutUtils.default_layout_action);
		
		return panel;
	}
	
	
	private void createControlers(){
		
		VisualItem rubberBand = (VisualItem) vis.getVisualGroup(LayoutUtils.RUBBER_BAND).tuples().next();
		rubberBand.set(VisualItem.POLYGON, new float[8]);
		rubberBand.setStrokeColor(ColorLib.color(ColorLib.getColor(255,0,0)));
		
		displayControlers = new ArrayList<Control>();
		displayControlers.add(new BVRubberBandSelect(rubberBand, vis, at));
		displayControlers.add(new FocusControl(1));
		displayControlers.add(new PanControl(MouseEvent.BUTTON3_MASK));
		displayControlers.add(new WheelZoomControl());
		displayControlers.add(new BioVisualizerHighlightControl(this.vis));
		displayControlers.add(new BVAggregateDragControl(vis,at));
		nodeClickControl = new BioVisualizerNodeInfoClickControl(this);
		displayControlers.add(nodeClickControl);
		
		
	}
	
	private void addControlersToDisplay(){
		
		if(displayControlers == null) createControlers();
		
		for(Control c : displayControlers)
			display.addControlListener(c);

	}
	
	private void removeControlersToDisplay(){
		
		if(displayControlers!=null){
			
			for(Control c : displayControlers){
				display.removeControlListener(c);
			}
		}
		
		displayControlers = null;
	}
	
	/**
	 * Adds a group visual filter to the visualization using the
	 * {@link VisibleElementsManagementAction}.
	 * @param s - name of the group
	 */
	public void addGroupVisualFilter(String s){
		this.visibleManager.addGroupVisualRestriction(s);
	}
	
	/**
	 * Sets the visual filter, removing the previous ones using the {@link VisibleElementsManagementAction}.
	 * @param s - name of the group
	 */
	private void setGroupVisualFilter(String s){
		this.visibleManager.removeAllGroupVisualRestrictions();
		this.addGroupVisualFilter(s);
	}
	
	/**
	 * Adds a group of invisible reactions to the {@link VisibleElementsManagementAction} 
	 * @param id - identification for the set of invisible reactions
	 * @param inv - id's of the reactions to turn invisible
	 */
	public void addInvisibleReactions(Set<String> inv){
		this.visibleManager.setInvisibleReactions(inv);
	}
	
	/**
	 * Removes all group visual filters running from the {@link VisibleElementsManagementAction}.
	 */
	private void removeAllGroupVisualFilters() {
		this.visibleManager.removeAllGroupVisualRestrictions();
		
	}
	
	/**
	 * Removes a group visual filter from the {@link VisibleElementsManagementAction}.
	 * @param s - name of the group
	 */
	public void removeGroupVisualFilter(String s){
		this.visibleManager.removeGroupVisualRestriction(s);
	}
	
	/**
	 * Removes all flux visual filters running from the {@link VisibleElementsManagementAction}.
	 */
	public void removeAllReactionsFilter(){
		this.visibleManager.removeAllFluxVisualRestrictions();
	}

	/**
	 * removes a reaction node visual filter from the {@link VisibleElementsManagementAction}.
	 * @param id - dentification of the reaction node
	 */
	private void removeInvisibleReactionFilter(String reaction_id){
		this.visibleManager.removeReactionVisualRestrictions(reaction_id);
	}
	
	
	
	/**
	 * Returns a map with the id of the node and the x and y coordinates. The
	 * 0 index is the x-axis and the 1 index is the y-axis.
	 * @return Map of coordinates.
	 * 
	 */
	public Map<String, Double[]> getNodesCoordinates(){
		
		Map<String, Double[]> result = new HashMap<String,Double[]>();
		
		Iterator items = vis.items(LayoutUtils.NODES);
		while(items.hasNext()){

			VisualItem item = (VisualItem) items.next();
			String id = item.getString(LayoutUtils.ID);
			
			Double x = item.getX();
			Double y = item.getY();

			Double[] coords = new Double[2];
			coords[0] = x;
			coords[1] = y;
			
			if(item.isVisible())
				result.put(id, coords);
			else{
				if(item.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T)){
					result.put(id, new Double[]{layoutContainer.getReactions().get(id).getX(), layoutContainer.getReactions().get(id).getY()});
				}
				else if(!item.getString(LayoutUtils.ID).equals(LayoutUtils.DUMMY)){
//					System.out.println(id + "\t" + layoutContainer.getNodes().get(id));
					result.put(id, new Double[]{layoutContainer.getNodes().get(id).getX(), layoutContainer.getNodes().get(id).getY()});
				}
			}
		}
		
		
		return result;
	}
	
	/**
	 * Builds a map with the identifier of all nodes and a boolean corresponding to the visibility of 
	 * the element in the display.
	 * @return Map id -> visible
	 */
	public Map<String, Boolean> getVisibilityOfNodes(){

		Map<String, Boolean> visibleReactions = new HashMap<String, Boolean>();
		
		
		Iterator items = vis.items(LayoutUtils.pred_reac);
		while(items.hasNext()){

			VisualItem item = (VisualItem) items.next();
			String id = item.getString(LayoutUtils.ID);
			visibleReactions.put(id, item.isVisible());
		}

		items = vis.items(new AndPredicate(new InGroupPredicate(LayoutUtils.NODES), LayoutUtils.pred_hub));
		while(items.hasNext()){

			VisualItem item = (VisualItem) items.next();
			String id = item.getString(LayoutUtils.ID);
			
				Node node = (Node) item.getSourceTuple();

				Iterator neighbors = node.edges();
				boolean visible = false;
				Set<String> invisibleReactions = visibleManager.getInvisibleReactions();

				while(neighbors.hasNext()){
					Edge edge = (Edge) neighbors.next();
					String reactId = edge.getString(LayoutUtils.ID);
					if(!invisibleReactions.contains(reactId))
						visible = true;

				visibleReactions.put(id, visible);
			}
		}

		items = vis.items(LayoutUtils.pred_met);
		while(items.hasNext()){

			VisualItem item = (VisualItem) items.next();
			String id = item.getString(LayoutUtils.ID);
			visibleReactions.put(id, item.isVisible());
		}

		items = vis.items(LayoutUtils.pred_info);
		while(items.hasNext()){
			
			VisualItem item = (VisualItem) items.next();
			String id = item.getString(LayoutUtils.ID);
			visibleReactions.put(id, item.isVisible());
		}
		
		return visibleReactions;
	}

	
	public void fixAllNodes(){
		Iterator items = vis.items(LayoutUtils.NODES);
		while(items.hasNext()){
			VisualItem item = (VisualItem) items.next();
			if(item.canSetBoolean(LayoutUtils.ISFIXED)) item.setBoolean(LayoutUtils.ISFIXED, true);
			item.setFixed(true);
			updatePositionInILayout(item, false);
		}
	}
	
	
	/**
	 * Given a node type and a boolean, sets all nodes of that type to
	 * fixed/unfixed
	 * @param nodeType
	 * @param isFixed
	 */
	public void updateFixedNodesByType(String nodeType, boolean isFixed){
		
		Iterator items = vis.items(LayoutUtils.NODES);
		
		while(items.hasNext()){
			VisualItem item = (VisualItem) items.next();
			
			if(item.getString(LayoutUtils.TYPE).equals(nodeType)){
				
				item.setBoolean(LayoutUtils.ISFIXED, isFixed);
				item.setFixed(isFixed);
				updatePositionInILayout(item, false);
			}
		}
		
		notifyListeners();
	}
	
	public void removeReaction(Node reactionNode){
		
		
		synchronized (vis) {
			
		stopActions();
		
		Iterator items = reactionNode.neighbors();
		Set<Node> nodes = new HashSet<Node>();
		
		while(items.hasNext()){
			
			Node targetNode = (Node) items.next();
			
			if(targetNode.getDegree() == 1){
				
				nodes.add(targetNode);
				
			}
		}
		
		String rId = reactionNode.getString(LayoutUtils.ID);
		Set<String> metIds = new HashSet<String>();
		met_graph.removeNode(reactionNode);
		for(Node n : nodes){
			String id = n.getString(LayoutUtils.ID);
			metIds.add(id);
			met_graph.removeNode(n);
		}
		
		for(String mId : metIds){
			if(getNode(mId) == null){
				this.layoutContainer.getNodes().remove(mId);
			}
		}
		
		TupleSet ts = vis.getFocusGroup(LayoutUtils.SELECTED);
		ts.clear();
		
		
		
		if(getNode(rId)==null)
		this.layoutContainer.getReactions().remove(rId);
		
		notifyListeners();
		
		cleanSelectionAndAggs();
		
		
		runActions();
		}
	}
	

	/**
	 * Given a {@link Node} with multiple connections, replicates the {@link Node} to
	 * have a single connection per {@link Node}.
	 * @param originalNode
	 */
	public void replicateNode(Node originalNode){


		Iterator items = originalNode.edges();

		synchronized (vis) {

			runActions();

			String label = originalNode.getString(LayoutUtils.LABEL);
			String type = originalNode.getString(LayoutUtils.TYPE);

			Double width = originalNode.getDouble(OptVisualExtensions.VISUAL_WIDTH);
			Double height = originalNode.getDouble(OptVisualExtensions.VISUAL_HEIGHT);

			//		boolean first = true;

			List<Edge> edgesToRemove = new ArrayList<Edge>();

			String originalNodeID = originalNode.getString(LayoutUtils.ID);
			NodeTypeLay nodeType = this.layoutContainer.getNodes().get(originalNodeID).getNodeType();

			Set<String> metIds = layoutContainer.getNodes().get(originalNodeID).getIds();

			while(items.hasNext()){

				Edge edge = (Edge) items.next();

				Node reactionNode = edge.getAdjacentNode(originalNode);

				Node newNode = met_graph.addNode();

				UUID idr = UUID.randomUUID();
				String id = idr.toString();

				newNode.setString(LayoutUtils.ID, id);
				newNode.setString(LayoutUtils.LABEL, label);
				newNode.setString(LayoutUtils.TYPE, type);

				newNode.setDouble(OptVisualExtensions.VISUAL_HEIGHT, height);
				newNode.setDouble(OptVisualExtensions.VISUAL_WIDTH, width);
				newNode.setBoolean(LayoutUtils.ISFIXED, true);

				Double[] coordinatesOfReaction = getNodeCoordinates(reactionNode.getString(LayoutUtils.ID));
				Double[] coordinatesOfNode = getNodeCoordinates(originalNodeID);
				Double x = (coordinatesOfNode[0]+coordinatesOfReaction[0])/2;
				Double y = (coordinatesOfReaction[1]+coordinatesOfNode[1])/2;

				newNode.setDouble(LayoutUtils.MY_X, x);
				newNode.setDouble(LayoutUtils.MY_Y, y);

				newNode.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				newNode.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);

				Edge newEdge = met_graph.addEdge(reactionNode, newNode);
				newEdge.setString(LayoutUtils.ID, reactionNode.getString(LayoutUtils.ID));
				newEdge.setString(LayoutUtils.LABEL, reactionNode.getString(LayoutUtils.LABEL));
				newEdge.setString(LayoutUtils.TYPE, edge.getString(LayoutUtils.TYPE));
				newEdge.setDouble(LayoutUtils.ARROWSIZE, edge.getDouble(LayoutUtils.ARROWSIZE));
				newEdge.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, edge.getInt(BioVisualizerEdgeRenderer.EDGE_TYPE));

				newEdge.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				newEdge.setBoolean(LayoutUtils.IS_REACTANT, edge.getBoolean(LayoutUtils.IS_REACTANT));

//				nodes.put(id, newNode);

				edgesToRemove.add(edge);

				String reactionNodeId = reactionNode.getString(LayoutUtils.ID);


				IReactionLay reaction = layoutContainer.getReactions().get(reactionNodeId);

				layoutContainer.addNode(id, label, metIds, nodeType, x, y);
				if(reaction.getProducts().containsKey(originalNodeID)){
					reaction.getProducts().remove(originalNodeID);
					reaction.getProducts().put(id, layoutContainer.getNodes().get(id));
					
				}
				else if(reaction.getReactants().containsKey(originalNodeID)){
					reaction.getReactants().remove(originalNodeID);
					reaction.getReactants().put(id, layoutContainer.getNodes().get(id));
					
				}

			}

			for(Edge e : edgesToRemove)
				met_graph.removeEdge(e);

			layoutContainer.getNodes().remove(originalNodeID);
			met_graph.removeNode(originalNode);
			
			
			cleanSelectionAndAggs();
			
			runActions();
		}
		
		notifyListeners();
	}
	
//	Should be used the method getNodesCoordinates
	@Deprecated 
	private Double[] getNodeCoordinates(String string) {
		
		Double[] result = new Double[2];
		
		Iterator reactionNodes = vis.items(LayoutUtils.NODES);
		
		while(reactionNodes.hasNext()){

			VisualItem item = (VisualItem) reactionNodes.next();

			if(item.canGetString(LayoutUtils.ID) && item.getString(LayoutUtils.ID).equals(string)){
				
				result[0] = item.getX();
				result[1] = item.getY();
			}
		}
		
		return result;
	}

	/**
	 * Given two {@link Node}s and new coordinates, merge these two {@link Node}s into a single 
	 * one, positioned in the given coordinates.
	 * @param sourceNode
	 * @param targetNode
	 * @param x
	 * @param y
	 */
	public void mergeNodes(Node sourceNode, Node targetNode, Double x, Double y) {
		
		synchronized (vis) {

			stopActions();
			
			
			UUID idr = UUID.randomUUID();
			String id = idr.toString();

			String label1 = sourceNode.getString(LayoutUtils.LABEL);
			String label2 = targetNode.getString(LayoutUtils.LABEL);

			String label = label1;
			if(!label1.equals(label2))
				label += " " + label2;

			Node nNode = met_graph.addNode();
			nNode.setString(LayoutUtils.ID, id);
			nNode.setString(LayoutUtils.LABEL, label);

			String nodeType1 = sourceNode.getString(LayoutUtils.TYPE);
			String nodeType2 = targetNode.getString(LayoutUtils.TYPE);

			String nodeType;
			if(nodeType1.equals(nodeType2)) nodeType = nodeType1;
			else
				nodeType = LayoutUtils.MET_T;

			nNode.setString(LayoutUtils.TYPE, nodeType);

			nNode.setBoolean(LayoutUtils.ISFIXED, true);
			nNode.setDouble(LayoutUtils.MY_X, x);
			nNode.setDouble(LayoutUtils.MY_Y, y);

			Double width1 = sourceNode.getDouble(OptVisualExtensions.VISUAL_WIDTH);
			Double height1 = sourceNode.getDouble(OptVisualExtensions.VISUAL_HEIGHT);
			Double width2 = targetNode.getDouble(OptVisualExtensions.VISUAL_WIDTH);
			Double height2 = targetNode.getDouble(OptVisualExtensions.VISUAL_HEIGHT);

			nNode.setDouble(OptVisualExtensions.VISUAL_HEIGHT, (height1+height2)/2);
			nNode.setDouble(OptVisualExtensions.VISUAL_WIDTH, (width1+width2)/2);

			nNode.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
			nNode.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);

			Iterator items = sourceNode.edges();
			String sourceNodeId = sourceNode.getString(LayoutUtils.ID);

			Set<String> reactionsSourceToEdit = new TreeSet<String>();
			Set<String> reactionsTargetToEdit = new TreeSet<String>();

			List<Edge> edgesToRemove = new ArrayList<Edge>();

			while(items.hasNext()){

				Edge edge = (Edge) items.next();

				if(!edge.getString(LayoutUtils.ID).equals(LayoutUtils.DUMMY)){
					Node reactionNode = edge.getAdjacentNode(sourceNode);

					Edge nEdge = met_graph.addEdge(reactionNode, nNode);
					nEdge.setString(LayoutUtils.ID, reactionNode.getString(LayoutUtils.ID));
					nEdge.setString(LayoutUtils.LABEL, reactionNode.getString(LayoutUtils.LABEL));
					nEdge.setString(LayoutUtils.TYPE, edge.getString(LayoutUtils.TYPE));
					nEdge.setDouble(LayoutUtils.ARROWSIZE, edge.getDouble(LayoutUtils.ARROWSIZE));
					nEdge.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, edge.getInt(BioVisualizerEdgeRenderer.EDGE_TYPE));
					nEdge.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
					nEdge.setBoolean(LayoutUtils.IS_REACTANT, edge.getBoolean(LayoutUtils.IS_REACTANT));
					
					edgesToRemove.add(edge);
					reactionsSourceToEdit.add(reactionNode.getString(LayoutUtils.ID));
				}
			}

			for(Edge e : edgesToRemove)
				met_graph.removeEdge(e);



			edgesToRemove = new ArrayList<Edge>();

			items = targetNode.edges();
			String targetNodeId = targetNode.getString(LayoutUtils.ID);

			while(items.hasNext()){

				Edge edge = (Edge) items.next();
				if(!edge.getString(LayoutUtils.ID).equals(LayoutUtils.DUMMY)){
					Node reactionNode = edge.getAdjacentNode(targetNode);

					Edge nEdge = met_graph.addEdge(reactionNode, nNode);
					nEdge.setString(LayoutUtils.ID, reactionNode.getString(LayoutUtils.ID));
					nEdge.setString(LayoutUtils.LABEL, reactionNode.getString(LayoutUtils.LABEL));
					nEdge.setString(LayoutUtils.TYPE, edge.getString(LayoutUtils.TYPE));
					nEdge.setDouble(LayoutUtils.ARROWSIZE, edge.getDouble(LayoutUtils.ARROWSIZE));
					nEdge.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, edge.getInt(BioVisualizerEdgeRenderer.EDGE_TYPE));
					nEdge.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
					nEdge.setBoolean(LayoutUtils.IS_REACTANT, edge.getBoolean(LayoutUtils.IS_REACTANT));
					
					edgesToRemove.add(edge);
					reactionsTargetToEdit.add(reactionNode.getString(LayoutUtils.ID));
				}
			}

			for(Edge e : edgesToRemove)
				met_graph.removeEdge(e);

			Set<String> metIdsLay = layoutContainer.getNodes().get(sourceNodeId).getIds();
			metIdsLay.addAll(layoutContainer.getNodes().get(targetNodeId).getIds());


			NodeTypeLay nodeTypeLay = layoutContainer.getNodes().get(sourceNodeId).getNodeType();

			layoutContainer.addNode(id, label, metIdsLay, nodeTypeLay, x, y);

			for(String s : reactionsSourceToEdit){

				IReactionLay reaction = layoutContainer.getReactions().get(s);
				if(reaction.getProducts().containsKey(sourceNodeId)){
					reaction.getProducts().remove(sourceNodeId);
					reaction.getProducts().put(id, layoutContainer.getNodes().get(id));
				}
				else if(reaction.getReactants().containsKey(sourceNodeId)){
					reaction.getReactants().remove(sourceNodeId);
					reaction.getReactants().put(id, layoutContainer.getNodes().get(id));
				}
			}

			for(String s : reactionsTargetToEdit){

				IReactionLay reaction = layoutContainer.getReactions().get(s);
				if(reaction.getProducts().containsKey(targetNodeId)){
					reaction.getProducts().remove(targetNodeId);
					reaction.getProducts().put(id, layoutContainer.getNodes().get(id));
				}
				else if(reaction.getReactants().containsKey(targetNodeId)){
					reaction.getReactants().remove(targetNodeId);
					reaction.getReactants().put(id, layoutContainer.getNodes().get(id));
				}
			}

			layoutContainer.getNodes().remove(sourceNodeId);
			layoutContainer.getNodes().remove(targetNodeId);


			met_graph.removeNode(sourceNode);
			met_graph.removeNode(targetNode);

			notifyListeners();
			
			cleanSelectionAndAggs();
			
			runActions();

		}
	}
	
	private void stopActions(){
		vis.cancel(LayoutUtils.default_nodeColoring);
//		vis.cancel(LayoutUtils.default_EdgeSizeAction);
		vis.cancel(LayoutUtils.default_EdgeColorAction);
		vis.cancel("layout");
	}
	
	private void removeActions(){
		vis.removeAction(LayoutUtils.default_nodeColoring);
//		vis.removeAction(LayoutUtils.default_EdgeSizeAction);
		vis.removeAction(LayoutUtils.default_EdgeColorAction);
		vis.removeAction(LayoutUtils.default_layout_action);
		vis.removeAction(LayoutUtils.default_repaint_action);
	}
	
	private void runActions(){
		vis.run(LayoutUtils.default_nodeColoring);
//		vis.run(LayoutUtils.default_EdgeSizeAction);
		vis.run(LayoutUtils.default_EdgeColorAction);
		vis.run("layout");
	}

	private void cleanSelectionAndAggs() {
		TupleSet ts = vis.getFocusGroup(LayoutUtils.SELECTED);
		ts.clear();
		at.clear();
		
	}

	
	
	public void replicateReaction(Node reactionNode){
		stopActions();
		
		String rId = reactionNode.getString(LayoutUtils.ID);
		IReactionLay reaction = reactions.get(rId);
		addReaction(reaction);
	
		runActions();
	
		notifyListeners();
	}
	
	/**
	 * If a reaction node has more than 1 id, these reactions can be replicated.
	 * @param reactionNode
	 */
	public void unfoldReaction(Node reactionNode){
		
		synchronized (vis) {

			stopActions();
			
			
			
			String rId = reactionNode.getString(LayoutUtils.ID);

			//This is the first reaction!
			IReactionLay reaction = reactions.get(rId);
			boolean originalReversibility = reactions.get(rId).isReversible();
			
			Set<String> metIds = reaction.getIDs();

			for(String mId : metIds){

				//Criar uma rea����o para cada e copiar os nodos
				Map<String, INodeLay> reactants = new HashMap<String, INodeLay>(reaction.getReactants());
				Map<String, INodeLay> products = new HashMap<String, INodeLay>(reaction.getProducts());

				String id = UUID.randomUUID().toString();
				String label = mId; //still not sure about this


				Node nNode = met_graph.addNode();
				nNode.setString(LayoutUtils.ID, id);
				nNode.setString(LayoutUtils.LABEL, label);
				nNode.setString(LayoutUtils.TYPE, LayoutUtils.REA_T);

				nNode.setDouble(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.reactionNodeSize);
				nNode.setDouble(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.reactionNodeSize);

				nNode.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				nNode.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);

				nNode.setBoolean(LayoutUtils.ISFIXED, false);

				Iterator edges = reactionNode.edges();

				while(edges.hasNext()){
					Edge edge = (Edge) edges.next();

					Node metNode = edge.getAdjacentNode(reactionNode);
					Edge nEdge = met_graph.addEdge(nNode, metNode);

					nEdge.setString(LayoutUtils.ID, id);
					nEdge.setString(LayoutUtils.LABEL, mId);
					nEdge.setString(LayoutUtils.TYPE, edge.getString(LayoutUtils.TYPE));
					nEdge.setDouble(LayoutUtils.ARROWSIZE, edge.getDouble(LayoutUtils.ARROWSIZE));
					nEdge.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, edge.getInt(BioVisualizerEdgeRenderer.EDGE_TYPE));
					
					nEdge.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
					nEdge.setBoolean(LayoutUtils.IS_REACTANT, edge.getBoolean(LayoutUtils.IS_REACTANT));
				}
				
				Set<String> nmetIds = new HashSet<String>();
				nmetIds.add(mId);
				
				this.layoutContainer.addReaction(id, label, nmetIds, reaction.infos(), reactants, products, originalReversibility, null, null);
			}

			this.layoutContainer.getReactions().remove(rId);
			met_graph.removeNode(reactionNode);

			
			
			cleanSelectionAndAggs();
			
			runActions();
			
		}
		notifyListeners();
	}
	
	/**
	 * Merges reactions that share exactly the same metabolites.
	 * @param sourceReaction
	 * @param targetReaction
	 * @param x
	 * @param y
	 */
	public void mergeReactions(Node sourceReaction, Node targetReaction, double x, double y){

		synchronized (vis) {

			stopActions();

			
			String sourceReaction_id = sourceReaction.getString(LayoutUtils.ID);
			String targetReaction_id = targetReaction.getString(LayoutUtils.ID);

			String uniqueID = UUID.randomUUID().toString();

			String label = layoutContainer.getReactions().get(sourceReaction_id).getLabel();
			
			if(!label.equals(layoutContainer.getReactions().get(targetReaction_id).getLabel()))
				label+= " " + layoutContainer.getReactions().get(targetReaction_id).getLabel();

			Set<String> metIds = layoutContainer.getReactions().get(sourceReaction_id).getIDs();
			metIds.addAll(layoutContainer.getReactions().get(targetReaction_id).getIDs());
			
			boolean isReversible = layoutContainer.getReactions().get(sourceReaction_id).isReversible() || layoutContainer.getReactions().get(targetReaction_id).isReversible();

			Map<String, INodeLay> infos = new HashMap<String, INodeLay>(layoutContainer.getReactions().get(sourceReaction_id).infos());
			for(String id : layoutContainer.getReactions().get(targetReaction_id).infos().keySet()){
				infos.put(id, layoutContainer.getReactions().get(targetReaction_id).infos().get(id));
			}

			Map<String, INodeLay> reactants = new HashMap<String, INodeLay>(layoutContainer.getReactions().get(sourceReaction_id).getReactants());
			for(String id : layoutContainer.getReactions().get(targetReaction_id).getReactants().keySet())
				reactants.put(id, layoutContainer.getReactions().get(targetReaction_id).getReactants().get(id));

			Map<String, INodeLay> products = new HashMap<String, INodeLay>(layoutContainer.getReactions().get(sourceReaction_id).getProducts());
			for(String id : layoutContainer.getReactions().get(targetReaction_id).getProducts().keySet())
				products.put(id, layoutContainer.getReactions().get(targetReaction_id).getProducts().get(id));


			Node newReaction = met_graph.addNode();
			newReaction.setString(LayoutUtils.ID, uniqueID);
			newReaction.setString(LayoutUtils.LABEL, label);
			newReaction.setString(LayoutUtils.TYPE, LayoutUtils.REA_T);

			newReaction.setDouble(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.reactionNodeSize);
			newReaction.setDouble(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.reactionNodeSize);

			newReaction.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
			newReaction.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);

			newReaction.setBoolean(LayoutUtils.ISFIXED, true);
			newReaction.setDouble(LayoutUtils.MY_X, x);
			newReaction.setDouble(LayoutUtils.MY_Y, y);


			//First, add all the connections to the new node

			Iterator sourceEdges = sourceReaction.edges();

			while(sourceEdges.hasNext()){
				Edge edgeItem = (Edge) sourceEdges.next();

				Node metNode = edgeItem.getAdjacentNode(sourceReaction);

				Edge newEdge = met_graph.addEdge(newReaction, metNode);
				newEdge.setString(LayoutUtils.ID, uniqueID);
				newEdge.setString(LayoutUtils.LABEL, label);
				newEdge.setString(LayoutUtils.TYPE, edgeItem.getString(LayoutUtils.TYPE));
				newEdge.setDouble(LayoutUtils.ARROWSIZE, edgeItem.getDouble(LayoutUtils.ARROWSIZE));
				newEdge.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, edgeItem.getInt(BioVisualizerEdgeRenderer.EDGE_TYPE));
				
				newEdge.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				newEdge.setBoolean(LayoutUtils.IS_REACTANT, edgeItem.getBoolean(LayoutUtils.IS_REACTANT));
			}


			met_graph.removeNode(sourceReaction);
			met_graph.removeNode(targetReaction);

			layoutContainer.getReactions().remove(sourceReaction_id);
			layoutContainer.getReactions().remove(targetReaction_id);
			layoutContainer.addReaction(uniqueID, label, metIds, infos, reactants, products, isReversible, x, y);

			notifyListeners();

			cleanSelectionAndAggs();
			
//			FIXME: we should do something to labels. imagine if we add new metabolite.
//			colorShapeManager.set;
			
			runActions();
		}
	}
	
	/**
	 * Adds a reaction to the visualization and to the {@link ILayout}. If the 
	 * nodes aren't present in the {@link ILayout} they will be added in the end.
	 * @param reaction
	 */
	public void addReaction(IReactionLay reaction){
		
		synchronized (vis) {

			Node r1 = met_graph.addNode();
			nodes.put(reaction.getUniqueId(), r1);
			somethingToUniqueId.put(reaction.getUniqueId(), reaction.getUniqueId());
			r1.setString(LayoutUtils.LABEL, reaction.getLabel());
			r1.setString(LayoutUtils.ID, reaction.getUniqueId());
			r1.setString(LayoutUtils.TYPE, LayoutUtils.REA_T);
			r1.setInt(VisualItem.SHAPE, Constants.SHAPE_RECTANGLE);
			r1.setDouble(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.reactionNodeSize);
			r1.setDouble(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.reactionNodeSize);



			if(reaction.getX()!=null && reaction.getY()!=null){
				r1.setBoolean(LayoutUtils.ISFIXED, true);
				r1.setDouble(LayoutUtils.MY_X, reaction.getX());
				r1.setDouble(LayoutUtils.MY_Y, reaction.getY());
			}
			
			r1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
			r1.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);
			
			Map<String, INodeLay> metabolites = reaction.getReactants();

			/**
			 * Reactants
			 */
			
			for(String metId : metabolites.keySet()){
				
				Node n1 = null;
				
				if(!layoutContainer.getNodes().containsKey(metId)){
					
					INodeLay met = metabolites.get(metId);
					
					int dim2 = 40;
					if(met.getNodeType().equals(NodeTypeLay.CURRENCY)) dim2 = 20;
					n1 = buildNode(met,20, dim2);nodes.put(met.getUniqueId(), n1);
					nodes.put(met.getUniqueId(), n1);
					somethingToUniqueId.put(met.getLabel(), met.getUniqueId());
				}
				else{
					n1 = getNode(metId);
				}
				
				Edge e1 = null;
				e1 = met_graph.addEdge(r1, n1);
				
				if(metabolites.get(metId).getNodeType().equals(NodeTypeLay.CURRENCY)){
					e1.setString(LayoutUtils.TYPE,LayoutUtils.LINK_HUBS);
				}
				else
					e1.setString(LayoutUtils.TYPE, LayoutUtils.LINK_CONVERTION);

					e1.setDouble(LayoutUtils.ARROWSIZE, 2);

				if(reaction.isReversible()){
					e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);	 
				}
				else{
					e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
				}

				e1.setString(LayoutUtils.ID, reaction.getUniqueId());
				e1.setString(LayoutUtils.LABEL, reaction.getUniqueId());
				e1.setBoolean(LayoutUtils.IS_REACTANT, true);
				
				//No special color for edge for now!
				e1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				e1.setBoolean(LayoutUtils.HAS_THICKNESS, false);
			}

			/**
			 * Products
			 */
			metabolites = reaction.getProducts();
			for(String metId : metabolites.keySet()){
				
				Node n1 = null;
				if(!layoutContainer.getNodes().containsKey(metId)){

					INodeLay met = metabolites.get(metId);
					int dim2 = 40;
					if(met.getNodeType().equals(NodeTypeLay.CURRENCY)) dim2 = 20;
					n1 = buildNode(met,20, dim2);
					nodes.put(met.getUniqueId(), n1);
					somethingToUniqueId.put(met.getLabel(), met.getUniqueId());
					
				}
				else{
					n1 = getNode(metId);
				}

				Edge e1 = null;
				e1 = met_graph.addEdge(r1, n1); 
				if(metabolites.get(metId).getNodeType().equals(NodeTypeLay.CURRENCY)){
					e1.setString(LayoutUtils.TYPE,LayoutUtils.LINK_HUBS);
				}
				else
					e1.setString(LayoutUtils.TYPE, LayoutUtils.LINK_CONVERTION);

					e1.setDouble(LayoutUtils.ARROWSIZE, 2);
				
				e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);
				
				e1.setString(LayoutUtils.LABEL, reaction.getLabel());
				e1.setString(LayoutUtils.ID, reaction.getUniqueId());
				e1.setBoolean(LayoutUtils.IS_REACTANT, false);
				
				//No special color for edge for now!
				e1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				e1.setBoolean(LayoutUtils.HAS_THICKNESS, false);

			}

			/**
			 * Infos
			 */
			Map<String, INodeLay> infos = reaction.infos();
			for(String infoId : infos.keySet()){
				Node n1 = null;
				INodeLay info = infos.get(infoId);

				n1 = buildNode(info, 25, 80);
				
				Edge e1 = null;
				e1 = met_graph.addEdge(r1, n1);
				e1.setDouble(LayoutUtils.ARROWSIZE, 1);

				e1.setString(LayoutUtils.TYPE, LayoutUtils.LINK_INFO);
				e1.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
				
				e1.setString(LayoutUtils.ID, reaction.getUniqueId());
				e1.setString(LayoutUtils.LABEL, reaction.getLabel());
				
				//No special color for edge for now!
				e1.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				e1.setBoolean(LayoutUtils.HAS_THICKNESS, false);
				
			}
			
			for(String metId : reaction.getReactants().keySet()){
				
				if(!layoutContainer.getNodes().containsKey(metId)){
					
					INodeLay node = reaction.getReactants().get(metId);
					layoutContainer.addNode(node.getUniqueId(), node.getLabel(), node.getIds(), node.getNodeType(), node.getX(), node.getY());
				}
			}

			for(String metId : reaction.getProducts().keySet()){

				if(!layoutContainer.getNodes().containsKey(metId)){

					INodeLay node = reaction.getProducts().get(metId);
					layoutContainer.addNode(node.getUniqueId(), node.getLabel(), node.getIds(), node.getNodeType(), node.getX(), node.getY());
				}
			}

			layoutContainer.addReaction(reaction.getUniqueId(), reaction.getLabel(), reaction.getIDs(), reaction.infos(), reaction.getReactants(), reaction.getProducts(), reaction.isReversible(), reaction.getX(), reaction.getY());
			
			cleanSelectionAndAggs();
			
			
		}
		notifyListeners();
		

	
	}
	
	/**
	 * Returns a node with the given Id.
	 * @param metId
	 * @return Node
	 */
	private Node getNode(String metId) {
		
		TupleSet items = vis.getGroup(LayoutUtils.NODES);
		
		Iterator iter = vis.items(LayoutUtils.NODES);
		
		while(iter.hasNext()){
			
			VisualItem nodeItem = (VisualItem) iter.next();
			if(nodeItem.getString(LayoutUtils.ID).equals(metId)){
				return (Node) nodeItem.getSourceTuple();
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a new layout (cloned) with the current set of coordinates from 
	 * the {@link Display}.
	 * @return
	 */
	public ILayout getNewLayoutWithCoords(){
		ILayout layout = layoutContainer.clone();
		layout.setNewCoordinates(getNodesCoordinates());
		return layout;
	}

	/**
	 * Change the {@link Node} to currency, and all the nodes that have the same metabolic
	 * ids. Used to transform metabolites into currency
	 * @param visualItem
	 */
	public void changeTypeToCurrency(VisualItem visualItem) {
		
		synchronized (vis) {
			Node node = (Node) visualItem.getSourceTuple();
			changeTypeToCurrency(node);
			
		}
		
	}
	
	
	public void changeEdgeType(Node node, String type) {
		
		synchronized (vis) {
			
			node.set(BioVisualizerConvEdgeRenderer.MY_EDGE_TYPE, type);
			
		}
		
	}
	
	public void changeTypeToCurrency(Node node){
		String uniqueID = node.getString(LayoutUtils.ID);

		//Change all nodes with the same metabolic ids
		Set<String> ids = layoutContainer.getNodes().get(uniqueID).getIds();
		
		changeNodesType(ids, LayoutUtils.HUBS, LayoutUtils.LINK_HUBS, NodeTypeLay.CURRENCY, 20.0, 20.0);
		notifyListeners();
	}
	
	/**
	 * Change the {@link Node} to metabolite, and all the nodes that have the same metabolic
	 * ids. Used to transform currency into metabolites
	 * @param visualItem {@link VisualItem}
	 */
	public void changeTypeToMetabolite(VisualItem visualItem) {
		
		synchronized (vis) {
			Node node = (Node) visualItem.getSourceTuple();
	
//			colorShapeManager.updateLabel(visualItem);
			changeTypeToMetabolite(node);
		}
	}
	
	public void changeTypeToMetabolite(Node node){
		String uniqueID = node.getString(LayoutUtils.ID);
//		layoutContainer.getNodes().get(uniqueID).setNodeType(NodeTypeLay.METABOLITE);
		Set<String> ids = layoutContainer.getNodes().get(uniqueID).getIds();
		
		changeNodesType(ids, LayoutUtils.MET_T, LayoutUtils.LINK_CONVERTION, NodeTypeLay.METABOLITE, LayoutUtils.defaultMetaboliteWidth, LayoutUtils.defaultNodeHeight);
		notifyListeners();
	}
	
	/**
	 * Changes the type of the {@link Node}s that have the same metabolic identifiers
	 * @param ids - metabolic identifiers
	 * @param nodeTypeForTabel - LayoutUtils string correspondent to the node type
	 * @param edgeTypeForTable - LayoutUtils string correspondent to the link type
	 * @param nodeTypeForILayout - {@link NodeTypeLay}- nodetype for the ILAYOUT
	 * @param width - widht of the node this new type
	 * @param height - height of the nodes of this new type
	 */
	private void changeNodesType(Set<String> ids, String nodeTypeForTabel, String edgeTypeForTable, NodeTypeLay nodeTypeForILayout, Double width, Double height){
		
		Iterator iter = vis.items(LayoutUtils.NODES);

		while (iter.hasNext()){

			VisualItem nodeItem = (VisualItem)iter.next();
			
			
			String nodeId = nodeItem.getString(LayoutUtils.ID);

			if(layoutContainer.getNodes().get(nodeId)!= null 
					&&  ids.size() == layoutContainer.getNodes().get(nodeId).getIds().size() 
					&& CollectionUtils.getReunionValues(ids, layoutContainer.getNodes().get(nodeId).getIds()).size() == ids.size()){

				nodeItem.setString(LayoutUtils.TYPE, nodeTypeForTabel);
				nodeItem.setDouble(OptVisualExtensions.VISUAL_HEIGHT, height);
				nodeItem.setDouble(OptVisualExtensions.VISUAL_WIDTH, width);
				colorShapeManager.updateLabel(nodeItem);

				Iterator edgesIter = ((Node)nodeItem.getSourceTuple()).edges();
				while(edgesIter.hasNext()){
					Edge edgeItem = (Edge) edgesIter.next();
					edgeItem.setString(LayoutUtils.TYPE, edgeTypeForTable);
				}
				
				layoutContainer.getNodes().get(nodeId).setNodeType(nodeTypeForILayout);
				
			}

		}
	}
	
	/**
	 * Updates the ILayouy with the new coordinates. It has a boolean
	 * that will notify the listeners if saving is needed - to avoid saving everytime
	 * a position is updated and it's not needed.
	 * 
	 * @param item - {@link VisualItem}
	 * @param save - true - will notify the listeners
	 */
	public void updatePositionInILayout(VisualItem item, boolean save) {
		String id = item.getString(LayoutUtils.ID);
		
		if(id!=null && id!=LayoutUtils.DUMMY){
			
			if(item.getBoolean(LayoutUtils.ISFIXED)){

				if(layoutContainer.getNodes().get(id)!=null){
					item.setDouble(LayoutUtils.MY_X, item.getX());
					item.setDouble(LayoutUtils.MY_Y, item.getY());
					layoutContainer.getNodes().get(id).setX(item.getX());
					layoutContainer.getNodes().get(id).setY(item.getY());
				}
				else if(layoutContainer.getReactions().get(id)!=null){
					item.setDouble(LayoutUtils.MY_X, item.getX());
					item.setDouble(LayoutUtils.MY_Y, item.getY());
					layoutContainer.getReactions().get(id).setX(item.getX());
					layoutContainer.getReactions().get(id).setY(item.getY());
				}
			}
			else{

				
					if(layoutContainer.getNodes().get(id)!=null){
						layoutContainer.getNodes().get(id).setX(null);
						layoutContainer.getNodes().get(id).setY(null);
					}
					else if(layoutContainer.getReactions().get(id)!=null){
						layoutContainer.getReactions().get(id).setX(null);
						layoutContainer.getReactions().get(id).setY(null);
					}
				}
	
		}
		
		if(save)
			notifyListeners();
	}
	
	public void setNewReactionLabels(Map<String, String> labels){
		colorShapeManager.setNewReactionLabels(labels);
	}
	
	public void setNewNodeLabels(Map<String, String> labels){
		colorShapeManager.overlapNodeLabels(labels);
	}
	
	public void setArrowShapesByDirection(Map<String, Boolean> directions){
		colorShapeManager.setArrowShapesByBoolean(directions, this.layoutContainer.getReactions());
	}
	
	public void setNewEdgeThickness(Map<String, Double> edgesThickness){
		colorShapeManager.setNewThickness(edgesThickness);
	}
	
	public void resetEdgesThickness(){
		colorShapeManager.resetEdgeThickness();
	}
	
	public void resetSpecialColorsAndShappes(){
		colorShapeManager.resetGroupSpecialFields(LayoutUtils.NODES);
		colorShapeManager.resetGroupSpecialFields(LayoutUtils.EDGES);
	}
	
	public void resetArrowShapes(){
		colorShapeManager.resetArrowShapes(this.layoutContainer.getReactions());
	}
	
	public void overLapSpecialColorsAndShapes(Map<String, Integer> nodeColors, Map<String, Integer> nodeShapes, Map<String, Pair<Integer, Integer>> reactionColors, Map<String, Integer> reactionShapes){
		colorShapeManager.overlapEdgesAndNodes(nodeColors, nodeShapes, reactionColors, reactionShapes);
	}
	
	public void resetReactionLabels(){
		colorShapeManager.resetReactionLabels();
	}
	
	public void resetNodeLabels(){
		colorShapeManager.resetNodeLabels();
	}
	
	public void addNodeSizes(Map<String, Pair<Double,Double>> sizes){
		colorShapeManager.addNewNodesSize(sizes);
	}
	
	public void addReactionSizes(Map<String, Pair<Double, Double>> sizes){
		colorShapeManager.addNewNodesSize(sizes);
	}
	
	public void resetNodeSizes(){
		colorShapeManager.resetNodesSizes();
	}
	
	/**
	 * Adds a listener to the visualization.
	 * @param listener
	 */
	public void addChangeLayoutListener(ChangeLayoutListener listener){
		this.listeners.add(listener);
	}
	
	public void removeChangeLayoutListener(ChangeLayoutListener listener) {
		this.listeners.remove(listener);
		
	}
	
	@Deprecated
	public int sizeListeners(){
		return this.listeners.size();
	}
	
	/**
	 * Notifies the listeners connected to the visualization of the 
	 * {@link ILayout}
	 */
	public void notifyListeners(){
		for(ChangeLayoutListener listener : listeners)
			listener.layoutChanged();
	}
	
	public void addNodeClickListener(NodeClickingListener listener){
		this.nodeClickControl.addNodeClickListener(listener);
	}
	
	/**
	 * Zooms the display according to a scale. If the 
	 * scale is smaller than 1 it will zoom out.
	 * @param scale
	 */
	public void zoomIn(double scale){
		
		Point2D p = display.getLocation();
		p.setLocation((p.getX() + display.getWidth()/2), (p.getY() + display.getHeight())/2);
		display.zoom(p, scale);
	}
	
	/**
	 * Fits the network to the display screen.
	 */
	public void fitToScreen(){
		
		int m_margin = 50;
		long m_duration = 2000;
		
		Visualization vis = display.getVisualization();
		
        Rectangle2D bounds = vis.getBounds(Visualization.ALL_ITEMS);
        GraphicsLib.expand(bounds, m_margin + (int)(1/display.getScale()));
        DisplayLib.fitViewToBounds(display, bounds, m_duration);
	}
	
	
//	public void ajustCentroid(){
////		Rectangle2D bounds = vis.getBounds(Visualization.ALL_ITEMS);
//		
//		Point p = new Point(0, 0);
//
//		DisplayLib.fitViewToBounds(display, display.getBounds(),p , 2000);
//	}
	
	public Set<String> getSelectedReactions(){
		
		Set<String> result = new HashSet<String>();
		
		TupleSet focus = vis.getFocusGroup(LayoutUtils.SELECTED);
		if(focus.getTupleCount()>0){
			
			Iterator it = focus.tuples();
			while(it.hasNext()){
				
				VisualItem item = (VisualItem) it.next();
				if(item.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T))
					result.add(item.getString(LayoutUtils.ID));
			}
			
		}
		
		return result;
	}
	
	public void setHighlightMetabolites(Set<String> mets){
		
		Iterator items = vis.items(LayoutUtils.NODES);
		
		TupleSet focus = vis.getFocusGroup(LayoutUtils.INPATHWAY);
		focus.clear();
		
		while(items.hasNext()){
			
			VisualItem item = (VisualItem) items.next();
			
			String uniqueId = item.getString(LayoutUtils.ID);
			
//			System.out.println(uniqueId);
			
			if(layoutContainer.getNodes().keySet().contains(uniqueId)){
//				System.out.println(layoutContainer.getNodes().keySet().size() + "\t" + layoutContainer.getNodes().keySet());
//				System.out.println(layoutContainer.getNodes().get(uniqueId).getIds());
				
				
				Set<String> ids = layoutContainer.getNodes().get(uniqueId).getIds();
				
				if(CollectionUtils.getIntersectionValues(mets, ids).size()>0){
					focus.addTuple(item);
//					System.out.println("ENTREI...");
				}
			}
			
			
		}
	}
	
	public void clearMetabolitesHighlight(){
		TupleSet focus = vis.getFocusGroup(LayoutUtils.INPATHWAY);
		focus.clear();
		
	}

	public void removeChangeListener(){
		listeners.clear();
	}
	
	public void removeAllListeners(){
		removeChangeListener();
		nodeClickControl.removeAllListeners();
		
	}
	
	public void stopAndclear(){
		System.out.println("Cleanning....");
		
		
//		ActivityManager.stopThread();
		
		synchronized (vis) {			
			fixAllNodes();
			colorShapeManager.stopDecorators();
			
			removeRubberBand();
			removeControlersToDisplay();
			vis.reset();
			display.reset();
			removeActions();
			nodeClickControl.reset();
			this.met_graph.clear();
			met_graph.clearSpanningTree();
			try {
				colorShapeManager.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
//			removeActions();
			
//			removeControlersToDisplay();
//			display.reset();
//			vis.removeGroup("graph");
//			

		}
//		ActivityManager.run();
		
	}

private void removeRubberBand() {

	//	VisualItem rubberBand = (VisualItem) vis.getVisualGroup(LayoutUtils.RUBBER_BAND).tuples().next();
	vis.removeGroup(LayoutUtils.RUBBER_BAND);
	}

//	public void runNewProperties(VisualizationProperties vps) {
//		
//		Map<String, String> metaboliteLabels = vps.getMetaboliteLabels();
//		Map<String, String> reactionLabels = vps.getReactionLabels();
//		Map<String, String> informationLabels = vps.getInformationLabels();
//		Map<String, String> currencyLabels = vps.getCurrencyLabels();
//		
//		for(String n : layoutContainer.getNodes().keySet()){
//			if(metaboliteLabels.containsKey(n)) layoutContainer.getNodes().get(n).setLabel(metaboliteLabels.get(n));
//			if(informationLabels.containsKey(n)) layoutContainer.getNodes().get(n).setLabel(informationLabels.get(n));
//			if(currencyLabels.containsKey(n)) layoutContainer.getNodes().get(n).setLabel(currencyLabels.get(n));
//		}
//		
//		for(String r : layoutContainer.getReactions().keySet())
//			if(reactionLabels.containsKey(r)) layoutContainer.getReactions().get(r).setLabel(reactionLabels.get(r));
//		
//		colorShapeManager.runNewProperties(vps);
//		
//	}
	
	synchronized public void runNewProperties(VisualizationProperties vps) {
		
		Map<String, String> metaboliteLabels = vps.getMetaboliteLabels();
		Map<String, String> reactionLabels = vps.getReactionLabels();
		Map<String, String> informationLabels = vps.getInformationLabels();
		Map<String, String> currencyLabels = vps.getCurrencyLabels();
//		
//		for(String n : layoutContainer.getNodes().keySet())
//		{
//			String newNodeLabel = "";
//			for(String metabolicId : layoutContainer.getNodes().get(n).getIds())
//			{
//				if(metaboliteLabels.containsKey(metabolicId))
//					newNodeLabel += "\n" + metaboliteLabels.get(metabolicId);
//				if(currencyLabels.containsKey(metabolicId))
//					newNodeLabel += "\n" + currencyLabels.get(metabolicId);
//				if(informationLabels.containsKey(metabolicId))
//					newNodeLabel += "\n" + informationLabels.get(metabolicId);
//			}
//			if(!newNodeLabel.equals(""))
//				layoutContainer.getNodes().get(n).setLabel(newNodeLabel);
//		}
//		
//		for(String r : layoutContainer.getReactions().keySet())
//		{
//			String newReactionLabel = "";
//			for(String metabolicId : layoutContainer.getReactions().get(r).getIDs())
//			{
//				if(reactionLabels.containsKey(metabolicId))
//					newReactionLabel += "\n" + reactionLabels.get(metabolicId);
//			}
//			if(!newReactionLabel.equals(""))
//				layoutContainer.getReactions().get(r).setLabel(newReactionLabel);
//		}
		
//		for(int i=0; i<met_graph.getNodeCount(); i++)
//		{
//			String nodeId = met_graph.getNode(i).getString(LayoutUtils.ID);
//			
//			INodeLay node = this.layoutContainer.getNodes().get(nodeId);
//			
//			if(node!=null){
//				
//				System.out.println(node.getNodeType());
//				if(node.getNodeType().equals(NodeTypeLay.METABOLITE) && vps.getMetaboliteLabels().containsKey(nodeId))
//					met_graph.getNode(i).setString(LayoutUtils.LABEL, vps.getMetaboliteLabels().get(nodeId));
//				if(node.getNodeType().equals(NodeTypeLay.CURRENCY) && vps.getCurrencyLabels().containsKey(nodeId))
//					met_graph.getNode(i).setString(LayoutUtils.LABEL, vps.getCurrencyLabels().get(nodeId));
//				
//				System.out.println(met_graph.getNode(i).getString(LayoutUtils.LABEL));
//				System.out.println();
//			}else if(vps.getReactionLabels().containsKey(nodeId))
//				met_graph.getNode(i).setString(LayoutUtils.LABEL, vps.getReactionLabels().get(nodeId));
//			
////			FIXME: what whe can do with information Nodes
////			if(vps.getInformationLabels().containsKey(nodeId))
////				met_graph.getNode(i).setString(LayoutUtils.LABEL, vps.getInformationLabels().get(nodeId));
//			
//		}
		stopActions();
		colorShapeManager.runNewProperties(vps);
		runActions();
	}

	public void removeReactions(Set<String> uuids) {
		Map<String, Node> nodeReactions = new HashMap<String, Node>();
		
		
		Iterator iter = vis.items(LayoutUtils.NODES);
		
		while(iter.hasNext()){
			VisualItem nodeItem = (VisualItem) iter.next();
			String id = nodeItem.getString(LayoutUtils.ID);
			
			if(uuids.contains(id)){
				nodeReactions.put(id, (Node) nodeItem.getSourceTuple());
			}
		}
		
		for(Node n : nodeReactions.values())
			removeReaction(n);
	}


	
}

