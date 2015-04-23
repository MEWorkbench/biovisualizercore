package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.Constants;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class OverlapFactory {
	
	public static IOverlapObject convertOverlap(IOverlapObject overlap, ILayout lc){
		
		AbstractOverlap newOverlap = new AbstractOverlap(overlap.getName());

		if(overlap.getNewReactionLabels()==null || overlap.getNewReactionLabels().size()==0){

			Map<String , String> newLabels = getConvertedLabels(lc, overlap.getEdgeThickness());
			newOverlap.setReactionLabels(newLabels);
		}
		else{
			//GOT TO DO SOMETHING ABOUT THE LABELS!
			Map<String, String> newLabels = getConvertedLabels2(lc, overlap.getNewReactionLabels());
			newOverlap.setReactionLabels(newLabels);
		}
		
		Map<String, Double> newThickness = getConvertedThickness(overlap.getEdgeThickness(), lc);
		newOverlap.setEdgeThickness(newThickness);
		
		Map<String, Integer> newShapes = getConvertedShapes(overlap.getNodeShapes(), lc);
		newOverlap.setNodeShapes(newShapes);
		
		Map<String, Integer> newColors = getConvertedNodeColors(overlap.getNodeColors(), lc);
		newOverlap.setNodeColors(newColors);
		
		Map<String, Pair<Integer, Integer>> newReactionColors = getConvertedReactionColors(overlap.getReactionsColors(), lc);
		newOverlap.setReactionColors(newReactionColors);
		
		Map<String, Set<String>> newFilters = getConvertedFilters(overlap.getPossibleInvR(), lc);
		newOverlap.setVisFilters(newFilters);
		
		Map<String, Boolean> newDirections = getConvertedDirections(overlap.getFluxDirections(), lc);
		newOverlap.setFluxDirections(newDirections);
		
		Map<String, Pair<Double,Double>> newNodeSizes = getConvertedNodeSizes(overlap.getNodeSizes(), lc);
		newOverlap.setNodeSizes(newNodeSizes);
		
		return newOverlap;
	}
	
	
	


	private static Map<String, Pair<Double, Double>> getConvertedNodeSizes(
			Map<String, Pair<Double, Double>> nodeSizes, ILayout lc) {

		Map<String, Pair<Double, Double>> result_sizes = new HashMap<String, Pair<Double,Double>>();
		
		for(String reaction_node_id : lc.getReactions().keySet()){
			
			Set<String> met_Ids = lc.getReactions().get(reaction_node_id).getIDs();
			
			for(String metId : met_Ids){
				if(nodeSizes.containsKey(metId)){
					result_sizes.put(reaction_node_id, nodeSizes.get(metId));
				}
			}
		}
		
		for(String met_node_id : lc.getNodes().keySet()){

			Set<String> met_Ids = lc.getNodes().get(met_node_id).getIds();

			for(String metId : met_Ids){
				if(nodeSizes.containsKey(metId)){
					result_sizes.put(met_node_id, nodeSizes.get(metId));
				}
			}
		}

		return result_sizes;
	}


	private static Map<String, Pair<Integer,Integer>> getConvertedReactionColors(
			Map<String, Pair<Integer, Integer>> reactionsColors, ILayout lc) {


		Map<String, Pair<Integer,Integer>> result_colors = new HashMap<String, Pair<Integer, Integer>>();
		
		for(String reaction_node_id : lc.getReactions().keySet()){
			
			Set<String> met_Ids = lc.getReactions().get(reaction_node_id).getIDs();
			
			boolean hasColor = false;
			Integer color1 = null;
			Integer color2 = null;
			for(String met_Id : met_Ids){
				
				if(reactionsColors.containsKey(met_Id)){
					
					if(hasColor){
						
						color1 += reactionsColors.get(met_Id).getA();
						color2 += reactionsColors.get(met_Id).getB();
					}
					else{
						hasColor = true;
						color1 = reactionsColors.get(met_Id).getA();
						color2 = reactionsColors.get(met_Id).getB();
					}
				}
			}
			
			Pair<Integer,Integer> pair = new Pair<Integer,Integer>(color1, color2);
			
			if(hasColor)
				result_colors.put(reaction_node_id, pair);
		}
		
		return result_colors;
	}

	private static Map<String, Integer> getConvertedNodeColors(
			Map<String, Integer> nodeColors, ILayout lc) {
		
		Map<String, Integer> result_colors = new HashMap<String, Integer>();
		
		for(String node_met_id : nodeColors.keySet()){
			
			//for the reactions
			for(String r_node_id : lc.getReactions().keySet()){
				
				if(lc.getReactions().get(r_node_id).getIDs().contains(node_met_id))
					if(!result_colors.containsKey(r_node_id)) result_colors.put(r_node_id, nodeColors.get(node_met_id));
					else result_colors.put(r_node_id, result_colors.get(r_node_id)+nodeColors.get(node_met_id));
			}
			
			//for metabolites
			for(String m_node_id : lc.getNodes().keySet()){
				
				if(lc.getNodes().get(m_node_id).getIds().contains(node_met_id))
					if(!result_colors.containsKey(m_node_id)) result_colors.put(m_node_id, nodeColors.get(node_met_id));
					else result_colors.put(m_node_id, result_colors.get(m_node_id)+nodeColors.get(node_met_id));
			}
		}

		return result_colors;
	}

	private static Map<String, Boolean> getConvertedDirections(
			Map<String, Boolean> fluxDirections, ILayout lc) {
	
		
		Map<String, Boolean> layout_directions = new HashMap<String, Boolean>();
		
		for(String reaction_node_Id : lc.getReactions().keySet()){
			
			Set<String> met_Ids = lc.getReactions().get(reaction_node_Id).getIDs();
			
			
			boolean direction = true;
			boolean hasDirection = false;
			
			for(String met_Id : met_Ids){
				if(fluxDirections.containsKey(met_Id)){
					hasDirection = true;
					direction = direction && fluxDirections.get(met_Id);
				}
			}
			
			if(hasDirection) layout_directions.put(reaction_node_Id, direction);
		}
		
		return layout_directions;
	}


	private static Map<String, Set<String>> getConvertedFilters(
			Map<String, Set<String>> possibleInvR,
			ILayout lc) {
		
		Map<String, Set<String>> new_layoutFilters = new HashMap<String, Set<String>>();
		
		//for each filter
		for(String s : possibleInvR.keySet()){
			
			Set<String> invisibleIds = possibleInvR.get(s);
			Set<String> new_InvisibleIds = new HashSet<String>();
			
			for(String r_node_Id : lc.getReactions().keySet()){
				
				Set<String> metIds = lc.getReactions().get(r_node_Id).getIDs();
				
				boolean allInvisible = true;
				for(String met_Id : metIds){
					
					if(!invisibleIds.contains(met_Id)) allInvisible = false;
				}
				
				if(allInvisible){
					new_InvisibleIds.add(r_node_Id);
				}
			}
			
			new_layoutFilters.put(s, new_InvisibleIds);
		}
		
		return new_layoutFilters;
	}


	
	private static Map<String, Integer> getConvertedShapes(
			Map<String, Integer> nodeShapes, ILayout lc) {
		
		Map<String, Integer> layout_node_shapes = new HashMap<String, Integer>();
		
		for(String metId : nodeShapes.keySet()){
			
			List<String> nodesWithMet = new ArrayList<String>();
			
			for(String s : lc.getReactions().keySet()){
				if(lc.getReactions().get(s).getIDs().contains(metId)) nodesWithMet.add(s);
			}
			for(String s : lc.getNodes().keySet()){
				if(lc.getNodes().get(s).getIds().contains(metId)) nodesWithMet.add(s);
			}
			
			for(String node_id : nodesWithMet){
				if(!layout_node_shapes.containsKey(node_id))
					layout_node_shapes.put(node_id, nodeShapes.get(metId));
				else
					layout_node_shapes.put(node_id, Constants.SHAPE_QUESTION);
			}
		}
		
		return layout_node_shapes;
	}

	private static Map<String, Double> getConvertedThickness(
			Map<String, Double> thickness, ILayout lc) {

		Map<String, Double> layoutThickness = new HashMap<String,Double>();
		
		for(String s : lc.getReactions().keySet()){
			
			Set<String> metIds = lc.getReactions().get(s).getIDs();
			
			Double thick = null;
			for(String metId : metIds){
				if(thickness.containsKey(metId)){
					if(thick == null) thick = 0.0;
					thick += thickness.get(metId);
				}
			}
			
			if(thick != null)
				layoutThickness.put(s, thick);
		}
		
		//layoutThickness = LayoutUtils.normalizeFluxes(layoutThickness, 3, 13);
		
		return layoutThickness;
	}


	private static Map<String, String> getConvertedLabels(ILayout lc, Map<String, Double> map) {
		
		Map<String, String> orverlapLabels = new HashMap<String,String>();
		
		
		for(String r_node_Id : lc.getReactions().keySet()){
			
			boolean hasFlux = false;
			Double flux = 0.0;
			
			Set<String> met_Ids = lc.getReactions().get(r_node_Id).getIDs();
			for(String met_Id : met_Ids){
				if(map.containsKey(met_Id)){
					
					flux = flux + map.get(met_Id);
					hasFlux = true;
				}
			}
			
			if(hasFlux){
				
				Double fluxValue = LayoutUtils.roundToDecimals(flux, 2);
				
				String label = lc.getReactions().get(r_node_Id).getLabel() + " : " + Math.abs(fluxValue);
				orverlapLabels.put(r_node_Id, label);
				
			}
//			else{
//				orverlapLabels.put(r_node_Id, lc.getReactions().get(r_node_Id).getLabel() + " : NiFD");
//			}
		}
		
		return orverlapLabels;
	}

	
	private static Map<String, String> getConvertedLabels2(ILayout lc,
			Map<String, String> oldLabels) {

		Map<String, String> newLabels = new HashMap<String,String>();
		
		for(String rId : lc.getReactions().keySet()){
			
			Set<String> metIds = lc.getReactions().get(rId).getIDs();
			
			String newLabel = "";
			for(String met_id : metIds){
				
				if(oldLabels.containsKey(met_id))
					newLabel += met_id + " " + oldLabels.get(met_id);
			}
			
			if(!newLabel.equals("")) newLabels.put(rId, newLabel);
		}
		
		return newLabels;
	}
	
	public static Set<String> convertVisibilityReactions(
			Set<String> invisibleReactions, ILayout lc) {

		Set<String> newResult = new HashSet<String>();
		
		for(String rNodeId : lc.getReactions().keySet()){
			
			Set<String> metIds = lc.getReactions().get(rNodeId).getIDs();
			
			boolean allInvisible = false;
			if(metIds.size()>0) allInvisible = true;
			
			for(String metId : metIds){
				if(!invisibleReactions.contains(metId)) allInvisible = false;
			}
			
			if(allInvisible) newResult.add(rNodeId);
		}
		
		return newResult;
	}

}
