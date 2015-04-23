package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners;

import java.util.EventObject;
import java.util.Set;

public class MetaboliteEvent extends EventObject {

	private static final long	serialVersionUID	= 1L;
	private Set<String> _ids = null;
	
	public MetaboliteEvent(Object source, Set<String> ids) {
		super(source);
		_ids = ids;
	}

	public Set<String> getIds() {
		return _ids;
	}

	public void setIds(Set<String> ids) {
		this._ids = ids;
	}
	
}
