package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes.NodeDraw;

public class ReactionShapeCellRenderer extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 5962934523653529513L;
	
	protected NodeDraw nd = new NodeDraw(10, 10);
	protected JTextField fillColorField = new JTextField();
	protected JTextField strokeColorField = new JTextField();
	protected NodeDraw previewNd = new NodeDraw(10, 10);
	
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(row==0)
		{
			Integer s = (Integer) value;
			if(s==null)
				s=-1;
			nd.setShape(s);
			return nd;
		}
		if(row==1)
		{
			Color c = null;
			try {
				c = (Color) value;
			} catch (Exception e) {
				c = Color.orange;
				e.printStackTrace();
			}
			fillColorField.setBackground(c);
			return fillColorField;
		}
		if(row==3)
		{
			Color c = null;
			try {
				c = (Color) value;
			} catch (Exception e) {
				c = Color.orange;
				e.printStackTrace();
			}
			strokeColorField.setBackground(c);
			return strokeColorField;
		}
		if(row==4)
		{
			Integer s = (Integer) table.getValueAt(0, column);
			if(s==null)
				s=-1;
			previewNd.setShape(s);
			Color fillColor = (Color) table.getValueAt(1, column);
			
			previewNd.setColor(fillColor);
			
			Double size = (double) ((Integer) table.getValueAt(2, column));
			
			previewNd.setShapeHeight(size);
			previewNd.setShapeWidth(size);
			
			return previewNd;
		}
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
