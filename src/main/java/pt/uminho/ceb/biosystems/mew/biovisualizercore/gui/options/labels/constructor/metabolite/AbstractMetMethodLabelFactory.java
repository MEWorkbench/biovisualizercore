package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public abstract class AbstractMetMethodLabelFactory implements IMethodLabelFactory<Container>{

	public MetaboliteCI getMetabolite(Container container, String metaboliteId){
		return (container.getMetabolites().containsKey(metaboliteId)) ? container.getMetabolite(metaboliteId) : null;
	}

}
