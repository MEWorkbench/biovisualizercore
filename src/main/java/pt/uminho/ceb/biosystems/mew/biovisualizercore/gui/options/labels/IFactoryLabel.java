package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IFactoryLabel<T> {
	
	/** Methods that allow to access the information of the attribute (key) in the object T */
	Map<String, IMethodLabelFactory<T>> methods;
	
	public IFactoryLabel(){
		methods=new HashMap<String, IMethodLabelFactory<T>>();
	}
	
	private void validateMethod(String id) throws MissingLabelException{
		if(!methods.containsKey(id))
			throw new MissingLabelException(id, methods.keySet());
	}
	
	public String getValue(T object, String methodId, String elementId) throws MissingLabelException{
		validateMethod(methodId);
		return methods.get(methodId).getInfo(object, elementId);
	}
	
	public Set<String> getAllMethodsIds(){
		return methods.keySet();
	}
	
	public void addLabelMethod(String id, IMethodLabelFactory<T> m){
		methods.put(id, m);
	}

}
