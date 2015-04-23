package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class LabelAndSeparatorComponent extends JComponent{
	
	private JComboBox comboBox;
	private JLabel lblInfo;
	
	
	public LabelAndSeparatorComponent(Object[] values) {
		initComponent(values);
	}

	
	private void initComponent(Object[] values) {
		setLayout(new GridLayout(1, 0, 0, 0));
		
		lblInfo = new JLabel("info");
		add(lblInfo);
		
		comboBox = new JComboBox();
		add(comboBox);
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel(values);
		comboBox.setModel(dcbm);
	}
	
	public void setSeparator(Object o){
		comboBox.setSelectedItem(o);
	}
	
	public void setMethodId(String o){
		lblInfo.setText(o);
	}

}
