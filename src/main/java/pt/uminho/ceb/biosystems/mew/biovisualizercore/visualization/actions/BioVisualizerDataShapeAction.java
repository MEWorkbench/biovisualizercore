package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions;

import java.util.Map;

import prefuse.action.assignment.ShapeAction;
import prefuse.data.tuple.TupleSet;
import prefuse.util.DataLib;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class BioVisualizerDataShapeAction extends ShapeAction{
			
	
protected static final int NO_SHAPE = Integer.MIN_VALUE;
    
    protected String m_dataField;
    protected int[]  m_palette;
    
    protected Map m_ordinalMap;
    
    
    /**
     * Create a new DataShapeAction.
     * @param group the data group to process
     * @param field the data field to base shape assignments on
     */
    public BioVisualizerDataShapeAction(String group, String field) {
        super(group, NO_SHAPE);
        m_dataField = field;
    }
    
    /**
     * Create a new DataShapeAction.
     * @param group the data group to process
     * @param field the data field to base shape assignments on
     * @param palette a palette of shape values to use for the encoding.
     * By default, shape values are assumed to be one of the integer SHAPE
     * codes included in the {@link prefuse.Constants} class.
     */
    public BioVisualizerDataShapeAction(String group, String field, int[] palette) {
        super(group, NO_SHAPE);
        m_dataField = field;
        m_palette = palette;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Returns the data field used to encode shape values.
     * @return the data field that is mapped to shape values
     */
    public String getDataField() {
        return m_dataField;
    }
    
    /**
     * Set the data field used to encode shape values.
     * @param field the data field to map to shape values
     */
    public void setDataField(String field) {
        m_dataField = field;
    }
    
    /**
     * This operation is not supported by the DataShapeAction type.
     * Calling this method will result in a thrown exception.
     * @see prefuse.action.assignment.ShapeAction#setDefaultShape(int)
     * @throws UnsupportedOperationException
     */
    public void setDefaultShape(int defaultShape) {
        throw new UnsupportedOperationException();
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefuse.action.EncoderAction#setup()
     */
    protected void setup() {
        TupleSet ts = m_vis.getGroup(m_group);
        m_ordinalMap = DataLib.ordinalMap(ts, m_dataField);
    }
    
    /**
     * @see prefuse.action.assignment.ShapeAction#getShape(prefuse.visual.VisualItem)
     */
    public int getShape(VisualItem item) {
        // check for any cascaded rules first
        int shape = super.getShape(item);
        if ( shape != NO_SHAPE ) {
            return shape;
        }

        // otherwise perform data-driven assignment
        Object v = item.get(m_dataField);
        //        int idx = ((Integer)m_ordinalMap.get(v)).intValue();
        //        
        //        String nodeType = (String) v;
        //        if ( m_palette == null ) {
        //            return idx % Constants.SHAPE_COUNT;
        //        } else {
        
        if(v == null) return m_palette[1];
        
        if(item.canGetBoolean(LayoutUtils.HAS_SPECIAL_SHAPE) && item.getBoolean(LayoutUtils.HAS_SPECIAL_SHAPE))
        	return(item.getInt(LayoutUtils.SPECIAL_SHAPE));
        
        if(v.equals(LayoutUtils.INFO))
        	return m_palette[0];
        if(v.equals(LayoutUtils.MET_T))
        	return m_palette[1];
        if(v.equals(LayoutUtils.HUBS))
        	return m_palette[2];
        if(v.equals(LayoutUtils.REA_T))
        	return m_palette[3];
        if(v.equals(LayoutUtils.DUMMY))
        	return m_palette[4];
        return m_palette[5];



    }
    
    public void upDateInfoShape(int shape){
    	this.m_palette[0] = shape;
    }
    
    public void upDateMetaboliteShape(int shape){
    	this.m_palette[1] = shape;
    }
    
    public void upDateCurrencyShape(int shape){
    	this.m_palette[2] = shape;
    }
    
    public void upDateReactionShape(int shape){
    	this.m_palette[3] = shape;
    }
}
