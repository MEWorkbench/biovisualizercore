package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.pathwaynavigation;

import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.JTableUtils;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.ButtonEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.cellrender.ButtonRenderer;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteListener;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class PathwayNavigationPanel extends JPanel implements MetaboliteListener {
	
	private static final long			serialVersionUID	= 1L;
	private Map<String, Set<String>>	_pathways;
	private JScrollPane					_pathwayTableScroll;
	private JTable						_pathwayTable;
	private TableModelListener			_tableListener;
	
	public PathwayNavigationPanel(Map<String, Set<String>> pathways) {
		super();
		_pathways = pathways;
		initGUI();
	}
	
	private void initGUI() {
		
		setLayout(new BorderLayout());
		_pathwayTableScroll = new JScrollPane();
		_pathwayTable = new JTable();
		_pathwayTableScroll.setViewportView(_pathwayTable);
		
		add(_pathwayTableScroll, BorderLayout.CENTER);
		_pathwayTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
	}
	
	public void setTableModelListener(TableModelListener listener) {
		_tableListener = listener;
	}
	
	@Override
	public void metaboliteChanged(MetaboliteEvent event) {
		Set<String> ids = event.getIds();
		if (ids != null && !ids.isEmpty()) updateInfo(ids);
	}
	
	public void updateInfo(Set<String> metaboliteIDs) {
		Set<String> pathwaysSet = new HashSet<String>();
		
		String[] t2 = { "Pathway", "" };
		DefaultTableModel tm2 = new DefaultTableModel() {
			private static final long	serialVersionUID	= -329979760397003009L;
			
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 0) return false;
				return true;
			}
		};
		tm2.setColumnIdentifiers(t2);
		
		for (String id : metaboliteIDs) {
			if (_pathways.get(id) != null) {
				for (String path : _pathways.get(id)) {
					
					if (!pathwaysSet.contains(path)) {
						pathwaysSet.add(path);
						String[] row = new String[2];
						row[0] = path;
						tm2.addRow(row);
					}
				}
			}
		}
		
		_pathwayTable.getModel().removeTableModelListener(_tableListener);
		
		_pathwayTable.setModel(tm2);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("biovisualizer/images/layout.png"));
		JTableUtils.setCellRender(_pathwayTable, 1, new ButtonRenderer(icon));
		JTableUtils.setCellEditor(_pathwayTable, 1, new ButtonEditor(icon));
		JTableUtils.setColumnWidth(_pathwayTable, 1, 30);
		JTableUtils.setRowHeight(_pathwayTable, 30);
		
		tm2.addTableModelListener(_tableListener);
		
		//		populatetable = false;
		
		_pathwayTable.setPreferredScrollableViewportSize(_pathwayTable.getMinimumSize());
		_pathwayTable.setFillsViewportHeight(true);
		_pathwayTable.setPreferredScrollableViewportSize(_pathwayTable.getMinimumSize());
		_pathwayTable.setFillsViewportHeight(false);
		this.updateUI();
	}
}
