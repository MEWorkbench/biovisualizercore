package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;

public class BiGGLayoutReader implements ILayoutBuilder{

	private Double factor = 2.0;
	private Map<String, INodeLay> nodes;
	private Map<String, IReactionLay> reactions;

	private Map<String, String[]> groupToLabelAndReversibility;

	public BiGGLayoutReader(String file) throws InvalidLayoutFileException, FileNotFoundException{
		this(new FileReader(file));
	}

	public BiGGLayoutReader(Reader file) throws InvalidLayoutFileException{

		nodes = new HashMap<String, INodeLay>();
		reactions = new HashMap<String, IReactionLay>();

		readFile(file);
	}


	private void readFile(Reader file) throws InvalidLayoutFileException {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(file);
			String line;

			String section = null;
			int line_count = 0;
			Map<String, String[]> molecules = new HashMap<String, String[]>();
			Map<String, String[]> reactionNodes = new HashMap<String, String[]>();
			List<String[]> connections = new ArrayList<String[]>();

			while((line=reader.readLine())!=null){

				line_count ++;

				if(line.equalsIgnoreCase("Molecules")||line.equalsIgnoreCase("Reactions Nodes")||line.equalsIgnoreCase("Reactions")
						||line.equalsIgnoreCase("Texts")){
					section = line;

				}
				else if(section == null) throw new InvalidLayoutFileException("Incorrect file format. Can't read the file, please check if it is a valid Bigg layout file.");

				else if(section.equalsIgnoreCase("Molecules")){

					String[] moleculeInfo = line.split("\\t", -1);

					if(moleculeInfo.length < 7 ) throw new InvalidLayoutFileException(line_count, "Incorrect \"Molecules\" section field incomplete. \"Molecules\" ");

					String unique_Id = moleculeInfo[7];
					String[] info = new String[4];

					//map node_id -> Id, Y/N, x, y
					info[0] = moleculeInfo[0];
					info[1] = moleculeInfo[2];
					info[2] = moleculeInfo[5];
					info[3] = moleculeInfo[6];

					if(!checkValidity(info)) throw new InvalidLayoutFileException(line_count, "Incorrect \"Molecules\" section field.\nCheck if the fields are correctly specified!");
					molecules.put(unique_Id, info);
				}
				else if(section.equalsIgnoreCase("Reactions Nodes")){

					String[] reactionNodeInfo = line.split("\\t", -1);

					if(reactionNodeInfo.length < 7 ) throw new InvalidLayoutFileException(line_count, "Incorrect file format. \"Reaction Nodes\" field.");

					String uniqueId = reactionNodeInfo[7];
					String[] info = new String[2];
					info[0] = reactionNodeInfo[5];
					info[1] = reactionNodeInfo[6];


					if(!checkValidity(info)) throw new InvalidLayoutFileException(line_count, "Incorrect file format.\" Reaction Nodes\" section field error. \nCheck if the fields are correctly specified!");
					reactionNodes.put(uniqueId, info);
				}
				else if(section.equalsIgnoreCase("Reactions")){

					String[] reactionInfo = line.split("\\t", -1);

					String info[] = new String[4];
					info[0] = reactionInfo[0];
					info[1] = reactionInfo[2];
					info[2] = reactionInfo[3];
					info[3] = reactionInfo[4];
					//				System.out.println(info[0] + "\t" + info[1] + "\t" + info[2] + "\t" + info[3]);

					if(!checkValidity(info)) throw new InvalidLayoutFileException(line_count, "Incorrect file format.\" Reactions\" section field error.\nCheck if the fields are correctly specified!");

					connections.add(info);
				}
			}

			reader.close();

			generateLayout(molecules, reactionNodes, connections);
		
		} catch (IOException e) {
			throw new InvalidLayoutFileException("Invalid file!" + "\n" + e.getMessage());
		}

	}


	private boolean checkValidity(String[] info) {

		for(String s : info) if (s == null) {
			return false;
		}

		return true;
	}


	private Double getCoordinateValue(String value){
		return Double.valueOf(value)/factor;
	}

	private void generateLayout(Map<String, String[]> molecules,
			Map<String, String[]> reactionNodes, List<String[]> connections) {


		//For Molecules
		for(String mol_node_id : molecules.keySet()){

			String label = molecules.get(mol_node_id)[0];
			Set<String> metIds = new HashSet<String>();
			metIds.add(label);

			NodeTypeLay nodeType = NodeTypeLay.METABOLITE;
			if(molecules.get(mol_node_id)[1].equalsIgnoreCase("N")) nodeType = NodeTypeLay.CURRENCY;

			Double x = getCoordinateValue(molecules.get(mol_node_id)[2]);
			Double y = getCoordinateValue(molecules.get(mol_node_id)[3]);

			NodeLay node = new NodeLay(mol_node_id, label, metIds, nodeType, x, y);
			nodes.put(mol_node_id, node);
		}

		//Group Reactions!
		Map<String, Set<String>> reactionGroups = groupReactions(reactionNodes, connections, molecules.keySet());

		//		System.out.println("\n\n\nGROUPS!");
		for(String g : reactionGroups.keySet()){

			//			System.out.println(g + " label: " + groupToLabelAndReversibility.get(g)[0] + "->" + "\t" + reactionGroups.get(g));
		}

		//		System.out.println("\n\n\n");

		//Build the reaction nodes
		for(String s : reactionGroups.keySet()){

			String uniqueId = s;
			String label = groupToLabelAndReversibility.get(uniqueId)[0];

			boolean reversible = false;

			if(groupToLabelAndReversibility.get(uniqueId)[1].equalsIgnoreCase("Reversible"))
				reversible = true;

			Set<String> nodes = reactionGroups.get(s);

			Double x = 0.0;
			Double y = 0.0;

			for(String rnId : nodes){

				x += getCoordinateValue(reactionNodes.get(rnId)[0]);
				y += getCoordinateValue(reactionNodes.get(rnId)[1]);
			}

			x = x / nodes.size();
			y = y / nodes.size();

			Set<String> metIds = new HashSet<String>();
			metIds.add(label);

			ReactionLay reaction = new ReactionLay(new HashMap<String, INodeLay>(),new HashMap<String, INodeLay>(), new HashMap<String, INodeLay>(), reversible, x, y, metIds, uniqueId, label);
			reactions.put(uniqueId, reaction);
		}

		//link the nodes
		for(String[] connection : connections){

			if(molecules.keySet().contains(connection[2])){

				//molecule is source
				String reactionId = groupsHaveReaction(reactionGroups, connection[3]);
				INodeLay node = nodes.get(connection[2]);
				reactions.get(reactionId).getReactants().put(node.getUniqueId(), node);
			}
			else if(molecules.keySet().contains(connection[3])){

				//molecule is target
				String reactionId = groupsHaveReaction(reactionGroups, connection[2]);
				INodeLay node = nodes.get(connection[3]);
				reactions.get(reactionId).getProducts().put(node.getUniqueId(), node);
			}
		}
	}


	private Map<String, Set<String>> groupReactions(
			Map<String, String[]> reactionNodes, List<String[]> connections,
			Set<String> molecules) {

		groupToLabelAndReversibility = new HashMap<String, String[]>();
		Map<String, Set<String>> groupReactions = new HashMap<String, Set<String>>();

		for(String[] connection : connections){

			//Reaction reaction
			if(!molecules.contains(connection[2]) && !molecules.contains(connection[3])){

				String group1 = groupsHaveReaction(groupReactions, connection[2]);
				String group2 = groupsHaveReaction(groupReactions, connection[3]);
				if(!group1.equals("") && !group2.equals("")){

					//merge groups
					String new_group = UUID.randomUUID().toString();

					Set<String> str_group1 = groupReactions.get(group1);
					Set<String> str_group2 = groupReactions.get(group2);

					Set<String> newSet = new HashSet<String>();
					newSet.addAll(str_group1);
					newSet.addAll(str_group2);

					String[] labelAndRev = this.groupToLabelAndReversibility.get(group1);

					groupReactions.remove(group1);
					groupReactions.remove(group2);
					groupReactions.put(new_group, newSet);

					groupToLabelAndReversibility.remove(group1);
					groupToLabelAndReversibility.remove(group2);
					groupToLabelAndReversibility.put(new_group, labelAndRev);


				}
				else if(!group1.equals("") && group2.equals("")){
					groupReactions.get(group1).add(connection[3]);
				}
				else if(group1.equals("") && !group2.equals("")){
					groupReactions.get(group2).add(connection[2]);
				}
				else{
					String new_group = UUID.randomUUID().toString();
					Set<String> rIds = new TreeSet<String>();
					rIds.add(connection[2]);
					rIds.add(connection[3]);
					groupReactions.put(new_group, rIds);
					String[] labelAndRev = new String[2];

					labelAndRev[0] = connection[0];
					labelAndRev[1] = connection[1];
					groupToLabelAndReversibility.put(new_group, labelAndRev);
				}
			}

			//molecule reaction
			else if(molecules.contains(connection[2]) && !molecules.contains(connection[3])){
				String group;
				if(groupsHaveReaction(groupReactions, connection[3]).equals("")){
					group = UUID.randomUUID().toString();
					Set<String> rIds = new TreeSet<String>();
					rIds.add(connection[3]);
					groupReactions.put(group, rIds);
					String[] labelAndRev = new String[2];
					labelAndRev[0] = connection[0];
					labelAndRev[1] = connection[1];
					groupToLabelAndReversibility.put(group, labelAndRev);
				}
			}

			//reaction molecule
			else if(!molecules.contains(connection[2]) && molecules.contains(connection[3])){
				String group;
				if(groupsHaveReaction(groupReactions, connection[2]).equals("")){
					group = UUID.randomUUID().toString();
					Set<String> rIds = new TreeSet<String>();
					rIds.add(connection[2]);
					groupReactions.put(group, rIds);
					String[] labelAndRev = new String[2];
					labelAndRev[0] = connection[0];
					labelAndRev[1] = connection[1];
					groupToLabelAndReversibility.put(group, labelAndRev);
				}
			}
		}

		return groupReactions;
	}


	private String groupsHaveReaction(
			Map<String, Set<String>> groupReactions, String string) {

		for(String group : groupReactions.keySet()){
			if(groupReactions.get(group).contains(string)) return group;
		}

		return "";
	}


	@Override
	public LayoutContainer buildLayout(){
		LayoutContainer lc = new LayoutContainer(nodes, reactions);
		return lc;
	}

}
