package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.generule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.TableCellBalloonTip;
import net.java.balloontip.positioners.LeftAbovePositioner;
import net.java.balloontip.styles.EdgedBalloonStyle;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.GeneCellEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.GeneCellListener;

public class GeneViewTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	
	private static final long	serialVersionUID	= 1L;
	protected JToggleButton		button;
	protected GeneCellObject	value;
	protected Container			container;
	protected JTable			table;
	protected int				row;
	protected int				column;
	protected int				prevRow				= -1;
	protected int				prevCol				= -1;
	private BalloonTip			tableBalloon;
	private GeneCellListener	listener;
	
	public GeneViewTableCellEditor(Container container, GeneCellListener listener) {
		this.container = container;
		this.button = new JToggleButton();
		this.button.setOpaque(true);
		this.listener = listener;
		button.addActionListener(this);
		
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(Color.BLACK);
			button.setBackground(Color.RED);
			button.setOpaque(true);
			button.setBorderPainted(false);
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
			button.setOpaque(true);
			button.setBorderPainted(false);
		}
		
		this.value = (GeneCellObject) value;
		this.table = table;
		this.row = row;
		this.column = column;
		return button;
	}
	
	public Object getCellEditorValue() {
		return value;
	}
	
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}
	
	public void cancelCellEditing() {
		super.cancelCellEditing();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("PRESSED CELL [" + row + "," + column + "] = " + value.toString());
		if (value != null && (row!=prevRow || column!=prevCol)) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					fireTooltipOpening();
					GeneRuleJPanel panel = new GeneRuleJPanel(value.getAst(), value.getConversion(), value.getGeneValues(), container);
					JScrollPane scroll = new JScrollPane(panel) {
						
						private static final long	serialVersionUID	= 1L;
						
						@Override
						public Dimension getPreferredSize() {
							Dimension preferred = super.getPreferredSize();
							Dimension maximum = getMaximumSize();
							
							int width= (int) ((preferred.getWidth() > maximum.getWidth()) ? maximum.getWidth() : preferred.getWidth());
							int height= (int) ((preferred.getHeight() > maximum.getHeight()) ? maximum.getHeight() : preferred.getHeight());
							
							return new Dimension(width+20,height+20);
						}
					};
					scroll.setBackground(Color.WHITE);
					scroll.setViewportView(panel);
					scroll.setMaximumSize(new Dimension((int) (500 * 1.6), 500));
					
					tableBalloon = new TableCellBalloonTip(table, scroll, row, column, new EdgedBalloonStyle(Color.WHITE, Color.BLUE), new LeftAbovePositioner(40, 20), null);
					tableBalloon.setPadding(5);
					tableBalloon.setCloseButton(BalloonTip.getDefaultCloseButton(), true);
					fireTooltipOpened();
					fireEditingStopped();
					prevRow = row;
					prevCol = column;
				}
			});
		} else {
			closeTooltip();
		}
	}
	
	public void closeTooltip() {
		if (tableBalloon != null) {
			tableBalloon.closeBalloon();
			tableBalloon.setEnabled(false);
			tableBalloon = null;
			prevCol = -1;
			prevRow = -1;
			fireEditingStopped();
		}
	}
	
	public void fireTooltipOpened() {
		if (listener != null) {
			GeneCellEvent event = new GeneCellEvent(tableBalloon, 0, GeneCellEvent.TOOLTIP_OPENED_EVENT);
			listener.fireGeneCellEvent(event);
		}
	}
	
	public void fireTooltipOpening() {
		if (listener != null) {
			GeneCellEvent event = new GeneCellEvent(this, 0, GeneCellEvent.TOOLTIP_OPENING_EVENT);
			listener.fireGeneCellEvent(event);
		}
	}
}
