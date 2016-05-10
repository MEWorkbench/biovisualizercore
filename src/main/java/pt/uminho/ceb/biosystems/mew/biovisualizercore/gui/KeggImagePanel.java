package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.kegg.KeggInfoProvider;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteListener;

public class KeggImagePanel extends JPanel implements MetaboliteListener, ReactionListener {
	
	private static final long		serialVersionUID		= 1L;
	public static final String		KEGG_REACTION_ID		= "KEGG_REACTION";
	public static final String		KEGG_METABOLITE_ID		= "KEGG_CPD";
	
	protected Container				_container;
//	protected JPanel				_containerPanel			= null;
//	protected JScrollPane			_scrollPanel			= null;
	protected boolean				_isReactions;
	protected Map<String, String>	_localIDs_to_keggIDs	= null;
	
	public KeggImagePanel(Container container, boolean isReactions) {
		super();
		_container = container;
		_isReactions = isReactions;
		if (_container != null) {
			if (_isReactions && container.getReactionsExtraInfo().containsKey(KEGG_REACTION_ID)) {
				_localIDs_to_keggIDs = _container.getReactionsExtraInfo().get(KEGG_REACTION_ID);
			} else if (!_isReactions && container.getMetabolitesExtraInfo().containsKey(KEGG_METABOLITE_ID)) {
				_localIDs_to_keggIDs = _container.getMetabolitesExtraInfo().get(KEGG_METABOLITE_ID);
			}
		}
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
//		_containerPanel = getContainerPanel();
//		_containerPanel.setLayout(new FlowLayout());
//		
//		_scrollPanel = new JScrollPane();
//		_scrollPanel.setViewportView(_containerPanel);
//		add(_scrollPanel, BorderLayout.CENTER);
	}
	
	public void reset() {
		synchronized (this) {
			removeAll();
		}
	}
	
//	public JPanel getContainerPanel() {
//		if (_containerPanel == null) _containerPanel = new JPanel(true);
//		return _containerPanel;
//	}
	
	@Override
	public void reactionChanged(ReactionEvent event) {
		final Set<String> ids = event.getIds();
		update(ids);
	}
	
	@Override
	public void metaboliteChanged(MetaboliteEvent event) {
		final Set<String> ids = event.getIds();
		update(ids);
	}
	
	protected void update(final Set<String> ids){

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {					
				try {
					populateKeggInfo(ids);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});		
	}
	
	private void populateKeggInfo(Set<String> ids) throws IOException {
		Set<String> keggids;
		if (!_localIDs_to_keggIDs.isEmpty()) {
			keggids = new HashSet<String>();
			for (String id : ids) {
				if (_localIDs_to_keggIDs.containsKey(id)) {
					keggids.add(_localIDs_to_keggIDs.get(id));
				}
			}
			
			if (keggids.isEmpty()) {
				reset();
			} else {
				reset();
				
				for (final String id : keggids) {
					final BufferedImage keggImage = (_isReactions) ?  KeggInfoProviderReactions.getImageForKEGGID(id) : KeggInfoProvider.getImageForKEGGID(id);
					final String[] keggNames = (_isReactions) ? KeggInfoProviderReactions.getNamesForKEGGID(id) : KeggInfoProvider.getNamesForKEGGID(id);
					JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					split.setResizeWeight(1.0);
					if(keggImage!=null){
						ScalablePane scalablePane = new ScalablePane(keggImage, this, true);
						split.setTopComponent(scalablePane);
					}
					if(keggNames!=null && keggNames.length>0){
						JPanel infoPanel = new JPanel(new BorderLayout());
						
						/** info string */
						final String infoString = (_isReactions) ? "KEGG reaction ["+id+"]" : "KEGG compound ["+id+"]";
						
						/** info table */
						String[][] tableData = new String[keggNames.length][1];
						String[] columnNames = new String[]{infoString};
						for(int i=0; i<keggNames.length; i++){
							tableData[i] = new String[]{keggNames[i].trim()};
						}
						DefaultTableModel tableModel = new DefaultTableModel(tableData,columnNames);
						final JTable table = new JTable(tableModel);
						
						/** info button */
						JButton infoButton = new JButton(infoString);
						infoButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								JDialog dialog = new JDialog();
								dialog.setTitle(infoString);
								dialog.setLayout(new BorderLayout());
								
								JSplitPane inSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
								inSplit.setResizeWeight(1.0);

								ScalablePane inScalable = new ScalablePane(keggImage, dialog.getRootPane(), true);
								
								Dimension imageDimension =new Dimension(keggImage.getWidth(),keggImage.getHeight()); 
								inSplit.setTopComponent(inScalable);
								
								inSplit.setBottomComponent(table);
								
								dialog.setPreferredSize(imageDimension);
								dialog.setMinimumSize(new Dimension((int) (300*1.6),300));
								dialog.add(inSplit,BorderLayout.CENTER);
								dialog.setModal(true);
								dialog.pack();
								dialog.setLocationRelativeTo(null);
								dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
								dialog.setVisible(true);
							}
						});
						
						infoPanel.add(infoButton,BorderLayout.NORTH);						
						infoPanel.add(table,BorderLayout.CENTER);
						split.setBottomComponent(infoPanel);
					}
					
					split.updateUI();
					split.revalidate();
					split.repaint();
					add(split,BorderLayout.CENTER);
				}
				updateUI();
				revalidate();
				repaint();
			}
		}
	}
}
