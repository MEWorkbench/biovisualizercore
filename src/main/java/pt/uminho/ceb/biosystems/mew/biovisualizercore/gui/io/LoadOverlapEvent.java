package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io;

import java.util.EventObject;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class LoadOverlapEvent extends EventObject{
	
	private static final long	serialVersionUID	= 1L;
	private IOverlapObject _overlapObject;

	public LoadOverlapEvent(Object source,IOverlapObject overlap) {		
		super(source);
		_overlapObject = overlap;
	}

	public IOverlapObject get_overlapObject() {
		return _overlapObject;
	}

	
}
