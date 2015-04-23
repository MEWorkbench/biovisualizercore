package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;

public class MetFormulaMethodLabelFactory extends AbstractMetMethodLabelFactory{

	@Override
	public String getInfo(Container container, String metaboliteId) {
		MetaboliteCI met = getMetabolite(container, metaboliteId);
		return (met == null) ? metaboliteId : met.getFormula();
	}

}
