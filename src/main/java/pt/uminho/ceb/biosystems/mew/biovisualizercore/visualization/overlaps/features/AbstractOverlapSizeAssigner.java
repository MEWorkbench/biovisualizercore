package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.OverlapUtilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.math.MathUtils;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.INormalizerMap;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.NormalizeMapBetweenRange;

public abstract class AbstractOverlapSizeAssigner<F extends Pair<Double, Double>> implements IOverlapFeatureAssigner {
	
	protected Double min, max;
	protected INormalizerMap normalizer;
	
	public AbstractOverlapSizeAssigner() {
	}
	
	public AbstractOverlapSizeAssigner(Double min, Double max) {
		this.min = min;
		this.max = max;
	}
	
	public AbstractOverlapSizeAssigner(INormalizerMap normalizer) {
		this.normalizer = normalizer;
	}

	@Override
	public void assignFeature(IOverlapObject overlap, Map<String, Double> infoToAssign) {
		Map<String, Pair<Double, Double>> sizes = new HashMap<String, Pair<Double, Double>>();
		Pair<Double, Double> minmax = MathUtils.minMaxT(infoToAssign.values());
		Map<String, Double> normalizedMap = normalize(infoToAssign, minmax);
		
		for (String key : normalizedMap.keySet()) {
			Double value = normalizedMap.get(key);
			value  = OverlapUtilities.formatDigits(value);
			sizes.put(key, new Pair<Double, Double>(value, value));
		}
		
		assignGenericFeature(overlap, sizes);
	}
	
	protected Map<String, Double> normalize(Map<String, Double> infoToAssign, Pair<Double, Double> minmax){
		if(normalizer == null){
			normalizer = new NormalizeMapBetweenRange(min, max, minmax.getA(), minmax.getB());
		}
		
		return normalizer.normalize(infoToAssign);
	}
	
}
