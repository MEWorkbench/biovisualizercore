package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io.LoadOverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io.LoadOverlapListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io.LoadOverlapPanel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io.SaveOverlapPanel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapsListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.LayoutVisualizator;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.AbstractOverlapFactory;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.MultipleConditionsOverlap;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class OverlapPanel implements ActionListener, LoadOverlapListener {
	
	public static final String							SAVE_OVERLAP_ACTION_COMMAND		= "saveOverlapActionCommand";
	public static final String							LOAD_OVERLAP_ACTION_COMMAND		= "loadOverlapActionCommand";
	public static final String							CONDITIONS_COMBO_ACTION_COMMAND	= "conditionsComboActionCommand";
	
	public static final String							LOADED_OVERLAP_TYPE				= "Manually loaded";
	
	private JComboBox<String>							typeOfOverlapCombo;
	private JComboBox<String>							overCombo;
	private JComboBox<String>							conditionsCombo;
	private JCheckBox									currencyCheck;
	
	private JScrollPane									checkSchrollPanel;
	private JCheckBox									infFlux;
	
	private MultipleCheckPanel							visFiltersPanel;
	private JCheckBox									infoCheck;
	private Map<String, Map<String, IOverlapObject>>	overObjectMap;
	
	private LayoutVisualizator							lViz;
	private Container									container;
	
	private String										currentOverlapType				= "---";
	private String										currentOverlap					= "---";
	private JCheckBox									chckbxNewCheckBox;
	private AbstractOverlapFactory						conversionFactory;
	
	private JPanel										defaultFiltersPanel;
	
	private JPanel										overlapPanel;
	
	private LoadOverlapPanel							panel;
	private JButton										exportOverlapButton;
	
	private List<OverlapsListener>						listeners;
	
	public OverlapPanel(Container container, Map<String, Map<String, IOverlapObject>> overlaps, AbstractOverlapFactory factory) {
		super();
		
		this.overObjectMap = overlaps;
		
		if (overlaps == null) this.overObjectMap = new HashMap<String, Map<String, IOverlapObject>>();
		this.container = container;
		
		this.conversionFactory = factory;
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				defaultFiltersPanel = new JPanel();
				GridBagLayout defaultFilterLayout = new GridBagLayout();
				defaultFilterLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
				defaultFilterLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
				defaultFilterLayout.columnWeights = new double[] { 0.1 };
				defaultFilterLayout.columnWidths = new int[] { 7 };
				defaultFiltersPanel.setLayout(defaultFilterLayout);
				
				chckbxNewCheckBox = new JCheckBox("Metabolites in other pathways");
				chckbxNewCheckBox.setVisible(false);
				chckbxNewCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
				gbc_chckbxNewCheckBox.anchor = GridBagConstraints.WEST;
				gbc_chckbxNewCheckBox.gridwidth = 1;
				gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 0, 0);
				gbc_chckbxNewCheckBox.gridx = 0;
				gbc_chckbxNewCheckBox.gridy = 0;
				defaultFiltersPanel.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
				
				infoCheck = new JCheckBox();
				defaultFiltersPanel.add(infoCheck, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				infoCheck.setText("Hide Information Nodes");
				infoCheck.addActionListener(this);
				infoCheck.setActionCommand("INFORMATION");
				infoCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				
				currencyCheck = new JCheckBox();
				defaultFiltersPanel.add(currencyCheck, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				currencyCheck.setText("Hide currency nodes");
				currencyCheck.addActionListener(this);
				currencyCheck.setActionCommand("CURRENCY");
				currencyCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				
				infFlux = new JCheckBox();
				defaultFiltersPanel.add(infFlux, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				infFlux.setText("Hide reactions not in model");
				infFlux.addActionListener(this);
				infFlux.setActionCommand("INVCONT");
				infFlux.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				
				//				this.add(defaultFiltersPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
				
			}
			
			{
				
				overlapPanel = new JPanel();
				GridBagLayout overlapPanelLayout = new GridBagLayout();
				overlapPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
				overlapPanelLayout.rowHeights = new int[] { 0, 0, 0, 0 };
				overlapPanelLayout.columnWeights = new double[] { 0.1, 0.1 };
				overlapPanelLayout.columnWidths = new int[] { 7, 7 };
				overlapPanel.setLayout(overlapPanelLayout);
				
				typeOfOverlapCombo = new JComboBox<String>();
				typeOfOverlapCombo.addItem("---");
				overlapPanel.add(typeOfOverlapCombo, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 5, 3), 0, 0));
				
				for (String s : overObjectMap.keySet()) {
					typeOfOverlapCombo.addItem(s);
				}
				
				typeOfOverlapCombo.addActionListener(this);
				typeOfOverlapCombo.setActionCommand("TYPEOFOVER");
				
				overCombo = new JComboBox<String>();
				if (overCombo.getItemCount() == 0) overCombo.addItem("---");
				overlapPanel.add(overCombo, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 5, 3), 0, 0));
				
				overCombo.addActionListener(this);
				overCombo.setActionCommand("OVERCOMBO");
				
				conditionsCombo = new JComboBox<String>();
				conditionsCombo.setEnabled(false);
				overlapPanel.add(conditionsCombo, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 5, 3), 0, 0));
				
				conditionsCombo.addActionListener(this);
				conditionsCombo.setActionCommand(CONDITIONS_COMBO_ACTION_COMMAND);
				
				checkSchrollPanel = new JScrollPane();
				overlapPanel.add(checkSchrollPanel, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
				checkSchrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "Visual Filters", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				
				visFiltersPanel = new MultipleCheckPanel(new HashSet<String>(), this);
				checkSchrollPanel.setViewportView(visFiltersPanel);
				
				exportOverlapButton = new JButton("save...");
				exportOverlapButton.setEnabled(false);
				overlapPanel.add(exportOverlapButton, new GridBagConstraints(0, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 5, 3), 0, 0));
				exportOverlapButton.addActionListener(this);
				exportOverlapButton.setActionCommand(SAVE_OVERLAP_ACTION_COMMAND);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addOverlapListener(OverlapsListener listener) {
		if (listeners == null) listeners = new ArrayList<OverlapsListener>();
		listeners.add(listener);
	}
	
	/**
	 * Method used when the visualized graph changes. This is necessary
	 * so the overlaps are applied to the current {@link LayoutVisualizator}.
	 * 
	 * @param lViz
	 */
	public void setLayoutVisualizator(LayoutVisualizator lViz) {
		this.lViz = lViz;
		rerunOverlap();
		runFilters();
	}
	
	/**
	 * Returns the overlap category.
	 * 
	 * @return overlap type
	 */
	private String getTypeOverlap() {
		return (String) typeOfOverlapCombo.getSelectedItem();
	}
	
	/**
	 * Returns the specific overlap.
	 * 
	 * @return overlap
	 */
	private String getOverlap() {
		return (String) overCombo.getSelectedItem();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//FOR VISUAL FILTERS//
		if (e.getActionCommand().equalsIgnoreCase("Check selected") || e.getActionCommand().equals("INVCONT")) {
			
			runFilters();
		}
		
		//HIDE INFORMATION//
		if (e.getActionCommand().equals("INFORMATION")) {
			
			if (infoCheck.isSelected()) {
				lViz.addGroupVisualFilter(LayoutUtils.INFO);
			} else {
				lViz.removeGroupVisualFilter(LayoutUtils.INFO);
			}
		}
		
		//HIDE CURRENCY//
		if (e.getActionCommand().equals("CURRENCY")) {
			if (currencyCheck.isSelected()) {
				lViz.addGroupVisualFilter(LayoutUtils.HUBS);
			} else {
				lViz.removeGroupVisualFilter(LayoutUtils.HUBS);
			}
		}
		
		//TYPE OF OVERLAP CHANGED//
		if (e.getActionCommand().equals("TYPEOFOVER")) {
			
			if (getTypeOverlap() != null && !currentOverlapType.equals(getTypeOverlap())) {
				if (getTypeOverlap().equals("---")) {
					overCombo.removeAllItems();
					overCombo.addItem("---");
					//				reset overlaps.
					currentOverlapType = "---";
					conditionsCombo.setEditable(false);
					resetOverlap();
					fireSelectedOverlapChanged();
				} else {
					currentOverlapType = getTypeOverlap();
					Set<String> overlapsByType = overObjectMap.get(currentOverlapType).keySet();
					overCombo.removeAllItems();
					overCombo.addItem("---");
					conditionsCombo.removeAllItems();
					conditionsCombo.setEditable(false);
					currentOverlap = "---";
					
					for (String s : overlapsByType)
						overCombo.addItem(s);
					
					resetOverlap();
					fireSelectedOverlapChanged();
				}
				
			}
		}
		
		//OVERLAP CHOSEN//
		if (e.getActionCommand().equals("OVERCOMBO")) {
			
			if (getOverlap() != null && !currentOverlap.equals(getOverlap())) {
				
				if (getOverlap().equals("---")) {
					
					currentOverlap = "---";
					visFiltersPanel = new MultipleCheckPanel(new HashSet<String>(), this);
					//reset overlaps
					resetOverlap();
					visFiltersPanel = new MultipleCheckPanel(new HashSet<String>(), this);
					checkSchrollPanel.setViewportView(visFiltersPanel);
					exportOverlapButton.setEnabled(false);
					conditionsCombo.setEnabled(false);
					fireSelectedOverlapChanged();
				} else {
					
					String overlap = getOverlap();
					IOverlapObject overlapObj = overObjectMap.get(getTypeOverlap()).get(overlap);
					if (overlapObj instanceof MultipleConditionsOverlap) {
						conditionsCombo.removeAllItems();
						for (String s : ((MultipleConditionsOverlap) overlapObj).get_conditions().keySet())
							conditionsCombo.addItem(s);
						conditionsCombo.setEnabled(true);
						conditionsCombo.setSelectedIndex(0);
						((MultipleConditionsOverlap) overlapObj).set_selectedCondition((String) conditionsCombo.getSelectedItem());
					} else {
						conditionsCombo.removeAllItems();
						conditionsCombo.setEnabled(false);
					}
					//resetOverlaps
					resetOverlap();
					
					//Run overlap!
					
					runOverlap(overlapObj);
					currentOverlap = overlap;
					exportOverlapButton.setEnabled(true);
					fireSelectedOverlapChanged();
				}
				
			}
		}
		
		if (e.getActionCommand().equals(CONDITIONS_COMBO_ACTION_COMMAND)) {
			
			if (conditionsCombo != null && conditionsCombo.getItemCount() > 0) {
				String condition = (String) conditionsCombo.getSelectedItem();
				if (condition != null && !condition.isEmpty()) {
					String overlap = getOverlap();
					IOverlapObject overlapObj = overObjectMap.get(getTypeOverlap()).get(overlap);
					
					if (overlapObj != null && overlapObj instanceof MultipleConditionsOverlap) {
						((MultipleConditionsOverlap) overlapObj).set_selectedCondition(condition);
						
						resetOverlap();						
						runOverlap(overlapObj);
						currentOverlap = overlap;
						exportOverlapButton.setEnabled(true);
						fireSelectedOverlapChanged();
					}
				}
			}
		}
		
		//SAVE OVERLAP CHOSEN //
		if (e.getActionCommand().equals(SAVE_OVERLAP_ACTION_COMMAND)) {
			
			if (getOverlap() != null) {
				
				String overlap = getOverlap();
				
				IOverlapObject overlapObj = overObjectMap.get(getTypeOverlap()).get(overlap);
				SaveOverlapPanel panel = new SaveOverlapPanel(overlapObj);
				panel.display();
			}
		}
		
		//LOAD OVERLAP CHOSEN //
		if (e.getActionCommand().equals(LOAD_OVERLAP_ACTION_COMMAND)) {
			
			if (getOverlap() != null) {
				
				Set<String> overlapNames = overObjectMap.containsKey(LOADED_OVERLAP_TYPE) ? overObjectMap.get("Manually loaded").keySet() : new HashSet<String>();
				
				panel = new LoadOverlapPanel(overlapNames);
				panel.addLoadOverlapListener(this);
				panel.display();
			}
		}
	}
	
	public void fireSelectedOverlapChanged() {
		
		IOverlapObject overlap = null;
		if (currentOverlap.equals("---") || currentOverlapType.equals("---"))
			overlap = null;
		else {
			overlap = overObjectMap.get(currentOverlapType).get(currentOverlap);
		}
		
		for (OverlapsListener listener : listeners)
			listener.selectedOverlapChanged(new OverlapEvent(this, overlap));
	}
	
	@Override
	public void loadOverLap(LoadOverlapEvent event) {
		if (event != null) {
			IOverlapObject overlapObj = event.get_overlapObject();
			addOverLap("Manually loaded", overlapObj);
		}
	}
	
	/**
	 * Runs a overlap in the visualization.
	 * 
	 * @param overlapObj
	 */
	public void runOverlap(IOverlapObject overlapObj) {
				
		IOverlapObject convertedOverlap = conversionFactory.convertOverlapWithMappings(overlapObj, this.lViz.getLayoutContainer());
		
		lViz.setNewReactionLabels(convertedOverlap.getNewReactionLabels());		
		lViz.setNewNodeLabels(convertedOverlap.getNewNodeLabels());
		
		lViz.setNewEdgeThickness(convertedOverlap.getEdgeThickness());
		lViz.setArrowShapesByDirection(convertedOverlap.getFluxDirections());
		
		lViz.overLapSpecialColorsAndShapes(convertedOverlap.getNodeColors(), convertedOverlap.getNodeShapes(), convertedOverlap.getReactionsColors(), convertedOverlap.getReactionShapes());
		
		lViz.addNodeSizes(convertedOverlap.getNodeSizes());
		lViz.addReactionSizes(convertedOverlap.getReactionSizes());
		
		//Set Invisible sets
		Set<String> invisibleSets = convertedOverlap.getPossibleInvR().keySet();
		visFiltersPanel = new MultipleCheckPanel(invisibleSets, this);
		checkSchrollPanel.setViewportView(visFiltersPanel);
		
	}
	
	/**
	 * Reruns an overlap. First the overlap is reseted, and
	 * after that runs again. This is used for cases when the graph is
	 * changed, for instance, when a node is replicated. The overlap is running
	 * according to the "previous" graph, so there is need to reset and run the
	 * overlap again.
	 */
	public void rerunOverlap() {
		
		String overlapType = getTypeOverlap();
		String overlap = getOverlap();
		Set<String> checks = visFiltersPanel.getSelectedChecks();
		
		if (!(overlapType.equals("---")) && !(overlap.equals("---"))) {
			
			resetOverlap();
			
			IOverlapObject overlapObj = overObjectMap.get(getTypeOverlap()).get(overlap);
			runOverlap(overlapObj);
			
			for (String s : checks)
				visFiltersPanel.setSelected(s, true);
			
			runFilters();
		}
	}
	
	/**
	 * Applies the selected visualization filters. All filters are converted
	 * for the visualization identifiers.
	 */
	private void runFilters() {
		
		Set<String> activeInvFilters = visFiltersPanel.getSelectedChecks();
		
		//Run invisible filters
		lViz.removeAllReactionsFilter();
		Set<String> invisibleReactions = new HashSet<String>();
		for (String s : activeInvFilters) {
			invisibleReactions.addAll(overObjectMap.get(getTypeOverlap()).get(getOverlap()).getPossibleInvR().get(s));
		}
		
		Set<String> convertedIR = conversionFactory.convertVisibilityReactions(invisibleReactions, this.lViz.getLayoutContainer());
		
		if (infFlux.isSelected()) {
			convertedIR.addAll(LayoutUtils.getInvisibilityFromContainer(lViz.getLayoutContainer(), container));
			System.out.println("REACTIONS NOT MAPPED: " + LayoutUtils.getInvisibilityFromContainer(lViz.getLayoutContainer(), container));
		}
		lViz.addInvisibleReactions(convertedIR);
	}
	
	/**
	 * Resets an overlap.
	 */
	public void resetOverlap() {
		
		lViz.resetReactionLabels();
		lViz.resetNodeLabels();
		
		lViz.resetEdgesThickness();
		
		lViz.resetArrowShapes();
		
		lViz.resetSpecialColorsAndShappes();
		
		lViz.resetNodeSizes();
		
		lViz.removeAllReactionsFilter();
	}
	
	/**
	 * Adds a overlap to the interface. If the category of overlaps
	 * does not exist, it creates a new one.
	 * 
	 * @param type
	 * @param simOverlap
	 */
	public void addOverLap(String type, IOverlapObject simOverlap) {
		
		if (!this.overObjectMap.containsKey(type)) {
			Map<String, IOverlapObject> overlaps = new HashMap<String, IOverlapObject>();
			overlaps.put(simOverlap.getName(), simOverlap);
			this.overObjectMap.put(type, overlaps);
			typeOfOverlapCombo.addItem(type);
			
		} else {
			
			overObjectMap.get(type).put(simOverlap.getName(), simOverlap);
			if (getTypeOverlap().equalsIgnoreCase(type)) {
				overCombo.addItem(simOverlap.getName());
			}
		}
		
	}
	
	/**
	 * Sets the selected pathways.
	 * 
	 * @param b
	 */
	public void selectedPathways(boolean b) {
		
		chckbxNewCheckBox.setSelected(b);
	}
	
	/**
	 * Adds listeners to the pathways interface.
	 * 
	 * @param l
	 */
	public void addListnerInPathway(ActionListener l) {
		chckbxNewCheckBox.setVisible(true);
		chckbxNewCheckBox.addActionListener(l);
	}
	
	/**
	 * Removes an overlap from the list
	 * 
	 * @param type
	 * @param name
	 */
	public void removeOverlap(String type, String name) {
		
		if(overObjectMap.containsKey(type) && overObjectMap.get(type).containsKey(name)){
			overObjectMap.get(type).remove(name);
			if(overObjectMap.get(type).size() == 0){
				overObjectMap.remove(type);
				typeOfOverlapCombo.setSelectedIndex(0);
				((DefaultComboBoxModel<String>)typeOfOverlapCombo.getModel()).removeElement(type);
			}else{
				Set<String> overlapsByType = overObjectMap.get(type).keySet();
				overCombo.removeAllItems();
				overCombo.addItem("---");
				for (String item : overlapsByType) {
					overCombo.addItem(item);
				}
			}
		}
		
	}
	
	public JPanel getDefaultFiltersPanel() {
		return defaultFiltersPanel;
	}
	
	public JPanel getOverlapPanel() {
		return overlapPanel;
	}
	
	public void setVisibleInfoFilter(boolean b) {
		this.infoCheck.setVisible(b);
	}
	
	public void updateComboBoxes(String itemName, String itemPreviousName, String itemType){
		
		if(overObjectMap.containsKey(itemType) && overObjectMap.get(itemType).containsKey(itemPreviousName)){
			// Update overObjectMap
			overObjectMap.get(itemType).put(itemName, overObjectMap.get(itemType).get(itemPreviousName));
			overObjectMap.get(itemType).remove(itemPreviousName);
			
			// Update combobox
			if(typeOfOverlapCombo.getSelectedItem().equals(itemType)){
				((DefaultComboBoxModel<String>)overCombo.getModel()).addElement(itemName);
				if(overCombo.getSelectedItem().equals(itemPreviousName))
					overCombo.setSelectedItem(itemName);
				((DefaultComboBoxModel<String>)overCombo.getModel()).removeElement(itemPreviousName);
			}
		}
	}
}
