package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners;

import java.util.Set;

public class ChangePathwayEvent {
	
	protected String pathway;
	protected Set<String> metabolites;
	
	public ChangePathwayEvent(String pathway, Set<String> metabolites){
		
		this.pathway = pathway;
		this.metabolites = metabolites;
	}

	public String getPathway() {
		return pathway;
	}

	public Set<String> getMetabolites() {
		return metabolites;
	}

	
}
