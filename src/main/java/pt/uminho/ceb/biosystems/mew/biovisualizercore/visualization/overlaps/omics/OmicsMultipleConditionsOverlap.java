package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.omics;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction.GenericRuleConversion;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.MultipleConditionsOverlap;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

public class OmicsMultipleConditionsOverlap extends MultipleConditionsOverlap {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected IndexedHashMap<String, Map<String, Double>> _genesInformation;
	protected IndexedHashMap<String, GenericRuleConversion> _geneRuleConversions;
	
	public OmicsMultipleConditionsOverlap(String id, IndexedHashMap<String, IOverlapObject> conditions, IndexedHashMap<String, Map<String, Double>> genesInformation, IndexedHashMap<String, GenericRuleConversion> geneRuleConversions) {
		super(id, conditions);
		_genesInformation = genesInformation;
		_geneRuleConversions = geneRuleConversions;
	}
	
	public OmicsMultipleConditionsOverlap(String id, IndexedHashMap<String, IOverlapObject> conditions, IndexedHashMap<String, Map<String, Double>> genesInformation, GenericRuleConversion globalGeneRuleConversion) {
		super(id, conditions);
		_genesInformation = genesInformation;
		_geneRuleConversions = new IndexedHashMap<String, GenericRuleConversion>();
		for (String conditionID : conditions.keySet()) {
			_geneRuleConversions.put(conditionID, globalGeneRuleConversion);
		}
	}

	public IndexedHashMap<String, Map<String, Double>> getGenesInformation() {
		return _genesInformation;
	}
	
	public void setGenesInformation(IndexedHashMap<String, Map<String, Double>> genesInformation) {
		_genesInformation = genesInformation;
	}
	
	public IndexedHashMap<String, GenericRuleConversion> getGeneRuleConversions() {
		return _geneRuleConversions;
	}

	public void setGeneRuleConversions(IndexedHashMap<String, GenericRuleConversion> geneRuleConversions) {
		_geneRuleConversions = geneRuleConversions;
	}

	
}
