package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public interface IOverlapObject extends Serializable{
	
	String getName();
	
	void setName(String name);
	
	Map<String, Double> getEdgesOriginalValues();
	
	Map<String, Double> getNodesOriginalValues();
	
	Map<String, String> getNewReactionLabels();
	
	Map<String, String> getNewNodeLabels();
	
	Map<String, Double> getEdgeThickness();
	
	Map<String, Integer> getNodeShapes();
	
	Map<String, Integer> getReactionShapes();
	
	Map<String, Pair<Double,Double>> getNodeSizes();
	
	Map<String, Pair<Double, Double>> getReactionSizes();
	
	Map<String, Integer> getNodeColors();
	
	//colors for reactants and products!
	Map<String, Pair<Integer,Integer>> getReactionsColors();
	
	//Used for flux distribution shape changing. If flux is positive then the value it's true; false otherwise.
	Map<String, Boolean> getFluxDirections();
	
	Map<String, Set<String>> getPossibleInvR();
	
	void setEdgesOriginalValues(Map<String, Double> originalValues);
	
	void setNodesOriginalValues(Map<String, Double> originalValues);

	void setReactionLabels(Map<String, String> newLabels);

	void setNodeLabels(Map<String, String> newLabels);
	
	void setEdgeThickness(Map<String, Double> newThickness);

	void setNodeShapes(Map<String, Integer> newShapes);

	void setNodeColors(Map<String, Integer> newColors);

	void setReactionColors(Map<String, Pair<Integer, Integer>> newReactionColors);

	void setVisFilters(Map<String, Set<String>> newFilters);

	void setFluxDirections(Map<String, Boolean> newDirections);
	
	void setNodeSizes(Map<String, Pair<Double,Double>> nodeSizes);
	
	void setReactionShapes(Map<String, Integer> reactionShapes);
	
	void setReactionSizes(Map<String, Pair<Double, Double>> reactionSizes);
	
	void saveToXML(String xmlFileReactions, String xmlFileMetabolites) throws IOException;
	
	void loadFromXML(String xmlFileMetabolites, String xmlFileReactions) throws FileNotFoundException;
	
	void saveToFile(String fileReactions, String fileMetabolites) throws IOException;
	
	void loadFromFile(String fileReactions, String fileMetabolites) throws IOException, GenericOverlapIOException;
	
	

}
