package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.kegg.KeggCompoundImagePanel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.multipleconditions.MultipleConditionsPlotPanel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.pathwaynavigation.PathwayNavigationPanel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ChangePathwayEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapsListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;

/**
 * Metabolite information panel, that appears in the {@link InformationPanel}
 * 
 * @author Alberto Noronha
 * 
 */
public class MetaboliteInfoWithPathPanel extends JPanel implements TableModelListener, IMetaboliteInfo, OverlapsListener {
	
	private static final long			serialVersionUID	= 1L;
	
	private JLabel						formulaLabel;
	private JTextField					layoutLabelText;
	private Map<String, Set<String>>	pathways;
	
	private boolean						populatetable		= false;
	
	private IChangePathwayListener		changePathListenerSet;
	
	private Set<String>					metabolitesIds		= null;
	
	private Container					container			= null;
	private List<MetaboliteListener>	metaboliteListeners	= null;
	private List<OverlapsListener>		overlapListeners	= null;
	
	private MetaboliteExtraInfoTabbedPanel tabbedPane =null;
	
	public MetaboliteInfoWithPathPanel(Map<String, Set<String>> pathways) {
		this(pathways, null);
	}
	
	public MetaboliteInfoWithPathPanel(Map<String, Set<String>> pathways, Container container) {
		super();
		this.pathways = pathways;
		this.container = container;
		
		initGUI();
	}
	
	public void addMetaboliteListener(MetaboliteListener listener) {
		if (listener != null) {
			if (metaboliteListeners == null) metaboliteListeners = new ArrayList<MetaboliteListener>();
			metaboliteListeners.add(listener);
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
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] { 0.0, 0.1 };
			thisLayout.rowHeights = new int[] { 0, 0 };
			thisLayout.columnWeights = new double[] { 0.0, 0.1};
			thisLayout.columnWidths = new int[] { 7, 7 };
			this.setLayout(thisLayout);
			{
				formulaLabel = new JLabel();
				this.add(formulaLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				formulaLabel.setText("Label");
			}
			
			{
				layoutLabelText = new JTextField();
				this.add(layoutLabelText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				layoutLabelText.setEditable(false);
			}
			
			createExtraInfoPanel();
			
			updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createExtraInfoPanel() {
		
		MetaboliteExtraInfoTabbedPanel tabbedPane = new MetaboliteExtraInfoTabbedPanel<>();
		GridBagConstraints tabbedPaneConstraints = new GridBagConstraints();
		tabbedPaneConstraints.fill = GridBagConstraints.BOTH;
		tabbedPaneConstraints.gridx = 0;
		tabbedPaneConstraints.gridy = 1;
		tabbedPaneConstraints.gridheight = GridBagConstraints.REMAINDER;
		tabbedPaneConstraints.gridwidth= GridBagConstraints.REMAINDER;
		
		if(pathways!=null && !pathways.isEmpty()){
			PathwayNavigationPanel pathwayPanel = new PathwayNavigationPanel(pathways);
			pathwayPanel.setTableModelListener(this);
			addMetaboliteListener(pathwayPanel);
			tabbedPane.addComponent("Navigation", pathwayPanel);
		}
		
		if (container != null && container.getMetabolitesExtraInfo().containsKey(KeggCompoundImagePanel.KEGG_COMPOUND_ID)) {
			KeggCompoundImagePanel keggPanel = new KeggCompoundImagePanel(container);
			addMetaboliteListener(keggPanel);
			tabbedPane.addComponent("Kegg", keggPanel);
		}
		
		MultipleConditionsPlotPanel plotPanel = new MultipleConditionsPlotPanel();
		addOverlapsListener(plotPanel);
		addMetaboliteListener(plotPanel);
		tabbedPane.addComponent("Data", plotPanel);
		
		tabbedPane.populateTabs();
		add(tabbedPane, tabbedPaneConstraints);
	}
	
	
//	public void addExtraInformationPanel(MetaboliteListener metaboliteListener){
//		addMetaboliteListener(keggPanel);
//	}
//	
	
	/**
	 * Method that displays the metabolite information.
	 * 
	 * @param nodeLay
	 * @param reactions
	 */
	public void putMetaboliteInfo(INodeLay nodeLay) {
		metabolitesIds = nodeLay.getIds();
		layoutLabelText.setText(nodeLay.getLabel());
//		updateInfo();
		fireMetaboliteEvent(nodeLay.getUniqueId(), metabolitesIds, nodeLay.getLabel());
	}
	
	public void fireMetaboliteEvent(String uuid, Set<String> mids, String label) {
		MetaboliteEvent event = new MetaboliteEvent(this,uuid, mids, label, container);
		for (MetaboliteListener listener : metaboliteListeners)
			listener.metaboliteChanged(event);
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
		if (!populatetable) notifyPathWayListener(path, metabolitesIds);
	}
	
	private void notifyPathWayListener(String path, Set<String> metabolitesIds) {
		ChangePathwayEvent e = new ChangePathwayEvent(path, metabolitesIds);
		changePathListenerSet.changePathway(e);
	}
	
	@Override
	public void updateInfo() {
		
//		if (metabolitesIds != null) {
//			populatetable = true;
//			String[] t = { "Model Id" };
//			DefaultTableModel tm = new DefaultTableModel(t, 0) {
//				private static final long	serialVersionUID	= -8738557545638331175L;
//				
//				@Override
//				public boolean isCellEditable(int row, int column) {
//					if (column == 0) return false;
//					return true;
//				}
//			};
//			;
//			
//			for (String s : metabolitesIds) {
//				String[] row = new String[1];
//				row[0] = s;
//				tm.addRow(row);
//			}
//			
//			this.table.setModel(tm);
		
			
//			Set<String> pathwaysSet = new HashSet<String>();
//			
//			String[] t2 = { "Pathway", "" };
//			DefaultTableModel tm2 = new DefaultTableModel() {
//				private static final long	serialVersionUID	= -329979760397003009L;
//				
//				@Override
//				public boolean isCellEditable(int row, int column) {
//					if (column == 0) return false;
//					return true;
//				}
//			};
//			tm2.setColumnIdentifiers(t2);
//			
//			for (String id : metabolitesIds) {
//				//				System.out.println(metabolitesIds + "\t" + pathways.get(id));
//				if (pathways.get(id) != null) {
//					for (String path : pathways.get(id)) {
//						
//						if (!pathwaysSet.contains(path)) {
//							pathwaysSet.add(path);
//							String[] row = new String[2];
//							row[0] = path;
//							tm2.addRow(row);
//						}
//					}
//				}
//			}
//			
//			table_1.getModel().removeTableModelListener(this);
//			
//			this.table_1.setModel(tm2);
//			ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/layout.png"));
//			JTableUtils.setCellRender(table_1, 1, new ButtonRenderer(icon));
//			JTableUtils.setCellEditor(table_1, 1, new ButtonEditor(icon));
//			JTableUtils.setColumnWidth(table_1, 1, 30);
//			JTableUtils.setRowHeight(table_1, 30);
//			
//			tm2.addTableModelListener(this);
//			
//			populatetable = false;
//			
//			table.setPreferredScrollableViewportSize(table.getMinimumSize());
//			table.setFillsViewportHeight(true);
//			table_1.setPreferredScrollableViewportSize(table_1.getMinimumSize());
//			table_1.setFillsViewportHeight(false);
//			this.updateUI();
//			
//			//			System.out.println("\n\n\n\nUPdated " + this.pathways.hashCode());		
//		}
		
	}
}
