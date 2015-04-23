package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class BVAggregateDragControl extends ControlAdapter{
	
	private VisualItem activeItem;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged;
	
    protected Visualization vis;
	protected AggregateTable at;
    
    /**
     * Creates a new drag control that issues repaint requests as an item
     * is dragged.
     */
    public BVAggregateDragControl(Visualization vis, AggregateTable at) {
    	
    	this.vis = vis;
    	this.at = at;
    }
        
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( !(item instanceof AggregateItem) )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            setFixed(item, false);
        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
      
    }
    
    /**
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = false;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), down);
        if ( item instanceof AggregateItem)
            setFixed(item, true);
        
        else if( item instanceof NodeItem){
        	  
            TupleSet focusSet = vis.getFocusGroup(LayoutUtils.SELECTED);
            if(!focusSet.containsTuple(item)){
    			addToFocusGroup(item,e);
    			setFixed(item, true);
            }
        }
       
    }
    
    /**
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( dragged ) {
            activeItem = null;
            setFixed(item, false);
            dragged = false;
        }            
    }
    
    /**
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = true;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), temp);
        double dx = temp.getX()-down.getX();
        double dy = temp.getY()-down.getY();

        if( item instanceof NodeItem && isInAggregate(item)){
        	
        	Iterator aggs = at.tuples();

        	while(aggs.hasNext()){
        		AggregateItem aggitem = (AggregateItem) aggs.next();
        		Iterator items = aggitem.items();
        		boolean found = false;
        		while(items.hasNext() && !found){
        			VisualItem i = (VisualItem) items.next();
        			if(i instanceof NodeItem){
        				if(i.getString(LayoutUtils.ID).equals(item.getString(LayoutUtils.ID))) found = true;
        			}
        		}

        		if(found){
        			move(aggitem, dx, dy);
        		}
        	}
        }
        else{
        	move(item, dx, dy);
        }
        down.setLocation(temp);
    }

    protected static void setFixed(VisualItem item, boolean fixed) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                setFixed((VisualItem)items.next(), fixed);
            }
        } else {
            item.setFixed(fixed);
        }
    }

    protected void move(VisualItem item, double dx, double dy) {
    	if ( item instanceof AggregateItem ) {
    		Iterator items = ((AggregateItem)item).items();
    		while ( items.hasNext() ) {
    			move((VisualItem)items.next(), dx, dy);
    		}
    	}
    	
//    	else if(item instanceof NodeItem && isInAggregate(item)){
//    		
//    		Iterator aggs = at.tuples();
//
//        	while(aggs.hasNext()){
//        		AggregateItem aggitem = (AggregateItem) aggs.next();
//        		Iterator items = aggitem.items();
//        		boolean found = false;
//        		while(items.hasNext() && !found){
//        			VisualItem i = (VisualItem) items.next();
//        			if(i instanceof NodeItem){
//        				if(i.getString(LayoutUtils.ID).equals(item.getString(LayoutUtils.ID))) found = true;
//        			}
//        		}
//        		
//        		if(found){
//        			Iterator items2 = aggitem.items();
//        			while ( items2.hasNext() ) {
//            			move((VisualItem)items2.next(), dx, dy);
//            		}
//        		}
//        	}
//		}
    	
    	else {
    		
    		setFixed(item, true);
    		double x = item.getX();
    		double y = item.getY();
    		item.setStartX(x);  item.setStartY(y);
    		item.setX(x+dx);    item.setY(y+dy);
    		item.setEndX(x+dx); item.setEndY(y+dy);
        }
    }

    private boolean isInAggregate(VisualItem item) {

    	Iterator aggs = at.tuples();

    	while(aggs.hasNext()){

    		AggregateItem aggitem = (AggregateItem) aggs.next();
    		Iterator items = aggitem.items();
    		while(items.hasNext()){
    			VisualItem i = (VisualItem) items.next();
    			if(i instanceof NodeItem){
    				if(i.getString(LayoutUtils.ID).equals(item.getString(LayoutUtils.ID))) return true;
    			}
    		}

    	}
    	return false;
    }
    
    private void addToFocusGroup(VisualItem vi,MouseEvent e) {
		TupleSet ts = vi.getVisualization().getFocusGroup(LayoutUtils.SELECTED);
		if (!e.isControlDown()) {
			ts.clear();
		}
		ts.addTuple(vi);
	}

}
