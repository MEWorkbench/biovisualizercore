package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.generule;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

public class GeneViewButtonCellRenderer extends JToggleButton implements TableCellRenderer {
	
	private static final long	serialVersionUID	= 1L;

	public GeneViewButtonCellRenderer() {
		setOpaque(true);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected && hasFocus) {
			setForeground(Color.BLACK);
			setBackground(Color.RED);
			setOpaque(true);
			setBorderPainted(false);
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
			setOpaque(true);
			setBorderPainted(false);
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}
