package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.awt.Color;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.INormalizerMap;

public class OverlapReactionColorAssigner extends AbstractOverlapColorAssigner<Pair<Integer, Integer>> {
	
	public static final String ID = "REACTION_COLOR_ASSIGNER";
	
	public OverlapReactionColorAssigner() {
	}
	
	public OverlapReactionColorAssigner(Color minColor, Color maxColor, INormalizerMap normalizer) {
		super(minColor, maxColor, normalizer);
	}
	
	public OverlapReactionColorAssigner(Color minColor, Color maxColor) {
		super(minColor, maxColor);
	}

	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setReactionColors((Map<String, Pair<Integer, Integer>>) infoToAssign);
	}

	@Override
	public Map getColorMap(Map colors) {
		return colors;
	}

}
