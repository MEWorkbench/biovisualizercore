package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners;

import java.util.EventObject;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

public class MetabolicComponentEvent extends EventObject{

	private static final long	serialVersionUID	= 1L;
	private Set<String> ids = null;
	private String uuid;
	private Container information;
	private String label;
	
	
	public MetabolicComponentEvent(Object source,String uuid, Set<String> ids,String label, Container information) {
		super(source);
		this.ids = ids;
		this.uuid = uuid;
		this.information = information;
		this.label = label;
	}

	
	public Set<String> getIds() {
		return ids;
	}

	public void setIds(Set<String> ids) {
		this.ids = ids;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public Container getContainer(){
		return information;
	}
	
	public String getLabel() {
		return label;
	}
}
