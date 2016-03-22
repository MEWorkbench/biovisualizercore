package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public enum EdgesFeatures implements IVisualizationFeatures<EdgesFeatures>, Serializable {
	
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
	THICKNESS {
		@Override
		public boolean hasConfigurationOptions() {
			return true;
		}
		
//		@Override
//		public AbstractConfigurationPanel getConfigurationPanelClass() {
//			return new GenericMinMaxValueSpinnerPanel(1,10,"Minimum Thickness","Maximum Thickness");
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
	
	public Set<EdgesFeatures> getValuesSet() {
		Set<EdgesFeatures> toret = new HashSet<EdgesFeatures>();
		for (EdgesFeatures e : values())
			toret.add(e);
		return toret;
	}
	
	public String getName(){
		return name();
	}
	
	public EdgesFeatures getValueOf(String string){
		return valueOf(string);
	}
}
