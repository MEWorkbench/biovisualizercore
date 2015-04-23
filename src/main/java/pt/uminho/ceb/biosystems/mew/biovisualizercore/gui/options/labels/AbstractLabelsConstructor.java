package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class AbstractLabelsConstructor<T> {
	
	protected List<String> infoIds;
	protected List<String> separators;
	protected String mask = null;
	
	IFactoryLabel<T> constructInfo;
	
	public AbstractLabelsConstructor(IFactoryLabel<T> constructInfo) {
		this.constructInfo = constructInfo;
		infoIds = new ArrayList<String>();
		separators = new ArrayList<String>();
	}
	
	public void addLabelInfo(String methodId, String separator){
		infoIds.add(methodId);
		separators.add(separator);
		mask=null;
	}
	
	public String getLabel(T o, String elementId) throws MissingLabelException{
		String ret = "";
		for(int i =0;i<infoIds.size() ; i++){
			String mValue = constructInfo.getValue(o, infoIds.get(i), elementId);
			if(mValue != null)
				ret += mValue + separators.get(i);
		}
		return ret;
	}
	
	public int getNumberOfLines(){
		return 1 + countNewLines(separators);
	}
	
	private int countNewLines(List<String> separator2) {
		int i =0;
		for(String s : separator2)
			if(s.contains("\n")) i++;
		return i;
	}

	public String getLabelMasK(){
		
		if(mask==null){
			mask = "";
			for(int i =0;i<infoIds.size() ; i++){
				mask+=infoIds.get(i) + separators.get(i);
			}
		}
		return mask;
	}
	
	public String toString(){
		return getLabelMasK();
	}
	
	public Set<String> getAllMethodIds(){
		return new TreeSet<String>(constructInfo.getAllMethodsIds());
	}
	
	public Pair<String, String> getMethodAndSep(int idx){
		Pair<String, String> ret = new Pair<String, String>(infoIds.get(idx), separators.get(idx));
		return ret;
	}
	
	public int countMethods(){
		return infoIds.size();
	}

	public IFactoryLabel<T> getConstructInfo() {
		return constructInfo;
	}

	public int getIdxInfo(String info){
		return info.indexOf(info);
	}
	
	public void removeInfo(String info){
		int i = getIdxInfo(info);
		infoIds.remove(i);
		separators.remove(i);
		mask = null;
	}
	
	public void clear() {
		this.infoIds.clear();
		this.separators.clear();
		this.mask = null;
	}
}
