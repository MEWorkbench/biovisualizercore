package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.awt.Point;
import java.util.Collection;
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
import pt.uminho.ceb.biosystems.mew.utilities.math.MathUtils;

public class AgregatorGenerator implements ILayoutBuilder{

	private int insert = 50;
	
	private Map<String, INodeLay> nodes;
	private Map<String, IReactionLay> reactions;
	private ILayout[] layouts;

	
	public AgregatorGenerator(Collection<? extends ILayout> layouts){
		this(layouts.toArray(new ILayout[layouts.size()]));
	}
	
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
			if(lay!=null){
				Point pMax = BioVisualizerUtils.getMaxPoint(lay);
				Point pMin = BioVisualizerUtils.getMinPoint(lay);
				
				Point slide = BioVisualizerUtils.getPutInInitialPoint(pMin);
				
				
				Map<String, String> metToLink = createOrAgregateMetabolite(x+slide.getX(), y+slide.getY(), lay.getNodes(), nodeslink);
				createOrAgregateReactions(x+slide.getX(), y+slide.getY(), lay.getReactions(), metToLink, reactionsLink);
				
				
				Point p = new Point((int)Math.abs(pMax.getX() - pMin.getX()), (int)Math.abs(pMax.getY() - pMin.getY()));
				x+= p.getX() + insert;
				y+= p.getY() + insert;
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
