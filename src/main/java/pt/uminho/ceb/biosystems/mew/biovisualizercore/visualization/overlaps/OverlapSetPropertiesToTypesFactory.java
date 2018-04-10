package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.Collection;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class OverlapSetPropertiesToTypesFactory {

	public static final String SHAPE = "SHAPE";
	public static final String SIZE = "SIZE";
	public static final String COLOR = "COLOR";
	public static final String THICKNESS = "THICKNESS";


	public static IOverlapObject buildOverlap(String name, Collection<String> metabolites, Collection<String> reactions, Map<String, Object> infom, Map<String, Object> infoR){
		
		AbstractOverlap overlapObject = new AbstractOverlap(name);
		addReactionInfo(overlapObject, reactions, infoR);
		addMetabolitesInfo(overlapObject, metabolites, infom);
		return overlapObject;
	}
	
	private static void addMetabolitesInfo(AbstractOverlap overlapObject, Collection<String> metabolites,
			Map<String, Object> infom) {
		
		if(metabolites == null || infom == null) return;
		Integer color = (Integer)infom.get(COLOR);
		Double size    = (Double)infom.get(SIZE);
		Integer shape = (Integer)infom.get(SHAPE);
		for(String id : metabolites){
			if(color != null) overlapObject.addNodeColor(id, color);
			if(size != null)  overlapObject.addNodeSize(id, new Pair<Double, Double> (size, size));
			if(shape != null) overlapObject.addNodeShape(id, shape);
		}
		
	}

	private static void addReactionInfo(AbstractOverlap overlapObject, Collection<String> reactions, Map<String, Object> infoR){
		
		if(reactions == null || infoR == null) return;
				
		Integer rcolor = (Integer)infoR.get(COLOR);
		Double rsize    = (Double)infoR.get(SIZE);
		Integer shape = (Integer)infoR.get(SHAPE);
		Double thickness = (Double)infoR.get(THICKNESS);
		
		for(String id : reactions){
			if(rcolor != null) overlapObject.addReactionColor(id, new Pair<Integer, Integer>(rcolor, rcolor));
			if(rsize != null)  overlapObject.addReactionSize(id, new Pair<Double, Double>(rsize, rsize));
			if(shape != null) overlapObject.addReactionShape(id, shape);
			if(thickness != null) overlapObject.addReactionThickness(id, thickness);
		}
	}
}
