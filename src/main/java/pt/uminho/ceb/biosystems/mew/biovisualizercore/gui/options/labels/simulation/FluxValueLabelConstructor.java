package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.simulation;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.AbstractLabelsConstructor;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.FluxValueMap;

public class FluxValueLabelConstructor extends AbstractLabelsConstructor<FluxValueMap>{

	public FluxValueLabelConstructor() {
		super(new FluxValueLabelFactory());
	}

	
}
