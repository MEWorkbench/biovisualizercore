package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.ReactionListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ReactionEvent;

public class ReactionInformationPanel extends JPanel implements ReactionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable tableInfo;
	
	public ReactionInformationPanel(){
		super();
		initGUI();
	}
	
	
	private void initGUI() {
		
		tableInfo = new JTable();
		
		setLayout(new GridLayout(1, 1));
		JScrollPane scrollPane = new JScrollPane(tableInfo);
//		JPanel l = new JPanel();
//		l.setLayout(new GridLayout(1, 1));
//		l.add(imagaPanel);
		
		
		Dimension minimumSize = new Dimension(200, 200);
		scrollPane.setMinimumSize(minimumSize);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(scrollPane);
//		splitPane.setBottomComponent(imagaPanel);
		splitPane.setOneTouchExpandable(true);
		
		add(splitPane);
		
	}


	@Override
	public void reactionChanged(ReactionEvent event) {
		populateTable(event);
	}

	private void populateTable(ReactionEvent event) {
		
		DefaultTableModel dataModel = new DefaultTableModel();
		dataModel.setColumnIdentifiers(new String[]{"TAG", "Information"});
		dataModel.addRow(new Object[]{"VisUUId", event.getUuid()});
		dataModel.addRow(new Object[]{"VisLabel", event.getLabel()});
		dataModel.addRow(new Object[]{"Cont Ids", event.getIds()});
		
		String idToMore = event.getUuid();
		ReactionCI mci = event.getContainer().getReaction(event.getUuid());

	
		dataModel.addRow(new Object[]{"Name", mci.getName()});
//		dataModel.addRow(new Object[]{"Formula", mci.getFormula()});
		
		Map<String, Map<String, String>> info = event.getContainer().getMetabolitesExtraInfo();
		for(String id : info.keySet()){
			
			Object value = null;
			try {
				value = info.get(id).get(idToMore);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(value !=null)
				dataModel.addRow(new Object[]{id, value});
		}
		
		tableInfo.setModel(dataModel);
		
//		tableInfo.updateUI();
//		updateUI();
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame("ole");
		
		JPanel j = new ReactionInformationPanel();
//		j.setLayout(new GridLayout(1, 1));
//		JTable tableInfo = new JTable();
//		imagaPanel = new ImagePanel(provider);
		
//		JScrollPane scrollPane = new JScrollPane(tableInfo);
//		j.add(scrollPane);
		
		j.setBackground(Color.BLUE);
		f.getContentPane().add(j);
		f.setSize(new Dimension(200, 200));
		f.setVisible(true);
		
	}

}
