package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class KGMLReaction implements Comparable<KGMLReaction>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer id;
	protected Set<String> accessions;
	protected Boolean reversible;
	protected Set<KGMLCompound> substrates;
	protected Set<KGMLCompound> products;
	
	public KGMLReaction(Integer id, List<String> accessions, Boolean reversible, List<KGMLCompound> substrates, List<KGMLCompound> products) {
		this.id = id;
		this.accessions = new TreeSet<String>(accessions);
		this.reversible = reversible;
		this.substrates = new TreeSet<KGMLCompound>(substrates);
		this.products = new TreeSet<KGMLCompound>(products);
	}

	public KGMLReaction(KGMLReaction reaction) {
		this.id = reaction.id;
		this.accessions = reaction.accessions;
		this.reversible = reaction.reversible;
		this.substrates = reaction.substrates;
		this.products = reaction.products;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getAccessions() {
		return new ArrayList<String>(accessions);
	}

	public void setAccession(List<String> accessions) {
		this.accessions = new TreeSet<String>(accessions);
	}

	public Boolean getReversible() {
		return reversible;
	}

	public void setReversible(Boolean reversible) {
		this.reversible = reversible;
	}

	public List<KGMLCompound> getSubstrates() {
		return new ArrayList<KGMLCompound>(substrates);
	}

	public void setSubstrates(List<KGMLCompound> substrates) {
		this.substrates = new TreeSet<KGMLCompound>(substrates);
	}

	public List<KGMLCompound> getProducts() {
		return new ArrayList<KGMLCompound>(products);
	}

	public void setProducts(List<KGMLCompound> products) {
		this.products = new TreeSet<KGMLCompound>(products);
	}

	@Override
	public int compareTo(KGMLReaction o) {
		if(o.id == this.id) return 1;
		return 0;
	}

	public List<KGMLCompound> getCompounds() {
		List<KGMLCompound> compounds = new ArrayList<KGMLCompound>();
		compounds.addAll(products);
		compounds.addAll(substrates);
		return compounds;
	}

	public KGMLReaction merge(KGMLReaction reaction) {
		KGMLReaction newR = new KGMLReaction(this);
		newR.accessions.addAll(reaction.accessions);
		List<Boolean> matchList = new ArrayList<Boolean>();
		for(KGMLCompound c : substrates) matchList.add(reaction.substrates.contains(c));
		if(matchList.contains(true)) {
			newR.substrates.addAll(reaction.substrates);
			newR.products.addAll(reaction.products);
		}
		newR.reversible = reversible || reaction.reversible;
		
		return newR;
	}

	public boolean isSameConversion(KGMLReaction reaction) {
		boolean c1 = products.containsAll(reaction.products) && substrates.containsAll(reaction.substrates);
		boolean c2 = products.containsAll(reaction.substrates) && substrates.containsAll(reaction.products);
		return c1 || c2;
	}
	
	
	
}
