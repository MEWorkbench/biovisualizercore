package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.io.Serializable;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.EntryType;

public class KGMLEntry implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer id;
	protected String name;
	protected EntryType type;
	protected String reactionId;
	protected KGMLGraphics graphics;
	
	public KGMLEntry(Integer id, String name, EntryType type, String reactionId, KGMLGraphics graphics) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.reactionId = reactionId;
		this.graphics = graphics;
	}
	
	public KGMLEntry(KGMLEntry entry) {
		this.id = entry.id;
		this.name = entry.name;
		this.type = entry.type;
		this.reactionId = entry.reactionId;
		this.graphics = entry.graphics.clone();
	}
	
	@Override
	public KGMLEntry clone() {
		return new KGMLEntry(this);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntryType getType() {
		return type;
	}

	public void setType(EntryType type) {
		this.type = type;
	}

	public String getReactionId() {
		return reactionId;
	}

	public void setReactionId(String reactionId) {
		this.reactionId = reactionId;
	}

	public KGMLGraphics getGraphics() {
		return graphics;
	}

	public void setGraphics(KGMLGraphics graphics) {
		this.graphics = graphics;
	}

	public KGMLEntry merge(KGMLEntry entry) {
		KGMLEntry newE = new KGMLEntry(this);
		if(this.name.compareTo(entry.name) != 0)
			this.name += " " + entry.name;
		newE.setGraphics(this.graphics.merge(entry.graphics));
		return newE;
	}
	
	
}
