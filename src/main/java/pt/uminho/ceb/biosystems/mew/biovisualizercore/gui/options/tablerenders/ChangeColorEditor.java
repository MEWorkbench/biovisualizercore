package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ChangeColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 1L;
	protected JButton button;
	protected Color value;
	private Component c;
	
	public ChangeColorEditor(Component c) {
		this.button = new JButton();
		this.c=c;
		button.addActionListener(this);
	}
		
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,int row, int column) {
		this.value=(Color) value;
		return button;
	}
	
	public Object getCellEditorValue() {
		System.out.println("COLOR\t" +value + " ALPHA: " + value.getAlpha() + " Transparency: " + value.getTransparency());
		return value;
	}
	
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}
	
	public void cancelCellEditing() {
		super.cancelCellEditing();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Color color = JColorChooser.showDialog(c, "Select Color", (Color) value);
		System.out.println(color+"\t"+value);
		if(color!=null)
			value = color;
		System.out.println(value);
		fireEditingStopped();
	}
}
