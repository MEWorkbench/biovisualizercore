package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public class ReactionFactoryLabel extends IFactoryLabel<Container>{
	
	public ReactionFactoryLabel() {
		super();
		
		IMethodLabelFactory<Container> idMethod = new ReactionIdMethodLabelFactory();
		IMethodLabelFactory<Container> nameMethod = new ReactionNameMethodLabelFactory();
		IMethodLabelFactory<Container> ecNumberMethod = new ReactionEcNumberMethodLabelFactory();
		
		addLabelMethod(ReactionLabelMethodIDs.ID, idMethod);
		addLabelMethod(ReactionLabelMethodIDs.NAME, nameMethod);
		addLabelMethod(ReactionLabelMethodIDs.EC_NUMBER, ecNumberMethod);
	}

}
