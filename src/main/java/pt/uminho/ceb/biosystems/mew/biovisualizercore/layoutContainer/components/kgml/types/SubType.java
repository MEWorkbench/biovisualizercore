package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types;

import java.io.Serializable;


public class SubType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected EntryType type;
	protected Integer id;
	
	public SubType(EntryType type, Integer id) {
		this.type = type;
		this.id = id;
	}

	public EntryType getType() {
		return type;
	}

	public void setType(EntryType type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
}
