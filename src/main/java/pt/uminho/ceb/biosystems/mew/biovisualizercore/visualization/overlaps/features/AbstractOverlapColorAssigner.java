package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import prefuse.util.ColorLib;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.OverlapUtilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.math.MathUtils;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.INormalizerMap;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.NormalizeMapPercent;

public abstract class AbstractOverlapColorAssigner<F> implements IOverlapFeatureAssigner {
	
	protected Color minColor, maxColor;
	protected INormalizerMap normalizer;
	
	public AbstractOverlapColorAssigner() {
	}
	
	public AbstractOverlapColorAssigner(Color minColor, Color maxColor) {
		this.minColor = minColor;
		this.maxColor = maxColor;
	}
	
	public AbstractOverlapColorAssigner(Color minColor, Color maxColor, INormalizerMap normalizer) {
		this(minColor, maxColor);
		this.normalizer = normalizer;
	}

	@Override
	public void assignFeature(IOverlapObject overlap, Map<String, Double> infoToAssign) {
		Map<String, Pair<Integer, Integer>> colors = new HashMap<String, Pair<Integer, Integer>>();
		Double max = MathUtils.minMaxT(infoToAssign.values()).getB();
		Map<String, Double> normalizedValues = normalize(infoToAssign, max);
		
		for (String key : normalizedValues.keySet()) {
			Color ncolor = OverlapUtilities.mixColors(maxColor, minColor, normalizedValues.get(key));
			Integer icolor = ColorLib.rgba(ncolor.getRed(), ncolor.getGreen(), ncolor.getBlue(), ncolor.getAlpha());
			colors.put(key, new Pair<Integer, Integer>(icolor, icolor));
		}
		
		assignGenericFeature(overlap, getColorMap(colors));
	}
	
	protected Map<String, Double> normalize(Map<String, Double> infoToAssign, Double max){
		if(normalizer == null){
			normalizer = new NormalizeMapPercent(max);
		}
		
		return normalizer.normalize(infoToAssign);
	}

	public abstract <F> Map<String, F> getColorMap(Map<String, Pair<Integer, Integer>> colors);

}
