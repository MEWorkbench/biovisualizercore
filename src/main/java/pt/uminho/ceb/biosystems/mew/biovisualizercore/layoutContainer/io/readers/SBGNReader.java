package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.sbgn.SbgnUtil;
import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Map;
import org.sbgn.bindings.Port;
import org.sbgn.bindings.Sbgn;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;

/**
 * Layout Builder for SBGN files. Only reads proccess description diagrams,
 * and is limited to simple chemical, macromolecule, process, consumption, production
 * and catalysis.
 * 
 * @author Alberto Noronha
 *
 */
public class SBGNReader implements ILayoutBuilder{

	public static String SIMPCH = "simple chemical";
	public static String PROC = "process";
	public static String CONS = "consumption";
	public static String PRO = "production";
	
	
	private Map sbgnMap;
	private HashSet<Glyph> infos;
	private HashMap<String, INodeLay> nodes;
	private HashMap<String, IReactionLay> reactions;
	
	private java.util.Map<String, List<Port>> portsMap;
	
	public SBGNReader(String file) throws InvalidLayoutFileException{
		
		File f = new File(file);
		
		Sbgn sbgn;
		try {
			sbgn = SbgnUtil.readFromFile(f);
			sbgnMap = sbgn.getMap();
			
			infos = new HashSet<Glyph>();
			nodes = new HashMap<String, INodeLay>();
			reactions = new HashMap<String, IReactionLay>();
			
			portsMap = new HashMap<String, List<Port>>();
			
			buildLayout();
		} catch (JAXBException e) {
			throw new InvalidLayoutFileException("Invalid SBGN-PD file!\nCause: " + e.getMessage());
		}
		
	}

	public Map getSbgnMap(){
		return sbgnMap;
	}

	@Override
	public LayoutContainer buildLayout() {
		
		for (Glyph g : sbgnMap.getGlyph())
		{
			if(g.getClazz().equals(SIMPCH))
				createNode(g);
			if(g.getClazz().equals(PROC))
				crateReaction(g);
			
		}
		
		for( Arc a : sbgnMap.getArc()){
			
			String source_id ="";
			String target_id ="";
			String reaction_id="";
			String nodes_id="";
			boolean reactant = false;
			
			if(a.getSource().getClass().equals(Port.class)){
				source_id = findGlyphFromPor((Port) a.getSource());
				if(nodes.containsKey(source_id)){
					nodes_id = source_id;
					reactant = true;
				}
				else if(reactions.containsKey(source_id))
					reaction_id = source_id;
			}
			else if(a.getSource().getClass().equals(Glyph.class)){
				source_id = ((Glyph) a.getSource()).getId();
				if(nodes.containsKey(source_id)){
					nodes_id = source_id;
					reactant = true;
				}
				else if(reactions.containsKey(source_id))
					reaction_id = source_id;
			}
			
			
			if(a.getTarget().getClass().equals(Port.class)){
				target_id = findGlyphFromPor((Port) a.getTarget());
				if(nodes.containsKey(target_id))
					nodes_id = target_id;
				else if(reactions.containsKey(target_id))
					reaction_id = target_id;
			}
			else if(a.getTarget().getClass().equals(Glyph.class)){
				target_id = ((Glyph) a.getTarget()).getId();
				if(nodes.containsKey(target_id))
					nodes_id = target_id;
				else if(reactions.containsKey(target_id))
					reaction_id = target_id;
			}
			
			if(a.getClazz().equals(CONS)){
				((ReactionLay)reactions.get(reaction_id)).setReversible(false);
			}
			
			if(!isInfo(nodes_id) && !nodes_id.equals("") && !reaction_id.equals("")){
				if(reactant){
					reactions.get(reaction_id).getReactants().put(nodes_id, nodes.get(nodes_id));
				}
				else{
					reactions.get(reaction_id).getProducts().put(nodes_id, nodes.get(nodes_id));
				}
			}
			else if(isInfo(nodes_id) && !nodes_id.equals("") && !reaction_id.equals("")){
				reactions.get(reaction_id).infos().put(nodes_id, nodes.get(nodes_id));
			}
		}
		
		LayoutContainer layout = new LayoutContainer(nodes, reactions);
		return layout;
	}
	
//	/**
//	 * Creates a information node from a {@link Glyph}.
//	 * @param g
//	 */
//	private void createInfo(Glyph g) {
//		
//		String id = g.getId();
//
//		String label = g.getLabel().getText();
//		Double x =  (double) (g.getBbox().getX() + (g.getBbox().getW() / 2));
//		Double y = (double) (g.getBbox().getY() + (g.getBbox().getH() / 2));
//
//		Set<String> ids = new HashSet<String>();
//		ids.add(label);
//
//		List<Port> ports = g.getPort();
//
//		NodeTypeLay nodeType = NodeTypeLay.INFORMATION;
//		
//		NodeLay node = new NodeLay(id, label, ids,nodeType, x, y);
//		portsMap.put(id, ports);
//		
//		this.nodes.put(id, node);
//
//	}


