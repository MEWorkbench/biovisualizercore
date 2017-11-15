package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.images;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.ReactionListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ReactionEvent;

public class ReactionImageListener extends ImagePanel implements ReactionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReactionImageListener(IImageProvider provider) {
		super(provider);
	}

	@Override
	public void reactionChanged(ReactionEvent event) {
		populateImages(event.getIds());
		
	}

}
