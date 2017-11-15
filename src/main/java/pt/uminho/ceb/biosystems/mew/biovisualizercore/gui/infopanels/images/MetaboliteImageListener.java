package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.images;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteListener;

public class MetaboliteImageListener extends ImagePanel implements MetaboliteListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetaboliteImageListener(IImageProvider provider) {
		super(provider);
	}

	@Override
	public void metaboliteChanged(MetaboliteEvent event) {
		populateImages(event.getIds());
	}

}
