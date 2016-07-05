package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.utilities.math.normalization.map.INormalizerMap;

public class OverlapFeatureAssignerFactory {
	
	protected Map<String,Class<? extends IOverlapFeatureAssigner>> map;
	
	public OverlapFeatureAssignerFactory() {
		map = new HashMap<>();
		map.put(OverlapEdgeThicknessAssigner.ID, OverlapEdgeThicknessAssigner.class);
		
		map.put(OverlapNodeColorAssigner.ID, OverlapNodeColorAssigner.class);
		map.put(OverlapReactionColorAssigner.ID, OverlapReactionColorAssigner.class);
		
		map.put(OverlapReactionSizeAssigner.ID, OverlapReactionSizeAssigner.class);
		map.put(OverlapNodeSizeAssigner.ID, OverlapNodeSizeAssigner.class);
		
		map.put(OverlapMetabolicReactionShapeAssigner.ID, OverlapMetabolicReactionShapeAssigner.class);
		map.put(OverlapMetabolicNodeShapeAssigner.ID, OverlapMetabolicNodeShapeAssigner.class);
		
		map.put(OverlapReactionShapeAssigner.ID, OverlapReactionShapeAssigner.class);
		map.put(OverlapNodeShapeAssigner.ID, OverlapNodeShapeAssigner.class);
		
		map.put(OverlapReactionLabelAssigner.ID, OverlapReactionLabelAssigner.class);
		map.put(OverlapNodeLabelAssigner.ID, OverlapNodeLabelAssigner.class);
	}
	
	public void register(String klassID, Class<? extends IOverlapFeatureAssigner> klass){
		this.map.put(klassID, klass);
	}
	
	public Map<String, Class<? extends IOverlapFeatureAssigner>> getMap(){
		return this.map;
	}
	
	public Set<String> getAllIDs(){
		return this.map.keySet();
	}
	
	public IOverlapFeatureAssigner getInstance(String klassID, Object... objects) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Class<? extends IOverlapFeatureAssigner> klass = map.get(klassID);
		if(klass == null)
			throw new ClassNotFoundException("Class " + klassID + " not registered in factory");
		
		Class<?>[] objectsKlasses = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; i++) {
			objectsKlasses[i] = identifyClass(objects[i].getClass());
		}
		
		IOverlapFeatureAssigner instance;
		try{
			instance = klass.getConstructor(objectsKlasses).newInstance(objects);
		}catch(NoSuchMethodException e){
			// Maybe the classes are already 
			Class<?>[] originalObjectKlasses = new Class<?>[objects.length];
			for (int i = 0; i < objects.length; i++) {
				originalObjectKlasses[i] = identifyClass(objects[i].getClass());
			}
			instance = klass.getConstructor(originalObjectKlasses).newInstance(objects);
		}
		
		return instance;
	}
	
	protected Class<?> identifyClass(Class<?> klazz){
		Class<?> toRet = null;
		if(INormalizerMap.class.isAssignableFrom(klazz)){
			toRet = INormalizerMap.class;
		}else{
			toRet = klazz;
		}
		return toRet;
	}

}
