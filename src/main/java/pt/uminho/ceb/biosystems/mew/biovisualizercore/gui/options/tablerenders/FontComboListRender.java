package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.tablerenders;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class FontComboListRender extends DefaultListCellRenderer {

	
	JLabel label = new JLabel();
	@Override
	public Component getListCellRendererComponent(JList arg0, Object arg1,
			int arg2, boolean arg3, boolean arg4) {
		Font f = (Font) arg1;
		this.setFont(f);
		this.setText(f.getName());
		return this;
//		label.setFont(f);
//		label.setText(f.getName());
//		return label;
	}

	
//	public Component getListCellRendererComponentnent(JList arg0, Object arg1,
//			int arg2, boolean arg3, boolean arg4) {
//		
//		Font f = (Font) arg1;
//		this.setFont(f);
//		this.setText(f.getName());
//		
//		System.out.println("OLE\t\t" + arg1);
//		return this;
//	}
}
