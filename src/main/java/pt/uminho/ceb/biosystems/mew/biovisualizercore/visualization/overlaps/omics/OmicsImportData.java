package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.omics;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.ConversionFunction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.EdgesFeatures;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.NodesFeatures;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.OverlapsWizardTypeOfData;

public class OmicsImportData implements Serializable {
	
	private static final long							serialVersionUID	= 1L;
	protected OverlapsWizardTypeOfData					_typeOfDataEdges	= null;
	protected OverlapsWizardTypeOfData					_typeOfDataNodes	= null;
	protected Map<OverlapsWizardTypeOfData, String>		_files				= null;
	
	protected Map<String, List<String>>					_edgesData			= null;
	protected Map<String, List<String>>					_nodesData			= null;
	
	protected Map<String, Map<String, Double>>			_edgesMultiValues	= null;
	protected Map<String, Map<String, Double>>			_nodesMultiValues	= null;
	
	Map<String, Map<String, Double>>					_genesOrigValues	= null;
	
	protected Map<EdgesFeatures, Map<String, Object>>	_edgesFeatures		= null;
	protected Map<NodesFeatures, Map<String, Object>>	_nodesFeatures		= null;
	
	protected ConversionFunction						_conversionAnd		= null;
	protected ConversionFunction						_conversionOr		= null;
	
	public OmicsImportData() {
		
	}
	
	public OverlapsWizardTypeOfData get_typeOfDataEdges() {
		return _typeOfDataEdges;
	}
	
	public OverlapsWizardTypeOfData get_typeOfDataNodes() {
		return _typeOfDataNodes;
	}
	
	public void set_typeOfDataEdges(OverlapsWizardTypeOfData _typeOfDataEdges) {
		this._typeOfDataEdges = _typeOfDataEdges;
	}
	
	public void set_typeOfDataNodes(OverlapsWizardTypeOfData _typeOfDataNodes) {
		this._typeOfDataNodes = _typeOfDataNodes;
	}
	
	public Map<OverlapsWizardTypeOfData, String> get_files() {
		return _files;
	}
	
	public void set_files(Map<OverlapsWizardTypeOfData, String> _files) {
		this._files = _files;
	}
	
	public Map<String, List<String>> get_edgesData() {
		return _edgesData;
	}
	
	public Map<String, List<String>> get_nodesData() {
		return _nodesData;
	}
	
	public void set_edgesData(Map<String, List<String>> edgesData) {
		this._edgesData = edgesData;
	}
	
	public void set_nodesData(Map<String, List<String>> nodesData) {
		this._nodesData = nodesData;
	}
	
	public Map<EdgesFeatures, Map<String, Object>> get_edgesFeatures() {
		return _edgesFeatures;
	}
	
	public void set_edgesFeatures(Map<EdgesFeatures, Map<String, Object>> _edgesFeatures) {
		this._edgesFeatures = _edgesFeatures;
	}
	
	public Map<NodesFeatures, Map<String, Object>> get_nodesFeatures() {
		return _nodesFeatures;
	}
	
	public void set_nodesFeatures(Map<NodesFeatures, Map<String, Object>> _nodesFeatures) {
		this._nodesFeatures = _nodesFeatures;
	}
	
	public Map<String, Map<String, Double>> get_edgesMultiValues() {
		return _edgesMultiValues;
	}
	
	public Map<String, Map<String, Double>> get_nodesMultiValues() {
		return _nodesMultiValues;
	}
	
	public void set_edgesMultiValues(Map<String, Map<String, Double>> _edgesMultiValues) {
		this._edgesMultiValues = _edgesMultiValues;
	}
	
	public void set_nodesMultiValues(Map<String, Map<String, Double>> _nodesMultiValues) {
		this._nodesMultiValues = _nodesMultiValues;
	}
	
	public ConversionFunction get_conversionAnd() {
		return _conversionAnd;
	}
	
	public ConversionFunction get_conversionOr() {
		return _conversionOr;
	}
	
	public void set_conversionAnd(ConversionFunction _conversionAnd) {
		this._conversionAnd = _conversionAnd;
	}
	
	public void set_conversionOr(ConversionFunction _conversionOr) {
		this._conversionOr = _conversionOr;
	}

	public Map<String, Map<String, Double>> get_genesOrigValues() {
		return _genesOrigValues;
	}

	public void set_genesOrigValues(Map<String, Map<String, Double>> _genesOrigValues) {
		this._genesOrigValues = _genesOrigValues;
	}
	
}
