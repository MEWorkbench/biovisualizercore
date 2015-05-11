package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import prefuse.util.ColorLib;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class SimulationComparisonOverlapBuilder {

	public static IOverlapObject convertSimulationPairToOverlap(
			SimulationComparison comp, String name) {

		AbstractOverlap overlapObject = new AbstractOverlap(name);

		Set<String> equals = new HashSet<String>();
		Map<String, Double> reds = new HashMap<String, Double>();
		Map<String, Double> greens = new HashMap<String, Double>();
		Set<String> exceptions = new HashSet<String>();
		
		Map<String, Double> diff = comp.getDifferences();
		Map<String, Double> ratio1to2 = comp.getRatios1to2();
//		Map<String, Double> ractio2to1 = comp.getRatios2to1();
		
		for(String id : comp.getIds()){
			
			if(diff.get(id)==0) equals.add(id);
			
			else if(ratio1to2.get(id)<0) exceptions.add(id);
			
			else if(Math.abs(comp.getFluxFrom1(id))- Math.abs(comp.getFluxFrom2(id))<0){
				//Map 2 bigger!
				Double factor;
				if(comp.getFluxFrom1(id)==0) factor = 0.0;
				else factor = Math.abs(comp.getFluxFrom1(id))/Math.abs(comp.getFluxFrom2(id));
				reds.put(id,factor); 
			}
			else if( Math.abs(comp.getFluxFrom1(id))- Math.abs(comp.getFluxFrom2(id))>0){
				//Map 1 bigger!
				Double factor;
				if(comp.getFluxFrom2(id)==0) factor = 0.0;
				else factor = Math.abs(comp.getFluxFrom2(id))/Math.abs(comp.getFluxFrom1(id));
				greens.put(id, factor);
			}
		}
		
		for(String id : comp.getIds()){
			
			Integer color = 0;
			if(equals.contains(id)){
				color = ColorLib.rgba(0, 0, 0, 127);
				overlapObject.addReactionColor(id, new Pair<Integer, Integer>(color, color));
			}
			else if(exceptions.contains(id)){
				
				if(comp.getFluxFrom1(id)<0){
					
					int color1 = ColorLib.rgb(255,0,0);
					int color2 = ColorLib.rgb(0, 255, 0);
					overlapObject.addReactionColor(id, new Pair<Integer,Integer>(color1,color2));
				}
				else{
					
					int color1 = ColorLib.rgb(0, 255, 0);
					int color2 = ColorLib.rgb(255,0,0);
					overlapObject.addReactionColor(id, new Pair<Integer,Integer>(color1,color2));
				}
			}
			else if(reds.containsKey(id)){
				int redComp = (int)(255*(1-reds.get(id)));
				color = ColorLib.rgb(redComp, 0, 0);
				overlapObject.addReactionColor(id, new Pair<Integer, Integer>(color, color));
			}
			else{
				int greenComp = (int)(255*(1-greens.get(id)));
				color = ColorLib.rgb(0, greenComp, 0);
				overlapObject.addReactionColor(id, new Pair<Integer, Integer>(color, color));
			}
			
			
		}

		Map<String, Double> fluxesMet = new HashMap<String,Double>();
		for(String rId : comp.getIds()){
			fluxesMet.put(rId, Math.abs(comp.getFluxFrom1(rId))+Math.abs(comp.getFluxFrom2(rId))/2);
		}
		
		overlapObject.setEdgeThickness(LayoutUtils.normalizeFluxes(fluxesMet, 2, 10, 1));
		
		//LABELS - to add labels, just add what number is wanted in front of default label! FIXME: Check this better later
		Map<String,String> newLabels = new HashMap<String,String>();
		
		for(String rId : comp.getIds()){
			newLabels.put(rId, "[ RF: "+ LayoutUtils.roundToDecimals(comp.getFluxFrom1(rId),4) + "  |  TF: " + LayoutUtils.roundToDecimals(comp.getFluxFrom2(rId),4) + " ]");
		}
		
		overlapObject.setReactionLabels(newLabels);
		
		
		//DIRECTIONS
		
		//FILTERS
		String f1name = "Hide zero value fluxes ";
		Set<String> zeros = new HashSet<String>();
		for(String id : comp.getIds()){
			
			if(comp.getFluxFrom1(id)==0.0 && comp.getFluxFrom2(id)==0.0){
				if(comp.getGC1()!=null || comp.getGC2()!=null){
					
					boolean isGeneticCond = false;
					if(comp.getGC1()!=null && comp.getGC1().getReactionList().containsReaction(id)){
						isGeneticCond = true;
					}
					else if(comp.getGC2()!=null && comp.getGC2().getReactionList().containsReaction(id)){
						isGeneticCond = true;
					}
					if(!isGeneticCond) zeros.add(id);
				}
				else
					zeros.add(id);
			}
		}
		
		overlapObject.addVisualFilter(f1name, zeros);
		
		Set<String> zerosRef = new HashSet<String>();
		String f2name = "Hide zeros from reference fluxes (RF)";
		for(String id2 : comp.getIds()){
			
			if(comp.getFluxFrom1(id2)==0.0) 
				if(comp.getGC1()==null || comp.getGC1().getReactionList().containsReaction(id2))
					zerosRef.add(id2);
		}
		
		overlapObject.addVisualFilter(f2name, zerosRef);
		
		Set<String> zerosComp = new HashSet<String>();
		String f3name = "Hide zeros from target fluxes (TF)";
		for(String id3: comp.getIds()){
			
			if(comp.getFluxFrom2(id3) == 0.0)
				if(comp.getGC2() == null || !comp.getGC2().getReactionList().containsReaction(id3))
					zerosComp.add(id3);
		}
		
		overlapObject.addVisualFilter(f3name, zerosComp);
		
		Map<String, Boolean> directions = getDirectionForComparisons(comp.getSimulation1().getFluxValues(), comp.getSimulation2().getFluxValues());
		overlapObject.setFluxDirections(directions);

		Map<String, Integer> shapes = new HashMap<String, Integer>();

		if(comp.getGC1() != null && comp.getGC1().getReactionList().size() >0){
			List<Pair<String,Double>> list1 = comp.getGC1().getReactionList().getPairsList();
			for(Pair<String,Double> pair : list1){

				shapes.put(pair.getA(), MetabolicOverlapConversionFactory.getShape(pair));
				overlapObject.addReactionSize(pair.getA(), new Pair<Double, Double>(5.0, 5.0));
			}
		}

		if(comp.getGC2() != null && comp.getGC2().getReactionList().size() >0){
			List<Pair<String,Double>> list2 = comp.getGC2().getReactionList().getPairsList();
			for(Pair<String,Double> pair : list2){
				
				if(shapes.containsKey(pair.getA())) shapes.put(pair.getA(), MetabolicOverlapConversionFactory.SHAPE_QUESTION);
				else shapes.put(pair.getA(), MetabolicOverlapConversionFactory.getShape(pair));
				overlapObject.addReactionSize(pair.getA(), new Pair<Double, Double>(5.0, 5.0));
			}
		}
		
		overlapObject.setReactionShapes(shapes);
		return overlapObject;
	}
	
private static Map<String, Boolean> getDirectionForComparisons(Map<String, Double> fluxes1, Map<String, Double> fluxes2){
		
		Map<String, Boolean> directions = new HashMap<String,Boolean>();
		
		for(String s : fluxes1.keySet()){
			
			if(!(fluxes1.get(s)-fluxes2.get(s)==0)){
				
				//Not equal!
				if(fluxes1.get(s)==0){
					
					if(fluxes2.get(s) > 0) directions.put(s, true);
					else directions.put(s, false);
					
				}
				else if(fluxes2.get(s) == 0){
					
					if(fluxes1.get(s)>0) directions.put(s, true);
					else directions.put(s, false);
					
				}
				else if(fluxes1.get(s)/fluxes2.get(s)>0){
					
					if(fluxes1.get(s)>0) directions.put(s, true);
					else directions.put(s, false);
				}
				
			}
			else{
				if(fluxes1.get(s)!=0 && (fluxes1.get(s)/fluxes2.get(s))>0){
					if(fluxes1.get(s)>0) directions.put(s, true);
					else directions.put(s, false);
				}
			}
		}
		
		return directions;
	}
}
