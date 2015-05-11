package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.GeneticConditions;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.ReactionChangesList;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.SteadyStateSimulationResult;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public class MetabolicOverlapConversionFactory {

//	public static IOverlapObject convertSimulationPairToOverlap(FluxValueMap fmap1, GeneticConditions gc1, FluxValueMap fmap2, GeneticConditions gc2, String name){
//		
//		AbstractOverlap overlapObject = new AbstractOverlap(name);
//		
//		Set<String> equals = new HashSet<String>();
//		Map<String, Double> reds = new HashMap<String, Double>();
//		Map<String, Double> greens = new HashMap<String, Double>();
//		
//		Set<String> exceptions = new HashSet<String>();
//	
//		for(String id : fmap1.keySet()){
//			
//			double diff = Math.abs(fmap1.get(id))-Math.abs(fmap2.get(id));
//			
//			if(diff==0) equals.add(id);
//			
//			else if(fmap1.get(id)/fmap2.get(id) < 0){
//				exceptions.add(id);
//			}
//			else if( Math.abs(fmap1.get(id))- Math.abs(fmap2.get(id))<0){
//				//Map 2 bigger!
//				Double factor;
//				if(fmap1.get(id)==0) factor = 0.0;
//				else factor = Math.abs(fmap1.get(id))/Math.abs(fmap2.get(id));
//				reds.put(id,factor); 
//				
//			}
//			else if( Math.abs(fmap1.get(id))- Math.abs(fmap2.get(id))>0){
//				//Map 1 bigger!
//				Double factor;
//				if(fmap2.get(id)==0) factor = 0.0;
//				else factor = Math.abs(fmap2.get(id))/Math.abs(fmap1.get(id));
//				greens.put(id, factor);
//			}
//			
//		}
//		
//		
//		for(String id : fmap1.keySet()){
//			
//			Integer color = 0;
//			if(equals.contains(id)){
//				color = ColorLib.rgba(0, 0, 0, 127);
//				overlapObject.addReactionColor(id, new Pair<Integer, Integer>(color, color));
//			}
//			else if(exceptions.contains(id)){
//				
//				if(fmap1.get(id)<0){
//					int color1 = ColorLib.rgb(255, 0, 0);
//					int color2 = ColorLib.rgb(0,255,0);
//					overlapObject.addReactionColor(id, new Pair<Integer,Integer>(color1,color2));
//				}
//				else{
//					int color1 = ColorLib.rgb(0,255,0);
//					int color2 = ColorLib.rgb(255, 0, 0);
//					overlapObject.addReactionColor(id, new Pair<Integer,Integer>(color1,color2));
//				}
//			}
//			else if(reds.containsKey(id)){
//				int redComp = (int)(255*(1-reds.get(id)));
//				color = ColorLib.rgb(redComp, 0, 0);
//				overlapObject.addReactionColor(id, new Pair<Integer, Integer>(color, color));
//			}
//			else{
//				int greenComp = (int)(255*(1-greens.get(id)));
//				color = ColorLib.rgb(0, greenComp, 0);
//				overlapObject.addReactionColor(id, new Pair<Integer, Integer>(color, color));
//			}
//			
//			
//		}
//
//		Map<String, Double> fluxesMet = new HashMap<String,Double>();
//		for(String rId : fmap1.keySet()){
//			fluxesMet.put(rId, (fmap1.get(rId)+fmap2.get(rId))/2);
//		}
//
//		overlapObject.setEdgeThickness(fluxesMet);
//		
//		//LABELS - to add labels, just add what number is wanted in front of default label! FIXME: Check this better later
//		Map<String,String> newLabels = new HashMap<String,String>();
//		
//		for(String rId : fmap1.keySet()){
//			newLabels.put(rId, "[ RF: "+ LayoutUtils.roundToDecimals(fmap1.get(rId),4) + "  |  TF: " + LayoutUtils.roundToDecimals(fmap2.get(rId),4) + " ]");
//		}
//		
//		overlapObject.setReactionLabels(newLabels);
//		
//		
//		//DIRECTIONS
//		
//		//FILTERS
//		String f1name = "Hide zero value fluxes ";
//		Set<String> zeros = new HashSet<String>();
//		for(String id : fmap1.keySet()){
//			
//			if(fmap1.get(id)==0.0 && fmap2.get(id)==0.0) zeros.add(id);
//		}
//		
//		overlapObject.addVisualFilter(f1name, zeros);
//		
//		Set<String> zerosRef = new HashSet<String>();
//		String f2name = "Hide zeros from reference fluxes (RF)";
//		for(String id2 : fmap1.keySet()){
//			
//			if(fmap1.get(id2)==0.0)  zerosRef.add(id2);
//		}
//		
//		overlapObject.addVisualFilter(f2name, zerosRef);
//		
//		Set<String> zerosComp = new HashSet<String>();
//		String f3name = "Hide zeros from target fluxes (TF)";
//		for(String id3: fmap2.keySet()){
//			
//			if(fmap2.get(id3) == 0.0) zerosComp.add(id3);
//		}
//		
//		overlapObject.addVisualFilter(f3name, zerosComp);
//		
//		Map<String, Boolean> directions = getDirectionForComparisons(fmap1, fmap2);
//		overlapObject.setFluxDirections(directions);
//
//		Map<String, Integer> shapes = new HashMap<String, Integer>();
//
//		if(gc1 != null && gc1.getReactionList().size() >0){
//			List<Pair<String,Double>> list1 = gc1.getReactionList().getPairsList();
//			for(Pair<String,Double> pair : list1){
//
//				shapes.put(pair.getA(), getShape(pair));
//				overlapObject.addReactionSize(pair.getA(), new Pair<Double, Double>(5.0, 5.0));
//			}
//		}
//
//		if(gc2 != null && gc2.getReactionList().size() >0){
//			List<Pair<String,Double>> list2 = gc2.getReactionList().getPairsList();
//			for(Pair<String,Double> pair : list2){
//				
//				if(shapes.containsKey(pair.getA())) shapes.put(pair.getA(), SHAPE_QUESTION);
//				else shapes.put(pair.getA(), getShape(pair));
//				overlapObject.addReactionSize(pair.getA(), new Pair<Double, Double>(5.0, 5.0));
//			}
//		}
//		
//		overlapObject.setReactionShapes(shapes);
//		return overlapObject;
//	}

	public static Integer getShape(Pair<String, Double> pair){
		if(pair.getB() > 1.0) return SHAPE_ARROW_UP;
		else if(pair.getB() < 1.0 && pair.getB()>0.0) return SHAPE_ARROW_DOWN;
		else return SHAPE_FATX;
	}
	
	public static AbstractOverlap convertSimulationToOverlap(SteadyStateSimulationResult simRes, String name, double minThickness, double maxThickness, double zeroThikness){

		AbstractOverlap overlapObject = new AbstractOverlap(name);

		Map<String, Double> fluxesMet = simRes.getFluxValues();


		Map<String, Boolean> directions = getDirectionFluxes(fluxesMet);

		Map<String, Double> edgeThickness = LayoutUtils.normalizeFluxes(fluxesMet, minThickness, maxThickness, zeroThikness);
		overlapObject.setEdgeThickness(edgeThickness);
		

		overlapObject.setFluxDirections(directions);	

		addZeroValueFluxesFilter(fluxesMet, simRes.getGeneticConditions(), overlapObject);

		if(simRes.getGeneticConditions()!=null){
			
			List<String> kos = simRes.getGeneticConditions().getReactionList().getReactionKnockoutList();
			if(kos.size()>0){
				Map<String, Integer> kosShapes = getKOSfromLayout(kos);

				Map<String, Integer> kosColors = getKOSColorsfromLayout(kos);

				for(String nodeId : kosShapes.keySet()){
					overlapObject.addReactionShape(nodeId, kosShapes.get(nodeId));
					overlapObject.addReactionColor(nodeId,new Pair<Integer, Integer>(kosColors.get(nodeId),kosColors.get(nodeId)));
					
//					overlapObject.addReactionSize(nodeId, new Pair<Double, Double>((maxThickness-minThickness)/2, (maxThickness-minThickness)/2));
				}
			}
			List<Pair<String, Double>> pairs = simRes.getGeneticConditions().getReactionList().getPairsList();
			setUOShapesAndColors(overlapObject, pairs);
		}
			
		return overlapObject;
	}



	private static Map<String, Integer> getKOSfromLayout(List<String> kos) {
		Map<String, Integer> result = new HashMap<String,Integer>();
		for(String s : kos){
			result.put(s, SHAPE_FATX);
		}
		return result;
	}



	private static Map<String, Integer> getKOSColorsfromLayout(List<String> kos) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(String s : kos){
			result.put(s, KO_COLOUR);
		}
		return result;
	}


	
	private static void addZeroValueFluxesFilter(Map<String, Double> fluxesMet,
			GeneticConditions geneticConditions, AbstractOverlap overlapObject) {

		
		Set<String> result = new HashSet<String>();
		for(String s : fluxesMet.keySet()){
			
			if(fluxesMet.get(s)==0){
				if(!(geneticConditions!=null && geneticConditions.getReactionList().keySet().contains(s)))
					result.add(s);
//					overlapObject.getReactionsColors().put(s, new Pair<Integer,Integer>(ColorLib.rgb(125,125,125),ColorLib.rgb(125,125,125)) );
			}
		}
		
		overlapObject.addVisualFilter("Hide zero value fluxes", result);
	}



	private static void setUOShapesAndColors(AbstractOverlap overlapObject,
			List<Pair<String, Double>> pairs) {

		for(Pair<String,Double> pair : pairs){
			
			if(pair.getB()>1.0){
				overlapObject.addReactionShape(pair.getA(), SHAPE_ARROW_UP);
				overlapObject.addReactionColor(pair.getA(), new Pair<Integer, Integer>(OVER_COLOUR,OVER_COLOUR));
				overlapObject.addReactionSize(pair.getA(), new Pair<Double, Double>(5.0, 5.0));
			}
			else if(pair.getB()<1.0 && pair.getB()>0.0){
				overlapObject.addReactionShape(pair.getA(), SHAPE_ARROW_DOWN);
				overlapObject.addReactionColor(pair.getA(), new Pair<Integer, Integer>(UNDER_COLOUR,UNDER_COLOUR));
				overlapObject.addReactionSize(pair.getA(), new Pair<Double, Double>(5.0, 5.0));

			}
		}
		
	}

