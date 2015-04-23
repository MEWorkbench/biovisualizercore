package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.FontManager;
import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.RenderingFont;

public class ReactionLabelCellRenderer extends DefaultTableCellRenderer{
	
	
	protected JLabel fontLabel = new JLabel();
	protected JLabel styleLabel = new JLabel();
	protected JLabel sizeLabel = new JLabel();
	protected JTextField colorField = new JTextField();
	protected JTextArea previewArea = new JTextArea();
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if(row==0)
		{
			try {
				RenderingFont f = (RenderingFont) value;
				fontLabel.setText(f.toString());
				fontLabel.setFont(f);
			} catch (Exception e) {e.printStackTrace();}
			return fontLabel;
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
			colorField.setBackground(c);
			return colorField;
		}
		if(row==5)
		{
			String text = (String) table.getValueAt(4, column).toString();
			Font f = (Font) table.getValueAt(0, column);
			Color c = (Color)table.getValueAt(3, column);
			int size = (Integer)table.getValueAt(2, column);
			int style = FontManager.convertStyle((String)table.getValueAt(1, column));
			
			Font pF = new Font(f.getName(), style, size);

			previewArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
			previewArea.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
			previewArea.setText(text);
			previewArea.setForeground(c);
			previewArea.setFont(pF);
			return previewArea;
		}
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
