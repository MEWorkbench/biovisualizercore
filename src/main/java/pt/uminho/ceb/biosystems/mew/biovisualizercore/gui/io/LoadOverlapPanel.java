package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.AbstractOverlap;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.GenericOverlapIOException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class LoadOverlapPanel extends JDialog implements ActionListener {
	
	private static final long		serialVersionUID		= 1L;
	private static final String		DEFAULT_PATH			= System.getProperty("user.home");
	private static final String		DEFAULT_PATH_DELIMITER	= FileSystems.getDefault().getSeparator();
	private static final String		DEFAULT_MET_FILE		= "metabolites.txt";
	private static final String		DEFAULT_REACTIONS_FILE	= "reactions.txt";
	
	protected NamePanel				_nameField				= null;
	protected FileSelectionPanel	_panelMetabolites		= null;
	protected FileSelectionPanel	_panelReactions			= null;
	protected OkCancelMiniPanel		_okCancelMiniPanel		= null;
	
	protected IOverlapObject		_overlap				= null;
	protected Set<String>			_overlapNames			= null;
	protected List<LoadOverlapListener> _listeners = null;
	
	public LoadOverlapPanel(Set<String> overlapNames) {
		super();
		_overlapNames = overlapNames;
		_listeners = new ArrayList<LoadOverlapListener>();
		initGUI();
	}
	
	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		getContentPane().setLayout(gridBagLayout);
				
		_nameField = new NamePanel("manual_overlap_" + _overlapNames.size());
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 0);
		gbc_nameField.fill = GridBagConstraints.BOTH;
		gbc_nameField.gridx = 0;
		gbc_nameField.gridy = 0;
		getContentPane().add(_nameField, gbc_nameField);
		
		_panelMetabolites = new FileSelectionPanel(DEFAULT_PATH + DEFAULT_PATH_DELIMITER + DEFAULT_MET_FILE, "Metabolites file", true);
		GridBagConstraints gbc_fileSelectionPanel = new GridBagConstraints();
		gbc_fileSelectionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_fileSelectionPanel.fill = GridBagConstraints.BOTH;
		gbc_fileSelectionPanel.gridx = 0;
		gbc_fileSelectionPanel.gridy = 1;
		getContentPane().add(_panelMetabolites, gbc_fileSelectionPanel);
		
		_panelReactions = new FileSelectionPanel(DEFAULT_PATH + DEFAULT_PATH_DELIMITER + DEFAULT_REACTIONS_FILE, "Reactions file", true);
		GridBagConstraints gbc_fileSelectionPanel_1 = new GridBagConstraints();
		gbc_fileSelectionPanel_1.fill = GridBagConstraints.BOTH;
		gbc_fileSelectionPanel_1.gridx = 0;
		gbc_fileSelectionPanel_1.gridy = 2;
		getContentPane().add(_panelReactions, gbc_fileSelectionPanel_1);
		
		_okCancelMiniPanel = new OkCancelMiniPanel();
		_okCancelMiniPanel.addButtonsActionListener(this);
		GridBagConstraints gbc_okcancelpanel = new GridBagConstraints();
		gbc_okcancelpanel.fill = GridBagConstraints.BOTH;
		gbc_okcancelpanel.gridx = 0;
		gbc_okcancelpanel.gridy = 3;
		getContentPane().add(_okCancelMiniPanel, gbc_okcancelpanel);
		
		setTitle("Load overlap");
		
	}
	
	public void display() {
		setPreferredSize(new Dimension(450, 170));
		setMinimumSize(new Dimension(450, 170));
		setMaximumSize(new Dimension(450, 170));
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void dispose() {
		_okCancelMiniPanel.removeListeners();
		_listeners.clear();
		setVisible(false);
		super.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
			case OkCancelMiniPanel.OK_BUTTON_ACTION_COMMAND:
				String name = _nameField.get_name();
				boolean validName = validate(name);
				if (validName) {
					_overlap = new AbstractOverlap(name);
					String fileMetabolites = _panelMetabolites.get_currentPath();
					String fileReactions = _panelReactions.get_currentPath();
					boolean ok = true;
					String message = "";
					try {
						_overlap.loadFromFile(fileReactions, fileMetabolites);
						fireLoadOverlapEvent(new LoadOverlapEvent(this, _overlap));
					} catch (IOException | GenericOverlapIOException e1) {
						ok = false;
						message = e1.getMessage();
						e1.printStackTrace();
					}
					if (ok)
						JOptionPane.showMessageDialog(this, "Overlap was correctly loaded.");
					else
						JOptionPane.showMessageDialog(this, "Overlap could not be loaded.\n"+message);
				}
				dispose();
				break;
			
			case OkCancelMiniPanel.CANCEL_BUTTON_ACTION_COMMAND:
				dispose();
				break;
			default:
				break;
		}
	}
	
	private boolean validate(String name) {
		if (name == null || name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "The new overlap name cannot be neither null nor empty.");
			return false;
		} else if (_overlapNames.contains(name)) {
			JOptionPane.showMessageDialog(this, "There another overlap with the same name. Choose a differente name.");
			return false;
		} else
			return true;
		
	}
	
	public void addOkCancelActionListener(ActionListener listener) {
		_okCancelMiniPanel.addButtonsActionListener(listener);
	}
	
	public void addLoadOverlapListener(LoadOverlapListener listener){
		_listeners.add(listener);
	}
	
	public void fireLoadOverlapEvent(LoadOverlapEvent event){
		for(LoadOverlapListener listener : _listeners)
			listener.loadOverLap(event);
	}
	
	public IOverlapObject get_overlap() {
		return _overlap;
	}
}

class NamePanel extends JPanel {
	
	private static final long	serialVersionUID	= 1L;
	private JTextField			_textField;
	private JLabel				_label;
	protected String			_name;
	
	public NamePanel(String defaultName) {
		super();
		_name = defaultName;
		initGUI();
	}
	
	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 1, 1};
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.2, 0.8,};
		gridBagLayout.rowWeights = new double[] { 0.0 };
		setLayout(gridBagLayout);
		
		_label = new JLabel("Overlap name");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(_label, gbc_lblNewLabel);
		
		_textField = new JTextField(_name);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(_textField, gbc_textField);
		_textField.setColumns(10);
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}
}
