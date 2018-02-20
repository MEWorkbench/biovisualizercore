package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.MetaboliteListener;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.MapUtils;

public class MetaboliteInformationPanel extends JPanel implements MetaboliteListener{

	JTable tableInfo;
	ImagePanel imagaPanel;
	public MetaboliteInformationPanel(IImageProvider provider){
		super();
		initGUI(provider);
	}
	
	
	private void initGUI(IImageProvider provider) {
		
		tableInfo = new JTable();
		imagaPanel = new ImagePanel(provider);
		
		setLayout(new GridLayout(1, 1));
		JScrollPane scrollPane = new JScrollPane(tableInfo);
		JPanel l = new JPanel();
		l.setLayout(new GridLayout(1, 1));
		l.add(imagaPanel);
		
		
		Dimension minimumSize = new Dimension(200, 200);
		scrollPane.setMinimumSize(minimumSize);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(scrollPane);
		splitPane.setBottomComponent(imagaPanel);
		splitPane.setOneTouchExpandable(true);
		
		add(splitPane);
		
	}


	@Override
	public void metaboliteChanged(MetaboliteEvent event) {
		populateTable(event);
		HashSet<String> s = new HashSet<String>();
		
		
		s.add(event.getContainer().getMetabolitesExtraInfo().get("IMAGE_FILE").get(event.getUuid()));
		
//		MapUtils.prettyPrint(event.getContainer().getMetabolitesExtraInfo().get("IMAGE_FILE"));
		System.out.println(event.getUuid());
		System.out.println(s);
		imagaPanel.populateImages(s);
	}

	private void populateTable(MetaboliteEvent event) {
		
		DefaultTableModel dataModel = new DefaultTableModel();
		dataModel.setColumnIdentifiers(new String[]{"TAG", "Information"});
		dataModel.addRow(new Object[]{"VisUUId", event.getUuid()});
		dataModel.addRow(new Object[]{"VisLabel", event.getLabel()});
		dataModel.addRow(new Object[]{"Cont Ids", event.getIds()});
		
		String idToMore = event.getUuid();
		MetaboliteCI mci = event.getContainer().getMetabolite(event.getUuid());
		System.out.println(mci);
		System.out.println(mci.getId());
	
		dataModel.addRow(new Object[]{"Name", mci.getName()});
		dataModel.addRow(new Object[]{"Formula", mci.getFormula()});
		
		System.out.println(event.getContainer().getMetabolites().keySet());
		Map<String, Map<String, String>> info = event.getContainer().getMetabolitesExtraInfo();
		for(String id : info.keySet()){
			
			Object value = null;
			try {
				value = info.get(id).get(idToMore);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(id+"\t"+value+ "\t" + idToMore);
			if(value !=null)
				dataModel.addRow(new Object[]{id, value});
		}
		
		tableInfo.setModel(dataModel);
		
//		tableInfo.updateUI();
//		updateUI();
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame("ole");
		
		JPanel j = new MetaboliteInformationPanel(null);
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
