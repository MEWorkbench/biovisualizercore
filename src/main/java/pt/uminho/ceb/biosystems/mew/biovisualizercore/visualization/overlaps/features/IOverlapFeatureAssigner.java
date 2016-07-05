package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public interface IOverlapFeatureAssigner {
	
	/**
	 * Assign a feature based on the raw data to an overlap.<br>
	 * Most of the available features will need pre-processing or normalization before applied
	 * @param overlap The overlap in which the feature will be added
	 * @param infoToAssign The raw data
	 */
	public void assignFeature(IOverlapObject overlap, Map<String, Double> infoToAssign);
	
	/**
	 * Assign a feature based directly in the overlap.<br>
	 * The data must be already processed or normalized to be applied to the feature
	 * @param overlap The overlap in which the feature will be added
	 * @param infoToAssign The data that will be present in the feature
	 */
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign);

}
