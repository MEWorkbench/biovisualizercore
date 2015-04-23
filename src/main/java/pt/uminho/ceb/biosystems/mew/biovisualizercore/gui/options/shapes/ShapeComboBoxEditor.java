package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.shapes;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes.NodeDraw;

public class ShapeComboBoxEditor implements ComboBoxEditor{
	
	protected Object item;
	
	public ShapeComboBoxEditor() {
		super();
	}
	
	@Override
	public void setItem(Object anObject) {
		this.item = anObject;
	}

	@Override
	public Component getEditorComponent() {
		return (NodeDraw) item;
	}

	@Override
	public Object getItem() {
		return item;
	}

	@Override
	public void selectAll() {}

	@Override
	public void addActionListener(ActionListener l) {}

	@Override
	public void removeActionListener(ActionListener l) {}
	

}
