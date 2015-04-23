package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class LabelConstructorListCellRenderer implements ListCellRenderer{

	LabelAndSeparatorComponent c = new LabelAndSeparatorComponent(new Object[]{" ", "," ,";", "_", "\n", ""});
	@Override
	public Component getListCellRendererComponent(JList arg0, Object arg1,
			int arg2, boolean arg3, boolean arg4) {
		
		Pair<String, String> p = (Pair<String, String>)arg1;
		
		c.setSeparator(p.getB());
		c.setMethodId(p.getA());
		
		return c;
	}

}
