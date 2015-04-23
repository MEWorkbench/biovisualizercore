package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.AbstractLabelsConstructor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.FakeIMethods;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.LabelConstructorDialog;

public class LabelConstructorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 1L;
	protected JButton button;
	protected AbstractLabelsConstructor<Object> labelConstructor;
	protected Component ownerComponent;
	
	public LabelConstructorEditor(Component ownerComponent) {
		this.button = new JButton();
		this.ownerComponent = ownerComponent;
		labelConstructor = new AbstractLabelsConstructor<Object>(new FakeIMethods());
		labelConstructor.addLabelInfo("id", ";");
		labelConstructor.addLabelInfo("name", "");
		button.addActionListener(this);
	}
		
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,int row, int column) {
		this.labelConstructor = (AbstractLabelsConstructor) value;
		return button;
	}
	
	public Object getCellEditorValue() {
		System.out.println("Value: " + labelConstructor.getLabelMasK());
		return labelConstructor;
	}
	
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}
	
	public void cancelCellEditing() {
		super.cancelCellEditing();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		AbstractLabelsConstructor lConstructor = LabelConstructorDialog.createDialogLabelConstructor(ownerComponent, labelConstructor);
		if(lConstructor==null)
			System.out.println("NULL");
		else
		{
			labelConstructor = lConstructor;
			System.out.println(labelConstructor.getLabelMasK());
		}
		
		fireEditingStopped();
	}
}
