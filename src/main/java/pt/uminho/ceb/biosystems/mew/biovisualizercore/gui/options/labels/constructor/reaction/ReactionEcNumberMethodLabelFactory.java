package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;

public class ReactionEcNumberMethodLabelFactory extends AbstractReactionMethodLabelFactory{

	@Override
	public String getInfo(Container container, String reactionId) {
		ReactionCI r = getReaction(container, reactionId);
		
		String ec = r.getEcNumber();
		return ec;
		
//		Set<String> ecs =  r.getEcNumbers();
//		if(ecs!=null)
//		{	
//			Iterator<String> it = ecs.iterator();
//			String ec = it.next(); 
//			while(it.hasNext())
//				ec += "," + it.next();
//			return ec;
//		}
//		return null;
	}

}
