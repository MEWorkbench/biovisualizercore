package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.example;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public class ValuePointMethodLabelFactory implements IMethodLabelFactory<Point>{

	@Override
	public String getInfo(Point p, String attibuteId) {
		if(attibuteId.equals("x"))
			return String.valueOf(p.getxValue());
		return String.valueOf(p.getyValue());
	}

}
