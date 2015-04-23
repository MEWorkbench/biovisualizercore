package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.simulation;

import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.FluxValueMap;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.simulation.method.RoundedValueLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.simulation.method.ScientificValueLabel;

public class FluxValueLabelFactory extends IFactoryLabel<FluxValueMap>{
	
	public FluxValueLabelFactory() {
		super();
		addLabelMethod("Scientific Value", new ScientificValueLabel());
		addLabelMethod("Rounded Value", new RoundedValueLabel());
	}
	
}
