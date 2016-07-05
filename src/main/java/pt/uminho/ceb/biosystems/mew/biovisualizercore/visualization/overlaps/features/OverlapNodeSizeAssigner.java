package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class OverlapNodeSizeAssigner extends AbstractOverlapSizeAssigner<Pair<Double, Double>> {
	
	public static final String ID = "NODE_SIZE_ASSIGNER";
	
	public OverlapNodeSizeAssigner(Double min, Double max) {
		super(min, max);
	}

	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setNodeSizes((Map<String, Pair<Double, Double>>) infoToAssign);
	}

}
