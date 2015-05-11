package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;

public class SimulationComparison {
	
	private SteadyStateSimulationResult simulation1;
	private SteadyStateSimulationResult simulation2;
	
	public SimulationComparison(SteadyStateSimulationResult s1, SteadyStateSimulationResult s2){
		
		simulation1 = s1;
		simulation2 = s2;
		
	}

	public SteadyStateSimulationResult getSimulation1() {
		return simulation1;
	}

	public void setSimulation1(SteadyStateSimulationResult simulation1) {
		this.simulation1 = simulation1;
	}

	public SteadyStateSimulationResult getSimulation2() {
		return simulation2;
	}

	public void setSimulation2(SteadyStateSimulationResult simulation2) {
		this.simulation2 = simulation2;
	}
	
	public Map<String, Double> getRatios1to2(){
		
		return getRatios(simulation1.getFluxValues(), simulation2.getFluxValues());
	}
	
	public Map<String, Double> getRatios2to1(){
		return getRatios(simulation2.getFluxValues(), simulation1.getFluxValues());
	}
	
	private Map<String, Double> getRatios(FluxValueMap f1, FluxValueMap f2){
		
		Map<String, Double> result = new HashMap<String, Double>();
		for(String id : f1.keySet()){
			Double value1 = f1.get(id);
			if(value1 != 0.0){
				Double value2 = f2.get(id);
				if(value2 != 0.0)
					result.put(id, value1/value2);
				else
					result.put(id, Double.NaN);
			}
			else
				result.put(id, 0.0);
		}
		
		return result;
	}
	
	public Set<String> getInacitveReactionsSim1(){
		
		return inactiveReactionsList(simulation1.getFluxValues());
	}
	
	public Set<String> getInactiveReactionsSim2(){
		return inactiveReactionsList(simulation2.getFluxValues());
	}
	
	public Set<String> getInactiveReactionsBoth(){
		return CollectionUtils.getIntersectionValues(inactiveReactionsList(simulation1.getFluxValues()), inactiveReactionsList(simulation2.getFluxValues()));
	}
	
	private Set<String> inactiveReactionsList(FluxValueMap fmap){
		
		Set<String> result = new HashSet<String>();
		for(String id : fmap.keySet()){
			if(fmap.get(id)==0) result.add(id);
		}
		
		return result;
	}
	
	public Map<String, Double> getDifferences(){
		
		Map<String, Double> result = new HashMap<String, Double>();
		for(String id : simulation1.getFluxValues().keySet()){
			double diff = Math.abs(simulation1.getFluxValues().get(id))-Math.abs(simulation2.getFluxValues().get(id));
			result.put(id, diff);
		}
		return result;
	}

	public Set<String> getIds() {

		return simulation1.getFluxValues().keySet();
	}
	
	public Double getFluxFrom1(String id){
		return simulation1.getFluxValues().get(id);
	}
	
	public Double getFluxFrom2(String id){
		return simulation2.getFluxValues().get(id);
	}
	
	public GeneticConditions getGC1(){
		return simulation1.getGeneticConditions();
	}
	
	public GeneticConditions getGC2(){
		return simulation2.getGeneticConditions();
	}
}
