package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class OverlapMetabolicReactionShapeAssigner extends AbstractOverlapMetabolicShapeAssigner {

	public static final String ID = "REACTION_METABOLIC_SHAPE_ASSIGNER";
	
	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setReactionShapes((Map<String, Integer>) infoToAssign);
	}
}
