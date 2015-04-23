package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public class MetaboliteFactoryLabel extends IFactoryLabel<Container>{
	
	public MetaboliteFactoryLabel() {
		super();
		
		IMethodLabelFactory<Container> idMethod = new MetIdMethodLabelFactory();
		IMethodLabelFactory<Container> nameMethod = new MetNameMethodLabelFactory();
		IMethodLabelFactory<Container> formulaMethod = new MetFormulaMethodLabelFactory();
		
		addLabelMethod(MetLabelMethodIDs.ID, idMethod);
		addLabelMethod(MetLabelMethodIDs.NAME, nameMethod);
		addLabelMethod(MetLabelMethodIDs.FORMULA, formulaMethod);
	}

}
