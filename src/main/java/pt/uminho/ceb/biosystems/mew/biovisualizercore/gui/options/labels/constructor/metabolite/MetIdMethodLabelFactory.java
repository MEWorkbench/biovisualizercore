package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

public class MetIdMethodLabelFactory extends AbstractMetMethodLabelFactory{

	@Override
	public String getInfo(Container container, String metaboliteId) {
		return metaboliteId;
	}

}
