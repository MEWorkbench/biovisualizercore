package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Metabolite information panel, that appears in the {@link InformationPanel}
 * @author Alberto Noronha
 *
 */
public class MetaboliteInfoPanel extends javax.swing.JPanel implements IMetaboliteInfo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel formulaLabel;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JList reactionList;
	private JTextField layoutLabelText;
	private JList metIdsList;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new MetaboliteInfoPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public MetaboliteInfoPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1, 0.0, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.1};
			thisLayout.columnWidths = new int[] {7, 7};
			this.setLayout(thisLayout);
			{
				formulaLabel = new JLabel();
				this.add(formulaLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				formulaLabel.setText("Label");
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
				jLabel2.setText("List of metabolic ids");
			}
			{
				ListModel model = new DefaultListModel();
				metIdsList = new JList();
				JScrollPane met_scrollPane = new JScrollPane(metIdsList);
				this.add(met_scrollPane, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
				metIdsList.setModel(model);
			}
			{
				layoutLabelText = new JTextField();
				this.add(layoutLabelText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
				layoutLabelText.setEditable(false);
			}
//			{
//				jLabel3 = new JLabel();
//				this.add(jLabel3, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				jLabel3.setText("List of reactions");
//			}
//			{
//				ListModel reactionListModel = 
//						new DefaultListModel();
//				reactionList = new JList();
//				JScrollPane react_scrolPane = new JScrollPane(reactionList);
//				this.add(react_scrolPane, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
//				reactionList.setModel(reactionListModel);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that displays the mebaolite information.
	 * @param nodeLay
	 */
	public void putMetaboliteInfo(INodeLay nodeLay) {

//		layoutIdText.setText(nodeLay.getUniqueId());
		layoutLabelText.setText(nodeLay.getLabel());

		DefaultListModel model = (DefaultListModel) metIdsList.getModel();
		model.clear();
		int index = 0;
		for(String s : nodeLay.getIds()){
			model.add(index, s);
			index++;
		}
		
//		DefaultListModel model2 = (DefaultListModel) reactionList.getModel();
//		model2.clear();
		
//		index = 0;
//		for(String s : reactions){
//			model2.add(index, s);
//			index++;
//		}
	}

	@Override
	public void updateInfo() {
		// TODO Auto-generated method stub
		
	}

}
