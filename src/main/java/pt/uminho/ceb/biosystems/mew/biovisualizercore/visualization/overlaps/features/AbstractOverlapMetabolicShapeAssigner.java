package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.features;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.MetabolicOverlapConversionFactory;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public abstract class AbstractOverlapMetabolicShapeAssigner extends AbstractOverlapShapeAssigner<Integer> {

	@Override
	protected Integer getShape(Double value) {
		Pair<String, Double> pair = new Pair<>("", value);
		return MetabolicOverlapConversionFactory.getShape(pair);
	}

}
