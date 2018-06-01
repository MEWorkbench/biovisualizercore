package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Interface the defines the reaction nodes.
 * @author Alberto Noronha
 *
 */
public interface IReactionLay extends Serializable{
	
	/**
	 * Set of metabolic ids of the reaction.
	 * @return set of reaction id's.
	 */
	public Set<String> getIDs();
	
	/**
	 * Each reaction node must have an unique id.
	 * @return id
	 */
	public String getUniqueId();
	
	/**
	 * Node label that will appear in the visualization.
	 * @return label
	 */
	public String getLabel();
	
	/**
	 * map of nodes that are connected as reactants
	 * to this reaction node.
	 * @return map node id {@link INodeLay}
	 */
	public Map<String, INodeLay> getReactants();	
	
	/**
	 * map of nodes that are connected as products
	 * to this reaction node.
	 * @return map node id {@link INodeLay}
	 */
	public Map<String, INodeLay> getProducts();
	
	/**
	 * Gives the reversibility of the reaction
	 * @return reversible
	 */
	public boolean isReversible();
	
	/**
	 * Map of information nodes that connect to this reaction
	* @return map node id {@link INodeLay}
	 */
	public Map<String, INodeLay> infos();
	
	/**
	 * X - coordinate of the reaction node
	 * @return coordinate
	 */
	public Double getX();
	
	/**
	 * Y - coordinate of the reaction node
	 * @return coordinate
	 */
	public Double getY();
	
	/**
	 * Sets a new x-coordinate to the reaction node
	 * @param x
	 */
	public void setX(Double x);
	
	/**
	 * Sets a new y-coordinate to the reaction node
	 * @param y
	 */
	public void setY(Double y);
	
	/**
	 * Sets a new reversibility to the reaction node
	 * @param rev
	 */
	public void setReversible(boolean rev);
	
	public void setLabel(String label);
	
	public IReactionLay clone(Map<String, INodeLay> nodes);

}
