package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class MultipleConditionsOverlap implements IOverlapObject {
	
	private static final long							serialVersionUID	= 1L;
	
	protected String									_id					= null;
	protected IndexedHashMap<String, IOverlapObject>	_conditions			= null;
	protected String									_selectedCondition	= null;
	
	public MultipleConditionsOverlap() {
	}
	
	public MultipleConditionsOverlap(String id, IndexedHashMap<String, IOverlapObject> conditions) {
		_id = id;
		_conditions = conditions;
		if (_conditions != null && !conditions.isEmpty()) {
			_selectedCondition = _conditions.getKeyAt(0);
		}
	}
	
	public IndexedHashMap<String, IOverlapObject> get_conditions() {
		if (_conditions == null) _conditions = new IndexedHashMap<String, IOverlapObject>();
		return _conditions;
	}
	
	public String get_id() {
		return _id;
	}
	
	public String get_selectedCondition() {
		return _selectedCondition;
	}
	
	public void set_conditions(IndexedHashMap<String, IOverlapObject> conditions) {
		this._conditions = conditions;
	}
	
	public void set_id(String id) {
		this._id = id;
	}
	
	public void set_selectedCondition(String selectedCondition) {
		this._selectedCondition = selectedCondition;
	}
	
	/**************************
	 * IOverlapObject methods *
	 **************************/
	
	@Override
	public String getName() {
		//		if(_conditions!=null && !_conditions.isEmpty()){
		//			return _conditions.get(_selectedCondition).getName();
		//		}
		//			
		//		return null;
		return get_id();
	}
	
	@Override
	public void setName(String name) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setName(name);
		}
	}
	
	@Override
	public Map<String, String> getNewReactionLabels() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getNewReactionLabels();
		}
		return null;
	}
	
	@Override
	public Map<String, String> getNewNodeLabels() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getNewNodeLabels();
		}
		return null;
	}
	
	@Override
	public Map<String, Double> getEdgeThickness() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getEdgeThickness();
		}
		return null;
	}
	
	@Override
	public Map<String, Integer> getNodeShapes() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getNodeShapes();
		}
		return null;
	}
	
	@Override
	public Map<String, Integer> getReactionShapes() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getReactionShapes();
		}
		return null;
	}
	
	@Override
	public Map<String, Pair<Double, Double>> getNodeSizes() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getNodeSizes();
		}
		return null;
	}
	
	@Override
	public Map<String, Pair<Double, Double>> getReactionSizes() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getReactionSizes();
		}
		return null;
	}
	
	@Override
	public Map<String, Integer> getNodeColors() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getNodeColors();
		}
		return null;
	}
	
	@Override
	public Map<String, Pair<Integer, Integer>> getReactionsColors() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getReactionsColors();
		}
		return null;
	}
	
	@Override
	public Map<String, Boolean> getFluxDirections() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getFluxDirections();
		}
		return null;
	}
	
	@Override
	public Map<String, Set<String>> getPossibleInvR() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getPossibleInvR();
		}
		return null;
	}
	
	@Override
	public void setReactionLabels(Map<String, String> newLabels) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setReactionLabels(newLabels);
		}
	}
	
	@Override
	public void setNodeLabels(Map<String, String> newLabels) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setNodeLabels(newLabels);
		}
	}
	
	@Override
	public void setEdgeThickness(Map<String, Double> newThickness) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setEdgeThickness(newThickness);
		}
	}
	
	@Override
	public void setNodeShapes(Map<String, Integer> newShapes) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setNodeShapes(newShapes);
		}
	}
	
	@Override
	public void setNodeColors(Map<String, Integer> newColors) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setNodeColors(newColors);
		}
	}
	
	@Override
	public void setReactionColors(Map<String, Pair<Integer, Integer>> newReactionColors) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setReactionColors(newReactionColors);
		}
	}
	
	@Override
	public void setVisFilters(Map<String, Set<String>> newFilters) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setVisFilters(newFilters);
		}
	}
	
	@Override
	public void setFluxDirections(Map<String, Boolean> newDirections) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setFluxDirections(newDirections);
		}
	}
	
	@Override
	public void setNodeSizes(Map<String, Pair<Double, Double>> nodeSizes) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setNodeSizes(nodeSizes);
		}
	}
	
	@Override
	public void setReactionShapes(Map<String, Integer> reactionShapes) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setReactionShapes(reactionShapes);
		}
	}
	
	@Override
	public void setReactionSizes(Map<String, Pair<Double, Double>> reactionSizes) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setReactionSizes(reactionSizes);
		}
	}
	
	@Override
	public void saveToXML(String xmlFileReactions, String xmlFileMetabolites) throws IOException {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).saveToXML(xmlFileReactions, xmlFileMetabolites);
		}
	}
	
	@Override
	public void loadFromXML(String xmlFileMetabolites, String xmlFileReactions) throws FileNotFoundException {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).loadFromXML(xmlFileMetabolites, xmlFileReactions);
		}
	}
	
	@Override
	public void saveToFile(String fileReactions, String fileMetabolites) throws IOException {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).saveToFile(fileReactions, fileMetabolites);
		}
	}
	
	@Override
	public void loadFromFile(String fileReactions, String fileMetabolites) throws IOException, GenericOverlapIOException {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).loadFromFile(fileReactions, fileMetabolites);
		}
	}
	
	@Override
	public Map<String, Double> getEdgesOriginalValues() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getEdgesOriginalValues();
		}
		return null;
	}
	
	@Override
	public Map<String, Double> getNodesOriginalValues() {
		if (_conditions != null && !_conditions.isEmpty()) {
			return _conditions.get(_selectedCondition).getNodesOriginalValues();
		}
		return null;
	}
	
	@Override
	public void setEdgesOriginalValues(Map<String, Double> originalValues) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setEdgesOriginalValues(originalValues);
		}
	}
	
	@Override
	public void setNodesOriginalValues(Map<String, Double> originalValues) {
		if (_conditions != null && !_conditions.isEmpty()) {
			_conditions.get(_selectedCondition).setNodesOriginalValues(originalValues);
		}
	}
	
}
