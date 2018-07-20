package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;

public class EscherLayoutReader implements ILayoutBuilder{

	Double readFactor = 0.5;
	
	private InputStream stream;
	private Map<String, INodeLay> nodes;
	private Map<String, IReactionLay> reactions;
	private boolean closeStream;
	
	public EscherLayoutReader(InputStream stream){
		this.stream = stream;
		this.closeStream = false;
	}
	
	public EscherLayoutReader(String layout_File) throws FileNotFoundException{
		this(new File(layout_File));
	}
	
	public EscherLayoutReader(File layout_File) throws FileNotFoundException {
		this(new BufferedInputStream(new FileInputStream(layout_File)));
		this.closeStream = true;
	}

	@Override
	public LayoutContainer buildLayout() throws JsonProcessingException, IOException {
		nodes = new HashMap<>();
		reactions = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode map = mapper.readTree(stream);
	
		
		JsonNode nodes = map.get(1).get("nodes");
		parseNodes(nodes);
		JsonNode reactions = map.get(1).get("reactions");
		parseReactions(reactions);
		if(closeStream) this.stream.close();
		return new LayoutContainer(this.nodes, this.reactions);
	}

	
	
	private void parseReactions(JsonNode reactions) {
		Iterator<Entry<String, JsonNode>> it = reactions.fields();
		
		while(it.hasNext()){
			Entry<String, JsonNode> entry = it.next();
			String id = entry.getKey();
			JsonNode n = entry.getValue();
			
			
			String name = n.get("name").asText();
			Boolean rev = n.get("reversibility").asBoolean();
			String biggId = n.get("bigg_id").asText();
			
			List<JsonNode> to = n.findValues("to_node_id");
			List<JsonNode> from = n.findValues("from_node_id");
			Double x = n.get("label_x").asDouble() * readFactor;
			Double y = n.get("label_y").asDouble() * readFactor;
			
			
			JsonNode metab = n.get("metabolites");
			
			List<Set<String>> stoiq = parseStoiq(metab);

			Map<String, INodeLay> reactants = new HashMap<>();
			Map<String, INodeLay> products = new HashMap<>();
//			Set<String> metaboliteNodes = verifyNodes(to);
//			metaboliteNodes.addAll(verifyNodes(from));
			verifyNodes(from, stoiq, reactants, products);
			verifyNodes(to, stoiq, reactants, products);
//			Map<String, INodeLay> reactants = MapUtils.subMap(nodes, stoiq.get(0), null);
//			Map<String, INodeLay> products = MapUtils.subMap(nodes, stoiq.get(0), null);
			
//			System.out.println(metaboliteNodes);
//			System.out.println(stoiq);
//			System.out.println(reactants);
//			System.out.println(products);
			ReactionLay rlay = new ReactionLay(reactants, products, new HashMap<String, INodeLay>(), rev, x, y, treatReactions(biggId), id, name);
			this.reactions.put(rlay.getUniqueId(), rlay);
		}
		
		
	}

	private void verifyNodes(List<JsonNode> from, List<Set<String>> stoiq, Map<String, INodeLay> reactants,
			Map<String, INodeLay> products) {
		
		for(JsonNode n : from){
			String id = n.asText();
			INodeLay nl = nodes.get(id);
			if(nl!=null){
				if(stoiq.get(0).containsAll(nl.getIds()))
					reactants.put(nl.getUniqueId(), nl);
				if(stoiq.get(1).containsAll(nl.getIds()))
					products.put(nl.getUniqueId(), nl);
			}
		}
		
	}

	private List<Set<String>> parseStoiq(JsonNode metab) {
		ArrayList<Set<String>> stoiq = new ArrayList<>();
		stoiq.add(new HashSet<String>());
		stoiq.add(new HashSet<String>());
		
		for(JsonNode m: metab){
			Double d = m.get("coefficient").asDouble();
			String bigg = m.get("bigg_id").asText();
			
			if(d<0) stoiq.get(0).add(bigg);
			else if(d>0) stoiq.get(1).add(bigg);
		}
		
		return stoiq;
	}

	private Set<String> verifyNodes(List<JsonNode> from) {
		
		HashSet<String> ret = new HashSet<>();
		for(JsonNode n : from){
			String id = n.asText();
			if(nodes.containsKey(id)) ret.add(id);
		}
		
		return ret;
	}

	private void parseNodes(JsonNode nodes){
		Iterator<Entry<String, JsonNode>> it = nodes.fields();
		
		while(it.hasNext()){
			Entry<String, JsonNode> entry = it.next();
			String id = entry.getKey();
			JsonNode n = entry.getValue();
			String type = n.get("node_type").asText();
			double x = n.get("x").asDouble() * readFactor;
			double y = n.get("y").asDouble() * readFactor;
		
			JsonNode jname = n.get("name");
			String name = (jname != null)?jname.asText():null;
			
			JsonNode jIsPr = n.get("node_is_primary");
			Boolean isPr = (jIsPr !=null)?jIsPr.asBoolean():null;
			
			JsonNode jBigg = n.get("bigg_id");
			String bigg_id = (jBigg!=null)?jBigg.asText():null;
			addNode(id,type, name, bigg_id, isPr, x, y);
		}
	}
	
	
	private void addNode(String id, String type, String name , String bigg_id, Boolean isPr, Double x, Double y){
		
		Set<String> linkIds = null;
		NodeTypeLay nodeType = NodeTypeLay.METABOLITE;
		switch (type) {
		case "metabolite":
			linkIds = treatMetabolites(bigg_id);
			if(!isPr) nodeType = NodeTypeLay.CURRENCY;
			
			break;
		case "reaction":
			linkIds = treatReactions(bigg_id);
			nodeType = NodeTypeLay.REACTION;
			break;
		default:
			return;
		}
		NodeLay n = new NodeLay(id, name, linkIds, nodeType , x, y);
		
//		System.out.printf("%s %s %s %s %s\n", id, name, linkIds, nodeType , x, y);
		this.nodes.put(n.getUniqueId(), n);
		
	}

	private Set<String> treatReactions(String bigg_id) {
		return new HashSet<>(java.util.Arrays.asList(bigg_id));
	}

	private Set<String> treatMetabolites(String bigg_id) {
		return new HashSet<>(java.util.Arrays.asList(bigg_id));
	}
}
