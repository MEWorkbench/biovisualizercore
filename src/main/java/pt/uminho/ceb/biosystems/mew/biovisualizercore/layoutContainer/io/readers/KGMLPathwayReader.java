package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLCompound;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLEntry;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLPathway;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLReaction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLRelation;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.EntryType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.RelationType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers.kgmmlParser.KGMLParser;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

/**
 * Uses {@link KGMLParser} to read a kgml file, and builds the
 * layout.
 * 
 * @author Alberto Noronha
 *
 */
public class KGMLPathwayReader implements ILayoutBuilder {
	
	public static boolean debug = false;

	protected KGMLParser fp;
	protected KGMLPathway kgmlPathway;
	
	private Map<String, INodeLay> nodes; 
	
	private Map<String, IReactionLay> reactions;

	private Map<String, String> metabolicIdsMap;
	
	public KGMLPathwayReader(File file, Map<String, String> names) throws InvalidLayoutFileException {
		try {
			fp = new KGMLParser(file);
			kgmlPathway = fp.parseFile();
			this.metabolicIdsMap = names;
			reactions = new HashMap<String, IReactionLay>();
			nodes = new HashMap<String, INodeLay>();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new InvalidLayoutFileException("Invalid KGML file!\nCause: " + e.getMessage());
		}
		
	}
	
	public KGMLPathwayReader(String kgmlText, Map<String, String> names) throws InvalidLayoutFileException {
		try {
			fp = new KGMLParser(kgmlText);
			kgmlPathway = fp.parseFile();
			this.metabolicIdsMap = names;
			reactions = new HashMap<String, IReactionLay>();
			nodes = new HashMap<String, INodeLay>();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new InvalidLayoutFileException("Invalid KGML file!\nCause: " + e.getMessage());
		}
	}


	public KGMLPathway getPathway() {
		return this.kgmlPathway;
	}

//	@Override
//	public LayoutContainer buildLayout() {
//		
//		
//		Map<String, String> keggToUniqueId = new HashMap<String, String>();
//		Map<String, Set<String>> uuidToAcc = new HashMap<String, Set<String>>();
//		
//		Map<Integer, KGMLReaction> kgml_reactions = kgmlPathway.getReactions();
//		
//		for(Integer i : kgml_reactions.keySet()){
//			
//			KGMLReaction r = kgml_reactions.get(i);
//			Set<String> ids = new HashSet<String>(r.getAccessions());
//
//			System.out.println("Id: " + i.toString() + " " + ids);
//			
//			Map<String, INodeLay> reactants = buildNodes(r.getSubstrates(), keggToUniqueId);
//			Map<String, INodeLay> products  = buildNodes(r.getProducts(), keggToUniqueId);
//			
//			Integer kgml_id = r.getId();
//			String uniqueId = UUID.randomUUID().toString();
//
//			boolean reversible = r.getReversible();
//			
//			Set<String> metabolicIdsSet = new HashSet<String>();
//			
//			KGMLEntry entry = kgmlPathway.getEntry(kgml_id);
//			
//			for(String metabolicId : metabolicIdsMap.keySet()){
//				
//				if(ids.contains(metabolicIdsMap.get(metabolicId)))
//					metabolicIdsSet.add(metabolicId);
//			}
//			
//			String label;
//			if(ids.size()==1)
//				label = (String) ids.toArray()[0];
//			else
//				label = LayoutUtils.buildName(ids);
//			
//			String name= "";
//			
//			if(metabolicIdsSet.size()>0){
//				name = CollectionUtils.join(metabolicIdsSet, "");
//			}
//			
//			if(!name.equals(""))
//				label = name;
//
//
//			Double x = Double.valueOf(entry.getGraphics().getX());
//			Double y = Double.valueOf(entry.getGraphics().getY());
//			
//			Map<String, INodeLay> infos = new HashMap<String, INodeLay>();
//			ReactionLay reactionLay = new ReactionLay(reactants, products, infos , reversible, x, y, metabolicIdsSet, uniqueId, label);
//			
//			Set<String> accs = new HashSet<String>(r.getAccessions());
//			uuidToAcc.put(uniqueId, accs);
//			reactions.put(uniqueId, reactionLay);
//		}
//		
//		
//		LayoutContainer layout = new LayoutContainer(nodes, reactions);
////		cleanRepeatedReactions(layout);
//		
//		LayoutUtils.simplifyLayout(layout, uuidToAcc);
//		return layout;
//	}
	
