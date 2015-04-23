package pt.uminho.ceb.biosystems.mew.biovisualizercore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLReader;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers.ModelPathwayGenerator;

public class GeneratePathwayTests {
	
	LayoutContainer layoutContainerBase, newLayoutContainer;
	
	@Before
	public void generatePathwayAsBase() throws Exception {
		JSBMLReader reader = new JSBMLReader("../metabolic3/files/models/ecoli_core_model.xml", "1",false);
		
		Container cont = new Container(reader);
		Set<String> met = cont.identifyMetabolitesIdByPattern(Pattern.compile(".*_b"));
		
		Map<String, ReactionCI> pathWayReactions = new HashMap<String, ReactionCI>();
		
		for (String string : cont.getReactions().keySet()) {
			pathWayReactions.put(string, cont.getReactions().get(string));
		}
		//pathWayReactions = cont.getReactionsByPathway(cont.getPathwaysIDs().first());
		
		ModelPathwayGenerator mgenerator = new ModelPathwayGenerator(pathWayReactions, cont);
		
		layoutContainerBase = mgenerator.buildLayout();
		
		newLayoutContainer = createNewLayoutUsingBase(layoutContainerBase, cont);
		
//		for (String string : layoutContainerBase.getNodes().keySet()) {
//			System.out.println("Node: " + string + "\nINodeLay: " + layoutContainerBase.getNodes().get(string));
//			System.out.println("NodeName: " + layoutContainerBase.getNodes().get(string).getLabel());
//		}
//		
//		System.out.println("------------------------------------------------------");
//		
//		for (String string : newLayoutContainer.getNodes().keySet()) {
//			System.out.println("Node: " + string + "\nINodeLay: " + newLayoutContainer.getNodes().get(string));
//			System.out.println("NodeName: " + newLayoutContainer.getNodes().get(string).getLabel());
//		}
	}

	@Test
	public void nodeAreDifferent() throws Exception {
		Assert.assertEquals(true, layoutsHaveDifferentNodesMemIndex(layoutContainerBase, newLayoutContainer));
	}
	
	@Test
	public void nodeChange() throws Exception {
		changeNodePosition(layoutContainerBase, newLayoutContainer);
	}
	
	private LayoutContainer createNewLayoutUsingBase(LayoutContainer layoutContainerBase, Container cont){
		
		ModelPathwayGenerator mgenerator = new ModelPathwayGenerator(cont.getReactionsByPathway(cont.getPathwaysIDs().first()),
											   cont, 
											   layoutContainerBase);
		
		return mgenerator.buildLayout();
	}
	
	private boolean layoutsHaveDifferentNodesMemIndex(LayoutContainer lc1, LayoutContainer lc2){
		for (String node : lc1.getNodes().keySet())
			if(lc1.getNodes().get(node) == lc2.getNodes().get(node))
				return false;
		
		return true;
	}
	
	private void changeNodePosition(LayoutContainer lcToChange, LayoutContainer lcStatic){
		
		for (String string : lcToChange.getNodes().keySet()) {
			lcToChange.getNodes().get(string).setX(1.5);
			lcToChange.getNodes().get(string).setY(9.5);
			System.out.println(string +": X: "+ lcToChange.getNodes().get(string).getX() + "\tY: " + lcToChange.getNodes().get(string).getY());
			System.out.println(string +": X: "+ lcStatic.getNodes().get(string).getX() + "\tY: " + lcStatic.getNodes().get(string).getY());
			break;
		}
		
		for (String string : lcToChange.getReactions().keySet()) {
			lcToChange.getReactions().get(string).setX(991.5);
			lcToChange.getReactions().get(string).setY(119.5);
			System.out.println(string +": X: "+ lcToChange.getReactions().get(string).getX() + "\tY: " + lcToChange.getReactions().get(string).getY());
			System.out.println(string +": X: "+ lcStatic.getReactions().get(string).getX() + "\tY: " + lcStatic.getReactions().get(string).getY());
			break;
		}
		
		for (String string : lcToChange.getNodes().keySet()) {
			lcStatic.getNodes().get(string).setX(1.5);
			lcStatic.getNodes().get(string).setY(9.5);
			System.out.println(string +": X: "+ lcStatic.getNodes().get(string).getX() + "\tY: " + lcStatic.getNodes().get(string).getY());
			break;
		}
		
		for (String string : lcToChange.getReactions().keySet()) {
			lcStatic.getReactions().get(string).setX(991.5);
			lcStatic.getReactions().get(string).setY(119.5);
			System.out.println(string +": X: "+ lcStatic.getReactions().get(string).getX() + "\tY: " + lcStatic.getReactions().get(string).getY());
			break;
		}
	}
	
}