	/**
	 * Checks if a node id belongs to an information node.
	 * @param nodes_id
	 * @return
	 */
	private boolean isInfo(String nodes_id) {
		for(Glyph g : infos)
			if(g.getId().equals(nodes_id)) return true;
		return false;
	}



	/**
	 * Find the glyph that a {@link Port} belongs to.
	 * @param a
	 * @return
	 */
	private String findGlyphFromPor(Port a) {
		
		String PortText = a.getId();
		String glyphID = "";
		for(String key : nodes.keySet()){
			
//			INodeLay node = nodes.get(key);
			for(Port p : portsMap.get(key)){
				if(p.getId().equals(PortText)){
					glyphID = key;
				}
			}
		}
		if(glyphID.equals("")){
			for(String key: reactions.keySet()){
				
//				SimpleReactionLay reaction = (SimpleReactionLay)reactions.get(key);
				for(Port p : portsMap.get(key)){
					if(p.getId().equals(PortText))
						glyphID = key;
				}
			}
		}
		return glyphID;
	}


	/**
	 * Creates a reaction node from a {@link Glyph}
	 * @param g
	 */
	private void crateReaction(Glyph g) {
		
		String id = g.getId();
		String label;
		Set<String> ids;
		if(g.getLabel()!=null){
			label = g.getLabel().getText();
			ids = new HashSet<String>();
			String[] x = label.split(";");
			for(String s : x)
				ids.add(s);
		}
		else{
			label = id;
			ids = new HashSet<String>();
			ids.add(id);
		}
		
		java.util.Map<String, INodeLay> reactants = new HashMap<String, INodeLay>();
		java.util.Map<String, INodeLay> products = new HashMap<String, INodeLay>();

		Double x =  (double) (g.getBbox().getX() + (g.getBbox().getW() / 2));
		Double y = (double) (g.getBbox().getY() + (g.getBbox().getH() / 2));
		
		List<Port> ports = g.getPort();
		
		java.util.Map<String, INodeLay> infos = new HashMap<String, INodeLay>();
		
		ReactionLay reaction = new ReactionLay(reactants, products, infos, true, x, y, ids, id, label);
		portsMap.put(id, ports);
		
		reactions.put(id, reaction);
	}



	/**
	 * Creates a metabolite or currency node from a {@link Glyph}
	 * @param g
	 */
	private void createNode(Glyph g) {

		String id = g.getId();


		String label = g.getLabel().getText();
		Double x =  (double) (g.getBbox().getX() + (g.getBbox().getW() / 2));
		Double y = (double) (g.getBbox().getY() + (g.getBbox().getH() / 2));

		Set<String> ids = new HashSet<String>();
		String[] v = label.split(";");
		for(String s : v)
			ids.add(s);

		List<Port> ports = g.getPort();

		NodeTypeLay nodeType = NodeTypeLay.METABOLITE;
		if(g.getClone()!=null)
			nodeType = NodeTypeLay.CURRENCY;

		NodeLay node = new NodeLay(id, label, ids,nodeType, x, y);
		portsMap.put(id, ports);
		this.nodes.put(id, node);
	}

}
