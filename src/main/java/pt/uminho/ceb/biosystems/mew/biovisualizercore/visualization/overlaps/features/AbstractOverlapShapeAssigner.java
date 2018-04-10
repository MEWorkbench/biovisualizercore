package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.INormalizerMap;

public abstract class AbstractOverlapShapeAssigner<F extends Integer> implements IOverlapFeatureAssigner {

	protected INormalizerMap normalizer;
	
	public AbstractOverlapShapeAssigner() {
	}
	
	public AbstractOverlapShapeAssigner(INormalizerMap normalizer) {
		this.normalizer = normalizer;
	}
	
	@Override
	public void assignFeature(IOverlapObject overlap, Map<String, Double> infoToAssign) {
		Map<String, Integer> shapes = new HashMap<String, Integer>();
		Map<String, Double> normalizedMap = normalize(infoToAssign);
		
		for (String key : normalizedMap.keySet()) {
			Pair<String, Double> pair = new Pair(key, infoToAssign.get(key));
			shapes.put(key, getShape(pair.getB()));
		}
		assignGenericFeature(overlap, shapes);
	}
	
	protected Map<String, Double> normalize(Map<String, Double> infoToAssign){
		if(normalizer == null){
			return infoToAssign;
		}
		
		return normalizer.normalize(infoToAssign);
	}
	
	protected abstract Integer getShape(Double value);
	
}
