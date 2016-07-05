package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public abstract class AbstractOverlapLabelAssigner<F extends String> implements IOverlapFeatureAssigner {

	@Override
	public void assignFeature(IOverlapObject overlap, Map<String, Double> infoToAssign) {
		Map<String, String> labels = new HashMap<String, String>();
		for (String edge : infoToAssign.keySet()) {
			double value = infoToAssign.get(edge);
			labels.put(edge, Double.toString(value));
		}
		assignGenericFeature(overlap, labels);
	}

}
