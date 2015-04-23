package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ShapeTableCellEditor extends AbstractCellEditor implements TableCellEditor, ItemListener{

	private static final long serialVersionUID = 2295715044569315367L;
	
	protected ShapesComboBox combo;
	
	
	public ShapeTableCellEditor(Object[] shapeCodes){
		combo = new ShapesComboBox(shapeCodes);
		combo.addItemListener(this);
	}
	
	@Override
	public Object getCellEditorValue() {
		return combo.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		combo.setSelectedItem(value);		
		fireEditingStopped();
		return combo;
	}

	public void addItemListener(ItemListener l){
		combo.addItemListener(l);
	}
	
   public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
 
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
 
    public void cancelCellEditing() {
        super.cancelCellEditing();
    }

	@Override
	public void itemStateChanged(ItemEvent e) {
		fireEditingStopped();
	}
}