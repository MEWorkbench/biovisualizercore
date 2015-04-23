package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


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
public class MultipleCheckPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Set<String> ceckLabels;
	private ActionListener actionListener;

	private JCheckBox[] checkBoxes;
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Set<String> set = new HashSet<String>();
		set.add("CHECK 1");
		set.add("CHECK 2");
		set.add("CHECK 3");
		set.add("CHECK 4");
		set.add("CHECK 5");
		
		ActionListener a = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
			}
		};
		
		frame.getContentPane().add(new MultipleCheckPanel(set, a));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	
	}
	
	public MultipleCheckPanel(Set<String> checkLabels, ActionListener a) {
		super();
		
		this.ceckLabels = new HashSet<String>();
		this.ceckLabels.addAll(checkLabels);
		this.actionListener = a;
		
		initGUI();
	}
	
	private void initGUI() {
		try {
			
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0};
			thisLayout.columnWidths = new int[] {7};
			thisLayout.rowWeights = new double[] {0.0};
			thisLayout.rowHeights = new int[] {7};

			this.setLayout(thisLayout);
			int dimension = this.ceckLabels.size()+1;
			
			thisLayout.rowWeights = new double[dimension];
			thisLayout.rowHeights = new int[dimension];

			for(int i = 0; i<dimension-1;i++){
				thisLayout.rowWeights[i] = 0;
				thisLayout.rowHeights[i] = 7;
			}
			thisLayout.rowWeights[dimension-1] = 0;
			thisLayout.rowHeights[dimension-1] = 7;

			thisLayout.columnWeights = new double[] {0};
			thisLayout.columnWidths = new int[] {7};

			{
				int counter = 0;
				if(this.ceckLabels.size()>0){
					this.checkBoxes = new JCheckBox[this.ceckLabels.size()];
					for(String s : this.ceckLabels){

						JCheckBox box = new JCheckBox();
						this.add(box, new GridBagConstraints(0, counter, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						box.setText(s);
						box.addActionListener(this.actionListener);
						box.setActionCommand("Check selected");
						checkBoxes[counter] = box;
						counter++;
					}
				}

			}
			
	} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Set<String> getSelectedChecks(){
		Set<String> result = new HashSet<String>();

		if(this.checkBoxes!=null){
			for(JCheckBox jc : this.checkBoxes){
				if(jc.isSelected()) result.add(jc.getText());
			}
		}
		return result;
	}
	
	
	public void setSelected(String checkText, boolean selected){
		
		for(JCheckBox jc : this.checkBoxes){
			if(jc.getText().equals(checkText)) jc.setSelected(selected);
		}
	}
}
