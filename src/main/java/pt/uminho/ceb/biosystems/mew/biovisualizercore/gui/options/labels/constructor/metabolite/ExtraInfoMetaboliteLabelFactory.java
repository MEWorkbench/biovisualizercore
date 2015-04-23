package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

public class ExtraInfoMetaboliteLabelFactory extends AbstractMetMethodLabelFactory{

	private String extraInfoId;
	
	public ExtraInfoMetaboliteLabelFactory(String extraInfoId){
		this.extraInfoId = extraInfoId;
	}
	
	@Override
	public String getInfo(Container Object, String elementId) {
		
		String info = null;
		try {
			info = Object.getMetabolitesExtraInfo().get(extraInfoId).get(elementId);
		} catch (Exception e) {
			
		}
		return info;
	}

}
