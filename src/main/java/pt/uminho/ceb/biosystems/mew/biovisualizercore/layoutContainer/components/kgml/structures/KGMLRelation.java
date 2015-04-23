package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.io.Serializable;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.RelationType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.SubType;

public class KGMLRelation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer entry1;
	protected Integer entry2;
	protected RelationType relationType;
	protected SubType subType;

	public KGMLRelation(Integer entry1, Integer entry2,
			RelationType relationType, SubType subType) {
		this.entry1 = entry1;
		this.entry2 = entry2;
		this.relationType = relationType;
		this.subType = subType;
	}

	public Integer getEntry1() {
		return entry1;
	}

	public void setEntry1(Integer entry1) {
		this.entry1 = entry1;
	}

	public Integer getEntry2() {
		return entry2;
	}

	public void setEntry2(Integer entry2) {
		this.entry2 = entry2;
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	public SubType getSubType() {
		return subType;
	}

	public void setSubType(SubType subType) {
		this.subType = subType;
	}

}
