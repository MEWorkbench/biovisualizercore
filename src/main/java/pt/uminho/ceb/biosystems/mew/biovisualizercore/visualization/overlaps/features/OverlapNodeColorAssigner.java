package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class OverlapNodeColorAssigner extends AbstractOverlapColorAssigner<Integer> {

	public static final String ID = "NODE_COLOR_ASSIGNER";
	
	public OverlapNodeColorAssigner() {
	}
	
	public OverlapNodeColorAssigner(Color minColor, Color maxColor) {
		super(minColor, maxColor);
	}

	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setNodeColors((Map<String, Integer>) infoToAssign);
	}

	//TODO: this is sooooo strange
	@Override
	public Map getColorMap(Map colors) {
		Map<String, Integer> toRet = new HashMap<>();
		Map<String, Pair<Integer, Integer>> thisColors = new HashMap<>(colors);

		for (String key : thisColors.keySet()) {
			toRet.put(key, thisColors.get(key).getA());
		}
		
		return toRet;
	}

}