	public LayoutContainer buildLayout(){
		
		Map<String, String> keggToUniqueId = new HashMap<String, String>();
		Map<String, Set<String>> uuidToAcc = new HashMap<String, Set<String>>();
		
		Map<Integer, KGMLEntry> kgml_entries = kgmlPathway.getEntries();
		List<KGMLRelation> kgml_relations = kgmlPathway.getRelations();
		Map<Integer, KGMLReaction> kgml_reactions = kgmlPathway.getReactions();
		
		for(Integer index : kgml_entries.keySet()){
			
			KGMLEntry kgml_entry = kgml_entries.get(index);
			EntryType entry_type = kgml_entry.getType();
			
			if(entry_type.equals(EntryType.REACTION)){
				
				if(kgml_reactions.containsKey(index)){
					
					KGMLReaction r = kgml_reactions.get(index);
					Set<String> ids = new HashSet<String>(r.getAccessions());

					Map<String, INodeLay> reactants = buildNodes(r.getSubstrates(), keggToUniqueId);
					Map<String, INodeLay> products  = buildNodes(r.getProducts(), keggToUniqueId);
					
					Integer kgml_id = r.getId();
					String uniqueId = UUID.randomUUID().toString();

					boolean reversible = r.getReversible();

					KGMLEntry entry = kgmlPathway.getEntry(kgml_id);

					Set<String> metabolicIdsSet = new HashSet<String>();

					for(String metabolicId : metabolicIdsMap.keySet()){

						if(ids.contains(metabolicIdsMap.get(metabolicId)))
							metabolicIdsSet.add(metabolicId);
					}
					
					String label = buildLabel(ids, metabolicIdsSet);


					Double x = Double.valueOf(entry.getGraphics().getX());
					Double y = Double.valueOf(entry.getGraphics().getY());
					
					Map<String, INodeLay> infos = new HashMap<String, INodeLay>();
					ReactionLay reactionLay = new ReactionLay(reactants, products, infos , reversible, x, y, metabolicIdsSet, uniqueId, label);
					
					Set<String> accs = new HashSet<String>(r.getAccessions());
					uuidToAcc.put(uniqueId, accs);
					reactions.put(uniqueId, reactionLay);
					keggToUniqueId.put(kgml_id.toString(), uniqueId);
				}
				else{

					//build reaction by relations
					Set<String> accs = new HashSet<String>();
					String name = kgml_entry.getReactionId();
					String[] names = name.split(" ");
					for(String n : names){
						accs.add(n.replaceAll("rn\\:", ""));
					}

					Double x = Double.valueOf(kgml_entry.getGraphics().getX());
					Double y = Double.valueOf(kgml_entry.getGraphics().getY());

					Integer kgml_id = kgml_entry.getId();
					String reaction_uniqueId = UUID.randomUUID().toString();

					Set<String> react_idsSet = new HashSet<String>();

					for(String metabolicId : metabolicIdsMap.keySet()){

						if(accs.contains(metabolicIdsMap.get(metabolicId)))
							react_idsSet.add(metabolicId);
					}

					String react_label = buildLabel(accs, react_idsSet);

					Map<String, INodeLay> reactants = new HashMap<String,INodeLay>();
					Map<String, INodeLay> products = new HashMap<String, INodeLay>();
					Map<String, INodeLay> infos = new HashMap<String, INodeLay>();
					
					boolean reversibility = false;
					
					for(KGMLRelation relation : kgml_relations){

						if((relation.getEntry1().equals(index) || relation.getEntry2().equals(index)) && relation.getRelationType().equals(RelationType.ECREL)){
							

							if(relation.getSubType().getType().equals(EntryType.COMPOUND)){
								
								
								Integer compound_id = relation.getSubType().getId();
								String node_unique_id;
								
								
								INodeLay layout_node;
								if(keggToUniqueId.containsKey(compound_id.toString())){
									System.out.println("NODE EXISTS");
									node_unique_id = keggToUniqueId.get(compound_id.toString());
									layout_node = nodes.get(node_unique_id);
								}
								else{
									System.out.println("CREATING NODE!");
									KGMLEntry entry = kgml_entries.get(compound_id);
									Double node_x = Double.valueOf(entry.getGraphics().getX());
									Double node_y = Double.valueOf(entry.getGraphics().getY());

									//MAP FROM METABOLIC ID -> KEGG_ID
									String label = entry.getName();
									String node_name = "";

									Set<String> metabolicIdsSet = new HashSet<String>();

									Set<String> ids = new HashSet<String>();
									String[] vector = entry.getName().split(" ");
									for(String v : vector){
										ids.add(v.replaceAll("cpd\\:", ""));
									}
									
									
									for(String metabolicId : metabolicIdsMap.keySet()){

										if(ids.contains(metabolicIdsMap.get(metabolicId))){
											metabolicIdsSet.add(metabolicId);
											node_name += metabolicId;
										}
									}

									if(!node_name.equals(""))
										label = node_name;
									else
										label = clean_name(label);

									node_unique_id = UUID.randomUUID().toString();
									layout_node = new NodeLay(node_unique_id,label,metabolicIdsSet, NodeTypeLay.METABOLITE, node_x,node_y);
									nodes.put(node_unique_id, layout_node);
									keggToUniqueId.put(compound_id.toString(), node_unique_id);
								}

								if(relation.getEntry1().equals(index)){
									if(!reactants.containsKey(node_unique_id))
										products.put(node_unique_id, layout_node);
									else 
										reversibility = true;
										

									
								}
								else{
									if(!products.containsKey(node_unique_id))
										reactants.put(node_unique_id, layout_node);
									else
										reversibility = true;
									
									
								}
							}
						}
					}
					
					if(reactants.keySet().size()>0 || products.keySet().size()>0){
						uuidToAcc.put(reaction_uniqueId, accs);
						IReactionLay reaction = new ReactionLay(reactants, products, infos, reversibility, x, y, react_idsSet, reaction_uniqueId, react_label);
						reactions.put(reaction_uniqueId, reaction);
						keggToUniqueId.put(index.toString(), reaction_uniqueId);
					}

				}
			}
		}
		
		LayoutContainer layout = new LayoutContainer(nodes, reactions);
		LayoutUtils.simplifyLayout(layout, uuidToAcc);
		return layout;
	}

	
		

