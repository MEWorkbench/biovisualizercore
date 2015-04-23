package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ChangePathwayEvent;


public class ClickPathway implements IChangePathwayListener{

	
	private LayoutVisualizerGUI visualizer;
	
	public ClickPathway(LayoutVisualizerGUI visualizer){
		this.visualizer = visualizer;
	}
	
	@Override
	public void changePathway(ChangePathwayEvent e) {
		visualizer.changeLayout(e.getPathway(), e.getMetabolites());
		
	}

}
