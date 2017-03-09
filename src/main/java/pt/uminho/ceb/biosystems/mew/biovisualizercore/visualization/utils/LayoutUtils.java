package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils;

import java.awt.Font;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prefuse.data.Schema;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.ComparisonPredicate;
import prefuse.data.expression.ObjectLiteral;
import prefuse.data.expression.Predicate;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.core.simulation.mfa.utils.MathUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;

public class LayoutUtils {
	
	public static final String GRAPH = "graph";
	public static final String NODES = "graph.nodes";
	public static final String EDGES = "graph.edges";
	public static final String EDGE_TYPE = "edgeType";
	public static final String AGGR = "aggregates";
	public static final String TYPE = "type";  
	public static final String MET_T = "metabolite";
	public static final String REA_T = "reaction";
	public static final String HUBS = "mini_met";
	public static final String INFO = "info";
	public static final String LINK_CONVERTION = "CONVERSION";
	public static final String LINK_HUBS = "hubs";
	public static final String LINK_INFO = "INFO";
	public static final String MY_Y = "Y_POSITION";
	public static final String MY_X = "X_POSITION";
	public static final String ISFIXED = "ISISISFIXED"; 
	public static final String ARROWSIZE = "ArrowSize";
	public static final String ID = "Node_Id";
	public static final String DUMMY = "DUMMY";
	public static final String LABEL = "PrefLABEL";
	public static final String IS_REACTANT = "isReactant";
	
	
	public static final String SELECTED = "sel";
	public static final String RUBBER_BAND = "rubberband";
	
	public static final double defaultNodeHeight = 25;
	public static final double defaultMetaboliteWidth = 45;
	public static final double reactionNodeSize = 10;
	
	public static final double defaultInfoHeight = 25;
	public static final double defaultInfoWidth = 80;
	 

	/**
	 * Default colors for the visualization
	 * 
	 */
	
	public static final int default_MetColor = ColorLib.rgb(100,255,100);
//	public static final int default_MetColor = ColorLib.rgba(255,255,255,0);
//	public static final int default_InfoColor = ColorLib.rgb(200, 200, 255);
	public static final int default_InfoColor = ColorLib.rgba(255, 255, 255,0);

	public static final int default_ReactColor = ColorLib.rgb(128, 128, 128);
	public static final int default_HUBColor = ColorLib.rgb(255,215,0);
//	public static final int default_HUBColor = ColorLib.rgba(255, 255, 255,0);
	
	public static final int default_hub_edgeColor = ColorLib.rgb(128, 128, 128);
	public static final int default_info_edgeColor = ColorLib.rgb(128, 128, 128);
	public static final int default_conversion_edgeColor = ColorLib.rgb(128, 128, 128);
	
	public static final int default_dummy_edgeColor = ColorLib.green(1);
	
	public static final String HAS_SPECIAL_COLOR = "hasSpecialColor";
	public static final String SPECIAL_COLOR = "SpeacialColor";
	
	public static final String HAS_SPECIAL_SHAPE = "hasSpecialShape";
	public static final String SPECIAL_SHAPE = "SpecialShape";
	
	public static String default_NodeFluxFilter = "defaultFluxFilter";
	public static int default_reaction_label_Color = ColorLib.gray(0);
	
	public static int default_met_label_color =ColorLib.gray(0);
	public static int default_info_label_color = ColorLib.gray(0);
	
	public static Font default_met_Font = new Font(Font.SANS_SERIF, Font.BOLD, 13);
	public static Font default_currency_Font = new Font(Font.SANS_SERIF, Font.BOLD, 13);
	public static Font default_reaction_Font = FontLib.getFont("Tahoma",Font.BOLD, 15);
	public static Font default_information_Font = new Font(Font.SANS_SERIF, Font.PLAIN,13);
	
	public static double default_edge_thickness = 2;
	public static int default_edge_color = ColorLib.rgb(128, 128, 128);
	
//	public static int default_met_label_color = ColorLib.rgb(200, 0, 0);
//	public static int default_info_label_color = ColorLib.rgb(0, 0, 180);
	
	/**
	 * Default Window size
	 */
	public static int default_windowXSize = 1000;
	public static int default_windowYSize = 700;
	
	/**
	 * Default shapes for the display
	 */
	
