package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.util.HashMap;
import java.util.Map;

public class FluxMap {

	protected Integer reactionId;
	protected Map<Integer, Double> fluxToCompound;
	
	public FluxMap(Integer reactionId) {
		this.reactionId = reactionId;
		this.fluxToCompound = new HashMap<Integer, Double>(); 
	}
	
	public void setFlux(Integer compoundId, Double fluxValue) {
		if(fluxToCompound.containsKey(compoundId)) {
			Double flux = fluxToCompound.get(compoundId);
			fluxToCompound.put(compoundId, flux + fluxValue);
		}
		else fluxToCompound.put(compoundId, fluxValue);
		
	}
	
	public Double fluxForCompound(Integer compoundId) {
		if(fluxToCompound.containsKey(compoundId)) return fluxToCompound.get(compoundId);
		else return 0.0;
	}
	
}
