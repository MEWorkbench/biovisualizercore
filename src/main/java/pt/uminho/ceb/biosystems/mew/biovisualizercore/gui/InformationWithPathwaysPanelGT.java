package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.CardLayout;
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
public class InformationWithPathwaysPanelGT extends AbstractInformationPanel {
	
	private static final long				serialVersionUID				= 1L;
	
	private static final String				METABOLITES_INFORMATION_PANEL	= "metabolitesInformationPanel";
	private static final String				REACTIONS_INFORMATION_PANEL		= "reactionsInformationPanel";
	
	private MetaboliteInfoWithPathPanel2	metPanel;
	private ReactionInfoWithPathPanel		reactPanel;
	
	public InformationWithPathwaysPanelGT(Map<String, Set<String>> pathwaysMetabolites, Map<String, Set<String>> pathwaysReactions) {
		this(pathwaysMetabolites, pathwaysReactions, null);
	}
	
	public InformationWithPathwaysPanelGT(Map<String, Set<String>> pathwaysMetabolites, Map<String, Set<String>> pathwaysReactions, Container container) {
		super();
		metPanel = new MetaboliteInfoWithPathPanel2(pathwaysMetabolites, container);
		reactPanel = new ReactionInfoWithPathPanel(pathwaysReactions, container);
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setLayout(new CardLayout());
			
			this.add(metPanel, METABOLITES_INFORMATION_PANEL);
			this.add(reactPanel, REACTIONS_INFORMATION_PANEL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void showNodeInformation(INodeLay node) {
		((CardLayout) getLayout()).show(this, METABOLITES_INFORMATION_PANEL);
		metPanel.putMetaboliteInfo(node);
	}
	
	public void clear() {
	}
	
	@Override
	public void showReactionInformation(IReactionLay reaction) {
		((CardLayout) getLayout()).show(this, REACTIONS_INFORMATION_PANEL);
		reactPanel.putReactionInfo(reaction);
	}
	
	public void addPatwayListener(IChangePathwayListener clickP) {
		metPanel.addChangePathListener(clickP);
		reactPanel.addChangePathListener(clickP);
	}
	
	@Override
	public void updateInfo() {
		metPanel.updateInfo();
		reactPanel.updateInfo();
	}
	
	@Override
	public void selectedOverlapChanged(OverlapEvent event) {
		metPanel.selectedOverlapChanged(event);
		reactPanel.selectedOverlapChanged(event);
	}
	
	public  MetaboliteInfoWithPathPanel2 getMetabolitesInformationPanel() {
		return metPanel;
	}
	
	public ReactionInfoWithPathPanel getReactionsInformationPanel() {
		return reactPanel;
	}
}
