package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants;

import java.util.HashSet;
import java.util.Set;

public enum NodesFeatures implements IVisualizationFeatures<NodesFeatures>{

	LABEL {
		@Override
		public boolean hasConfigurationOptions() {
			return false;
		}
		
//		@Override
//		public AbstractConfigurationPanel getConfigurationPanelClass() {
//			return null;
//		}
	},
	SIZE {
		@Override
		public boolean hasConfigurationOptions() {
			return true;
		}
		
//		@Override
//		public AbstractConfigurationPanel getConfigurationPanelClass() {
//			return new GenericMinMaxValueSpinnerPanel(3,20,"Minimum Size","Maximum Size");
//		}
	},
	COLOR {
		@Override
		public boolean hasConfigurationOptions() {
			return true;
		}
		
//		@Override
//		public AbstractConfigurationPanel getConfigurationPanelClass() {
//			return new ColorPanelWithPreview();
//		}
	};
	
	public abstract boolean hasConfigurationOptions();
//	public abstract AbstractConfigurationPanel getConfigurationPanelClass();
	
	public Set<NodesFeatures> getValuesSet() {
		Set<NodesFeatures> toret = new HashSet<NodesFeatures>();
		for (NodesFeatures e : values())
			toret.add(e);
		return toret;
	}
	
	public String getName(){
		return name();
	}
	
	public NodesFeatures getValueOf(String string){
		return valueOf(string);
	}
}
