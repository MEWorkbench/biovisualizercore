package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class OverlapReactionLabelAssigner extends AbstractOverlapLabelAssigner<String> {
	
	public static final String ID = "REACTION_LABEL_ASSIGNER";
	
	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setReactionLabels((Map<String, String>) infoToAssign);
	}

}
