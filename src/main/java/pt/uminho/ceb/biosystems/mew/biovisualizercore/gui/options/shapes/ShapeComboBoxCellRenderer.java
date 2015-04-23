package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes.NodeDraw;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.Constants;

public class ShapeComboBoxCellRenderer extends DefaultListCellRenderer{
	
	private static final long serialVersionUID = -2012508798680987652L;
	
	protected NodeDraw nd;
	
	
	public ShapeComboBoxCellRenderer(){
		super();
		nd = new NodeDraw();
	}
	
	
	/** @param value is the shape code */
	public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus){  
		nd.setShape((Integer) value);
		return nd;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("RunNodesComboBox");
		
		Integer[] values = new Integer[]{Constants.SHAPE_QUESTION, Constants.SHAPE_HEXAGON, Constants.SHAPE_QUESTION,Constants.SHAPE_QUESTION, Constants.SHAPE_HEXAGON, Constants.SHAPE_QUESTION};
		JComboBox<Integer> nodesBox = new JComboBox<Integer>(values);
		
		nodesBox.setRenderer(new ShapeComboBoxCellRenderer());
		
		JPanel p = new JPanel();
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1f, 0.1f, 0.1f};
		thisLayout.columnWeights = new double[] {1.0f};
		p.setLayout(thisLayout);
		
		p.add(nodesBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
		p.add(new JLabel("-----"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
		
		frame.setContentPane(p);
		
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setSize(300, 100);
	}
}