package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class OverlapNodeLabelAssigner extends AbstractOverlapLabelAssigner<String> {

	public static final String ID = "NODE_LABEL_ASSIGNER";
	
	@Override
	public <F> void assignGenericFeature(IOverlapObject overlap, Map<String, F> infoToAssign) {
		overlap.setNodeLabels(appendLabels((Map<String, String>) infoToAssign));
	}
	
	protected Map<String, String> appendLabels(Map<String, String> infoToAssign){
		Map<String, String> toRet = new HashMap<String, String>();
		for (String key : infoToAssign.keySet()) {
			toRet.put(key, key + "\n" + infoToAssign.get(key));
		}
		return toRet;
	}
	
}
