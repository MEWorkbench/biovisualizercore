package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

public class ReactionIdMethodLabelFactory extends AbstractReactionMethodLabelFactory{

	@Override
	public String getInfo(Container container, String reactionId) {
		return reactionId;
	}

}
