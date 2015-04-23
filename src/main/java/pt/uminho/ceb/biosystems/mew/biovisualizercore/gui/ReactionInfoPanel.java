package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

/**
 * Reaction information panel that appears in the {@link InformationPanel}
 * @author Alberto Noronha
 *
 */
public class ReactionInfoPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel reactionNameLabel;
	private JLabel jLabel2;
	private JTextField layoutLabelText;
	private JList metabolicIdsList;
	private JLabel jLabel3;
	
	
	private Set<String> idReaction;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ReactionInfoPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ReactionInfoPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.1};
			thisLayout.columnWidths = new int[] {7, 7};
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createCompoundBorder(
					null, 
					null));
			{
				reactionNameLabel = new JLabel();
				this.add(reactionNameLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
				reactionNameLabel.setText("");
				
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel2.setText("Label");
			}
			{
				layoutLabelText = new JTextField();
				this.add(layoutLabelText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 2, 0), 0, 0));
				layoutLabelText.setEditable(false);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel3.setText("Metabolic reactions");
			}
			{
				ListModel metabolicIdsListModel = 
						new DefaultListModel();
				metabolicIdsList = new JList();
				JScrollPane met_scrollpane = new JScrollPane(metabolicIdsList);
				this.add(met_scrollpane, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
				metabolicIdsList.setModel(metabolicIdsListModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void putReactionInfo(IReactionLay r) {
		
		this.idReaction = r.getIDs();
//		layoutIdText.setText(r.getUniqueId());
		layoutLabelText.setText(r.getLabel());
		updateInfo();
		
	}
	
	public void updateInfo(){
		if(idReaction!=null){
			DefaultListModel model = (DefaultListModel) metabolicIdsList.getModel();
			model.clear();
			int i = 0;
			for(String s : idReaction){
				
				model.add(i, s);
				i++;
			}
		}
	}

}
