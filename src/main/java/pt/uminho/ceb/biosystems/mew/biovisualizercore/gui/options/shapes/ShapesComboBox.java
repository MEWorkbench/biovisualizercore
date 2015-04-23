package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes.NodeDraw;

public class ShapesComboBox extends JComboBox<Integer>{
	
	private static final long serialVersionUID = -6551770850057845607L;

	
	public ShapesComboBox(Object[] shapeCodes){
		super();
		setRenderer(new ShapeComboBoxCellRenderer());
		setEditable(true);
//		setEditor(new ShapeComboBoxEditor());
		buildComboModel(shapeCodes);
//		setSelectedFont(fontName);
	}
	
	
	public NodeDraw getSelectedShape(){
		NodeDraw nd = new NodeDraw();
		nd.setShape((Integer) getSelectedItem());
		return nd;
	}
	
	protected void buildComboModel(Object[] shapeCodes){
		setModel(new DefaultComboBoxModel<Integer>((Integer[]) shapeCodes));
	}
	
	@Override
	public ListCellRenderer getRenderer() {
		return new ShapeComboBoxCellRenderer();
	}
}
