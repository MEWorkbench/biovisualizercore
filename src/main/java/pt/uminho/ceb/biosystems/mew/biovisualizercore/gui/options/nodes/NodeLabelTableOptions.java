package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes;

import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.FontManager;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.JTableUtils;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.ComboEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.FontTableEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.SpinnerEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.cellrender.ColorRenderer;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.cellrender.FontTableCellRenderer;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.AbstractTableOptions;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.InfoNames;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.TableOptionsException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.AbstractLabelsConstructor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.FakeIMethods;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.ChangeColorEditor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.LabelConstructorEditor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.NodeLabelRenderer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;

public class NodeLabelTableOptions extends AbstractTableOptions{
	
	private static final long serialVersionUID = 5490618022841706661L;
	
	protected boolean isChanged;
	
	
	public NodeLabelTableOptions(){
		super();
		
		List<String> rowIds = new ArrayList<String>();
		rowIds.add(InfoNames.METABOLITES);
		rowIds.add(InfoNames.CURRENCY);
		rowIds.add(InfoNames.INFO);
		
		populateEmpty(new Object[]{"Type", "Font", "Style", "Size", "Color", "Information", "Preview"}, rowIds);
		populateEmpty();
		
		isChanged = false;
	}
	
	private void populateEmpty() {
		
		JTableUtils.setCellRender(table, 1, new FontTableCellRenderer());
		JTableUtils.setCellEditor(table, 1, new FontTableEditor(FontManager.getManager().getAllFonts()));
		
		JTableUtils.setCellEditor(table, 2, new ComboEditor(new Object[] {"Plain", "Italic", "Bold"}));
//		JTableUtils.setCellRender(table, columnIdx, render)
		
		JTableUtils.setCellEditor(table, 3, new SpinnerEditor(new SpinnerNumberModel(10, 8, 50, 1)));
		
		JTableUtils.setCellRender(table, 4, new ColorRenderer());
		JTableUtils.setCellEditor(table, 4, new ChangeColorEditor(this));
		
		JTableUtils.setCellEditor(table, 5, new LabelConstructorEditor(this));
		
		JTableUtils.setCellRender(table, 6, new NodeLabelRenderer());
		
		fixColumn(2, 40);
		fixColumn(3, 35);
		fixColumn(4, 35);
	}
	
	/** @return 
	 *  [0] font :: Font
		[1] style :: Integer
		[2] size :: Integer
		[3] color :: Color
	 	[4] labelConstructor :: AbstractLabelsConstructor */
	public List<Object> getInfoNameProperties(String infoNameId) throws TableOptionsException{
		int row = lines.indexOf(infoNameId);
		List<Object> result = new ArrayList<Object>();
		result.add(table.getValueAt(row, 1));
		result.add(FontManager.convertStyle((String) table.getValueAt(row, 2)));
		result.add(table.getValueAt(row, 3));
		result.add(table.getValueAt(row, 4));
		result.add(table.getValueAt(row, 5));
		return result;
	}
	
	/** @return 
	 *  [0] font :: Font
		[1] style :: Integer
		[2] size :: Integer
		[3] color :: Color
	 	[4] labelConstructor :: AbstractLabelsConstructor */
	public List<Object> getMetaboliteProperties() throws TableOptionsException{
		return getInfoNameProperties(InfoNames.METABOLITES);
	}
	
	/** @return 
	 *  [0] font :: Font
		[1] style :: Integer
		[2] size :: Integer
		[3] color :: Color
	 	[4] labelConstructor :: AbstractLabelsConstructor */
	public List<Object> getCurrencyProperties() throws TableOptionsException{
		return getInfoNameProperties(InfoNames.CURRENCY);
	}
	
	/** @return 
	 *  [0] font :: Font
		[1] style :: Integer
		[2] size :: Integer
		[3] color :: Color
	 	[4] labelConstructor :: AbstractLabelsConstructor */
	public List<Object> getInfoProperties() throws TableOptionsException{
		return getInfoNameProperties(InfoNames.INFO);
	}
	
	public void setOptions(String id, Object... data){
		this.setInfo(id, data);
		
	}
	
	public void setOptions(String id, Font f, int size, Color color){
		this.setInfo(id, f, size, color);
	}
	
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("LabelOptions");
		NodeLabelTableOptions lo = new NodeLabelTableOptions();
		frame.getContentPane().add(lo);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		AbstractLabelsConstructor<Object> metInfo = new AbstractLabelsConstructor<Object>(new FakeIMethods());
		metInfo.addLabelInfo("id", "\n");
		metInfo.addLabelInfo("name", "");

		lo.setOptions(InfoNames.METABOLITES, FontManager.getManager().getRenderingFont("Norasi"),
				FontManager.convertStyle (Font.BOLD), 10,Color.CYAN, metInfo);
		
		lo.setOptions(InfoNames.CURRENCY, FontManager.getManager().getRenderingFont("Norasi Bold"),
				FontManager.convertStyle(Font.ITALIC),10,Color.YELLOW, new AbstractLabelsConstructor<Object>(new FakeIMethods()));
		
		lo.setOptions(InfoNames.INFO, FontManager.getManager().getRenderingFont("Norasi Bold Italic"),
				FontManager.convertStyle(Font.PLAIN),10, Color.DARK_GRAY, new AbstractLabelsConstructor<Object>(new FakeIMethods()));
	}

	public boolean hasChanged(){
		return isChanged;
	}
	
	@Override
	public int getRowHeight(int row) {
		
		Object v = table.getValueAt(row, 3);
		int ret = (v==null) ? 0 : (Integer) v;
		
		AbstractLabelsConstructor ac = (AbstractLabelsConstructor) table.getValueAt(row, 5);
		
		if(ac!=null)
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
