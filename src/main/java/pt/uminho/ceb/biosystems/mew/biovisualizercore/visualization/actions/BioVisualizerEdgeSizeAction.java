package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions;

import java.util.logging.Logger;

import prefuse.action.EncoderAction;
import prefuse.action.assignment.SizeAction;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class BioVisualizerEdgeSizeAction extends EncoderAction{
	public static final String TYPE = "type"; 
	public static final String LINK_CONVERTION = "CONVERSION";
	public static final String LINK_HUBS = "hubs";
	protected double m_defaultSize = 1.0;

	
	/**
	 * Constructor. A default size value of 1.0 will be used.
	 * @param group the data group processed by this Action.
	 */
	public BioVisualizerEdgeSizeAction(String group) {
		super(group);
	}

	/**
	 * Constructor which specified a default size value.
	 * @param group the data group processed by this Action.
	 * @param size the default size to use
	 */
	public BioVisualizerEdgeSizeAction(String group, double size) {
		super(group);
		m_defaultSize = size;
	}

	/**
	 * Returns the default size value assigned to items.
	 * @return the default size value
	 */
	public double getDefaultSize() {
		return m_defaultSize;
	}

	/**
	 * Sets the default size value assigned to items. Items will be assigned
	 * the default size if they do not match any registered rules.
	 * @param defaultSize the new default size value
	 */
	public void setDefaultSize(double defaultSize) {
		m_defaultSize = defaultSize;
	}

	/**
	 * Add a size mapping rule to this SizeAction. VisualItems that match
	 * the provided predicate will be assigned the given size value (assuming
	 * they do not match an earlier rule).
	 * @param p the rule Predicate 
	 * @param size the size value
	 */
	public void add(Predicate p, double size) {
		super.add(p, new Double(size));
	}

	/**
	 * Add a size mapping rule to this SizeAction. VisualItems that match
	 * the provided expression will be assigned the given size value (assuming
	 * they do not match an earlier rule). The provided expression String will
	 * be parsed to generate the needed rule Predicate.
	 * @param expr the expression String, should parse to a Predicate. 
	 * @param size the size value
	 * @throws RuntimeException if the expression does not parse correctly or
	 * does not result in a Predicate instance.
	 */
	public void add(String expr, double size) {
		Predicate p = (Predicate)ExpressionParser.parse(expr);
		add(p, size);       
	}

	/**
	 * Add a size mapping rule to this SizeAction. VisualItems that match
	 * the provided predicate will be assigned the size value returned by
	 * the given SizeAction's getSize() method.
	 * @param p the rule Predicate 
	 * @param f the delegate SizeAction to use
	 */
	public void add(Predicate p, SizeAction f) {
		super.add(p, f);
	}

	/**
	 * Add a size mapping rule to this SizeAction. VisualItems that match
	 * the provided expression will be assigned the given size value (assuming
	 * they do not match an earlier rule). The provided expression String will
	 * be parsed to generate the needed rule Predicate.
	 * @param expr the expression String, should parse to a Predicate. 
	 * @param f the delegate SizeAction to use
	 * @throws RuntimeException if the expression does not parse correctly or
	 * does not result in a Predicate instance.
	 */
	public void add(String expr, SizeAction f) {
		Predicate p = (Predicate)ExpressionParser.parse(expr);
		super.add(p, f);
	}

	// ------------------------------------------------------------------------

	/**
	 * @see prefuse.action.ItemAction#process(prefuse.visual.VisualItem, double)
	 */
	public void process(VisualItem item, double frac) {
		double size = getSize(item);
		double old = item.getSize();

//		size = m_defaultSize;
		if(item.get(TYPE).equals(LINK_CONVERTION) || item.get(TYPE).equals(LINK_HUBS)){
			
			if(item.getBoolean(LayoutUtils.HAS_THICKNESS)){
				size = item.getDouble(LayoutUtils.THICKNESS);
			}
		}

		item.setStartSize(old);
		item.setEndSize(size);
		item.setSize(size);
	}

	/**
	 * Returns a size value for the given item.
	 * @param item the item for which to get the size value
	 * @return the size value for the item
	 */
	public double getSize(VisualItem item) {
		Object o = lookup(item);
		if ( o != null ) {
			if ( o instanceof SizeAction ) {
				return ((SizeAction)o).getSize(item);
			} else if ( o instanceof Number ) {
				return ((Number)o).doubleValue();
			} else {
				Logger.getLogger(this.getClass().getName())
				.warning("Unrecognized Object from predicate chain.");
			}
		}
		return m_defaultSize;   
	}
}
