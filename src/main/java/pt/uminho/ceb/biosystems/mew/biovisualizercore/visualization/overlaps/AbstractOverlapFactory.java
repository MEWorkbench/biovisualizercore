package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.Constants;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public abstract class AbstractOverlapFactory {
	
	private Set<String>	info;
	
	abstract protected Set<String> convertToNodeLayoutId(ILayout lc, String source);
	
	abstract protected Set<String> convertToReactionLayoutId(ILayout lc, String source);
	
	abstract protected Set<String> convertToExtraInfoLayoutId(ILayout lc, String source);
	
	/**
	 * This method converts a {@link VisualizationProperties} labels to match
	 * with the viusalization
	 * identifiers. All the other properties are set by type, so no convertion
	 * is needed.
	 * 
	 * @param vp - {@link VisualizationProperties}
	 * @param lc - {@link ILayout}
	 * @return Converted {@link VisualizationProperties}
	 */
	public VisualizationProperties converProperties(VisualizationProperties vp, ILayout lc) {
		
		Map<String, Set<String>> infoMappings = new HashMap<String, Set<String>>();
		Map<String, Set<String>> nodeMappings = new HashMap<String, Set<String>>();
		Map<String, Set<String>> reacMappings = new HashMap<String, Set<String>>();
		
		buildMappings(infoMappings, reacMappings, nodeMappings, lc);
		
		vp.setReactionLabels(createLabelsMap(reacMappings, vp.getReactionLabels(), lc));
		vp.setMetaboliteLabels(createLabelsMap(nodeMappings, vp.getMetaboliteLabels(), lc));
		vp.setCurrencyLabels(createLabelsMap(nodeMappings, vp.getCurrencyLabels(), lc));
		vp.setInformationLabels(createLabelsMap(infoMappings, vp.getInformationLabels(), lc));
		
		for (String id : reacMappings.keySet()) {
			String original = lc.getReactions().get(id).getLabel();
			String label = vp.getReactionLabels().get(id);
			if (label != null){
				label = label.replaceAll("\\$\\$\\$\\$\\$\\$", original);
//				lc.getReactions().get(id).setLabel(label);
				vp.getReactionLabels().put(id, label);
			}
		}
		
		for (String id : nodeMappings.keySet()) {
			String label = (vp.getMetaboliteLabels().containsKey(id)) ? vp.getMetaboliteLabels().get(id) : null;
			if (label == null && vp.getCurrencyLabels().containsKey(id)) label = vp.getCurrencyLabels().get(id);
			if (label != null) lc.getNodes().get(id).setLabel(label);
		}
		
		for (String id : infoMappings.keySet()) {
			String label = (vp.getInformationLabels().containsKey(id)) ? vp.getInformationLabels().get(id) : null;
			if (label != null) lc.getNodes().get(id).setLabel(label);
		}
		
		return vp;
	}
	
	private Map<String, String> createLabelsMap(Map<String, Set<String>> mappings, Map<String, String> oldLabels, ILayout lc) {
		
		Map<String, String> newLabels = new HashMap<String, String>();
		
		for (String r : mappings.keySet()) {
			
			String label = "";
			boolean found = false;
			
			Set<String> metIds = mappings.get(r);
			for (String id : metIds) {
				if (oldLabels.containsKey(id)) {
					label += oldLabels.get(id);
					found = true;
				}
			}
			if (found) newLabels.put(r, label);
		}
		
		return newLabels;
	}
	
	/**
	 * Method that converts and {@link IOverlapObject} to map with the entities
	 * of the visualization.
	 * 
	 * @param overlap {@link IOverlapObject}
	 * @param lc {@link ILayout}
	 * @return converted {@link IOverlapObject}
	 */
	public IOverlapObject convertOverlapWithMappings(IOverlapObject overlap, ILayout lc) {
		
		Map<String, Set<String>> infoMappings = new HashMap<String, Set<String>>();
		Map<String, Set<String>> nodeMappings = new HashMap<String, Set<String>>();
		Map<String, Set<String>> reacMappings = new HashMap<String, Set<String>>();
		
		buildMappings(infoMappings, reacMappings, nodeMappings, lc);
		
		AbstractOverlap newOverlap = new AbstractOverlap(overlap.getName());
		
//		//REACTION LABELS!
//		if (overlap.getNewReactionLabels() == null || overlap.getNewReactionLabels().size() == 0) {
//			
//			Map<String, String> newLabels = getNewReactionLabels(lc, overlap.getEdgeThickness(), reacMappings);
//			newOverlap.setReactionLabels(newLabels);
//		} else {
//			
			Map<String, String> newLabels = getNewReactionLabels3(lc, overlap.getNewReactionLabels(), reacMappings);
			newOverlap.setReactionLabels(newLabels);
//		}
		
		Map<String, Double> newThickness = getConvertedThickness(overlap.getEdgeThickness(), lc, reacMappings);
		newOverlap.setEdgeThickness(newThickness);
		
		Map<String, Integer> reacShapes = getConvertedReactionShapes(overlap.getReactionShapes(), reacMappings);
		newOverlap.setReactionShapes(reacShapes);
		
		Map<String, Integer> nodeShapes = getConvertedNodeShapes(overlap.getNodeShapes(), nodeMappings, infoMappings);
		newOverlap.setNodeShapes(nodeShapes);
		
		Map<String, String> nodeLabels = getConvertedNodeLabels(overlap.getNewNodeLabels(), nodeMappings, infoMappings);
		newOverlap.setNodeLabels(nodeLabels);
		
		Map<String, Pair<Integer, Integer>> reactionColors = getConvertedReactionColors(overlap.getReactionsColors(), reacMappings);
		newOverlap.setReactionColors(reactionColors);
		
		Map<String, Integer> nodeColors = getConvertedNodeColors(overlap.getNodeColors(), nodeMappings, infoMappings);
		newOverlap.setNodeColors(nodeColors);
		
		Map<String, Set<String>> newFilters = getConvertedFilters(overlap.getPossibleInvR(), reacMappings);
		newOverlap.setVisFilters(newFilters);
		
		Map<String, Boolean> newDirections = getConvertedDirections(overlap.getFluxDirections(), reacMappings);
		newOverlap.setFluxDirections(newDirections);
		
//		System.err.println("OLD SIZES");
//		System.err.println(MapUtils.prettyToString(overlap.getNodeSizes()));
		
		Map<String, Pair<Double, Double>> nodeSizes = getConvertedNodeSizes(overlap.getNodeSizes(), nodeMappings, infoMappings);
		newOverlap.setNodeSizes(nodeSizes);
		
//		System.err.println("NEW SIZES");
//		System.err.println(MapUtils.prettyToString(nodeSizes));
		
		Map<String, Pair<Double, Double>> reactionSizes = getConvertedReactionSizes(overlap.getReactionSizes(), reacMappings);
		newOverlap.setReactionSizes(reactionSizes);
		
		return newOverlap;
	}
	
	private void buildMappings(Map<String, Set<String>> infoMappings, Map<String, Set<String>> reacMappings, Map<String, Set<String>> nodeMappings, ILayout lc) {
		
		info = getExtraInfoNode(lc.getReactions());
		
		for (String infonode : info) {
			infoMappings.put(infonode, convertToExtraInfoLayoutId(lc, infonode));
		}
		for (String s : lc.getReactions().keySet()) {
			
			Set<String> info = convertToReactionLayoutId(lc, s);
			System.out.println(s + "\t" + info);
			reacMappings.put(s, info);
		}
		for (String n : lc.getNodes().keySet()) {
			if (!info.contains(n)) nodeMappings.put(n, convertToNodeLayoutId(lc, n));
		}
		
	}
	
	private Set<String> getExtraInfoNode(Map<String, IReactionLay> reactions) {
		
		Set<String> extraInforNodes = new HashSet<String>();
		for (IReactionLay reaction : reactions.values()) {
			extraInforNodes.addAll(reaction.infos().keySet());
		}
		return extraInforNodes;
	}
	
	private Map<String, Pair<Double, Double>> getConvertedReactionSizes(Map<String, Pair<Double, Double>> reactionSizes, Map<String, Set<String>> reacMappings) {
		
		Map<String, Pair<Double, Double>> result_sizes = new HashMap<String, Pair<Double, Double>>();
		
		for (String rId : reacMappings.keySet()) {
			
			for (String id : reacMappings.get(rId)) {
				if (reactionSizes.containsKey(id)) result_sizes.put(rId, reactionSizes.get(id));
			}
		}
		
		return result_sizes;
	}
	
	private Map<String, Pair<Double, Double>> getConvertedNodeSizes(Map<String, Pair<Double, Double>> nodeSizes, Map<String, Set<String>> nodeMappings, Map<String, Set<String>> infoMappings) {
		
		Map<String, Pair<Double, Double>> result_sizes = new HashMap<String, Pair<Double, Double>>();
		
		for (String node_Id : nodeMappings.keySet()) {
			System.err.println("NODE ["+node_Id+"]");
			Set<String> mappings = nodeMappings.get(node_Id);
			for (String id : mappings) {
				System.err.print("\t ["+id+"]");
				if (nodeSizes.containsKey(id)) {
					System.err.println("="+nodeSizes.get(id));
					result_sizes.put(node_Id, nodeSizes.get(id));
				}else
					System.err.println();
			}
		}
		
		for (String info_id : infoMappings.keySet()) {
			System.err.println("INFO["+info_id+"]");
			Set<String> mappings = nodeMappings.get(info_id);
			if (mappings != null) 
				for (String id : mappings) {
					System.err.print("\t ["+id+"]");
					if (nodeSizes.containsKey(id)) {
						System.err.println("="+nodeSizes.get(id));
						result_sizes.put(info_id, nodeSizes.get(id));
					}else
						System.err.println();	
				}
		}
		
		return result_sizes;
	}
	
	public Map<String, Pair<Integer, Integer>> getConvertedReactionColors(Map<String, Pair<Integer, Integer>> colors, Map<String, Set<String>> reacMappings) {
		
		Map<String, Pair<Integer, Integer>> converted_reaction_colors = new HashMap<String, Pair<Integer, Integer>>();
		
		for (String rId : reacMappings.keySet()) {
			
			Set<String> mappings = reacMappings.get(rId);
			Pair<Integer, Integer> color = null;
			
			for (String id : mappings) {
				
				if (colors.containsKey(id)) {
					if (color == null) color = new Pair<Integer, Integer>(0, 0);
					
					color.setA(color.getA() + colors.get(id).getA());
					color.setB(color.getB() + colors.get(id).getB());
				}
			}
			
			if (color != null) converted_reaction_colors.put(rId, color);
		}
		
		return converted_reaction_colors;
	}
	
	private Map<String, Integer> getConvertedNodeColors(Map<String, Integer> colors, Map<String, Set<String>> nodeMappings, Map<String, Set<String>> infoMappings) {
		
		Map<String, Integer> converted_node_colors = new HashMap<String, Integer>();
		
		for (String rId : nodeMappings.keySet()) {
			
			Set<String> mappings = nodeMappings.get(rId);
			Integer color = null;
			
			for (String id : mappings) {
				
				if (colors.containsKey(id)) {
					if (color == null) color = 0;
					color += colors.get(id);
				}
			}
			
			if (color != null) converted_node_colors.put(rId, color);
		}
		
		for (String rId : infoMappings.keySet()) {
			
			Set<String> mappings = infoMappings.get(rId);
			Integer color = null;
			
			if (mappings != null) for (String id : mappings) {
				
				if (colors.containsKey(id)) {
					if (color == null) color = 0;
					color += colors.get(id);
				}
			}
			
			if (color != null) converted_node_colors.put(rId, color);
		}
		
		return converted_node_colors;
	}
	
	private Map<String, Boolean> getConvertedDirections(Map<String, Boolean> fluxDirections, Map<String, Set<String>> reacMappings) {
		
		Map<String, Boolean> layout_directions = new HashMap<String, Boolean>();
		
		for (String rId : reacMappings.keySet()) {
			
			Set<String> mappings = reacMappings.get(rId);
			
			boolean hasDirection = false;
			boolean allNegative = true;
			
			for (String s : mappings) {
				
				if (fluxDirections.containsKey(s)) {
					hasDirection = true;
					if (fluxDirections.get(s)) allNegative = false;
				}
			}
			if (hasDirection) {
				if (allNegative)
					layout_directions.put(rId, false);
				else
					layout_directions.put(rId, true);
			}
		}
		return layout_directions;
	}
	
	private Map<String, Set<String>> getConvertedFilters(Map<String, Set<String>> filters, Map<String, Set<String>> reacMappings) {
		
		Map<String, Set<String>> new_layoutFilters = new HashMap<String, Set<String>>();
		
		for (String filter : filters.keySet()) {
			
			Set<String> newFilter = new HashSet<String>();
			Set<String> invSources = filters.get(filter);
			
			for (String rId : reacMappings.keySet()) {
				
				Set<String> mappings = reacMappings.get(rId);
				if (mappings.size() > 0 && invSources.containsAll(mappings)) newFilter.add(rId);
			}
			
			new_layoutFilters.put(filter, newFilter);
		}
		
		return new_layoutFilters;
	}
	
	private Map<String, Integer> getConvertedNodeShapes(Map<String, Integer> nodeShapes, Map<String, Set<String>> nodeMappings, Map<String, Set<String>> infoMappings) {
		
		Map<String, Integer> layout_node_shapes = new HashMap<String, Integer>();
		
		for (String nodeId : nodeMappings.keySet()) {
			
			Set<String> metIds = nodeMappings.get(nodeId);
			Integer shape = null;
			for (String s : metIds) {
				if (nodeShapes.containsKey(s)) {
					if (shape == null)
						shape = nodeShapes.get(s);
					else
						shape = Constants.SHAPE_QUESTION;
				}
			}
			if (shape != null) layout_node_shapes.put(nodeId, shape);
		}
		
		for (String nodeId : infoMappings.keySet()) {
			
			Set<String> metIds = nodeMappings.get(nodeId);
			Integer shape = null;
			if (metIds != null) for (String s : metIds) {
				if (nodeShapes.containsKey(s)) {
					if (shape == null)
						shape = nodeShapes.get(s);
					else
						shape = Constants.SHAPE_QUESTION;
				}
			}
			if (shape != null) layout_node_shapes.put(nodeId, shape);
		}
		
		return layout_node_shapes;
	}
	
	private Map<String, String> getConvertedNodeLabels(Map<String, String> nodeLabels, Map<String, Set<String>> nodeMappings, Map<String, Set<String>> infoMappings) {
		
		Map<String, String> layout_node_labels = new HashMap<String, String>();
		
		for (String nodeId : nodeMappings.keySet()) {
			
			Set<String> metIds = nodeMappings.get(nodeId);
			String label = null;
			for (String s : metIds) {
				if (nodeLabels.containsKey(s)) {
					if (label == null)
						label = nodeLabels.get(s);
					else
						label = s;
				}
			}
			if (label != null) layout_node_labels.put(nodeId, label);
		}
		
		for (String nodeId : infoMappings.keySet()) {
			
			Set<String> metIds = nodeMappings.get(nodeId);
			String label = null;
			if (metIds != null) for (String s : metIds) {
				if (nodeLabels.containsKey(s)) {
					if (label == null)
						label = nodeLabels.get(s);
					else
						label = s;
				}
			}
			if (label != null) layout_node_labels.put(nodeId, label);
		}
		
		return layout_node_labels;
	}
	
	private Map<String, Integer> getConvertedReactionShapes(Map<String, Integer> rShapes, Map<String, Set<String>> reacMappings) {
		
		Map<String, Integer> layout_reaction_shapes = new HashMap<String, Integer>();
		
		for (String rId : reacMappings.keySet()) {
			
			Set<String> metIds = reacMappings.get(rId);
			Integer shape = null;
			for (String s : metIds) {
				if (rShapes.containsKey(s)) {
					if (shape == null)
						shape = rShapes.get(s);
					else
						shape = Constants.SHAPE_QUESTION;
				}
			}
			if (shape != null) layout_reaction_shapes.put(rId, shape);
		}
		
		return layout_reaction_shapes;
	}
	
	private Map<String, Double> getConvertedThickness(Map<String, Double> thickness, ILayout lc, Map<String, Set<String>> reacMappings) {
		
		Map<String, Double> resultThickness = new HashMap<String, Double>();
		
		for (String rId : lc.getReactions().keySet()) {
			
			Set<String> met_ids = reacMappings.get(rId);
			Double thick = null;
			for (String metId : met_ids) {
				
				if (thickness.containsKey(metId)) {
					if (thick == null) thick = 0.0;
					thick += thickness.get(metId);
				}
			}
			if (thick != null) resultThickness.put(rId, thick);
		}
		return resultThickness;
	}
	
	private Map<String, String> getNewReactionLabels(ILayout lc, Map<String, Double> edgeThickness, Map<String, Set<String>> reacMappings) {
		
		Map<String, String> fluxLabels = new HashMap<String, String>();
		
		for (String reac_Id : reacMappings.keySet()) {
			
			boolean hasFlux = false;
			Double fluxLabel = 0.0;
			Set<String> map = reacMappings.get(reac_Id);
			for (String metId : map) {
				if (edgeThickness.containsKey(metId)) {
					hasFlux = true;
					fluxLabel += edgeThickness.get(metId);
				}
			}
			
			if (hasFlux) {
//				String label = lc.getReactions().get(reac_Id).getLabel() + " : " + fluxLabel;
				String label = lc.getReactions().get(reac_Id).getLabel(); //NOTE: changed by pmaia
				fluxLabels.put(reac_Id, label);
			}
		}
		return fluxLabels;
	}
	
	private Map<String, String> getNewReactionLabels2(Map<String, String> oldLabels, Map<String, Set<String>> reacMappings) {
		
		Map<String, String> convertedLabels = new HashMap<String, String>();
		
		for (String rId : reacMappings.keySet()) {
			String newLabel = "";
			Set<String> metIds = reacMappings.get(rId);
			for (String m : metIds) {
				if (oldLabels.containsKey(m)) {
					newLabel += m + " " + oldLabels.get(m);
				}
			}
			
			if (!newLabel.equals("")) {
				convertedLabels.put(rId, newLabel);
			}
		}
		
		return convertedLabels;
	}
	
	private Map<String, String> getNewReactionLabels3(ILayout lc, Map<String, String> extendeLabel, Map<String, Set<String>> reacMappings) {
		
		Map<String, String> fluxLabels = new HashMap<String, String>();
		
		for (String reac_Id : reacMappings.keySet()) {
			
			boolean hasFlux = false;
			String fluxLabel = "";
			
			Set<String> map = reacMappings.get(reac_Id);
			for (String rId : map) {
				if (extendeLabel.containsKey(rId)) {
					if (hasFlux)
						fluxLabel += ", " + extendeLabel.get(rId);
					else
						fluxLabel = extendeLabel.get(rId);
					hasFlux = true;
					
				}
			}
			
			if (hasFlux) {
				String label = lc.getReactions().get(reac_Id).getLabel() + "\n" + fluxLabel;
				fluxLabels.put(reac_Id, label);
			}
		}
		return fluxLabels;
	}
	
	/**
	 * Converts the identifiers of the invisible reactions.
	 * 
	 * @param invisibleReactions
	 * @param lc
	 * @return converted identifiers.
	 */
	public Set<String> convertVisibilityReactions(Set<String> invisibleReactions, ILayout lc) {
		
		Map<String, Set<String>> reacMappings = new HashMap<String, Set<String>>();
		
		for (String s : lc.getReactions().keySet()) {
			reacMappings.put(s, convertToReactionLayoutId(lc, s));
		}
		
		Set<String> newResult = new HashSet<String>();
		
		for (String rId : reacMappings.keySet()) {
			
			if (invisibleReactions.size() > 0 && invisibleReactions.containsAll(reacMappings.get(rId))) newResult.add(rId);
		}
		
		return newResult;
	}
}
