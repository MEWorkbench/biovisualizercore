package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

/**
 * Implementation of the interface {@link IReactionLay}.
 * @author Alberto Noronha
 *
 */
public class ReactionLay implements IReactionLay{
	
	
	protected Map<String, INodeLay> reactants;
	protected Map<String, INodeLay> products;
	protected Map<String, INodeLay> infos;
	protected boolean reversible;
	protected Double x,y;
	protected Set<String> reactionIds;
	protected String uniqueId;
	protected String label;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ReactionLay(Map<String, INodeLay> reactants,
			Map<String, INodeLay> products, Map<String,INodeLay> infos, boolean reversible, Double x,
			Double y, Set<String> ids,String idLayout, String label) {
		this.reactants = reactants;
		this.products = products;
		this.infos = infos;
		this.reversible = reversible;
		this.x = x;
		this.y = y;
		this.reactionIds = ids;
		this.uniqueId = idLayout;
		this.label = label;
	}
	
	@Override
	public Set<String> getIDs() {
		return reactionIds;
	}

	@Override
	public Map<String, INodeLay> getReactants() {
		return reactants;
	}

	@Override
	public Map<String, INodeLay> getProducts() {
		return products;
	}

	@Override
	public boolean isReversible() {
		return reversible;
	}

	@Override
	public Map<String, INodeLay> infos() {
		return infos;
	}

	@Override
	public Double getX() {
		return x;
	}

	@Override
	public Double getY() {
		return y;
	}

	@Override
	public void setX(Double x) {
		this.x = x;		
	}

	@Override
	public void setY(Double y) {
		this.y = y;		
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getLabel() {
		return label;
	}
	@Override
	public void setReversible(boolean rev) {
		this.reversible = rev;
	}
	@Override
	public void setLabel(String label) {
		this.label = label;
		
	}

	@Override
	public IReactionLay clone(Map<String, INodeLay> nodes) {
		Map<String, INodeLay> reactants = getNodes(nodes, this.reactants);
		Map<String, INodeLay> products = getNodes(nodes, this.products);
		Map<String, INodeLay> infos = getNodes(nodes, this.infos);
		return new ReactionLay(reactants, products, infos, this.reversible, 
				   this.x, this.y, new HashSet<>(reactionIds), this.uniqueId, this.label);
	}

	private Map<String, INodeLay> getNodes(Map<String, INodeLay> nodes,
			Map<String, INodeLay> r) {
		
		Map<String, INodeLay> ret = new HashMap<String, INodeLay>();
		for(String uuid : r.keySet()){
			INodeLay node = nodes.get(uuid);
			if(node == null) throw new RuntimeException("Problem in node " + uuid);
			ret.put(node.getUniqueId(), node);
		}
		
		return ret;
	}
}
