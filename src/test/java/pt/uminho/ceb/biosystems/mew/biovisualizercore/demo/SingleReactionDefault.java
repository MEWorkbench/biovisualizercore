package pt.uminho.ceb.biosystems.mew.biovisualizercore.demo;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.StandaloneVisualization;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers.BioVisualizerConvEdgeRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class SingleReactionDefault {

	
	public static void main(String[] args) throws Exception {
		LayoutContainer cont = new LayoutContainer();


		Map<String, StoichiometryValueCI> products = new HashMap<String, StoichiometryValueCI>();
		Map<String, StoichiometryValueCI> reactants= new HashMap<String, StoichiometryValueCI>();
		reactants.put("A", new StoichiometryValueCI("A", 1.0, "c"));
		reactants.put("a", new StoichiometryValueCI("a", 1.0, "c"));
		products.put("B", new StoichiometryValueCI("B", 1.0, "c"));
		products.put("b", new StoichiometryValueCI("b", 1.0, "c"));
		ReactionCI r = new ReactionCI("R", "R", false, reactants, products);
		
		BioVisualizerConvEdgeRenderer.EDGE_DRAW_TYPE = BioVisualizerConvEdgeRenderer.EDGE_DEFAULT_DRAW_TYPE;
		IReactionLay reaction =  LayoutUtils.converReactionCItoIReactionLay(r, cont);
		
		StandaloneVisualization ole = new StandaloneVisualization(new Container());
		
		ole.addLayout("test", cont);
		ole.addReaction(reaction);
		
	}
	
	
}
