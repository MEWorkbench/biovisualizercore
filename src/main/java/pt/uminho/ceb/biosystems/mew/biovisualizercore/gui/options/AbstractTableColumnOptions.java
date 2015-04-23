package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public abstract class AbstractTableColumnOptions extends JPanel implements TableModelListener {
	
	protected List<String> columns;
	protected JTable table;
	protected boolean isEditing = false;
	/** Holds specific cell editor for each row of the table. If a row do not have a different cell editor defined, the value in the corresponding index will be null*/
	protected TableCellEditor[] rowCellEditors;
	
	
	public AbstractTableColumnOptions() {}

	
	public abstract int getRowHeight(int column);

	public void setRowCellEditors(TableCellEditor[] rowCellEditors){
		this.rowCellEditors = rowCellEditors;
	}
	
	protected void initGUI() {
		setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		table = new JTable(){
			@Override
			public boolean isCellEditable(int l, int c) {
				if(columns!=null && c==0)
					return false;
				if(this.getModel().getRowCount()-1 == l)
					return false;
				return true;
							
			}
			
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				if(rowCellEditors==null || rowCellEditors[row]==null)
					return super.getCellEditor(row, column);
				
				return rowCellEditors[row];
			}
			
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if(column==0)
				{
					return new TableCellRenderer() {
						
//						JLabel[] labels = new JLabel[table.getRowCount()];
						JTextField[] labels = new JTextField[table.getRowCount()];
						
						@Override
						public Component getTableCellRendererComponent(JTable table, Object value,
								boolean isSelected, boolean hasFocus, int row, int column) {
							if(labels[row]==null)
							{
//								labels[row] = new JLabel((String) value);
								labels[row] = new JTextField((String) value);
								labels[row].setBackground(table.getTableHeader().getBackground());
								labels[row].setBorder(null);
							}
							return labels[row];
						}
					};
				}
				return super.getCellRenderer(row, column);
			}
		
		};
		scrollPane.setViewportView(table);
	}

	public void fixColumn(int c, int width){
		table.getColumnModel().getColumn(c).setMaxWidth(width);
		table.getColumnModel().getColumn(c).setMinWidth(width);
		table.getColumnModel().getColumn(c).setWidth(width);
	}

	protected void populateEmpty(List<String> columns, Object[] rowHeaders) {
		
		isEditing = true;
		DefaultTableModel dtm = null;
		if(columns!=null){
			dtm = new DefaultTableModel(columns.toArray(), rowHeaders.length);
		}else dtm = new DefaultTableModel(rowHeaders.length, 2);
		
		if(rowHeaders!=null)
			for(int row=0; row<rowHeaders.length; row++)
				dtm.setValueAt(rowHeaders[row], row, 0);
		this.columns=columns;
		dtm.addTableModelListener(this);
		table.setModel(dtm);
		isEditing = false;
	}
	
	public Object getValueAt(int row, String columnId) throws TableOptionsException{
		int column = columns.indexOf(columnId) + 1;
		if(column<0)
			throw new TableOptionsException("Invalid column identifier '" + columnId + "'!");
		return table.getValueAt(row, column);
	}
	
	protected void setInfo(String columnId, Object... data){
		isEditing = true;
		for(int i = 0; i < data.length; i++)
			table.setValueAt(data[i], i, columns.indexOf(columnId)+1);
		isEditing = false;
	}
	
	protected void setInfo(int column, Object... data){
		isEditing = true;
		for(int i = 0; i < data.length; i++)
			table.setValueAt(data[i], i, column);
		isEditing = false;
	}
	
	public void tableChanged(TableModelEvent arg0) {
//		if(getRowHeight(arg0.getLastRow())>20)
		table.setRowHeight(5, getRowHeight(arg0.getColumn()));
	}
	
	
	
}
