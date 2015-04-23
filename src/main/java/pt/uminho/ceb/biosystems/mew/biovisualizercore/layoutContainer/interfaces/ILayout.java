/**
O * SilicoLife
 * 
 * Copyright 2010
 * 
 * <p>This is PROPRIETARY software.</p>
 * <p>You can not copy, distribute, modify or for this matter,</p> 
 * <p>proceed into any other type of unauthorized form of use for this code</p>
 * 
 * 
 * www.silicolife.com @see <a href="http://www.silicolife.com">SilicoLife</a>
 * 
 * (c) All rights reserved
 */
package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Interface that defines what a metabolic layout is, and 
 * what methods it must implement.
 * @author Alberto Noronha
 *
 */
public interface ILayout extends Serializable, Cloneable{
	
	/**
	 * Returns a map of {@link IReactionLay} to build the layout.
	 * @return map of {@link IReactionLay}
	 */
	Map<String, IReactionLay> getReactions();
	
	/**
	 * Returns the nodes map to build the layout.
	 * @return map of {@link INodeLay}
	 */
	Map<String, INodeLay> getNodes();
	
	/**
	 * All implementations of the {@link ILayout} must allow the addition 
	 * of new nodes to the list.
	 * @param id - unique id
	 * @param label 
	 * @param metIds - metabolic ids
	 * @param nodeType - type of node
	 * @param x 
	 * @param y
	 */
	void addNode(String id, String label, Set<String> metIds, NodeTypeLay nodeType, Double x, Double y);
	
	/**
	 * All implementations of the {@link ILayout} must allow the addition of new reactions.
	 * @param id
	 * @param label
	 * @param metIds
	 * @param reactants
	 * @param products
	 * @param x
	 * @param y
	 */
	void addReaction(String id, String label, Set<String> metIds, Map<String, INodeLay> infos, Map<String, INodeLay> reactants, Map<String, INodeLay> products,boolean isReversible, Double x, Double y);

	
	/**
	 * Method to add a new set of coordinates to the layout.
	 * @param coordinates
	 */
	void setNewCoordinates(Map<String, Double[]> coordinates);
	
	/**
	 * Clone method.
	 * @return
	 */
	ILayout clone();

	boolean hasInformationNodes();

}
