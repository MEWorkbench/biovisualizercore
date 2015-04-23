package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileSelectionPanel extends JPanel implements ActionListener {
	
	private static final long	serialVersionUID					= -4762279688339774265L;
	public static final String	SELECT_FILE_BUTTON_ACTION_COMMAND	= "selectFileButton";
	
	private JTextField			_textField;
	private JLabel				_label;
	private JButton				_chooseButton;
	private String				_textLabel							= "File";
	private String				_currentPath						= System.getProperty("user.home");
	private String				_buttonText							= "Find...";
	private boolean				_open								= false;
	
	public FileSelectionPanel(String defaultPath, String textLabel, boolean open) {
		if (defaultPath != null) _currentPath = defaultPath;
		_textLabel = textLabel;
		_open = open;
		initGUI();
	}
	
	private void initGUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 1, 1, 1 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 0.2, 0.8, 0.2 };
		gridBagLayout.rowWeights = new double[] { 0.0 };
		setLayout(gridBagLayout);
		
		_label = new JLabel(_textLabel);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(_label, gbc_lblNewLabel);
		
		_textField = new JTextField(_currentPath);
		_textField.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(_textField, gbc_textField);
		_textField.setColumns(10);
		
		_chooseButton = new JButton(_buttonText);
		_chooseButton.addActionListener(this);
		_chooseButton.setActionCommand(SELECT_FILE_BUTTON_ACTION_COMMAND);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		add(_chooseButton, gbc_btnNewButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
			case SELECT_FILE_BUTTON_ACTION_COMMAND:
					handleFileSelection();
				break;
			
			default:
				break;
		}
		
	}
	
	private void handleFileSelection() {
		final JFileChooser fc = new JFileChooser();
		int returnVal = _open ? fc.showOpenDialog(null) : fc.showSaveDialog(null);
		switch (returnVal) {
			case JFileChooser.APPROVE_OPTION:
				File file = fc.getSelectedFile();
				if(file!=null)
					set_currentPath(file.getAbsolutePath());
				break;
			
			case JFileChooser.CANCEL_OPTION:
				break;
			case JFileChooser.ERROR_OPTION:
				break;
			default:
				break;
		}
	}
	
	public String get_currentPath() {
		return _currentPath;
	}
	
	public void set_buttonText(String _buttonText) {
		this._buttonText = _buttonText;
	}
	
	public void set_currentPath(String _currentPath) {
		this._currentPath = _currentPath;
		this._textField.setText(_currentPath);
	}
	
	public void setLabel(String label){
		_textLabel = label;
		_label.setText(_textLabel);
	}
	
}
