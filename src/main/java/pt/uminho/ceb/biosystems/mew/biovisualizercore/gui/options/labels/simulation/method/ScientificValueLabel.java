package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.simulation.method;

import java.text.DecimalFormat;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IMethodLabelFactory;
import pt.uminho.ceb.biosystems.mew.mewcore.simulation.components.FluxValueMap;
import pt.uminho.ceb.biosystems.mew.utilities.java.StringUtils;

public class ScientificValueLabel implements IMethodLabelFactory<FluxValueMap>{

	protected int precision = 2; 
	
	public void setPrecision(int precision){
		this.precision = precision;
	}
	
	@Override
	public String getInfo(FluxValueMap map, String attributeId) {
		
		Double v = map.get(attributeId);
		String ret = "N/A";
		if (v!=null && !Double.isNaN(v)){
//        	if(v==0)
//        		return v+"";
			v = Math.abs(v);
        	DecimalFormat form = new DecimalFormat();
    		form.setMaximumFractionDigits(precision);
    		form.setMinimumFractionDigits(1);
    		form.setGroupingUsed(false);
    		ret = form.format(v);
    		
    		Double t = 0.0;
    		try{
    			t = Double.parseDouble(ret);
    		}catch (Exception e) {
//				e.printStackTrace();
//				System.out.println(value + "\t" + t);
			}
    		
    		if(t==0 && v!=0){
//			    			form.setGroupingUsed(true);
    			form=new DecimalFormat("0."+ StringUtils.repeat("#", precision) + "E0");
    			ret = form.format(v);
    		}

		
		}
		return ret;
	}
}
