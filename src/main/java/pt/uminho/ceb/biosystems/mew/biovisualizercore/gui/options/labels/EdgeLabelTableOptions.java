package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.AbstractTableColumnOptions;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.TableOptionsException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.ChangeColorEditor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.LabelConstructorEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.FontManager;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.ComboEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.FontTableEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.SpinnerEditor;

public class EdgeLabelTableOptions extends AbstractTableColumnOptions{
	
	private static final long serialVersionUID = -6011283535527262130L;
	
	protected boolean isChanged;
	
	
	public EdgeLabelTableOptions(){
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
		populateEmpty(columnHeaders, new Object[]{"Font", "Style", "Size", "Color", "Information", "Preview"});
		configureCells();
		
		isChanged = false;
	}
	
	protected void configureCells() {
		
		table.getColumnModel().getColumn(1).setCellRenderer(new ReactionLabelCellRenderer());

		fixColumn(0, 100);
	}
	
	
	protected TableCellEditor[] buildRowCellEditors(){
		TableCellEditor[] editors = new TableCellEditor[5];
		editors[0] = new FontTableEditor(FontManager.getManager().getAllFonts());
		editors[1] = new ComboEditor(new Object[] {"Plain", "Italic", "Bold"});
		editors[2] = new SpinnerEditor(new SpinnerNumberModel(10, 8, 50, 1));
		editors[3] = new ChangeColorEditor(this);
		editors[4] = new LabelConstructorEditor(this);
		return editors;
	}
	
	/** @return 
	 *  [0] font :: Font
		[1] style :: Integer
		[2] size :: Integer
		[3] color :: Color
	 	[4] labelConstructor :: AbstractLabelsConstructor */
	public List<Object> getProperties(String columnId) throws TableOptionsException{
		return getProperties(columns.indexOf(columnId));
	}
	
	/** @return 
	 *  [0] font :: Font
		[1] style :: Integer
		[2] size :: Integer
		[3] color :: Color
	 	[4] labelConstructor :: AbstractLabelsConstructor */
	public List<Object> getProperties(int column) throws TableOptionsException{
		List<Object> result = new ArrayList<Object>();
		result.add(table.getValueAt(0, column));
		result.add(FontManager.convertStyle((String) table.getValueAt(1, column)));
		result.add(table.getValueAt(2, column));
		result.add(table.getValueAt(3, column));
		result.add(table.getValueAt(4, column));
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
