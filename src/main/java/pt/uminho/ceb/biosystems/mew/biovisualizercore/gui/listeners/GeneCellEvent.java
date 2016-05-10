package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners;

import java.awt.event.ActionEvent;

import net.java.balloontip.TableCellBalloonTip;

public class GeneCellEvent extends ActionEvent {
	
	public static final String	TOOLTIP_OPENED_EVENT	= "tooltipOpened";
	public static final String	TOOLTIP_OPENING_EVENT	= "tooltipOpening";
	private static final long	serialVersionUID		= 1L;
	
	public GeneCellEvent(Object source, int id, String command) {
		super(source, id, command);
	}

	public TableCellBalloonTip getTooltip() {
		return (TableCellBalloonTip) source;
	}

	
}
