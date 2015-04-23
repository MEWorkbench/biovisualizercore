package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners;

import java.util.EventObject;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class OverlapEvent extends EventObject{

	private static final long	serialVersionUID	= 1L;
	
	private IOverlapObject _selectedOverlap = null;
	
	public OverlapEvent(Object source, IOverlapObject overlap) {
		super(source);	
		_selectedOverlap = overlap;
	}

	public IOverlapObject get_selectedOverlap() {
		return _selectedOverlap;
	}

	public void set_selectedOverlap(IOverlapObject _selectedOverlap) {
		this._selectedOverlap = _selectedOverlap;
	}

	
}
