package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;

/**
 * Implementation of the {@link ILayoutBuilder} for automatically generated layouts
 * from a list of {@link ReactionCI}
 * @author Alberto Noronha
 *
 */
public class ModelPathwayGenerator implements ILayoutBuilder{

	Map<String, INodeLay> nodes;
	Map<String, IReactionLay> reactions;
	
	int hubDefinition = 10000;
	Set<String> cofactors;
	
	Map<String, ReactionCI> pathway;
	Set<String> hubMetabolites;
	
	@Deprecated
	public ModelPathwayGenerator(Map<String, ReactionCI> pathway, Container container){
		this(pathway, container, null);
	}
	
	public ModelPathwayGenerator(Map<String, ReactionCI> pathway, ILayout layout){
		if(layout == null){
			nodes = new HashMap<String, INodeLay>();
			reactions = new HashMap<String, IReactionLay>();
		}else{
			nodes = createNewNodesFromLayout(layout);
			reactions = createNewReactionsFromLayout(layout, nodes);
		}
		
		cofactors = new HashSet<String>();
		hubMetabolites = findHubMtabolites(pathway, layout);
		
		this.pathway = pathway;
	}
	
	@Deprecated
	public ModelPathwayGenerator(Map<String, ReactionCI> pathway, Container container, ILayout layout){
		this(pathway, layout);
	}
	
	public Set<String> getHubMetabolites() {
		return hubMetabolites;
	}
	
	private Set<String> findHubMtabolites(Map<String, ReactionCI> p, ILayout l) {
		Set<String> hubMetab = new HashSet<String>();
		Map<String, Set<String>> reactMetabAssociation = new HashMap<String, Set<String>>();
		
		for (String reaction : p.keySet()) {
			reactMetabAssociation.put(reaction, new HashSet<String>());
			
			reactMetabAssociation.get(reaction).addAll(p.get(reaction).getReactants().keySet());
			reactMetabAssociation.get(reaction).addAll(p.get(reaction).getProducts().keySet());
		}
		
		if(l != null){
			for (IReactionLay reaction : l.getReactions().values()){
				Set<String> allMetabs = new HashSet<String>();
				
				for (String prod : reaction.getProducts().keySet()) 
					allMetabs.addAll(reaction.getProducts().get(prod).getIds());
				
				for (String react : reaction.getReactants().keySet()) 
					allMetabs.addAll(reaction.getReactants().get(react).getIds());
				
				reactMetabAssociation.put(reaction.getLabel(), allMetabs);
			}
		}
		
		Map<String,Integer> metabCount = countNumOfMetabolites(reactMetabAssociation);
		for (String metab : metabCount.keySet())
			if (metabCount.get(metab) >= hubDefinition)
				hubMetab.add(metab);
				
		return hubMetab;
	}
	
	private Map<String,Integer> countNumOfMetabolites(Map<String, Set<String>> reactMetabAssociation){
		Map<String, Integer> numOfMetabolites = new HashMap<String, Integer>();
		
		for (Set<String> metabList : reactMetabAssociation.values()) {
			for (String string : metabList) {
				if(numOfMetabolites.containsKey(string))
					numOfMetabolites.put(string, numOfMetabolites.get(string)+1);
				else
					numOfMetabolites.put(string, 1);
			}
		}
		
		return numOfMetabolites;
		
	}
	
	private Map<String, IReactionLay> createNewReactionsFromLayout(
			ILayout layout, Map<String, INodeLay> newNodes) {
		Map<String, IReactionLay> reactions = new HashMap<>();
		for (IReactionLay r : layout.getReactions().values())
			reactions.put(r.getUniqueId(), r.clone(newNodes));
		
		return reactions;
	}

	private Map<String, INodeLay> createNewNodesFromLayout(ILayout layout) {
		Map<String, INodeLay> nodes = new HashMap<>();
		for (INodeLay n: layout.getNodes().values())
			nodes.put(n.getUniqueId(), n.clone());
		return nodes;
	}

	private void addReaction(ReactionCI r){
		if(!reactionExists(r.getId())){
			String reaction_unique_id = UUID.randomUUID().toString();
			Set<String> reaction_met_ids = new HashSet<String>();
			reaction_met_ids.add(r.getId());
			
			Map<String, INodeLay> reactants = createReactantsOrProducts(r.getReactants().keySet());
			Map<String, INodeLay> products = createReactantsOrProducts(r.getProducts().keySet());
			
			ReactionLay reaction = new ReactionLay(reactants, products, new HashMap<String, INodeLay>(), r.isReversible(), null, null, reaction_met_ids, reaction_unique_id, r.getId());
			reactions.put(reaction_unique_id, reaction);
		}
	}
	
	private Map<String, INodeLay> createReactantsOrProducts(Set<String> rs) {
		Map<String, INodeLay> nodesMap = new HashMap<>();
		for(String react_met_Id : rs){
			
			NodeTypeLay type = cofactors.contains(react_met_Id)?NodeTypeLay.CURRENCY:NodeTypeLay.METABOLITE;
			boolean replicate = false;
			replicate = cofactors.contains(react_met_Id) || hubMetabolites.contains(react_met_Id);
			
			String uniqueId = getUIDMetaboliteToReaction(react_met_Id, type, replicate);
			nodesMap.put(uniqueId, nodes.get(uniqueId));
		}
		return nodesMap;
	}


	private String getUIDMetaboliteToReaction(String metaboliteModelID, NodeTypeLay type, boolean replicate) {
		String uniqueId = getNodeIdFromMet(metaboliteModelID);
		
		if(uniqueId == null || isNodeReplicated(metaboliteModelID) || type.equals(NodeTypeLay.CURRENCY) || getHubMetabolites().contains(metaboliteModelID)){
			NodeLay l = buildNode(metaboliteModelID, metaboliteModelID, type);
			uniqueId = l.getUniqueId();
			nodes.put(uniqueId, l);
		}
		return uniqueId;
	}

	private LayoutContainer generateNewLayout(Map<String, ReactionCI> pathway) {

		for(ReactionCI r : pathway.values())
			addReaction(r);
		
		return new LayoutContainer(nodes, reactions);
	}


	private NodeLay buildNode(String nodeMetId, String label, NodeTypeLay nodeType){
		String node_unique_id = UUID.randomUUID().toString();
		Set<String> node_ids = new HashSet<String>();
		node_ids.add(nodeMetId);

		NodeLay node = new NodeLay(node_unique_id, label, node_ids, nodeType, null, null);
		return node;
	}

	private boolean isNodeReplicated(String metabolicId) {
		int counter = 0;

		for(String nodeId : nodes.keySet())
			if(nodes.get(nodeId).getIds().contains(metabolicId)) counter++;

		if(counter > 1) return true;
		return false;
	}

	private String getNodeIdFromMet(String s) {
		
		for(String uniqueId : nodes.keySet())
			if(nodes.get(uniqueId).getIds().contains(s)) return uniqueId;
		
		return null;
	}


	private boolean metaboliteExists(String s) {

		for(String n : nodes.keySet())
			if(nodes.get(n).getIds().contains(s)) return true;
		
		return false;
	}


	private boolean reactionExists(String rId) {

		for(String s : reactions.keySet())
			if(reactions.get(s).getIDs().contains(rId)) return true;

		return false;
	}
	
	public void setCofactors(Set<String> cofactors) {
		this.cofactors = cofactors;
	}
	
	public void setHubDefinition(int hubDefinition) {
		this.hubDefinition = hubDefinition;
	}
	
	@Override
	public LayoutContainer buildLayout(){
		LayoutContainer container = generateNewLayout(pathway);
		return container;
	}

}
