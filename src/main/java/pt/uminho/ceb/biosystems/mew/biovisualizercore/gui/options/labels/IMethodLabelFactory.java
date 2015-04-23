package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

public interface IMethodLabelFactory<T> {
	
	/** @param attibuteId refers to the specific part (attributes) in T, for which the method will build the information  */
	public String getInfo(T Object, String elementId);

}
