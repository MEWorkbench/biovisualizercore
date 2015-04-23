package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface the defines the metabolite nodes that can be
 * metabolites, currency or information.
 * @author Alberto Noronha
 *
 */
public interface INodeLay extends Serializable{
	
	/**
	 * Unique identifier of the node.
	 * @return
	 */
	public String getUniqueId();
	
	/**
	 * Label of the node that will appear in the visualization.
	 * @return
	 */
	public String getLabel();
	
	/**
	 * Set of metabolic identifiers that will provide connection with the metabolic model.
	 * @return
	 */
	public Set<String> getIds();

	/**
	 * Type of the node.
	 * Can be currency, metabolite or information.
	 * @return
	 */
	public NodeTypeLay getNodeType();
	
	/**
	 * Sets a new type of node.
	 * @param nodeType
	 */
	public void setNodeType(NodeTypeLay nodeType);
	
	/**
	 * Get the x-coordinate of the node
	 */
	public Double getX();
	
	/**
	 * Get the y-coordinate of the node
	 * @return
	 */
	public Double getY();
	
	/**
	 * Sets a new x-coordinate to the node.
	 * @param x
	 */
	public void setX(Double x);
	
	/**
	 * Sets a new y-coordinate to the node.
	 * @param y
	 */
	public void setY(Double y);

	public void setLabel(String string);
	
	public INodeLay clone();

}
