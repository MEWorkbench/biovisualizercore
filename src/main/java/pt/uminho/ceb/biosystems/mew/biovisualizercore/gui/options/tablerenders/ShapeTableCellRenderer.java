package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes.NodeDraw;

public class ShapeTableCellRenderer extends DefaultTableCellRenderer{
	
	
	protected NodeDraw nd = new NodeDraw(10, 10);
	
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		
		Integer s = (Integer)arg1;
		if(s==null)
			s=-1;
		nd.setShape(s);
		return nd;
	}
}
