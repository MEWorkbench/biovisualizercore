package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.logging.Logger;

import prefuse.action.EncoderAction;
import prefuse.action.assignment.StrokeAction;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.util.StrokeLib;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class BioVisualizerStrokeAction extends EncoderAction{
protected BasicStroke[] defaultStroke = new BasicStroke[]{
									StrokeLib.getStroke(2.0f),
									StrokeLib.getStroke(2.0f),
									StrokeLib.getStroke(2.0f),
									StrokeLib.getStroke(2.0f)};
    
    /**
     * Create a new StrokeAction that processes all data groups.
     */
    public BioVisualizerStrokeAction() {
        super();
    }
    
    /**
     * Create a new StrokeAction that processes the specified group.
     * @param group the data group to process
     */
    public BioVisualizerStrokeAction(String group) {
        super(group);
    }
    
    /**
     * .
     * @param group the data group to process
     * @param defaultStroke the default Stroke to assign
     */
    public BioVisualizerStrokeAction(String group, Float[] defaultStrokes) {
        super(group);
        this.defaultStroke = new BasicStroke[]{
        		StrokeLib.getStroke(defaultStrokes[0]),
        		StrokeLib.getStroke(defaultStrokes[1]),
        		StrokeLib.getStroke(defaultStrokes[2]),
        		StrokeLib.getStroke(defaultStrokes[3]),
        };
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Set the default BasicStroke to be assigned to items. Items will be
     * assigned the default Stroke if they do not match any registered rules.
     * @param f the default BasicStroke to use
     */
    public void setDefaultStroke(BasicStroke[] f) {
        defaultStroke = f;
    }
    
    /**
     * Get the default BasicStroke assigned to items.
     * @return the default BasicStroke
     */
    public BasicStroke[] getDefaultStroke() {
        return defaultStroke;
    }
    
    /**
     * Add a mapping rule to this StrokeAction. VisualItems that match
     * the provided predicate will be assigned the given BasicStroke value
     * (assuming they do not match an earlier rule).
     * @param p the rule Predicate 
     * @param stroke the BasicStroke
     */
    public void add(Predicate p, BasicStroke stroke) {
        super.add(p, stroke);
    }

    /**
     * Add a mapping rule to this StrokeAction. VisualItems that match
     * the provided expression will be assigned the given BasicStroke value
     * (assuming they do not match an earlier rule). The provided expression
     * String will be parsed to generate the needed rule Predicate.
     * @param expr the expression String, should parse to a Predicate. 
     * @param stroke the BasicStroke
     * @throws RuntimeException if the expression does not parse correctly or
     * does not result in a Predicate instance.
     */
    public void add(String expr, BasicStroke stroke) {
        Predicate p = (Predicate)ExpressionParser.parse(expr);
        add(p, stroke);       
    }
    
    /**
     * Add a mapping rule to this StrokeAction. VisualItems that match
     * the provided predicate will be assigned the BasicStroke value returned
     * by the given StrokeAction's getStroke() method.
     * @param p the rule Predicate 
     * @param f the delegate StrokeAction to use
     */
    public void add(Predicate p, StrokeAction f) {
        super.add(p, f);
    }

    /**
     * Add a mapping rule to this StrokeAction. VisualItems that match
     * the provided expression will be assigned the given BasicStroke value
     * (assuming they do not match an earlier rule). The provided expression
     * String will be parsed to generate the needed rule Predicate.
     * @param expr the expression String, should parse to a Predicate. 
     * @param f the delegate StrokeAction to use
     * @throws RuntimeException if the expression does not parse correctly or
     * does not result in a Predicate instance.
     */
    public void add(String expr, StrokeAction f) {
        Predicate p = (Predicate)ExpressionParser.parse(expr);
        super.add(p, f);
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefuse.action.ItemAction#process(prefuse.visual.VisualItem, double)
     */
    public void process(VisualItem item, double frac) {
        item.setStroke(getStroke(item));
    }
    
    /**
     * Returns the stroke to use for a given VisualItem. Subclasses should
     * override this method to perform customized Stroke assignment.
     * @param item the VisualItem for which to get the Stroke
     * @return the BasicStroke for the given item
     */
    public BasicStroke getStroke(VisualItem item) {
        Object o = lookup(item);
        if ( o != null ) {
            if ( o instanceof StrokeAction ) {
                return ((StrokeAction)o).getStroke(item);
            } else if ( o instanceof Stroke ) {
                return (BasicStroke)o;
            } else {
                Logger.getLogger(this.getClass().getName())
                    .warning("Unrecognized Object from predicate chain.");
            }
        }
        
        if(item.canGet(LayoutUtils.TYPE, String.class)){
        	
	        String type = item.getString(LayoutUtils.TYPE);
	        if(type !=null)
	        if(type.equals(LayoutUtils.REA_T))
	        	return defaultStroke[0];
	        else if(type.equals(LayoutUtils.MET_T))
	        	return defaultStroke[1];
	        else if(type.equals(LayoutUtils.HUBS))
	        	return defaultStroke[2];
	        else if(type.equals(LayoutUtils.INFO))
	        	return defaultStroke[3];
 
        }
        return StrokeLib.getStroke(1.0f);   
    }
    
    
    public void setReactStroke(float f){
    	defaultStroke[0] = StrokeLib.getStroke(f);
    }
    public void setMetStroke(float f){
    	defaultStroke[1] = StrokeLib.getStroke(f);
    }
    public void setCurrencyStroke(float f){
    	defaultStroke[2] = StrokeLib.getStroke(f);
    }
    public void setInfotroke(float f){
    	defaultStroke[3] = StrokeLib.getStroke(f);
    }
}
