package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.util.Set;

public class MissingLabelException extends Exception {
	
	private String id;
	private Set<String> keySet;
	
	
	public MissingLabelException(String id, Set<String> keySet) {
		super(id +  "  " + keySet.toString());
		this.id = id;
		this.keySet = keySet;
		
	}

}
