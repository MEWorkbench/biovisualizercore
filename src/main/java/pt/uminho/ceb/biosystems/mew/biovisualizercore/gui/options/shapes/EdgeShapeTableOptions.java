package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes;

import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.SpinnerEditor;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.AbstractTableColumnOptions;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.TableOptionsException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.AbstractLabelsConstructor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.ChangeColorEditor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.ShapeManager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;

public class EdgeShapeTableOptions extends AbstractTableColumnOptions{
	
	private static final long serialVersionUID = -7340301749431541003L;
	
	protected boolean isChanged;
	
	
	public EdgeShapeTableOptions(){
		super();
		
		setRowCellEditors(buildRowCellEditors());
		
		initGUI();
	}
	
	@Override
	protected void initGUI() {
		super.initGUI();
		List<String> columnHeaders = new ArrayList<String>();
		columnHeaders.add("Properties");
		columnHeaders.add("Values");
		populateEmpty(columnHeaders, new Object[]{"Shape", "Fill Color", "Stroke Thickness", "Stroke color", "Preview"});
		configureCells();
		
		isChanged = false;
	}
	
	protected void configureCells() {
		
		table.getColumnModel().getColumn(1).setCellRenderer(new ReactionShapeCellRenderer());

		fixColumn(0, 100);
	}
	
	
	protected TableCellEditor[] buildRowCellEditors(){
		TableCellEditor[] editors = new TableCellEditor[5];
		editors[0] = new ShapeTableCellEditor(ShapeManager.getAvailableShapeCodes());
		editors[1] = new ChangeColorEditor(this);
		editors[2] = new SpinnerEditor(new SpinnerNumberModel(10, 1, 30, 1));
		editors[3] = new ChangeColorEditor(this);
		return editors;
	}
	
	/** @return 
	 *  [0] shape :: Integer
		[1] fill color :: Color
		[2] stroke thickness :: Integer
		[3] stroke color :: Color */
	public List<Object> getProperties(String columnId) throws TableOptionsException{
		return getProperties(columns.indexOf(columnId));
	}
	
	/** @return 
	 *  [0] shape :: Integer
		[1] fill color :: Color
		[2] stroke thickness :: Integer
		[3] stroke color :: Color */
	public List<Object> getProperties(int column) throws TableOptionsException{
		List<Object> result = new ArrayList<Object>();
		result.add(table.getValueAt(0, column));
		result.add(table.getValueAt(1, column));
		result.add(table.getValueAt(2, column));
		result.add(table.getValueAt(3, column));
		return result;
	}
	
	public void setOptions(String id, Object... data){
		this.setInfo(id, data);
		
	}
	
	public void setOptions(int column, Object... data){
		this.setInfo(column, data);
	}
	
	public boolean hasChanged(){
		return isChanged;
	}
	
	@Override
	public int getRowHeight(int column) {
		
		Object v = table.getValueAt(2, column);
		
		int ret = (v==null) ? 0 : (Integer) v; 
		
		AbstractLabelsConstructor ac = (AbstractLabelsConstructor) table.getValueAt(4, column);
		
		if(ac==null)
			return 20;
			
		try {
			ret *= ac.getNumberOfLines()*1.8;
		} catch (Exception e) {e.printStackTrace();}
		
		return ret;
	}
	
	@Override
	public void tableChanged(TableModelEvent arg0) {
		super.tableChanged(arg0);
		isChanged = true;
	}
}
