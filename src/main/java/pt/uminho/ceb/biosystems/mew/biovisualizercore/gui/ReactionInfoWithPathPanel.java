package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ChangePathwayEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapsListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

public class ReactionInfoWithPathPanel extends JPanel implements TableModelListener, IReactionInfo, OverlapsListener {
	
	private static final long			serialVersionUID	= 1L;
	
	private JLabel						formulaLabel;
	private JTextField					layoutLabelText;
	private Map<String, Set<String>>	pathways;
	
	private boolean						populatetable		= false;
	
	private IChangePathwayListener		changePathListenerSet;
	
	private Set<String>					reactionIds			= null;
	
	private Container					container			= null;
	private List<ReactionListener>		reactionListeners	= null;
	private List<OverlapsListener>		overlapListeners	= null;
	
	public ReactionInfoWithPathPanel(Map<String, Set<String>> pathways) {
		this(pathways, null);
	}
	
	public ReactionInfoWithPathPanel(Map<String, Set<String>> pathways, Container container) {
		super();
		this.pathways = pathways;
		this.container = container;
		
		initGUI();
	}
	
	public void addReactionListener(ReactionListener listener) {
		if (listener != null) {
			if (reactionListeners == null) reactionListeners = new ArrayList<ReactionListener>();
			reactionListeners.add(listener);
		}
	}
	
	public void addOverlapsListener(OverlapsListener listener) {
		if (listener != null) {
			if (overlapListeners == null) overlapListeners = new ArrayList<OverlapsListener>();
			overlapListeners.add(listener);
		}
	}

	private void initGUI() {
		try {
			setLayout(new BorderLayout());
			GridBagLayout panelLayout = new GridBagLayout();
			panelLayout.rowWeights = new double[] { 1.0 };
			panelLayout.columnWeights = new double[] { 0.2, 0.8 };
			JPanel labelsPanel = new JPanel();
			labelsPanel.setLayout(panelLayout);
			{
				formulaLabel = new JLabel();
				GridBagConstraints formulaLabelConstraints = new GridBagConstraints();
				formulaLabelConstraints.fill = GridBagConstraints.BOTH;
				formulaLabelConstraints.gridx = 0;
				formulaLabelConstraints.gridy = 0;
				formulaLabelConstraints.gridheight = GridBagConstraints.REMAINDER;
				formulaLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
				labelsPanel.add(formulaLabel, formulaLabelConstraints);
				formulaLabel.setText("Label");
			}
			
			{
				layoutLabelText = new JTextField();
				GridBagConstraints layoutLabelConstraints = new GridBagConstraints();
				layoutLabelConstraints.fill = GridBagConstraints.BOTH;
				layoutLabelConstraints.gridx = 1;
				layoutLabelConstraints.gridy = 0;
				layoutLabelConstraints.gridheight = GridBagConstraints.REMAINDER;
				layoutLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
				labelsPanel.add(layoutLabelText,layoutLabelConstraints);
				layoutLabelText.setEditable(false);
			}
			
			add(labelsPanel,BorderLayout.NORTH);
			
			createExtraInfoPanel();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createExtraInfoPanel() {
		
		ReactionExtraInfoTabbedPanel tabbedPane = new ReactionExtraInfoTabbedPanel<>();
		
		if (pathways != null && !pathways.isEmpty()) {
			ReactionsPathwayNavigationPanel pathwayPanel = new ReactionsPathwayNavigationPanel(pathways);
			pathwayPanel.setTableModelListener(this);
			addReactionListener(pathwayPanel);
			tabbedPane.addComponent("Navigation", pathwayPanel);
		}
		
		if (container != null && container.getReactionsExtraInfo().containsKey(KeggImagePanel.KEGG_REACTION_ID)) {
			KeggImagePanel keggPanel = new KeggImagePanel(container, true);
			addReactionListener(keggPanel);
			tabbedPane.addComponent("Kegg", keggPanel);
		}
		
		MultipleConditionsReactionPlotPanel plotPanel = new MultipleConditionsReactionPlotPanel();
		addOverlapsListener(plotPanel);
		addReactionListener(plotPanel);
		tabbedPane.addComponent("Data", plotPanel);
		
		tabbedPane.populateTabs();
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void fireReactionEvent() {
		ReactionEvent event = new ReactionEvent(this, reactionIds);
		for (ReactionListener listener : reactionListeners)
			listener.reactionChanged(event);
	}
	
	@Override
	public void selectedOverlapChanged(OverlapEvent event) {
		fireOverlapsChangedEvent(event);
	}
	
	private void fireOverlapsChangedEvent(OverlapEvent event) {
		for (OverlapsListener listener : overlapListeners)
			listener.selectedOverlapChanged(event);
	}
	
	public void addChangePathListener(IChangePathwayListener listener) {
		changePathListenerSet = listener;
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		DefaultTableModel dft = (DefaultTableModel) e.getSource();
		String path = (String) dft.getValueAt(e.getFirstRow(), 0);
		if (!populatetable) {
			notifyPathWayListener(path, reactionIds);
		}
	}
	
	private void notifyPathWayListener(String path, Set<String> reactionIDs) {
		ChangePathwayEvent e = new ChangePathwayEvent(path, reactionIDs);
		changePathListenerSet.changePathway(e);
	}
	
	@Override
	public void updateInfo() {
		
	}
	
	@Override
	public void putReactionInfo(IReactionLay nodeLay) {
		reactionIds = nodeLay.getIDs();
		layoutLabelText.setText(nodeLay.getLabel());
		fireReactionEvent();
	}
}
