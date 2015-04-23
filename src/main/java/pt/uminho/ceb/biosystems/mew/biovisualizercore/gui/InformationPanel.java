package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

/**
 * Panel that displays information of clicked nodes on the visualization.
 * @author Alberto Noronha
 *
 */
public class InformationPanel extends AbstractInformationPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel currentPanel;
	
	private MetaboliteInfoPanel metPanel;
	private ReactionInfoPanel reactPanel;
	
	private JScrollPane infoPane;

	public InformationPanel() {
		super();

		this.metPanel = new MetaboliteInfoPanel();
		this.reactPanel = new ReactionInfoPanel();
		
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.0, 0.1, 0.1, 0.0, 0.0, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.1};
			thisLayout.columnWidths = new int[] {7, 7};
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			{
				currentPanel = new JPanel();
				infoPane = new JScrollPane(currentPanel); 
				this.add(infoPane, new GridBagConstraints(0, 0, 2, 8, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void showNodeInformation(INodeLay node) {
//		Set<String> ids = node.getIds();

		Set<String> reactions = new HashSet<String>();
//		for(String s : ids){
//			if(metabolites.containsKey(s)){
//				MetaboliteCI metabolite = this.metabolites.get(s);
//				reactions.addAll(metabolite.getReactionsId());
//			}
//		}

		this.infoPane.setViewportView(metPanel);
		metPanel.putMetaboliteInfo(node);
		
	}

	@Override
	public void showReactionInformation(IReactionLay reaction) {
		
		this.infoPane.setViewportView(reactPanel);
		reactPanel.putReactionInfo(reaction);
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectedOverlapChanged(OverlapEvent event) {
		
	}
}
