package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options;

import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.JTableUtils;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.SpinnerEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.cellrender.ColorRenderer;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes.NodeDraw;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes.ShapeTableCellEditor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.ChangeColorEditor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders.ShapeTableCellRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.Constants;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.ShapeManager;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellRenderer;

public class ShapeAndColorOptions extends AbstractTableOptions{
	
	private static final long serialVersionUID = -1233988161941697922L;
	
	private boolean isChanged = false;

	public ShapeAndColorOptions() throws IOException {
		super();
		List<String> array = new ArrayList<String>();
		array.add(InfoNames.METABOLITES);
		array.add(InfoNames.CURRENCY);
		array.add(InfoNames.INFO);
		
		populateEmpty(new Object[]{"Type", "Shape", "Fill Color", "Stroke Thickness", "Stroke color", "Preview"}, array);
		populateEmpty();
	}
	
	
	private void populateEmpty() throws IOException{
		
		JTableUtils.setCellRender(table, 1, new ShapeTableCellRenderer());
		JTableUtils.setCellEditor(table, 1, new ShapeTableCellEditor(ShapeManager.getAvailableShapeCodes()));
		
		JTableUtils.setCellRender(table, 2, new ColorRenderer());
		JTableUtils.setCellEditor(table, 2, new ChangeColorEditor(this));
		
		JTableUtils.setCellEditor(table, 3, new SpinnerEditor(new SpinnerNumberModel(10, 1, 30, 1)));
		
		JTableUtils.setCellRender(table, 4, new ColorRenderer());
		JTableUtils.setCellEditor(table, 4, new ChangeColorEditor(this));
		
		JTableUtils.setCellRender(table, 5, new TableCellRenderer(){

			NodeDraw previewNd = new NodeDraw(10,10);
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				
				Integer s = (Integer) table.getValueAt(row, 1);
				if(s==null)
					s=-1;
				previewNd.setShape(s);
				Color fillColor = (Color) table.getValueAt(row, 2);
				
				previewNd.setColor(fillColor);
				
				Double size = (double) ((Integer) table.getValueAt(row, 3));
				
				previewNd.setShapeHeight(size);
				previewNd.setShapeWidth(size);
				
				return previewNd;
			}
		});
			
		
//		fixColumn(2, 35);
		fixColumn(3, 120);
//		fixColumn(4, 35);
	}
	

	/** @return 
	 *  [0] shape :: Integer
		[1] fill color :: Color
		[2] stroke thickness :: Integer
		[3] stroke color :: Color */
	public List<Object> getInfoNameProperties(String infoNameId) throws TableOptionsException{
		int row = lines.indexOf(infoNameId);
		List<Object> result = new ArrayList<Object>();
		result.add((Integer) table.getValueAt(row, 1));
		result.add(table.getValueAt(row, 2));
		result.add(table.getValueAt(row, 3));
		result.add(table.getValueAt(row, 4));
		return result;
	}
	
	/** @return 
	 *  [0] shape :: Integer
		[1] fill color :: Color
		[2] stroke thickness :: Integer
		[3] stroke color :: Color */
	public List<Object> getMetaboliteProperties() throws TableOptionsException{
		return getInfoNameProperties(InfoNames.METABOLITES);
	}
	
	/** @return 
	 *  [0] shape :: Integer
		[1] fill color :: Color
		[2] stroke thickness :: Integer
		[3] stroke color :: Color */
	public List<Object> getCurrencyProperties() throws TableOptionsException{
		return getInfoNameProperties(InfoNames.CURRENCY);
	}
	
	/** @return 
	 *  [0] shape :: Integer
		[1] fill color :: Color
		[2] stroke thickness :: Integer
		[3] stroke color :: Color */
	public List<Object> getInfoProperties() throws TableOptionsException{
		return getInfoNameProperties(InfoNames.INFO);
	}
	
	public void setOptions(String id, Integer shape, Color colorshape, int line, Color colorline){
		this.setInfo(id, shape, colorshape, line, colorline);
	}
	
	public boolean hasChanged(){
		return isChanged;
	}
	
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("ShapeAndColorOptions");
		
		ShapeAndColorOptions sc = new ShapeAndColorOptions();
		frame.getContentPane().add(sc);
		
		sc.setOptions(InfoNames.METABOLITES, Constants.SHAPE_ARROW_DOWN,
				Color.CYAN, 6,Color.CYAN);
		sc.setOptions(InfoNames.CURRENCY, Constants.EDGE_TYPE_CURVE,
				Color.CYAN, 6,Color.CYAN);
		sc.setOptions(InfoNames.INFO, Constants.SHAPE_HEXAGON,
				Color.CYAN, 6,Color.CYAN);
		
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		
		List<Object> props = sc.getMetaboliteProperties();
		for(Object o : props)
			System.out.println(o.getClass());
	}


	@Override
	public int getRowHeight(int row) {
		Object v = table.getValueAt(row, 3);
		int ret = (v==null) ? 0 : (Integer) v;
		isChanged = true;
		return ret * 2;
	}	
}