	public static final int default_infoShape = Constants.SHAPE_RECTANGLE;
	public static final int default_MetShape = Constants.SHAPE_ELLIPSE;
	public static final int default_HUBShape = Constants.SHAPE_ELLIPSE;
	public static final int default_ReactShape = Constants.SHAPE_RECTANGLE;
	public static final int default_DUMMY_SHAPE = Constants.SHAPE_NONE;
	
	/**
	 * Some useful predicates for the layout treatment
	 */
	
	public static final Predicate pred_met = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(MET_T));
	public static final Predicate pred_hub = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(HUBS));
	public static final Predicate pred_reac = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(REA_T));
	public static final Predicate pred_info = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(INFO));   
	public static final Predicate pred_info_edge = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(LINK_INFO));
	public static final Predicate pred_conv_edge = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(LINK_CONVERTION));
	public static final Predicate pred_hub_edge = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral(LINK_HUBS));
	
	public static final Predicate pred_conv_DUMMY = new ComparisonPredicate(ComparisonPredicate.EQ, 
			new ColumnExpression(TYPE), new ObjectLiteral("DUMMY"));
	
	/**
	 * Default Decorator Schema
	 */
	public static final Schema DECORATOR_SCHEMA = OptVisualExtensions.getVisualItemSchema();
	static {

		DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false); 
		DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128)); 
		DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma",8));
	
	}
	
	/**
	 * Default actions
	 */
	
	public static final String default_nodeColoring = "defNodeCol";
