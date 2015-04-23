package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteListener;

public class MetaboliteExtraInfoTabbedPanel<T extends JComponent & MetaboliteListener> extends JPanel {
	
	private static final long	serialVersionUID		= 1L;
	
	protected JTabbedPane		_componentsTabbedPane	= null;
	protected Map<String, T>	_componentsMap			= null;
	
	public MetaboliteExtraInfoTabbedPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		_componentsTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		add(_componentsTabbedPane, BorderLayout.CENTER);
	}
	
	public Map<String, T> getComponentsMap() {
		if (_componentsMap == null) _componentsMap = new IndexedHashMap<String, T>();
		return _componentsMap;
	}
	
	public void populateTabs() {
		_componentsTabbedPane.removeAll();
		for (String id : _componentsMap.keySet()) {
			_componentsTabbedPane.addTab(id, _componentsMap.get(id));
		}
	}
	
	public void addComponent(String id, T component) {
		getComponentsMap().put(id, component);
	}
	
}
