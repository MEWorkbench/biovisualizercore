package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions;


import java.util.logging.Logger;

import prefuse.action.assignment.ColorAction;
import prefuse.data.expression.Predicate;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class BioVisualizerColorAction extends ColorAction{
	
	public BioVisualizerColorAction(String group, Predicate p, String field){
		super(group,p, field);
	}
	
	public BioVisualizerColorAction(String group, String field){
		super(group, field);
	}
	
	public BioVisualizerColorAction(String group, Predicate p, String field, int color){
		super(group, p, field, color);
	}
	
	public BioVisualizerColorAction(String group, String field, int color){
		super(group, field, color);
	}
	
	public int getColor(VisualItem item) {
        Object o = lookup(item);
        
//        if(item.canGet(LayoutUtils.HAS_SPECIAL_COLOR, boolean.class) && item.getBoolean(LayoutUtils.HAS_SPECIAL_COLOR)){
//        	return item.getInt(LayoutUtils.SPECIAL_COLOR);
//        }
        
        if ( o != null ) {
            if ( o instanceof ColorAction ) {
            	return ((ColorAction)o).getColor(item);
            } else if ( o instanceof Integer ) {
            	if(((Integer)o)!=Integer.MIN_VALUE)
            		return ((Integer)o).intValue();
            	else
            		return item.getInt(LayoutUtils.SPECIAL_COLOR);
            } else {
                Logger.getLogger(this.getClass().getName())
                    .warning("Unrecognized Object from predicate chain.");
            }
        }
        return m_defaultColor;   
    }
}
