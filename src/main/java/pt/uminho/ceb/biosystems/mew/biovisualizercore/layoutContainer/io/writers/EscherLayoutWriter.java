package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;

public class EscherLayoutWriter {

	private OutputStream out;

	Map<Integer, String> multimarketReactionId;
	Map<Integer,String> midmarker;
	Map<Integer, String> nodesIds;
	int id=0;
	double racio;
	
	public EscherLayoutWriter(OutputStream out){
		this(out, 2.0);
	}
	
	public EscherLayoutWriter(OutputStream out, double racio){
		this.out = out;
		this.racio = racio;
	}

	public void write(ILayout cont) throws IOException{
		multimarketReactionId = new HashMap<>();
		midmarker = new HashMap<>();
		
		JsonGenerator node = new JsonFactory().createGenerator(out);
		node.setPrettyPrinter(new DefaultPrettyPrinter());
		node.writeStartArray();
		node.writeStartObject();
		node.writeStringField("map_name", "");
		node.writeStringField("map_id", "");
		node.writeStringField("map_description", "");
		node.writeStringField("homepage", "www.optflux.org");
		node.writeStringField("schema", "https://escher.github.io/escher/jsonschema/1-0-0#");
		node.writeEndObject();
		node.writeStartObject();
		writeReactions(node, cont.getReactions(), cont.getNodes());
		writeMetabolites(node, cont.getNodes(), cont.getReactions());
		
		node.writeObjectFieldStart("canvas");
		node.writeEndObject();
		
		node.writeObjectFieldStart("text_labels");
		node.writeEndObject();
		
		node.writeEndObject();
		node.writeEndArray();
		node.flush();
		node.close();
	}

	double convert(double p) {
		return p*racio;
	}
	
	private void writeMetabolites(JsonGenerator node, Map<String, INodeLay> nodes, Map<String, IReactionLay> reactions) throws IOException {
		node.writeFieldName("nodes");
		node.writeStartObject();
		for(INodeLay nl : nodes.values()){
			
			String id = nl.getUniqueId();
			int x = nl.getX().intValue();
			int y = nl.getY().intValue();
			String bigg_id = nl.getIds().iterator().next();
			String node_type = "metabolite";
			String name = nl.getLabel();
			Boolean is_primary = nl.getNodeType().equals(NodeTypeLay.METABOLITE);
			
			node.writeObjectFieldStart(id);
			node.writeObjectField("node_is_primary", is_primary);
			node.writeStringField("name", name);
			node.writeStringField("node_type", node_type);
			node.writeStringField("bigg_id", bigg_id);
			node.writeObjectField("x", convert(x));
			node.writeObjectField("y", convert(y));
			node.writeObjectField("label_x", convert(x-3));
			node.writeObjectField("label_y", convert(y+3));
			node.writeEndObject();
		}
		
		for(Integer id : multimarketReactionId.keySet()){
			IReactionLay reaction = reactions.get(multimarketReactionId.get(id));
			node.writeObjectFieldStart(id+"");
			node.writeStringField("node_type", "multimarker");
			node.writeObjectField("y", convert(reaction.getY().intValue()));
			node.writeObjectField("x", convert(reaction.getX().intValue()));
			node.writeEndObject();
		}
		
		for(Integer id : midmarker.keySet()){
			IReactionLay reaction = reactions.get(midmarker.get(id));
			node.writeObjectFieldStart(id+"");
			node.writeObjectField("y", convert(reaction.getY().intValue()));
			node.writeObjectField("x", convert(reaction.getX().intValue()));
			node.writeStringField("node_type", "midmarker");
			node.writeEndObject();
		}
		
		node.writeEndObject();
	}

	private void writeReactions(JsonGenerator node, Map<String, IReactionLay> reactions, Map<String, INodeLay> nodesLay) throws IOException {
		node.writeFieldName("reactions");
		node.writeStartObject();
		for(String id : reactions.keySet()){
//			System.out.println("==>>" +id);
			IReactionLay r = reactions.get(id);
			Double x = r.getX();
			Double y = r.getY();
			
			node.writeObjectFieldStart(id);
			node.writeStringField("name", r.getLabel());
			node.writeStringField("bigg_id", r.getIDs().iterator().next());

			//			node.writeObjectFieldStart("segments");
			
			writeSegments(node, id,r.getReactants(), r.getProducts(), x , y);
			node.writeArrayFieldStart("genes");
			node.writeEndArray();
			node.writeObjectField("reversibility", r.isReversible());

			writeStoichiometry(node, r.getReactants(), r.getProducts(), nodesLay);
			node.writeObjectField("label_x", convert(x));
			node.writeObjectField("label_y", convert(y));
			node.writeStringField("gene_reaction_rule", "");
			node.writeEndObject();
		}
		

		node.writeEndObject();

	}

	private void writeStoichiometry(JsonGenerator node, Map<String, INodeLay> reactants, Map<String, INodeLay> products, Map<String, INodeLay> nodesLay) throws IOException {
		node.writeFieldName("metabolites");
		node.writeStartArray();
		
		writeSinglestoichiometry(node, reactants, -1, nodesLay);
		writeSinglestoichiometry(node, products, 1, nodesLay);
		
		node.writeEndArray();
	}

	private void writeSinglestoichiometry(JsonGenerator node, Map<String, INodeLay> reactants, int i, Map<String, INodeLay> nodesLay) throws IOException {
		for(String id : reactants.keySet()){
			node.writeStartObject();
			node.writeObjectField("coefficient", i);
			node.writeStringField("bigg_id", nodesLay.get(id).getIds().iterator().next());
			node.writeEndObject();
		}
		
	}

	private void writeSegments(JsonGenerator node, String reactionId, Map<String, INodeLay> reactants, Map<String, INodeLay> products, Double x, Double y) throws IOException {
		
//		int multimarketId = id++;
//		multimarketReactionId.put(multimarketId, reactionId);
		int mid = id++;
		this.midmarker.put(mid, reactionId);
		
		node.writeFieldName("segments");
		node.writeStartObject();

		
		writeSingleSegments(node, reactionId,mid+"", reactants, -1);
		writeSingleSegments(node, reactionId,mid+"", products, 1);
		node.writeEndObject();
	}
	
	void writeSingleSegments(JsonGenerator node, String reactionId,String midmarketId, Map<String, INodeLay> info, int flag) throws IOException{
		
		int mult = id++;
		multimarketReactionId.put(mult, reactionId);
		
//		int m = id++;
//		this.midmarker.put(m, reactionId);
//		String midmarketId = m+"";
		String multimarketId = mult+"";
		for(INodeLay rect: info.values()){
			node.writeObjectFieldStart("" + (id++));
			node.writeStringField("to_node_id",(flag == -1)?multimarketId: rect.getUniqueId());
			node.writeStringField("from_node_id", (flag == -1)?rect.getUniqueId():multimarketId);
			node.writeNullField("b2");
			node.writeNullField("b1");
			node.writeEndObject();
		}
		
		node.writeObjectFieldStart("" + (id++));
		node.writeStringField("to_node_id",(flag != -1)?multimarketId: midmarketId);
		node.writeStringField("from_node_id", (flag != -1)?midmarketId: multimarketId);
		node.writeNullField("b2");
		node.writeNullField("b1");
		node.writeEndObject();
		
	}
}
