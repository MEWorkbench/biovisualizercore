package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.simulation.FluxValueLabelConstructor;

public class SimulationOverlapProperties {
	
	protected FluxValueLabelConstructor labelConstructor;
	protected double minThickness = 3.0;
	protected double maxThickness = 10;
	protected double zeroThickness = 2;
	
	public SimulationOverlapProperties() {
		this.labelConstructor = new FluxValueLabelConstructor();
		
//		TODO: this should be read on properties
		labelConstructor.addLabelInfo("Scientific Value", "");
	}
	
	
	public FluxValueLabelConstructor getSimulationLabelConstructor(){
		return labelConstructor;
	}
	
	public void setSimulationLabelConstructor(FluxValueLabelConstructor labelConstructor){
		this.labelConstructor = labelConstructor;
	}
	
	public double getMinThickness() {
		return minThickness;
	}
	
	public void setMinThickness(double minThickness) {
		this.minThickness = minThickness;
	}

	public double getMaxThickness() {
		return maxThickness;
	}

	public void setMaxThickness(double maxThickness) {
		this.maxThickness = maxThickness;
	}
	
	public double getZeroThickness() {
		return zeroThickness;
	}
	
	public void setZeroThickness(double zeroThickness) {
		this.zeroThickness = zeroThickness;
	}

}
