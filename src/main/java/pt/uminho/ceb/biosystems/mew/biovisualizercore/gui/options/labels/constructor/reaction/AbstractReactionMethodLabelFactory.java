package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public abstract class AbstractReactionMethodLabelFactory implements IMethodLabelFactory<Container>{

	public ReactionCI getReaction(Container container, String reactionId){
		return (container.getReactions().containsKey(reactionId)) ? container.getReaction(reactionId) : null;
	}

}
