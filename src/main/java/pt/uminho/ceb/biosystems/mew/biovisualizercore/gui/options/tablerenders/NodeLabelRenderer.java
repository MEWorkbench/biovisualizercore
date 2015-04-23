package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;

import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.FontManager;

public class NodeLabelRenderer extends DefaultTableCellRenderer{
	
	private JTextArea l = new JTextArea();
	
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
	System.out.println("################################### " + arg4);	
		String text = (String) arg0.getValueAt(arg4, 5).toString();
		Font f = (Font) arg0.getValueAt(arg4, 1);
		Color c = (Color)arg0.getValueAt(arg4, 4);
		int size = (Integer)arg0.getValueAt(arg4, 3);
		int stile = FontManager.convertStyle((String)arg0.getValueAt(arg4, 2));
		
		Font pF = new Font(f.getName(), stile, size);
//		this.setText(text);
//		this.setForeground(c);
//		this.setFont(pF);
//		pF.
		l.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		l.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		l.setText(text);
		l.setForeground(c);
		l.setFont(pF);
		return l;
//		return this;
	}
	
//	public int getHeight(){
//		return fontSize;
//	}

}
