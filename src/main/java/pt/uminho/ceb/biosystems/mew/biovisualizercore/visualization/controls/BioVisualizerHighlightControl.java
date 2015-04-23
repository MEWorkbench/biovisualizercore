package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls;

import java.awt.event.MouseEvent;
import java.util.Iterator;

import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

/**
 * <p>
 * A ControlListener that sets the highlighted status (using the
 * {@link prefuse.visual.VisualItem#setHighlighted(boolean)
 * VisualItem.setHighlighted} method) for nodes neighboring the node 
 * currently under the mouse pointer and for nodes that have the same
 * id, considering the type of the node. 
 * The highlight flag might then be used
 * by a color function to change node appearance as desired.
 * This class is a copy with alterations from the NeighborHighLightControl
 * class from the prefuse package.
 * </p>
 *
 * @author <a>Alberto Noronha</a>
 */
public class BioVisualizerHighlightControl extends ControlAdapter{
	 private String activity = null;
	    private boolean highlightWithInvisibleEdge = false;
		private Visualization m_vis;
	    
	    /**
	     * Creates a new highlight control.
	     */
	    public BioVisualizerHighlightControl(Visualization vis) {
	        this.m_vis = vis;
	    }
	    
	    /**
	     * Creates a new highlight control that runs the given activity
	     * whenever the neighbor highlight changes.
	     * @param activity the update Activity to run
	     */
	    public BioVisualizerHighlightControl(String activity, Visualization vis) {
	        this.activity = activity;
	        this.m_vis = vis;
	    }
	    
	    /**
	     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	     */
	    public void itemEntered(VisualItem item, MouseEvent e) {
	        if ( item instanceof NodeItem )
	            setNeighborHighlight((NodeItem)item, true);
	    }
	    
	    /**
	     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	     */
	    public void itemExited(VisualItem item, MouseEvent e) {
	        if ( item instanceof NodeItem )
	            setNeighborHighlight((NodeItem)item, false);
	    }
	    
	    /**
	     * Set the highlighted state of the neighbors of a node if it is a reaction.
	     * Set the highligted state of nodes with the same id if it is a metabolite.
	     * @param n the node under consideration
	     * @param state the highlighting state to apply to neighbors
	     */
	    protected void setNeighborHighlight(NodeItem n, boolean state) {
	    	if(n.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T) || n.getString(LayoutUtils.TYPE).equals(LayoutUtils.INFO)){
	    		Iterator iter = n.edges();
	    		while ( iter.hasNext() ) {
	    			EdgeItem eitem = (EdgeItem)iter.next();
	    			NodeItem nitem = eitem.getAdjacentItem(n);
	    			if (eitem.isVisible() || highlightWithInvisibleEdge) {
	    				eitem.setHighlighted(state);
	    				nitem.setHighlighted(state);
	    			}
	    		}
	    	}
	    	else{
	    		/**
	    		 * Get the Nodes with the same ID!
	    		 */
	    		
//	    		String nodeId = n.getSourceTuple().getString(LayoutUtils.ID);
	    		String nodeLabel = n.getSourceTuple().getString(LayoutUtils.LABEL);
	    		
//	    		if(this.mapOfNodes.containsKey(nodeLabel)){
//	    			for(Node node : mapOfNodes.get(nodeLabel)){
//	    				NodeItem nitem = (NodeItem) m_vis.getVisualItem(LayoutUtils.NODES, node);
//	    				nitem.setHighlighted(state);
//	    			}
//	    		}
	    		
	    		Iterator items = m_vis.items(LayoutUtils.NODES);
	    		while(items.hasNext()){
	    			VisualItem vitem = (VisualItem) items.next();
	    			if(vitem.canGetString(LayoutUtils.LABEL) && vitem.getString(LayoutUtils.LABEL).equals(nodeLabel))
	    				vitem.setHighlighted(state);
	    		}
	    	}
	        if ( activity != null )
	            n.getVisualization().run(activity);
	    }
	    
	    /**
	     * Indicates if neighbor nodes with edges currently not visible still
	     * get highlighted.
	     * @return true if neighbors with invisible edges still get highlighted,
	     * false otherwise.
	     */
	    public boolean isHighlightWithInvisibleEdge() {
	        return highlightWithInvisibleEdge;
	    }
	   
	    /**
	     * Determines if neighbor nodes with edges currently not visible still
	     * get highlighted.
	     * @param highlightWithInvisibleEdge assign true if neighbors with invisible
	     * edges should still get highlighted, false otherwise.
	     */
	    public void setHighlightWithInvisibleEdge(boolean highlightWithInvisibleEdge) {
	        this.highlightWithInvisibleEdge = highlightWithInvisibleEdge;
	    }
}
