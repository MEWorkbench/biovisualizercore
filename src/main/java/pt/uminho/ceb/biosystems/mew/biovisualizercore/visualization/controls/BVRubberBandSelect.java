package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class BVRubberBandSelect extends ControlAdapter  {


	private int downX1, downY1;
	private VisualItem rubberBand;
	Point2D screenPoint = new Point2D.Float();
	Point2D absPoint = new Point2D.Float();
	Rectangle2D rect = new Rectangle2D.Float();
	Rectangle r = new Rectangle();
	
	private Visualization vis;
	private AggregateTable at;   

	public BVRubberBandSelect(VisualItem rubberband, Visualization vis, AggregateTable at) {
		
		rubberBand = rubberband;
		rubberBand.setVisible(false);
		
		this.vis = vis;
		this.at = at;
	}

	public void mousePressed(MouseEvent e) {  

		if (!SwingUtilities.isLeftMouseButton(e)) return;


		Display d = (Display)e.getComponent();
		Visualization vis = d.getVisualization();
		TupleSet focus = vis.getFocusGroup(LayoutUtils.SELECTED);
		if (!e.isShiftDown()) {
			focus.clear();
		}
		
		float[] bandRect = (float[]) rubberBand.get(VisualItem.POLYGON);
		bandRect[0] = bandRect[1] = bandRect[2] = bandRect[3] = 
				bandRect[4] = bandRect[5] = bandRect[6] = bandRect[7] = 0;

		screenPoint.setLocation(e.getX(), e.getY());
		d.getAbsoluteCoordinate(screenPoint, absPoint);
		downX1 = (int) absPoint.getX();
		downY1= (int) absPoint.getY();
		rubberBand.setVisible(true);
	}



	public void mouseDragged (MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;

		Display d = (Display)e.getComponent();           
		screenPoint.setLocation(e.getX(), e.getY());
		d.getAbsoluteCoordinate(screenPoint, absPoint);
		int x1 = downX1;
		int y1 = downY1;
		int x2 = (int) absPoint.getX();
		int y2 = (int) absPoint.getY();

		float[] bandRect = (float[]) rubberBand.get(VisualItem.POLYGON);
		bandRect[0] = x1;
		bandRect[1] = y1;
		bandRect[2] = x2;
		bandRect[3] = y1;
		bandRect[4] = x2;
		bandRect[5] = y2;
		bandRect[6] = x1;
		bandRect[7] = y2;

		if (x2 < x1){
			int temp = x2;
			x2 = x1;
			x1 = temp;
		}
		if (y2 < y1) {
			int temp = y2;
			y2 = y1;
			y1 = temp;
		}
		rect.setRect(x1,y1,x2-x1, y2-y1);

		Visualization vis = d.getVisualization();
		TupleSet focus = vis.getFocusGroup(LayoutUtils.SELECTED);

		if (!e.isShiftDown()) {
			focus.clear();
		}

		//allocate the maximum space we could need

		Tuple[] selectedItems = new Tuple[vis.getGroup(LayoutUtils.NODES).getTupleCount()];
		Iterator it = vis.getGroup(LayoutUtils.NODES).tuples();

		//in this example I'm only allowing Nodes to be selected
		int i=0;
		while (it.hasNext()) {
			VisualItem item = (VisualItem) it.next();             
			if (item.isVisible() && item.getBounds().intersects(rect)){
				selectedItems[i++] = item;
			}
		}

		//Trim the array down to the actual size
		Tuple[] properlySizedSelectedItems = new Tuple[i];
		System.arraycopy(selectedItems, 0, properlySizedSelectedItems,0, i);
		for (int j = 0; j < properlySizedSelectedItems.length; j++) {
			Tuple tuple = properlySizedSelectedItems[j];
			focus.addTuple(tuple);
		}

		rubberBand.setValidated(false);
		d.repaint();
	}

	public void mouseReleased (MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		
		Display d = (Display)e.getComponent();   
	
		at.clear();
		
		TupleSet focus = vis.getFocusGroup(LayoutUtils.SELECTED);
		if(focus.getTupleCount()>0){
			AggregateItem aitem = (AggregateItem)at.addItem();
			aitem.setInt("id", 1);
			
			Iterator it = focus.tuples();
			while(it.hasNext()){
				aitem.addItem((VisualItem)it.next());
			}

		}
		
		rubberBand.setVisible(false);
		d.getVisualization().repaint();
	}

	public void itemClicked(VisualItem item, MouseEvent e) { 
		Visualization vis = item.getVisualization();
		TupleSet focusSet = vis.getFocusGroup(LayoutUtils.SELECTED);
		
		super.itemPressed(item, e); 
		if(!focusSet.containsTuple(item)){
			addToFocusGroup(item,e);
		}

		at.clear();
		
		TupleSet focus = vis.getFocusGroup(LayoutUtils.SELECTED);
		if(focus.getTupleCount()>0){
			
			AggregateItem aitem = (AggregateItem)at.addItem();
			aitem.setInt("id", 1);
			
			Iterator it = focus.tuples();
			while(it.hasNext()){
				aitem.addItem((VisualItem)it.next());
			}
		}
		
		rubberBand.setVisible(false);
		Display d = (Display)e.getComponent();       
		d.repaint();
	}
	

	private void addToFocusGroup(VisualItem vi,MouseEvent e) {
		TupleSet ts = vi.getVisualization().getFocusGroup(LayoutUtils.SELECTED);
		if (!e.isControlDown()) {
			ts.clear();
		}
		ts.addTuple(vi);
	}
}

