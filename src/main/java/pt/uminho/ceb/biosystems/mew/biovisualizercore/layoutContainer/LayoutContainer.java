package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

/**
 * The layout container is where the layout information is stored.
 * It implements the {@link ILayout} interface. There are 2 maps stored. One of the
 * is the nodes map, where the node information of metabolites and information nodes
 * is stored, and the reactions, where the information of reaction nodes is stored, and
 * to which nodes those reactions are connected (bi-partite graph).
 * 
 * @author Alberto Noronha
 *
 */
public class LayoutContainer implements ILayout, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Map<String, INodeLay> nodes;
	protected Map<String, IReactionLay> reactions;
	protected Boolean hasInfoNodes;
	
	public LayoutContainer(){
		this.nodes = new HashMap<String, INodeLay>();
		this.reactions = new HashMap<String, IReactionLay>();
		this.hasInfoNodes = false; 
	}
	
	public LayoutContainer(Map<String, INodeLay> nodes, Map<String, IReactionLay> reactions){
		this.nodes = nodes;
		this.reactions = reactions;
//		TODO: this will be removed
		LayoutUtils.verifiedDepBetweenClasses(this, false);
	}
	
	/**
	 * Gets the ids of the nodes (Metabolites and Currency) that are not information nodes.
	 * 
	 * @return set of node ids
	 */
	public Set<String> getModelMetabolitesNotInfos(){
		Set<String> met = new TreeSet<String>();
		
		for(INodeLay n : nodes.values()){
			if(n.getNodeType() != NodeTypeLay.INFORMATION)
				met.add(n.getUniqueId());
		}
		
		return met;
	}
	
	@Override
	public Map<String, IReactionLay> getReactions() {
		return reactions;
	}

	@Override
	public Map<String, INodeLay> getNodes() {
		return nodes;
	}

	@Override
	public void addNode(String id, String label, Set<String> metIds,
			NodeTypeLay nodeType, Double x, Double y) {

		NodeLay node = new NodeLay(id, label, metIds, nodeType, x, y);
		this.nodes.put(id, node);
	}

	@Override
	public void addReaction(String id, String label, Set<String> metIds,
			Map<String, INodeLay> infos, Map<String, INodeLay> reactants,
			Map<String, INodeLay> products, boolean isReversible, Double x,
			Double y) {
		
		ReactionLay reaction = new ReactionLay(reactants, products, infos, isReversible, x, y, metIds, id, label);
		this.reactions.put(id, reaction);
	}
	
	@Override
	public void setNewCoordinates(Map<String, Double[]> coordinates) {
	
		for(String nodeId : nodes.keySet()){
//			System.out.println(nodeId + "\t" + nodes.get(nodeId) + "\t" + coordinates.get(nodeId) + "\t" + nodes.get(nodeId).getLabel());
			
			if(coordinates.get(nodeId)!=null){
				this.nodes.get(nodeId).setX(coordinates.get(nodeId)[0]);
				this.nodes.get(nodeId).setY(coordinates.get(nodeId)[1]);
			}else{
				System.err.println(nodeId + "\t" + nodes.get(nodeId) + "\t" + coordinates.get(nodeId) + "\t" + nodes.get(nodeId).getLabel());
			}
		}
		
		for(String nodeId : reactions.keySet()){
			if(coordinates.get(nodeId)!=null){
				this.reactions.get(nodeId).setX(coordinates.get(nodeId)[0]);
				this.reactions.get(nodeId).setY(coordinates.get(nodeId)[1]);
			}else
				System.err.println(nodeId + "\t" + nodes.get(nodeId) + "\t" + coordinates.get(nodeId) + "\t" + nodes.get(nodeId).getLabel());
		}
		
	}
	
	/**
	 * Clone method for the exportation.
	 */
	public LayoutContainer clone(){

		Map<String, INodeLay> newNodes = new HashMap<String, INodeLay>(nodes);
		Map<String, IReactionLay> newReactions = new HashMap<String, IReactionLay>(reactions);
		return new LayoutContainer(newNodes, newReactions);
	}

	public void setNewNodeIds(Map<String, String> newMetaboliteIds) {
	
		for(String s : newMetaboliteIds.keySet()){
			
			for(String node_uniqueId : nodes.keySet()){
				
				if(nodes.get(node_uniqueId).getIds().contains(s)){
					nodes.get(node_uniqueId).getIds().add(newMetaboliteIds.get(s));
					nodes.get(node_uniqueId).getIds().remove(s);
					if(nodes.get(node_uniqueId).getLabel().equals(s)) nodes.get(node_uniqueId).setLabel(newMetaboliteIds.get(s));
					
				}
					
			}
		}
	}

	public void setNewReactionIds(Map<String, String> newReactionIds){
		
		for(String r : newReactionIds.keySet()){
			
			for(String reaction_uniqueId : reactions.keySet()){
				
				if(reactions.get(reaction_uniqueId).getIDs().contains(r)){
					reactions.get(reaction_uniqueId).getIDs().add(newReactionIds.get(r));
					reactions.get(reaction_uniqueId).getIDs().remove(r);
					if(reactions.get(reaction_uniqueId).getLabel().equalsIgnoreCase(r)) reactions.get(reaction_uniqueId).setLabel(newReactionIds.get(r));
					
				}
			}
		}
	}

	@Override
	public boolean hasInformationNodes() {
		if(hasInfoNodes == null){
			hasInfoNodes = false;
			for(INodeLay n : nodes.values())
				if(n.getNodeType().equals(NodeTypeLay.INFORMATION)) hasInfoNodes = true;
			
		}
		
		return hasInfoNodes;
	}

}
