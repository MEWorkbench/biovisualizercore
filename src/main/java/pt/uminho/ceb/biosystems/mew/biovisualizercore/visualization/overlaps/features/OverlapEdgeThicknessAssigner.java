package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.math.MathUtils;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.INormalizerMap;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.NormalizeMapBetweenRange;

public class OverlapEdgeThicknessAssigner implements IOverlapFeatureAssigner {
	
	public static final String ID = "EDGE_THICKNESS_ASSIGNER";
	
	protected Integer minThickness, maxThickness;
	protected INormalizerMap normalizer;
	
	public OverlapEdgeThicknessAssigner(Integer minThickness, Integer maxThickness) {
		this.minThickness = minThickness;
		this.maxThickness = maxThickness;
	}
	
	public OverlapEdgeThicknessAssigner(INormalizerMap normalizer) {
		this.normalizer = normalizer;
	}

	@Override
	public void assignFeature(IOverlapObject overlap, Map<String, Double> infoToAssign) {
		Pair<Double, Double> minmax = MathUtils.minMaxT(infoToAssign.values());
		Map<String, Double> thicknesses = normalize(infoToAssign, minmax);
		
		assignGenericFeature(overlap, thicknesses);
	}
	
	protected Map<String, Double> normalize(Map<String, Double> infoToAssign, Pair<Double, Double> minmax){
		if(normalizer == null){
			normalizer = new NormalizeMapBetweenRange(minThickness.doubleValue(), maxThickness.doubleValue(), minmax.getA(), minmax.getB());
		}
		
		return normalizer.normalize(infoToAssign);
	}

	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setEdgeThickness((Map<String, Double>) infoToAssign);
	}

}