//	public static final String default_EdgeSizeAction = "defEdgeSize";
	public static final String default_EdgeColorAction = "defEdgeColor";
	public static final String default_nodesVisFilter = "defNodesVis";
	public static final String default_layout_action = "layout";
	public static final String default_repaint_action = "repaint";
	
	public static final String HAS_THICKNESS = "hasthickness";
	public static final String THICKNESS = "thicknesss";
	public static final String INPATHWAY = "IN_PATHWAY";
	
	public static final String reactionDecorators = "reactionNodes_deco";
	public static final String metabolitesDecorators = "nodes_deco";
	public static final String informationDecorators = "inforNodes_deco";
	
	
	/**
	 * Method to compare metabolites in the layout container and the in metabolic model container.
	 * @param layoutContainer
	 * @param container
	 * @return set of metabolites
	 */
	@Deprecated
	public static Set<String> compareMetabolites(ILayout layoutContainer, Container container){
		Map<String, INodeLay> mapMetabolitesCD = layoutContainer.getNodes();
		Set<String> metabolites = container.getMetabolites().keySet();
		Set<String> result = new TreeSet<String>();
		
		for(INodeLay n : mapMetabolitesCD.values()){
			if(n.getNodeType() != NodeTypeLay.INFORMATION && !metabolites.contains(n.getUniqueId()))
				result.add(n.getUniqueId());
		}
		
		return result;
	}
	
	/**
	 * Function that recieves a metabolic container and a layout container.
	 * Reactions not present in metabolic container and present in the layout
	 * are added to the resulting set.
	 * @param layoutContainer - the layout container
	 * @param container - metabolic model container
	 * @return set of reactions
	 */
	public static Set<String> compareReactions(ILayout layoutContainer, Container container){
		
		Map<String, IReactionLay> mapReactionsLayout = layoutContainer.getReactions();
		Set<String> met_reactions = container.getReactions().keySet();
		
		Set<String> result = new TreeSet<String>();
		
		for(String layout_id : mapReactionsLayout.keySet()){
			
			boolean found = false;
			
			Set<String> layout_met_ids = mapReactionsLayout.get(layout_id).getIDs();
			
			for(String s : layout_met_ids){
				if(met_reactions.contains(s)){ 
					found = true;
				}
			}
			
			if(!found) {
				result.add(layout_id);
				System.out.println(layout_id + " NOT PRESENT!");
			}
		}

		return result;
	}
	
	/**
	 * Function that returns the metabolites present in the layout that don't have a mapping to the metabolic model container
	 * @param layoutContainer - layout container
	 * @param container - metabolic model container
	 * @return set of reaction identifiers
	 */
	public static Set<String> getMetabolitesNotMapped(ILayout layoutContainer, Container container){
		
		Set<String> result = new TreeSet<String>();
		Set<String> allMetabolitesInLayout = new TreeSet<String>();
		
		for(String s : layoutContainer.getNodes().keySet()){
			if(!layoutContainer.getNodes().get(s).getNodeType().equals(NodeTypeLay.INFORMATION))
				allMetabolitesInLayout.addAll(layoutContainer.getNodes().get(s).getIds());
		}

		for(String mId : allMetabolitesInLayout){
			if(!container.getMetabolites().containsKey(mId)) result.add(mId);
		}
		
		return result;
	}
	
	/**
	 * Calculates the shape of the node taking into account it's string.
	 * NOTE: Shape strings are from celldesigner.
	 * @param shape
	 * @return prefuse shape
	 */
	public static int getShape(String shape){

		if(shape.equalsIgnoreCase("GENE")) return Constants.SHAPE_HEXAGON;
		if(shape.equalsIgnoreCase("RNA")) return Constants.SHAPE_TRIANGLE_LEFT;
		if(shape.equalsIgnoreCase("ANTISENSE RNA")) return Constants.SHAPE_TRIANGLE_RIGHT;
		if(shape.equalsIgnoreCase("PHENOTYPE")) return Constants.SHAPE_CROSS;
		if(shape.equalsIgnoreCase("ION")) return Constants.SHAPE_HEXAGON;
		if(shape.equalsIgnoreCase("SIMPLE_MOLECULE")) return Constants.SHAPE_DIAMOND;
		if(shape.equalsIgnoreCase("UNKNOWN")) return Constants.SHAPE_NONE;
		if(shape.equalsIgnoreCase("COMPLEX")) return Constants.SHAPE_STAR;
		if(shape.equalsIgnoreCase("SQUARE")) return Constants.SHAPE_RECTANGLE;
		if(shape.equalsIgnoreCase("OVAL")) return Constants.SHAPE_ELLIPSE;
		return -1;
	}

	/**
	 * Function that gives the edge types for different nodes.
	 * All nodes are LINK_CONVERTION, unless the node is a information node.
	 * @param nt node type - type of the node
	 * @return Constant LINK TYPE - edge/link type
	 */
	public static String getArrowType(NodeTypeLay nt){
		switch(nt){
		case GENE: return LINK_CONVERTION;
		case INFORMATION: return LINK_INFO;
		case METABOLITE: return LINK_CONVERTION;
		case REACTION: return LINK_CONVERTION;
		default: return LINK_CONVERTION;
		}
	}

	/**
	 * Given a String returns the correspondent NodeType (from string to enum)
	 * @param nodeType
	 * @return CONstANT NODE TYPE
	 */
	public static String getNodeType(NodeTypeLay nodeType) {
		String returnvalue = "";

		switch(nodeType){
		case GENE: returnvalue = INFO;break;
		case INFORMATION: returnvalue = INFO;break;
		case METABOLITE: returnvalue = MET_T;break;
		case CURRENCY: returnvalue = HUBS;break;
		case REACTION: returnvalue = REA_T;break;
		default: returnvalue = ""; break;
		}
		return returnvalue;
	}

	public static Integer getKOColor() {
		return ColorLib.rgb(255, 0, 0);
	}
	
	public static Integer getUNDERColor(){
		return ColorLib.rgb(255, 125, 0);
	}
	
	public static Integer getOVERColor(){
		return ColorLib.rgb(0, 255, 0);
	}
	
	
	public static Integer mixColors(Integer[] colors){
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		for(Integer color : colors){
			
			red += ColorLib.red(color);
			green += ColorLib.green(color);
			blue += ColorLib.blue(color);
		
		}
		
		int size = colors.length;
		return ColorLib.rgb(red/size, green/size, blue/size);
	}
	
	/**
	 * 
	 * Normalization of the values of a  flux distribution to a given range.
	 * @param values - flux values
	 * @param minBound - minimum bound 
	 * @param maxBound - maximum bound
	 * @param zeroThikness 
	 * @return map of reaction ids to fluxes values (normalized)
	 */
	public static Map<String, Double> normalizeFluxes(Map<String, Double> values, double minBound, double maxBound, double zeroThikness){
		
		Map<String, Double> absValues = new HashMap<String, Double>();
		double minValue = 0, maxValue = Double.MIN_VALUE;
		
		Map<String, Double> value2 = removeZeros(values);
		
//		System.out.println(value2);
		double mean = mean(value2.values());
		double sd = sd(value2.values(), mean);
		
		Set<String> outliers = new HashSet<String>();
//		for(String s : values.keySet()){
//			if(values.get(s) > (mean+sd) || values.get(s) < (mean-sd)){
//				outliers.add(s);
//				normalized_values.put(s, maxBound);
//			}
//		}
	
		
		for(String s : values.keySet()){
			if(!outliers.contains(s))
				absValues.put(s, roundToDecimals(Math.abs(values.get(s)),2));
		}

		for(double d : absValues.values()){
			//			if(minValue > d) minValue = d;
			if(maxValue < d) maxValue = d;
		}

		//		minValue = 0;


		return normalize(absValues, minBound, maxBound, zeroThikness, minValue, maxValue);
	}
	
	
	
	public static Map<String, Double> normalize(Map<String, Double> absValues, Double minBound, Double maxBound, Double zeroThikness, Double minValue, Double maxValue){
		
		Map<String, Double> normalized_values = new HashMap<String, Double>();

		
		for(String s : absValues.keySet()){
//			if(!outliers.contains(s)){
				if(absValues.get(s) != 0.0){
					Double normalized = (absValues.get(s) - minValue) / (maxValue - minValue) * (maxBound - minBound) + minBound;
					normalized_values.put(s, normalized);
				}else
					normalized_values.put(s, zeroThikness);
//			}
		}
		
		return normalized_values;
	}
	
	private static Map<String, Double> removeZeros(Map<String, Double> values) {
		
		Map<String, Double> ret = new HashMap<String, Double>();
		for(String key: values.keySet()){
			Double v = values.get(key);
			
			if(!v.equals(0.0))
				ret.put(key, v);
		}
		
		return ret;
	}

	private static double sd(Collection<Double> values, double mean) {
		
		double sum = 0.0;
		for(Double d : values){
			
			sum+= Math.pow((d-mean), 2);
		}
		
		sum = sum / values.size();
		
		return Math.sqrt(sum);
	}

	private static double mean(Collection<Double> values) {
		double sum = 0.0;
		for(Double d : values){
			sum += d;
		}
		
		return sum/values.size();
	}

	public static double normalizeValue(double min, double max, double x){
		
		return ((x - min)/(max-min));
	}
	
	public static Map<String,Double> standardization(Map<String, Double> values){
		
		Map<String, Double> result = new HashMap<String,Double>();
		double sum = 0.0;
		
		for(String s : values.keySet()){
			sum += values.get(s);
		}
		
		double median = sum / values.size();
		
		double sum2 = 0.0;
		for(String s : values.keySet()){
			sum2 += Math.pow((values.get(s)-median), 2);
		}
		
		double sd = Math.sqrt(sum2/(values.size()-1));
		
		for(String s : values.keySet()){
			
			double st_value = (values.get(s) - median) / sd;
			result.put(s, st_value);
		}
		
		return result;
	}
	
	public static Set<String> getInvisibilityFromContainer(
			ILayout layoutContainer, Container container) {
		
		Set<String> r_not_contained = new HashSet<String>();
		

		for(String r_node_id : layoutContainer.getReactions().keySet()){
			
			Set<String> metIds = layoutContainer.getReactions().get(r_node_id).getIDs();
			boolean exists = false;
			for(String met_Id : metIds){
				
				if(container.getReactions().containsKey(met_Id)) exists = true;
			}
			
			if(!exists) r_not_contained.add(r_node_id);
				
		}
		
		return r_not_contained;
	}

	public static double roundToDecimals(double d, int c) {
		int temp=(int)((d*Math.pow(10,c)));
		return (((double)temp)/Math.pow(10,c));
	}
	
	public static Map<String, String> getRegexMatches(String regexModelString, String regexLayoutString, Set<String> metIdsInLayout, Set<String> modelIds){

		Pattern regLayout = Pattern.compile(regexLayoutString);
		Pattern regModel = Pattern.compile(regexModelString);

		//result should be uniqueId -> new Id
		Map<String, String> oldToNew = new HashMap<String, String>();

		for(String s : metIdsInLayout){

			Matcher layoutMatcher = regLayout.matcher(s);


			String filteredLayoutId = null;

			if(layoutMatcher.find()){

				filteredLayoutId = layoutMatcher.group(1);

				for(String model_s : modelIds){

					Matcher modelMatcher = regModel.matcher(model_s);
					String filteredModelId;

					if(modelMatcher.find()){
						filteredModelId= modelMatcher.group(1);

						if(filteredModelId.equals(filteredLayoutId)){
							oldToNew.put(s, model_s);
						}
					}
				}
			}
		}
		return oldToNew;
	}

	public static IReactionLay converReactionCItoIReactionLay(ReactionCI reaction, ILayout lc){
		
		ReactionLay reactionlay;
		
		Map<String, INodeLay> reactants = new HashMap<String, INodeLay>();
		Map<String, INodeLay> products = new HashMap<String, INodeLay>();
		
		String uniqueId = UUID.randomUUID().toString();
		String label = reaction.getId();
		Set<String> ids = new HashSet<String>();
		ids.add(reaction.getId());
		
		
		for(String reactId : reaction.getReactants().keySet()){
			
			INodeLay node  = getNodeLayFromMet(reaction, lc, reactId);
			reactants.put(node.getUniqueId(), node);
		}
	
		for(String prodId : reaction.getProducts().keySet()){
			
			INodeLay node = getNodeLayFromMet(reaction, lc, prodId);
			products.put(node.getUniqueId(), node);
		}
		
		reactionlay = new ReactionLay(reactants, products, new HashMap<String, INodeLay>(), reaction.isReversible(), null, null, ids, uniqueId, label);
		return reactionlay;
		
	}

	private static INodeLay getNodeLayFromMet(ReactionCI reaction, ILayout lc,
			String metId) {
		
		
		NodeTypeLay nodeType = NodeTypeLay.METABOLITE;
		int counter = 0;
		String id_of_node = "";
		
		for(String nodeId : lc.getNodes().keySet()){
			
			INodeLay node = lc.getNodes().get(nodeId);
			if(node.getIds().contains(metId)){
				
				id_of_node = nodeId;
				counter++;
				nodeType = node.getNodeType();
			}
		}
		
		if(counter == 1) return lc.getNodes().get(id_of_node);
		
		else{
			
			if(counter == 0) nodeType = NodeTypeLay.METABOLITE;
			
			String id = UUID.randomUUID().toString();
			Set<String> ids = new HashSet<String>();
			ids.add(metId);
			
			NodeLay node = new NodeLay(id, metId, ids, nodeType, null, null);
			
			return node;
		}
	}
	
	/**
	 * function that takes all reactions that are equal, and merges them. To
	 * be equal a reaction must share all the nodes. All the metabolic identifiers of
	 * those reactions are merged. If the reaction does not have any metabolic identifier
	 * it will also be merged.
	 * @param lc
	 * @param map 
	 */
	public static void simplifyLayout(ILayout lc, Map<String, Set<String>> map){

		Map<String, Set<String>> equivalences = new HashMap<String, Set<String>>();
		
		for(String key : lc.getReactions().keySet()){
			
			if(!isInEquivalence(key, equivalences)){
				Set<String> equiv_values = getEquivalences(key, lc.getReactions());
				equivalences.put(key, equiv_values);
			}
		}
		
		for(String rkey : equivalences.keySet()){
			
			Set<String> eqs = equivalences.get(rkey);
			
			Set<String> names = new HashSet<String>();
			names.addAll(map.get(rkey));
			
			if(eqs.size() > 0){
				
				
				for(String eq : eqs){
					lc.getReactions().get(rkey).getIDs().addAll(lc.getReactions().get(eq).getIDs());
					lc.getReactions().get(rkey).setX(null);
					lc.getReactions().get(rkey).setY(null);
					
					names.addAll(map.get(eq));
					lc.getReactions().remove(eq);
				}
				
				String name = buildName(names);
				lc.getReactions().get(rkey).setLabel(name);
			}
		}
	}

	public static String buildName(Set<String> eqs) {
		
		boolean first = true;
		String name = "";
		for(String s : eqs){
			if(first){
				name = s;
				first = false;
			}
			else{
				name += " " + s;
			}
		}
		
		return name;
	}

	private static Set<String> getEquivalences(String key,
			Map<String, IReactionLay> reactions) {
		
		Set<String> eqs = new HashSet<String>();
		IReactionLay reaction1 = reactions.get(key);
		
		for(String r : reactions.keySet()){
			
			if(r!=key){
				IReactionLay reaction2 = reactions.get(r);
				if(equalReactions(reaction1, reaction2)) eqs.add(r);
			}
		}
		
		return eqs;
	}



	private static boolean equalReactions(IReactionLay reaction1,
			IReactionLay reaction2) {
		
		boolean areEqual = true;
		
		
		if(reaction1.isReversible() != reaction2.isReversible()){
			areEqual = false;
		}
		
		if(reaction1.getProducts().size() != reaction2.getProducts().size() || reaction1.getReactants().size()!= reaction2.getReactants().size()){
			areEqual = false;
		}
		
		if(!reaction1.getProducts().keySet().containsAll(reaction2.getProducts().keySet()) 
				|| !reaction1.getReactants().keySet().containsAll(reaction2.getReactants().keySet())) 
		{
			areEqual = false;
		}
			
		
		return areEqual;
	}

	private static boolean isInEquivalence(String key,
			Map<String, Set<String>> equivalences) {
		
		boolean isEquivalence = false;
		if(equivalences.keySet().contains(key)) isEquivalence = true;
		for(String s : equivalences.keySet()) if(equivalences.get(s).contains(key)) isEquivalence = true;
		
		return isEquivalence;
	}

	public static ILayout cleanInconsistencies(ILayout newLayoutWithCoords) {
		
		
		for(String rId : newLayoutWithCoords.getReactions().keySet()){
			IReactionLay reaction = newLayoutWithCoords.getReactions().get(rId);
			Set<String> toIterate = new HashSet<>(reaction.getReactants().keySet());
			for(String reactId : toIterate){
				if(!newLayoutWithCoords.getNodes().containsKey(reactId)){
					System.err.println("Removing inconsistencie: reaction " + reaction.getIDs() + "\tmet: "+reactId);
					newLayoutWithCoords.getReactions().get(rId).getReactants().remove(reactId);
				}
			}
			
			toIterate = new HashSet<>(reaction.getProducts().keySet());
			for(String prodIf : toIterate){
				if(!newLayoutWithCoords.getNodes().containsKey(prodIf)){
					System.err.println("Removing inconsistencie: reaction " + reaction.getIDs() + "\tmet: "+prodIf);
					newLayoutWithCoords.getReactions().get(rId).getProducts().remove(prodIf);
				}
			}
		}
		return newLayoutWithCoords;
	}
	
	
	public static boolean verifiedDepBetweenClasses(ILayout newLayoutWithCoords, boolean removeProblems){
		
		boolean toRet = false;
		Set<String> metabolitesInReactions = new HashSet<String>();
		
		for(String rId : newLayoutWithCoords.getReactions().keySet()){
			IReactionLay reaction = newLayoutWithCoords.getReactions().get(rId);
			Set<String> toIterate = new HashSet<>(reaction.getReactants().keySet());
			for(String reactId : toIterate){
				if(!newLayoutWithCoords.getNodes().containsKey(reactId)){
					System.err.println("Problem inconsistencie: reaction " + reaction.getIDs() + "\tmet: "+reactId);
					if(removeProblems)newLayoutWithCoords.getReactions().get(rId).getReactants().remove(reactId);
					toRet = true;
				}
			}
			metabolitesInReactions.addAll(toIterate);
			
			toIterate = new HashSet<>(reaction.getProducts().keySet());
			metabolitesInReactions.addAll(toIterate);
			
			for(String prodIf : toIterate){
				if(!newLayoutWithCoords.getNodes().containsKey(prodIf)){
					System.err.println("Problem inconsistencie: reaction " + reaction.getIDs() + "\tmet: "+prodIf);
					if(removeProblems)newLayoutWithCoords.getReactions().get(rId).getProducts().remove(prodIf);
					toRet = true;
				}
			}
		}
		
		Set<String> metabolitesWithoutReactions = CollectionUtils.getSetDiferenceValues(newLayoutWithCoords.getNodes().keySet()
				, metabolitesInReactions);
		
		for(String id: metabolitesWithoutReactions){
			INodeLay node = newLayoutWithCoords.getNodes().get(id);
			if(node.getNodeType().equals(NodeTypeLay.METABOLITE) || node.getNodeType().equals(NodeTypeLay.CURRENCY)){
				System.err.println("Problem inconsistencie: metabolite " + id + "\t" + node.getIds());
				newLayoutWithCoords.getNodes().remove(id);
			}
		}
		
		return toRet;
	}
	
}
