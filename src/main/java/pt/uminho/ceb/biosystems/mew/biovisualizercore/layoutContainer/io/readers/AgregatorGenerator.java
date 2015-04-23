package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.BioVisualizerUtils;

public class AgregatorGenerator implements ILayoutBuilder{

	
	
	private Map<String, INodeLay> nodes;
	private Map<String, IReactionLay> reactions;
	private ILayout[] layouts;

	public AgregatorGenerator(ILayout... layouts) {
		this.layouts = layouts;
	}
	
	@Override
	public LayoutContainer buildLayout() {
		buildInfo();
		
		System.out.println(nodes);
		System.out.println(reactions);
		
		
		LayoutContainer layoutContainer = new LayoutContainer(nodes, reactions);
		return layoutContainer;
	}

	private void buildInfo() {
		
		nodes = new HashMap<String, INodeLay>();
		reactions = new HashMap<String, IReactionLay>();
		
		Map<TreeSet<String>, INodeLay> nodeslink = new HashMap<TreeSet<String>, INodeLay>();
		Map<TreeSet<String>, String> reactionsLink = new HashMap<TreeSet<String>, String>();
		
		double x = 0;
		double y = 0;
		for(ILayout lay : layouts){
//			System.out.println("PVPVPVPVPVPVPVVP>>>>>>> OLE::"+ lay);
			if(lay!=null){
				Point p = BioVisualizerUtils.getMaxPoint(lay);
				
				Map<String, String> metToLink = createOrAgregateMetabolite(x, y, lay.getNodes(), nodeslink);
				createOrAgregateReactions(x, y, lay.getReactions(), metToLink, reactionsLink);
				x+= p.getX();
				y+= p.getY();
			}
		}
		
	}

	private void createOrAgregateReactions(double x, double y,
			Map<String, IReactionLay> reactions2,
			Map<String, String> metToLink,
			Map<TreeSet<String>, String> reactionsLink) {
		
		for(IReactionLay r : reactions2.values()){
			
			
			
			IReactionLay copyR = new ReactionLay(getEquivalentNodes(r.getReactants(), metToLink), 
					getEquivalentNodes(r.getProducts(), metToLink), 
					getEquivalentNodes(r.infos(), metToLink), r.isReversible(),
					(r.getX()!=null)?x+r.getX():null, r.getY(), 
							new HashSet<String>(r.getIDs()), r.getUniqueId(), r.getLabel());
			
			this.reactions.put(copyR.getUniqueId(), copyR);
		}
	}
	
	private Map<String, INodeLay> getEquivalentNodes(Map<String, INodeLay> nodes, Map<String, String> dic){
		Map<String, INodeLay> newMap = new HashMap<String, INodeLay>();
		for(INodeLay node : nodes.values()){
			String id = dic.get(node.getUniqueId());
			System.out.println(id + "\t" + node + "\t" + this.nodes.get(id));
			newMap.put(id, this.nodes.get(id));
		}
		return newMap;
	}

	private Map<String, String> createOrAgregateMetabolite(double x, double y,
			Map<String, INodeLay> nodes2,
			Map<TreeSet<String>, INodeLay> nodeslink) {
		
		Map<String, String> links = new HashMap<String, String>();
		for(INodeLay node : nodes2.values()){
			String equivalentNode = node.getUniqueId();
			
			INodeLay copy = new NodeLay(node.getUniqueId(), node.getLabel(), new HashSet<String>(node.getIds()), node.getNodeType()
					, (node.getX()!=null)?x+node.getX():null, node.getY());
			
			
			nodeslink.put(new TreeSet<String>(node.getIds()), copy);
			links.put(node.getUniqueId(), equivalentNode);
			nodes.put(equivalentNode, copy);
		}
		return links;
	}

}
