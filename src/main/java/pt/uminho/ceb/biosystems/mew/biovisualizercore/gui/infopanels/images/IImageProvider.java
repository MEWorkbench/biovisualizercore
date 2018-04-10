package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetabolicComponentEvent;

public interface IImageProvider {

	BufferedImage getImage(String source) throws IOException;
	
	String[] getNames(String source);
	
}
