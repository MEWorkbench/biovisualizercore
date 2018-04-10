package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class OverlapMetabolicNodeShapeAssigner extends AbstractOverlapMetabolicShapeAssigner {

	public static final String ID = "NODE_METABOLIC_SHAPE_ASSIGNER";
	
	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setNodeShapes((Map<String, Integer>) infoToAssign);
	}
	
}
