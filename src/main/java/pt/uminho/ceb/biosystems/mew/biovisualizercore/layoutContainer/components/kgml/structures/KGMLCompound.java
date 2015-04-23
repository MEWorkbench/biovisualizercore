package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KGMLCompound implements Comparable<KGMLCompound>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer id;
	protected Set<String> accessions;
	
	public KGMLCompound(Integer id, List<String> accessions) {
		this.id = id;
		this.accessions = new HashSet<String>(accessions);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getAccessions() {
		if(accessions == null)
			return new ArrayList<String>();
		return new ArrayList<String>(accessions);
	}

	public void addAccession(String accession) {
		this.accessions.add(accession);
	}
	
	@Override
	public int compareTo(KGMLCompound c) {
		return this.id - c.id;
	}

	public KGMLCompound merge(KGMLCompound c2) {
		KGMLCompound newC = new KGMLCompound(id, getAccessions());
		newC.accessions.addAll(c2.getAccessions());
		return newC;
	}

	public boolean isSameAs(KGMLCompound c2) {
		return this.accessions.containsAll(c2.accessions);
	}
	
}
