package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

/**
 * Panel that displays information of clicked nodes on the visualization.
 * 
 * @author Alberto Noronha
 * 
 */
public class InformationWithPathwaysPanel extends AbstractInformationPanel {
	
	private static final long			serialVersionUID	= 1L;
	
	private MetaboliteInfoWithPathPanel	metPanel;
	private ReactionInfoPanel			reactPanel;
	
	public InformationWithPathwaysPanel(Map<String, Set<String>> pathways) {
		this(pathways, null);
	}
	
	public InformationWithPathwaysPanel(Map<String, Set<String>> pathways, Container container) {
		super();		
		metPanel = new MetaboliteInfoWithPathPanel(pathways, container);	
		reactPanel = new ReactionInfoPanel();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] { 1.0, 1.0 };
			thisLayout.rowHeights = new int[] { 0, 0 };
			thisLayout.columnWeights = new double[] { 1.0 };
			thisLayout.columnWidths = new int[] { 0 };
			this.setLayout(thisLayout);
			
			this.add(metPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(reactPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			
			clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void showNodeInformation(INodeLay node) {
		clear();
		metPanel.setVisible(true);
		metPanel.putMetaboliteInfo(node);
		updateUI();
	}
	
	public void clear() {
		metPanel.setVisible(false);
		reactPanel.setVisible(false);
		updateUI();
	}
	
	@Override
	public void showReactionInformation(IReactionLay reaction) {
		clear();
		reactPanel.setVisible(true);
		reactPanel.putReactionInfo(reaction);
		updateUI();
	}
	
	public void addPatwayListener(IChangePathwayListener clickP) {
		metPanel.addChangePathListener(clickP);
	}
	
	@Override
	public void updateInfo() {
		metPanel.updateInfo();
		reactPanel.updateInfo();
	}

	@Override
	public void selectedOverlapChanged(OverlapEvent event) {
		metPanel.selectedOverlapChanged(event);
	}
}
