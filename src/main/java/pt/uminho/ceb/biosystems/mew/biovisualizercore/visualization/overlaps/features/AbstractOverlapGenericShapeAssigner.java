package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

public abstract class AbstractOverlapGenericShapeAssigner extends AbstractOverlapShapeAssigner<Integer> {
	
	@Override
	protected Integer getShape(Double value) {
		return value.intValue();
	}


}
