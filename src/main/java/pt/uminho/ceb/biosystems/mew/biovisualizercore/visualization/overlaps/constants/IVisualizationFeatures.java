package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants;

import java.util.Set;

public interface IVisualizationFeatures<T extends Enum<T>> {

	boolean hasConfigurationOptions();
	Set<T> getValuesSet();
	String getName();
	T getValueOf(String s);
}
