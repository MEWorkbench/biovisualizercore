package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

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

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ReactionEvent;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.JTableUtils;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.ButtonEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.cellrender.ButtonRenderer;

public class ReactionsPathwayNavigationPanel extends JPanel implements ReactionListener {
	
	private static final long			serialVersionUID	= 1L;
	private Map<String, Set<String>>	_pathways;
	private JScrollPane					_pathwayTableScroll;
	private JTable						_pathwayTable;
	private TableModelListener			_tableListener;
	
	public ReactionsPathwayNavigationPanel(Map<String, Set<String>> pathways) {
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
		_pathwayTableScroll.setPreferredSize(getMinimumSize());
		
	}
	
	public void setTableModelListener(TableModelListener listener) {
		_tableListener = listener;
	}	
	
	public void updateInfo(Set<String> ids) {
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
		
		for (String id : ids) {
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
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/layout.png"));
		JTableUtils.setCellRender(_pathwayTable, 1, new ButtonRenderer(icon));
		JTableUtils.setCellEditor(_pathwayTable, 1, new ButtonEditor(icon));
		JTableUtils.setColumnWidth(_pathwayTable, 1, 30);
		JTableUtils.setRowHeight(_pathwayTable, 30);
		
		tm2.addTableModelListener(_tableListener);
		
		_pathwayTable.setPreferredScrollableViewportSize(_pathwayTable.getMinimumSize());
		_pathwayTable.setFillsViewportHeight(true);
		_pathwayTable.setPreferredScrollableViewportSize(_pathwayTable.getMinimumSize());
		_pathwayTable.setFillsViewportHeight(false);
		this.updateUI();
	}

	@Override
	public void reactionChanged(ReactionEvent event) {
		Set<String> ids = event.getIds();
		if (ids != null && !ids.isEmpty()) updateInfo(ids);
	}
}
