package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileSystems;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class SaveOverlapPanel extends JDialog implements ActionListener {
	
	private static final long		serialVersionUID		= 1L;
	private static final String		DEFAULT_PATH			= System.getProperty("user.home");
	private static final String		DEFAULT_PATH_DELIMITER	= FileSystems.getDefault().getSeparator();
	private static final String		DEFAULT_MET_FILE		= "metabolites.txt";
	private static final String		DEFAULT_REACTIONS_FILE	= "reactions.txt";
	
	protected IOverlapObject		_overlap				= null;
	protected FileSelectionPanel	_panelMetabolites		= null;
	protected FileSelectionPanel	_panelReactions			= null;
	protected OkCancelMiniPanel		_okCancelMiniPanel		= null;
	
	public SaveOverlapPanel(IOverlapObject overlap) {
		super();
		_overlap = overlap;
		initGUI();
	}
	
	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0 };
		getContentPane().setLayout(gridBagLayout);
		
		_panelMetabolites = new FileSelectionPanel(DEFAULT_PATH + DEFAULT_PATH_DELIMITER + DEFAULT_MET_FILE, "Metabolites file", false);
		GridBagConstraints gbc_fileSelectionPanel = new GridBagConstraints();
		gbc_fileSelectionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_fileSelectionPanel.fill = GridBagConstraints.BOTH;
		gbc_fileSelectionPanel.gridx = 0;
		gbc_fileSelectionPanel.gridy = 0;
		getContentPane().add(_panelMetabolites, gbc_fileSelectionPanel);
		
		_panelReactions = new FileSelectionPanel(DEFAULT_PATH + DEFAULT_PATH_DELIMITER + DEFAULT_REACTIONS_FILE, "Reactions file", false);
		GridBagConstraints gbc_fileSelectionPanel_1 = new GridBagConstraints();
		gbc_fileSelectionPanel_1.fill = GridBagConstraints.BOTH;
		gbc_fileSelectionPanel_1.gridx = 0;
		gbc_fileSelectionPanel_1.gridy = 1;
		getContentPane().add(_panelReactions, gbc_fileSelectionPanel_1);
		
		_okCancelMiniPanel = new OkCancelMiniPanel();
		_okCancelMiniPanel.addButtonsActionListener(this);
		GridBagConstraints gbc_okcancelpanel = new GridBagConstraints();
		gbc_okcancelpanel.fill = GridBagConstraints.BOTH;
		gbc_okcancelpanel.gridx = 0;
		gbc_okcancelpanel.gridy = 2;
		getContentPane().add(_okCancelMiniPanel, gbc_okcancelpanel);
		
		setTitle("Save overlap");
			
	}
	
	public void display(){
		setPreferredSize(new Dimension(450, 140));
		setMinimumSize(new Dimension(450, 140));
		setMaximumSize(new Dimension(450, 140));
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	public void dispose(){
		setVisible(false);
		super.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
			case OkCancelMiniPanel.OK_BUTTON_ACTION_COMMAND:
				String fileMetabolites = _panelMetabolites.get_currentPath();
				String fileReactions = _panelReactions.get_currentPath();
				boolean ok = true;
				try {
					_overlap.saveToFile(fileReactions, fileMetabolites);
				} catch (IOException e1) {
					ok=false;
					e1.printStackTrace();
				}
				if(ok)
					JOptionPane.showMessageDialog(this, "Overlap was correctly saved.");
				else
					JOptionPane.showMessageDialog(this, "Overlap could not be saved..");
				
				dispose();
				break;
			
			case OkCancelMiniPanel.CANCEL_BUTTON_ACTION_COMMAND:
				dispose();
				break;
			default:
				break;
		}
	}
}
