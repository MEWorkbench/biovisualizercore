package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;

public class ReactionNameMethodLabelFactory extends AbstractReactionMethodLabelFactory{

	@Override
	public String getInfo(Container container, String reactionId) {
		ReactionCI r = getReaction(container, reactionId);
		return (r == null) ? reactionId : r.getName();
	}

}
