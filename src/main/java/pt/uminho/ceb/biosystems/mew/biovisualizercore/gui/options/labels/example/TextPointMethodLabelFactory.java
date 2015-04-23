package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.example;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public class TextPointMethodLabelFactory implements IMethodLabelFactory<Point>{

	@Override
	public String getInfo(Point p, String attibuteId) {
		if(attibuteId.equals("id"))
			return p.getId();
		return p.getComment();
	}

}
