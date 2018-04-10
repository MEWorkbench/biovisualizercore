package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners;

import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

public class ReactionEvent extends MetabolicComponentEvent {

	private static final long	serialVersionUID	= 1L;
	public ReactionEvent(Object source,String uuid, Set<String> ids,String label, Container information) {
		super(source, uuid, ids, label, information);
	}

	
}
