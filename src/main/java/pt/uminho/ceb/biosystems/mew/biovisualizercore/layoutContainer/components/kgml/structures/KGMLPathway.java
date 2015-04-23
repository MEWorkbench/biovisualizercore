package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.EntryType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.RelationType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.SubType;

public class KGMLPathway implements Cloneable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String accession;
	protected Map<Integer, KGMLEntry> entries;
	protected Map<Integer, KGMLCompound> compounds;
	protected List<KGMLRelation> relations;
	protected Map<Integer, KGMLReaction> reactions;
	
	public KGMLPathway(String name, String accession) {
		this.name = name;
		this.accession = accession;
		this.entries = new HashMap<Integer, KGMLEntry>();
		this.relations = new ArrayList<KGMLRelation>();
		this.reactions = new HashMap<Integer, KGMLReaction>();
		this.compounds = new HashMap<Integer, KGMLCompound>();
	}
	 
	public KGMLPathway(String name, String accession, Map<Integer, KGMLEntry> entries, List<KGMLRelation> relations, Map<Integer, KGMLReaction> reactions, Map<Integer, KGMLCompound> compounds) {
		this.name = name;
		this.accession = accession;
		this.entries = entries;
		this.relations = relations;
		this.reactions = reactions;
		this.compounds = compounds;
	}
	
	public KGMLPathway(KGMLPathway pathway) {
		this.name = pathway.name;
		this.accession = pathway.accession;
		this.entries = new HashMap<Integer, KGMLEntry>(pathway.entries);
		this.relations = new ArrayList<KGMLRelation>(pathway.relations);
		this.reactions = new HashMap<Integer, KGMLReaction>(pathway.reactions);
		this.compounds = new HashMap<Integer, KGMLCompound>(pathway.compounds);
	}
	
	public KGMLPathway mergeReactions() {
		KGMLPathway p = new KGMLPathway(this);
		for(KGMLReaction r1 : reactions.values()) {
			for(KGMLReaction r2 : reactions.values()) {
				if(r1 != r2 && r1.isSameConversion(r2)) {
					int id1 = r1.getId();
					int id2 = r2.getId();
					KGMLEntry e1 = entries.get(id1);
					KGMLEntry e2 = entries.get(id2);
					KGMLEntry newE = e1.merge(e2);
					p.setEntry(id1, newE);
					p.removeEntry(id2);
					KGMLReaction newR = r1.merge(r2);
					p.setReaction(id1, newR);
					p.removeReaction(id2);
				}
			}

		}
		return p;
	}
	
	public List<Integer> mapEntriesForCompound(int compoundId) {
		List<Integer> mapIds = new ArrayList<Integer>();
		for(KGMLRelation r : relations) {
			SubType s = r.getSubType();
			if(s.getId() == compoundId) {
				KGMLEntry e1 = entries.get(r.getEntry1());
				KGMLEntry e2 = entries.get(r.getEntry2());
				if(e1 != null && e1.getType() == EntryType.MAP) mapIds.add(e1.getId());
				if(e2 != null && e2.getType() == EntryType.MAP) mapIds.add(e2.getId());
			}
		}
		return mapIds;
	}
	
	
	
	public void removeReaction(int reactionId) {
		reactions.remove(reactionId);
	}
	
	public void removeCompound(int compoundId) {
		compounds.remove(compoundId);
	}

	public void removeEntry(int entryId) {
		entries.remove(entryId);
	}

	public void setReaction(int id, KGMLReaction reaction) {
		reactions.put(id, reaction);
	}
	
	public void setCompound(int id, KGMLCompound compound) {
		compounds.put(id, compound);
	}

	public List<KGMLRelation> getRelationsByType(RelationType relType) {
		List<KGMLRelation> relationsList = new ArrayList<KGMLRelation>();
		for(KGMLRelation rel : relations) {
			System.out.println(rel.getRelationType());
			if(rel.getRelationType() == relType)
				relationsList.add(rel);
		}
		return relationsList;
	}

	public KGMLReaction findReactionByAcc(String acc) {
		for(KGMLReaction r : reactions.values()) {
			for(String a :r.getAccessions())
				if(a.compareToIgnoreCase(acc) == 0) return r;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, KGMLEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<Integer, KGMLEntry> entries) {
		this.entries = entries;
	}

	public List<KGMLRelation> getRelations() {
		return relations;
	}

	public void setRelations(List<KGMLRelation> relations) {
		this.relations = relations;
	}

	public Map<Integer, KGMLReaction> getReactions() {
		return reactions;
	}

	public void setReactions(Map<Integer, KGMLReaction> reactions) {
		this.reactions = reactions;
	}

	public void addEntry(Integer index, KGMLEntry entry) {
		if(entries.containsValue(entry)) {
			KGMLEntry newE = entries.get(index).merge(entry); 
			entries.put(index, newE);
		}
		else
		entries.put(index, entry);
	}
	public void setEntry(Integer index, KGMLEntry entry) {
		entries.put(index, entry);
	}
	

	public void addRelation(KGMLRelation relation) {
		relations.add(relation);
	}

	public void addReaction(Integer index, KGMLReaction reaction) {
			reactions.put(index, reaction);		
	}
	
	public void addCompound(Integer index, KGMLCompound compound) {
		if(!compounds.containsKey(index))
			compounds.put(index, compound);
	}

	public KGMLReaction getReaction(Integer id) {
		return reactions.get(id);
	}

	public KGMLCompound getCompound(Integer id) {
		return compounds.get(id);
	}
	
	public KGMLCompound getCompoundByAcc(String acc) {
		for(KGMLCompound c : compounds.values()) {
			for(String a : c.getAccessions())
				if(a.compareToIgnoreCase(acc) == 0) return c;
		}
		return null;
	}

	public KGMLEntry getEntry(Integer id) {
		return entries.get(id);
	}

	public Map<Integer, KGMLCompound> getCompounds() {
		return compounds;
	}

	public String getAccession() {
		return accession;
	}
	
	public KGMLPathway clone(){
		
		return new KGMLPathway(this);
	}
	
}
