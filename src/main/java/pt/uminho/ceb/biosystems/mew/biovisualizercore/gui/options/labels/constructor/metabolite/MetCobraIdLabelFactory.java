package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;

public class MetCobraIdLabelFactory extends AbstractMetMethodLabelFactory{

	static Pattern p = Pattern.compile("M_(.*)_.");
	
	@Override
	public String getInfo(Container container, String metaboliteId) {
		Matcher m = p.matcher(metaboliteId);
		String id = metaboliteId;
		if(m.matches()) id = m.group(1);
		return id;
	}

}
