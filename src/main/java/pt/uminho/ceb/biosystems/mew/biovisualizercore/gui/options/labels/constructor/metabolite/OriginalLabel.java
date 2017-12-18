package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public class OriginalLabel implements IMethodLabelFactory<Container>{

	@Override
	public String getInfo(Container Object, String elementId) {
		return "$$$$$$";
	}


}
