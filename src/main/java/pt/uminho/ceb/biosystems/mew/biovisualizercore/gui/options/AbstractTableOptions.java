package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;


public abstract class AbstractTableOptions extends JPanel implements TableModelListener {
	
	protected List<String> lines;
	protected JTable table;
	protected boolean isEditing = false;
	
	public AbstractTableOptions() {
		initGUI();
	}

	public abstract int getRowHeight(int row);

	private void initGUI() {
		setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		table = new JTable(){
			@Override
			public boolean isCellEditable(int l, int c) {
				if(c == 0)
					return false;
				if(this.getModel().getColumnCount()-1 == c)
					return false;
				return true;
							
			}
		};
		scrollPane.setViewportView(table);
		
	}

	public void fixColumn(int c, int width){
		table.getColumnModel().getColumn(c).setMaxWidth(width);
		table.getColumnModel().getColumn(c).setMinWidth(width);
		table.getColumnModel().getColumn(c).setWidth(width);
		
	}

	protected void populateEmpty(Object[] headers, List<String> lines) {
		
		isEditing = true;
		DefaultTableModel dtm = null;
		if(headers!=null){
			dtm = new DefaultTableModel(headers, 0);
		}else dtm = new DefaultTableModel();
		
		if(lines!=null)
			for(String info : lines){
				Object[] row = new Object[dtm.getColumnCount()];
				row[0] = info;
				dtm.addRow(row);
			}
		this.lines=lines;
		dtm.addTableModelListener(this);
		table.setModel(dtm);
		isEditing = false;
	}
	
	public Object getValueAt(String rowId, int column) throws TableOptionsException{
		int row = lines.indexOf(rowId);
		if(row<0)
			throw new TableOptionsException("Invalid row identifier '" + rowId + "'!");
		return table.getValueAt(row, column);
	}
	
	protected void setInfo(String info, Object... data){
		isEditing = true;
		for(int i = 0; i < data.length; i++)
			table.setValueAt(data[i], lines.indexOf(info), i+1);
		isEditing = false;
	}
	
	public void tableChanged(TableModelEvent arg0) {
		
		if(getRowHeight(arg0.getLastRow())>20)
		table.setRowHeight(arg0.getLastRow(), getRowHeight(arg0.getLastRow()));
	}
	
	
	
}