//	private static Map<String, String> getLabelsFromFlux(
//			Map<String, Double> fluxesMet) {
//
//		Map<String,String> result = new HashMap<String,String>();
//		for(String s : fluxesMet.keySet()){
//			
//			result.put(s, s + ": " + fluxesMet.get(s));
//		}
//		
//		return result;
//	}

	
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

	private static Map<String, Boolean> getDirectionFluxes(
			Map<String, Double> fluxesMet) {

		Map<String, Boolean> directions = new HashMap<String,Boolean>();
		for(String s : fluxesMet.keySet()){
			
			if(fluxesMet.get(s) != 0){
				if(fluxesMet.get(s)>=0) directions.put(s, true);
				else directions.put(s, false);
			}
		}
		
		return directions;
	}
		
	public static IOverlapObject convertFluxDistOverlap(FluxValueMap f, String name, Double minThickness, Double maxThickness, Double zeroThikness) {


		AbstractOverlap overlapObject = new AbstractOverlap(name);
		
		Map<String, Boolean> directions = getDirectionFluxes(f);

		Map<String, Double> edgeThickness = LayoutUtils.normalizeFluxes(f, minThickness, maxThickness, zeroThikness);
		overlapObject.setEdgeThickness(edgeThickness);		
		overlapObject.setFluxDirections(directions);			
		
		addZeroValueFluxesFilter(f, new GeneticConditions(new ReactionChangesList(), false), overlapObject);
		
		return overlapObject;
	}
	
	
	/**STATIC VARIABLES*/
	 /** No shape. Draw nothing. */
    public static final int SHAPE_NONE           = -1;
    /** Rectangle/Square shape */
    public static final int SHAPE_RECTANGLE      = 0;
    /** Ellipse/Circle shape */
    public static final int SHAPE_ELLIPSE        = 1;
    /** Diamond shape */
    public static final int SHAPE_DIAMOND        = 2;
    /** Cross shape */
    public static final int SHAPE_CROSS          = 3;
    /** Star shape */
    public static final int SHAPE_STAR           = 4;
    /** Up-pointing triangle shape */
    public static final int SHAPE_TRIANGLE_UP    = 5;
    /** Down-pointing triangle shape */
    public static final int SHAPE_TRIANGLE_DOWN  = 6;
    /** Left-pointing triangle shape */
    public static final int SHAPE_TRIANGLE_LEFT  = 7;
    /** Right-pointing triangle shape */
    public static final int SHAPE_TRIANGLE_RIGHT = 8;
    /** Hexagon shape */
    public static final int SHAPE_HEXAGON        = 9;
    /**Rotated cross shape*/
    public static final int SHAPE_X				 = 10;
    /**Rotated cross fat shape*/
    public static final int SHAPE_FATX				= 11;
    /**Arrow pointing down */
    public static final int SHAPE_ARROW_DOWN			= 12;
    /**Arrow pointing up */
    public static final int SHAPE_ARROW_UP 			= 13;
    /**Quention mark */
    public static final int SHAPE_QUESTION 			=14;
    /** The number of recognized shape types */
    public static final int SHAPE_COUNT          = 15;
    
    public static final int KO_COLOUR = -65536;
    public static final int UNDER_COLOUR = -33536;
    public static final int OVER_COLOUR = -16711936;
	
    
}
