package pt.uminho.ceb.biosystems.mew.biovisualizercore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.ClickPathway;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.InformationWithPathwaysPanelGT;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.LayoutVisualizerGUI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.BioVisualizerUtils;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IdMappingOverlapFactory;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.VisualizationProperties;

public class StandaloneVisualization{
	
	
	VisualizationProperties properties = new VisualizationProperties();
	private Map<String, Set<String>> metLayoutLinks = new TreeMap<>();
	private Map<String, Set<String>> reactionPathways = new TreeMap<>();
	private LayoutVisualizerGUI gui;
	private Map<String, Map<String, IOverlapObject>> overlaps = new HashMap<>();
	private Map<String, ILayout> layouts = new HashMap<>();
	JFrame frame;
	public StandaloneVisualization(Container cont) {
		
		
		InformationWithPathwaysPanelGT info = new InformationWithPathwaysPanelGT(metLayoutLinks, reactionPathways, cont);
		gui = new LayoutVisualizerGUI(cont, layouts, metLayoutLinks, overlaps, info, new IdMappingOverlapFactory(),properties);
		info.addPatwayListener(new ClickPathway(gui));
		
		frame = new JFrame("BioVisualizer StandAlone");
		frame.getContentPane().add(gui);
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	
	public void addLayout(String name, LayoutContainer layout) throws Exception{
		layouts.put(name, layout);
		BioVisualizerUtils.addLayoutToMapMetabolite(name, layout, metLayoutLinks);
		changeLayout(name);
		
		
	}
	
	public void changeLayout(String name){
		gui.changeLayout(name);
//		gui.updateInfo();
//		gui.updateUI();
	}
	
}