		private String clean_name(String label) {
			
			String[] vector = label.split(" ");
			String finalLabel = "";
			for(String v : vector){
				finalLabel += v.replaceAll("cpd\\:", "") + " ";
			}
			
			return finalLabel;
	}

		private String buildLabel(Set<String> ids, Set<String> metabolicIdsSet) {
			
			String label;
			if(ids.size()==1)
				label = (String) ids.toArray()[0];
			else
				label = LayoutUtils.buildName(ids);

			String name= "";

			if(metabolicIdsSet.size()>0){
				name = CollectionUtils.join(metabolicIdsSet, "");
			}

			if(!name.equals(""))
				label = name;

			return label;
		}
		
	public static void cleanRepeatedReactions(LayoutContainer layout) {
		
		Set<String> mappedReactions = new HashSet<String>();
		for(String r : layout.getReactions().keySet()){
			
			IReactionLay reaction = layout.getReactions().get(r);
			if(layout.getReactions().get(r).getIDs().size()>0){
				
				Set<String> rIds = reaction.getIDs();
				
				int removed = 0;
				for(String id : rIds){
					if(mappedReactions.contains(id)){
						removed++;
						layout.getReactions().get(r).getIDs().remove(id);
					}
					else
						mappedReactions.add(id);
				}
				
				if(removed == rIds.size()) layout.getReactions().remove(r);
			}
		}
		
		//remove nodes that don't have reactions
		for(String nodeId : layout.getNodes().keySet()){
			
			int count = 0;
			for(String s : layout.getReactions().keySet()){
				
				IReactionLay reaction = layout.getReactions().get(s);
				if(reaction.getReactants().containsKey(nodeId) || reaction.getProducts().containsKey(nodeId)) count ++;
			}
			
			if(count == 0) layout.getNodes().remove(nodeId);
		}
		
	}
	
	/**
	 * Builds nodes from a list of {@link KGMLCompound}.
	 * @param list
	 * @param uniqueIdtoKeggId 
	 * @return
	 */
	private Map<String, INodeLay> buildNodes(List<KGMLCompound> list, Map<String, String> keggToUniqueId) {

		Map<String, INodeLay> result = new HashMap<String, INodeLay>();

		for(KGMLCompound compound : list){
			NodeLay node = null;

			Set<String> ids = new HashSet<String>(compound.getAccessions());
			Integer kgml_id  = compound.getId();
			
			String unique_id = UUID.randomUUID().toString();
			
			KGMLEntry entry = kgmlPathway.getEntry(kgml_id);
			Double x = Double.valueOf(entry.getGraphics().getX());
			Double y = Double.valueOf(entry.getGraphics().getY());

			//MAP FROM METABOLIC ID -> KEGG_ID

			String label = entry.getGraphics().getName();

			String name = "";

			Set<String> metabolicIdsSet = new HashSet<String>();

			for(String metabolicId : metabolicIdsMap.keySet()){

				if(ids.contains(metabolicIdsMap.get(metabolicId))){
					metabolicIdsSet.add(metabolicId);
					name += metabolicId;
				}
			}

			if(!name.equals(""))
				label = name;

			NodeTypeLay type = NodeTypeLay.METABOLITE;

			
			if(!keggToUniqueId.containsKey(kgml_id.toString())){

				node = new NodeLay(unique_id,label,metabolicIdsSet,type,x,y);
				nodes.put(unique_id, node);
				result.put(unique_id, node);
				keggToUniqueId.put(kgml_id.toString(), unique_id);
				

			}
			else{
				
				String nodeId = keggToUniqueId.get(kgml_id.toString());
				node = (NodeLay) nodes.get(nodeId);
				result.put(nodeId, node);
			}
			
		}

		return result;
	}
	
}
