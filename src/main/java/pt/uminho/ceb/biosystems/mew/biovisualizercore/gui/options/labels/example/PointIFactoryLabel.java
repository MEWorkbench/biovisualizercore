package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.example;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;

public class PointIFactoryLabel extends IFactoryLabel<Point>{
	
	public PointIFactoryLabel() {
		super();
		IMethodLabelFactory<Point> valueFactory = new ValuePointMethodLabelFactory();
		IMethodLabelFactory<Point> textFactory = new TextPointMethodLabelFactory();
		
		addLabelMethod("id", textFactory);
		addLabelMethod("comment", textFactory);
		addLabelMethod("x", valueFactory);
		addLabelMethod("y", valueFactory);
	}

}
