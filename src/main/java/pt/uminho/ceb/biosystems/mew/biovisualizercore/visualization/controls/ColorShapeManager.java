package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls;

import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.assignment.ColorAction;
import prefuse.activity.Activity;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.ComparisonPredicate;
import prefuse.data.expression.ObjectLiteral;
import prefuse.data.expression.Predicate;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.visual.expression.HoverPredicate;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.expression.VisiblePredicate;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions.BioVisualizerColorAction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions.BioVisualizerDataShapeAction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions.BioVisualizerEdgeSizeAction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions.BioVisualizerStrokeAction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.VisualizationProperties;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers.BioVisualizerEdgeRenderer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.OptVisualExtensions;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class ColorShapeManager {
	
	private int[]							colors					= new int[] { LayoutUtils.default_InfoColor, //INFOS
			LayoutUtils.default_MetColor, //METABOLITES
			ColorLib.rgb(255, 255, 200), LayoutUtils.default_HUBColor, LayoutUtils.default_ReactColor //REACTION
																	};
	
	private int								met_stroke_color		= ColorLib.rgb(128, 128, 128);
	private int								reac_stroke_color		= ColorLib.rgb(128, 128, 128);
	private int								currency_stroke_color	= ColorLib.rgb(128, 128, 128);
	private int								info_stroke_color		= ColorLib.rgb(128, 128, 128);
	
	private Font							metaboliteLabelFont		= LayoutUtils.default_met_Font;
	private Font							currencyLabelFont		= LayoutUtils.default_currency_Font;
	private Font							reactionLabelFont		= LayoutUtils.default_reaction_Font;
	private Font							informationFont			= LayoutUtils.default_information_Font;
	
	private int								reaction_Label_Color	= LayoutUtils.default_reaction_label_Color;
	private int								metabolite_label_Color	= LayoutUtils.default_met_label_color;
	private int								information_label_Color	= LayoutUtils.default_info_label_color;
	private int								currency_Label_Color	= LayoutUtils.default_met_label_color;
	
	private Visualization					my_viz;
	
	private Map<String, String>				oldLabelsToNewReactions;
	private HashMap<String, String>			oldLabelsToNewNodes;
	
	private int[]							shapes					= new int[] { LayoutUtils.default_infoShape, //INFOS
			LayoutUtils.default_MetShape, //METABOLITES
			LayoutUtils.default_HUBShape, //HUBS
			LayoutUtils.default_ReactShape, //REACTION
			LayoutUtils.default_DUMMY_SHAPE, Constants.SHAPE_TRIANGLE_UP
																	
																	};
	
	private Float							reaction_stroke			= 1f;
	private Float							metabolite_stroke		= 1f;
	private Float							currency_stroke			= 1f;
	private Float							information_stroke		= 1f;
	
	private float							edge_thickness			= 2;
	
	private int								hub_edgeColor			= ColorLib.rgb(128, 128, 128);
	private int								info_edgeColor			= ColorLib.rgb(128, 128, 128);
	private int								conversion_edgeColor	= ColorLib.rgb(128, 128, 128);
	
	private BioVisualizerColorAction		node_colorInfo;
	private BioVisualizerColorAction		node_colorMet;
	private BioVisualizerColorAction		node_colorReact;
	private BioVisualizerColorAction		node_colorHUB;
	
	private BioVisualizerColorAction		node_strokeInfo;
	private BioVisualizerColorAction		node_strokeMet;
	private BioVisualizerColorAction		node_strokeReact;
	private BioVisualizerColorAction		node_strokeHUB;
	
	private BioVisualizerDataShapeAction	shapeAction;
	
	private BioVisualizerStrokeAction		shapeStrokeThicknessAction;
	
	private BioVisualizerColorAction		edgeInfoStrokeColorAction;
	private BioVisualizerColorAction		arrowInfoColorAction;
	private BioVisualizerColorAction		arrowHubColorAction;
	private BioVisualizerColorAction		arrowConvColorAction;
	private BioVisualizerColorAction		edgeHubStrokeColorAction;
	private BioVisualizerColorAction		edgeConvStrokeColorAction;
	
	private BioVisualizerEdgeSizeAction		edgeStrokeAction;
	
	//	NOTE:   memory to labels
	//	FIXME:  find an alternative to this solution;
	private Map<String, String>				metaboliteLabels;
	private Map<String, String>				reactionLabels;
	private Map<String, String>				currencyLabels;
	private Map<String, String>				infoLabels;
	
	private VisualTable						reactionDecorator;
	
	private VisualTable						metaboliteDecorator;
	
	private VisualTable						hubDecorator;
	
	private VisualTable						infoDecorator;
	
	private VisiblePredicate				visibility_Predicate;
	private AndPredicate					reactionPredicate;
	private AndPredicate					metabolitePredicate;
	private AndPredicate					hubPredicate;
	private AndPredicate					infoPredicate;
	
	public ColorShapeManager() {
	}
	
	public ColorShapeManager(VisualizationProperties vps) {
		//		colors = new int[]{
		//			vps.getInformationFillColor(),
		//			vps.getMetaboliteFillColor(),
		//			ColorLib.rgb(255,255,200),
		//			vps.getCurrencyFillColor(),
		//			LayoutUtils.default_ReactColor
		//		};
		//		
		//		updateFillandStroke(vps);
		//		updateFont(vps);
		//		updateShapeThickness(vps);
		//		updateEdgeThickness(vps);
		//		updateEdgeColor(vps);
		//		setDefaultLabels(vps);
		if (vps != null) setBase(vps);
	}
	
	private void setBase(VisualizationProperties vps) {
		shapes[0] = vps.getInformationShape();
		shapes[1] = vps.getMetaboliteShape();
		shapes[2] = vps.getCurrencyShape();
		shapes[3] = vps.getReactionShape();
		
		colors = new int[] { vps.getInformationFillColor(), vps.getMetaboliteFillColor(), ColorLib.rgb(255, 255, 200), vps.getCurrencyFillColor(), LayoutUtils.default_ReactColor };
		
		met_stroke_color = vps.getMetaboliteStrokeColor();
		reac_stroke_color = vps.getReactionStrokeColor();
		currency_stroke_color = vps.getCurrencyStrokeColor();
		info_stroke_color = vps.getInformationStrokeColor();
		
		edge_thickness = vps.getEdgeThickness();
		hub_edgeColor = vps.getReactionStrokeColor();
		info_edgeColor = vps.getReactionStrokeColor();
		conversion_edgeColor = vps.getReactionStrokeColor();
		
		//		System.out.println("Metabolite font: " + vps.getMetaboliteFont().getFontName() + " style : " + vps.getMetaboliteFont().getStyle() + " size: " + vps.getMetaboliteFont().getSize());
		metaboliteLabelFont = FontLib.getFont(vps.getMetaboliteFont().getFontName(), vps.getMetaboliteFont().getStyle(), vps.getMetaboliteFont().getSize());
		currencyLabelFont = FontLib.getFont(vps.getCurrencyFont().getFontName(), vps.getCurrencyFont().getStyle(), vps.getCurrencyFont().getSize());
		informationFont = FontLib.getFont(vps.getInformationFont().getName(), vps.getInformationFont().getStyle(), vps.getInformationFont().getSize());
		reactionLabelFont = FontLib.getFont(vps.getReactionFont().getName(), vps.getReactionFont().getStyle(), vps.getReactionFont().getSize());
		//		
		
		metabolite_label_Color = vps.getMetaboliteFontSize();
		currency_Label_Color = vps.getCurrencyFontSize();
		reaction_Label_Color = vps.getReactionFonSize();
		information_label_Color = vps.getInformationFontColor();
		
		reaction_stroke = vps.getReactionStrokeThickness();
		metabolite_stroke = vps.getMetaboliteStrokeThickness();
		currency_stroke = vps.getCurrencyStrokeThickness();
		information_stroke = vps.getInformationStrokeThickness();
	}
	
	public void setVisualization(Visualization vis) {
		
		this.my_viz = vis;
	}
	
	/**
	 * Gets the edge coloring of the graph and also
	 * the highlight coloring.
	 * 
	 * @return action list to run in the visualization
	 */
	public ActionList getEdgeColoring() {
		
		ActionList list = new ActionList(Activity.INFINITY);
		
		Predicate p = new ComparisonPredicate(ComparisonPredicate.EQ, new ColumnExpression(LayoutUtils.HAS_SPECIAL_COLOR), new ObjectLiteral(true));
		
		edgeInfoStrokeColorAction = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_info_edge, VisualItem.STROKECOLOR, this.info_edgeColor);
		edgeInfoStrokeColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		edgeInfoStrokeColorAction.add(p, Integer.MIN_VALUE);
		
		edgeHubStrokeColorAction = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_hub_edge, VisualItem.STROKECOLOR, this.hub_edgeColor);
		edgeHubStrokeColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		edgeHubStrokeColorAction.add(p, Integer.MIN_VALUE);
		
		edgeConvStrokeColorAction = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_conv_edge, VisualItem.STROKECOLOR, this.conversion_edgeColor);
		edgeConvStrokeColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		edgeConvStrokeColorAction.add(p, Integer.MIN_VALUE);
		
		arrowInfoColorAction = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_info_edge, VisualItem.FILLCOLOR, this.info_edgeColor);
		arrowInfoColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		arrowInfoColorAction.add(p, Integer.MIN_VALUE);
		
		arrowHubColorAction = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_hub_edge, VisualItem.FILLCOLOR, this.hub_edgeColor);
		arrowHubColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		arrowHubColorAction.add(p, Integer.MIN_VALUE);
		
		arrowConvColorAction = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_conv_edge, VisualItem.FILLCOLOR, this.conversion_edgeColor);
		arrowConvColorAction.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		arrowConvColorAction.add(p, Integer.MIN_VALUE);
		
		edgeStrokeAction = new BioVisualizerEdgeSizeAction(LayoutUtils.EDGES, edge_thickness);
		
		/**
		 * DUMMY STUFF!
		 */
		ColorAction dummy = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_conv_DUMMY, VisualItem.STROKECOLOR, ColorLib.rgb(0, 255, 0));
		dummy.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		
		ColorAction arrow_dummy = new BioVisualizerColorAction(LayoutUtils.EDGES, LayoutUtils.pred_conv_DUMMY, VisualItem.FILLCOLOR, ColorLib.rgb(0, 255, 0));
		dummy.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 200));
		
		list.add(arrowInfoColorAction);
		list.add(arrowHubColorAction);
		list.add(arrowConvColorAction);
		list.add(edgeInfoStrokeColorAction);
		list.add(edgeHubStrokeColorAction);
		list.add(edgeConvStrokeColorAction);
		list.add(edgeStrokeAction);
		list.add(dummy);
		list.add(arrow_dummy);
		return list;
	}
	
	/**
	 * Gets the {@link ActionList} to run in the {@link Visualization}.
	 * This is the default coloring.
	 * Also sets the hovering color for the nodes (the
	 * color the node get when cursor is hovering it)
	 * 
	 * @return {@link ActionList} that deals with the coloring of the nodes
	 */
	public ActionList getNodesColoring() {
		
		int t = 200;
		
		ActionList list = new ActionList(Activity.INFINITY);
		
		Predicate p = new ComparisonPredicate(ComparisonPredicate.EQ, new ColumnExpression(LayoutUtils.HAS_SPECIAL_COLOR), new ObjectLiteral(true));
		
		//		ColorAction nStroke = new ColorAction(LayoutUtils.NODES, VisualItem.STROKECOLOR);
		//		nStroke.setDefaultColor(this.node_strokeColor );
		
		node_strokeReact = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_reac, VisualItem.STROKECOLOR, reac_stroke_color);
		node_strokeHUB = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_hub, VisualItem.STROKECOLOR, currency_stroke_color);
		node_strokeMet = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_met, VisualItem.STROKECOLOR, met_stroke_color);
		node_strokeInfo = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_info_edge, VisualItem.STROKECOLOR, info_stroke_color);
		
		node_colorMet = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_met, VisualItem.FILLCOLOR, colors[1]);
		node_colorMet.add(new HoverPredicate(), ColorLib.rgba(255, 100, 100, t));
		node_colorMet.add(VisualItem.HIGHLIGHT, ColorLib.rgba(255, 100, 100, t));
		node_colorMet.add(new InGroupPredicate(LayoutUtils.SELECTED), ColorLib.rgba(255, 255, 0, t));
		node_colorMet.add(new InGroupPredicate(LayoutUtils.INPATHWAY), ColorLib.rgba(153, 50, 204, t));
		node_colorMet.add(p, Integer.MIN_VALUE);
		
		node_colorReact = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_reac, VisualItem.FILLCOLOR, colors[4]);
		node_colorReact.add(new HoverPredicate(), ColorLib.rgb(255, 100, 100));
		node_colorReact.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 100, 100));
		node_colorReact.add(new InGroupPredicate(LayoutUtils.SELECTED), ColorLib.rgb(255, 255, 0));
		node_colorReact.add(p, Integer.MIN_VALUE);
		
		node_colorInfo = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_info, VisualItem.FILLCOLOR, colors[0]);
		node_colorInfo.add(new HoverPredicate(), ColorLib.rgba(255, 100, 100, t));
		node_colorInfo.add(VisualItem.HIGHLIGHT, ColorLib.rgba(255, 100, 100, t));
		node_colorInfo.add(new InGroupPredicate(LayoutUtils.SELECTED), ColorLib.rgba(255, 255, 0, t));
		node_colorInfo.add(p, Integer.MIN_VALUE);
		
		node_colorHUB = new BioVisualizerColorAction(LayoutUtils.NODES, LayoutUtils.pred_hub, VisualItem.FILLCOLOR, colors[3]);
		node_colorHUB.add(new HoverPredicate(), ColorLib.rgba(255, 100, 100, t));
		node_colorHUB.add(VisualItem.HIGHLIGHT, ColorLib.rgba(255, 100, 100, t));
		node_colorHUB.add(new InGroupPredicate(LayoutUtils.SELECTED), ColorLib.rgba(255, 255, 0, t));
		node_colorHUB.add(new InGroupPredicate(LayoutUtils.INPATHWAY), ColorLib.rgba(153, 50, 204, t));
		node_colorHUB.add(p, Integer.MIN_VALUE);
		
		//FIXME: AGGREGATES TEST
		ColorAction aFill = new ColorAction(LayoutUtils.AGGR, VisualItem.FILLCOLOR);
		aFill.setDefaultColor(ColorLib.rgba(255, 255, 255, 0));
		ColorAction aStroke = new ColorAction(LayoutUtils.AGGR, VisualItem.STROKECOLOR);
		aStroke.setDefaultColor(ColorLib.rgba(255, 255, 255, 0));
		
		ActionList color_met = new ActionList();
		color_met.add(node_colorMet);
		color_met.add(node_colorReact);
		color_met.add(node_colorInfo);
		color_met.add(node_colorHUB);
		
		color_met.add(aFill);
		color_met.add(aStroke);
		
		shapeAction = new BioVisualizerDataShapeAction(LayoutUtils.NODES, LayoutUtils.TYPE, shapes);
		
		list.add(node_strokeHUB);
		list.add(node_strokeInfo);
		list.add(node_strokeReact);
		list.add(node_strokeMet);
		
		Float[] strokes = new Float[] { this.reaction_stroke, this.metabolite_stroke, this.currency_stroke, this.information_stroke };
		shapeStrokeThicknessAction = new BioVisualizerStrokeAction(LayoutUtils.NODES, strokes);
		
		list.add(shapeAction);
		list.add(color_met);
		list.add(shapeStrokeThicknessAction);
		
		return list;
	}
	
	/**
	 * Returns the shapes of the edges
	 * 
	 * @return {@link ActionList} runnable in the visualization
	 */
	//	public ActionList getEdgeShaping(){
	//		BioVisualizerEdgeSizeAction info_size = new BioVisualizerEdgeSizeAction(LayoutUtils.EDGES, edge_thickness);
	//		ActionList list = new ActionList(Activity.INFINITY);
	//		list.add(info_size);
	//		return list;
	//	}
	//	
	
	/**
	 * Runs a thickness edge {@link Action} based on a given.
	 * flux distribution.
	 * 
	 * @param sizes
	 */
	public void setNewThickness(Map<String, Double> sizes) {
		setEdgesThickness(sizes);
	}
	
	private void setEdgesThickness(Map<String, Double> sizes) {
		
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.EDGES);
			
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				String id = item.getString(LayoutUtils.ID);
				
				if ((item.get(LayoutUtils.TYPE).equals(LayoutUtils.LINK_CONVERTION) || item.get(LayoutUtils.TYPE).equals(LayoutUtils.LINK_HUBS)) && sizes.containsKey(id)) {
					
					item.setBoolean(LayoutUtils.HAS_THICKNESS, true);
					item.setDouble(LayoutUtils.THICKNESS, sizes.get(id));
				}
				
			}
		}
	}
	
	/**
	 * Resets the {@link Action} that deals with the thickness
	 * of the edges.
	 */
	public void resetEdgeThickness() {
		
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.EDGES);
			
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				String id = item.getString(LayoutUtils.ID);
				
				if (item.get(LayoutUtils.TYPE).equals(LayoutUtils.LINK_CONVERTION) || item.get(LayoutUtils.TYPE).equals(LayoutUtils.LINK_HUBS)) {
					
					item.setBoolean(LayoutUtils.HAS_THICKNESS, false);
				}
				
			}
		}
	}
	
	/**
	 * Given a flux distribution sets the arrow shapes
	 * of reversible reactions to correspond to the orientation
	 * based by the value of the flux (0 flux reactions aren't
	 * changed - they can be hidden by another feature)
	 * 
	 * @param fluxes
	 */
	public void setArrowShapesByFluxes(Map<String, Double> fluxes, Map<String, IReactionLay> reactions) {
		
		synchronized (my_viz) {
			Iterator items = my_viz.items(LayoutUtils.NODES);
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T)) {
					Node react = (Node) item.getSourceTuple();
					
					String rId = react.getString(LayoutUtils.ID);
					if (reactions.get(rId).isReversible() && fluxes.containsKey(rId)) {
						double flux = fluxes.get(rId);
						Iterator e = react.edges();
						while (e.hasNext()) {
							Edge temp = (Edge) e.next();
							Node met = temp.getAdjacentNode(react);
							String metId = met.getString(LayoutUtils.ID);
							
							if (containsId(reactions.get(rId).getProducts(), metId) && fluxes.get(rId) < 0) {
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
							}
							if (containsId(reactions.get(rId).getReactants(), metId) && fluxes.get(rId) > 0) {
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
							}
						}
					}
				}
			}
		}
		
	}
	
	public void setArrowShapesByBoolean(Map<String, Boolean> direction, Map<String, IReactionLay> reactions) {
		
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.NODES);
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T)) {
					
					Node react = (Node) item.getSourceTuple();
					
					String rId = react.getString(LayoutUtils.ID);
					if (direction.containsKey(rId)) {
						
						Boolean flux = direction.get(rId);
						Iterator e = react.edges();
						
						while (e.hasNext()) {
							Edge temp = (Edge) e.next();
							Node met = temp.getAdjacentNode(react);
							//								String metId = met.getString(LayoutUtils.ID);
							
							if (temp.getBoolean(LayoutUtils.IS_REACTANT) && !flux) {
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);
							} else if (temp.getBoolean(LayoutUtils.IS_REACTANT) && flux) {
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
							}
							
							if (!temp.getBoolean(LayoutUtils.IS_REACTANT) && !flux) {
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
							} else if (!temp.getBoolean(LayoutUtils.IS_REACTANT) && flux) {
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Resets the arrow shapes if changed by a flux distribution.
	 */
	public void resetArrowShapes(Map<String, IReactionLay> reactions) {
		
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.NODES);
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				
				if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T)) {
					Node react = (Node) item.getSourceTuple();
					String rId = react.getString(LayoutUtils.ID);
					
					Iterator e = react.edges();
					while (e.hasNext()) {
						Edge temp = (Edge) e.next();
						Node met = temp.getAdjacentNode(react);
						String metId = met.getString(LayoutUtils.ID);
						if (containsId(reactions.get(rId).getProducts(), metId))
							temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);
						else if (containsId(reactions.get(rId).getReactants(), metId)) {
							if (reactions.get(rId).isReversible())
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_FORWARD);
							else
								temp.setInt(BioVisualizerEdgeRenderer.EDGE_TYPE, Constants.EDGE_ARROW_NONE);
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * Reactions from the layout may come with a different Id from the
	 * metabolic {@link Container}. This function was made to simplify the code
	 * when identification of a metabolic id from the {@link ILayout} is
	 * needed.
	 * 
	 * @param nodes - set of nodes where we want to identify the id
	 * @param metId - metabolic identification
	 * @returnmetaboliteLabels
	 */
	private boolean containsId(Map<String, INodeLay> nodes, String metId) {
		boolean contains = false;
		for (INodeLay node : nodes.values()) {
			if (node.getUniqueId().equals(metId)) contains = true;
		}
		return contains;
	}
	
	public boolean visNull() {
		return (this.my_viz == null);
	}
	
	/**
	 * Method that resets the labels of the reactions,
	 * removing the flux information.
	 */
	public void resetReactionLabels() {
		
		synchronized (my_viz) {
			
			if (this.oldLabelsToNewReactions != null && this.oldLabelsToNewReactions.size() > 0) {
				
				Iterator items = my_viz.items(LayoutUtils.NODES);
				while (items.hasNext()) {
					VisualItem item = (VisualItem) items.next();
					
					String id = item.getString(LayoutUtils.ID);
					
					if (oldLabelsToNewReactions.containsKey(id)) {
						
						item.setString(LayoutUtils.LABEL, oldLabelsToNewReactions.get(id));
					}
				}
				//				}
			}
			oldLabelsToNewReactions = new HashMap<String, String>();
		}
		
	}
	
	public void setNewReactionLabels(Map<String, String> newLabels) {
		
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.NODES);
			
			oldLabelsToNewReactions = new HashMap<String, String>();
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				
				String id = item.getString(LayoutUtils.ID);
				String oldLabel;
				if (newLabels.containsKey(id)) {
					oldLabel = item.getString(LayoutUtils.LABEL);
					item.setString(LayoutUtils.LABEL, newLabels.get(id));
					oldLabelsToNewReactions.put(id, oldLabel);
				}
			}
		}
	}
	
	/**
	 * Method that resets the labels of the nodes.
	 */
	public void resetNodeLabels() {
		
		synchronized (my_viz) {
			
			if (this.oldLabelsToNewNodes != null && this.oldLabelsToNewNodes.size() > 0) {
				
				Iterator items = my_viz.items(LayoutUtils.NODES);
				while (items.hasNext()) {
					VisualItem item = (VisualItem) items.next();
					
					String id = item.getString(LayoutUtils.ID);
					
					if (oldLabelsToNewNodes.containsKey(id)) {
						
						item.setString(LayoutUtils.LABEL, oldLabelsToNewNodes.get(id));
					}
				}
				//				}
			}
			oldLabelsToNewNodes = new HashMap<String, String>();
		}
	}
	
	public void overlapNodeLabels(Map<String, String> newLabels) {
		
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.NODES);
			
			oldLabelsToNewNodes = new HashMap<String, String>();
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				
				String id = item.getString(LayoutUtils.ID);
				String oldLabel;
				if (newLabels.containsKey(id)) {
					oldLabel = item.getString(LayoutUtils.LABEL);
					item.setString(LayoutUtils.LABEL, newLabels.get(id));
					oldLabelsToNewNodes.put(id, oldLabel);
				}
			}
		}
	}
	
	public void resetGroupSpecialFields(String group) {
		
		synchronized (my_viz) {
			
			Iterator i = my_viz.items(group);
			while (i.hasNext()) {
				
				VisualItem item = (VisualItem) i.next();
				if (item.canGetBoolean(LayoutUtils.HAS_SPECIAL_COLOR)) item.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, false);
				if (item.canGetBoolean(LayoutUtils.HAS_SPECIAL_SHAPE)) item.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, false);
			}
		}
	}
	
	public void addNewNodesSize(Map<String, Pair<Double, Double>> sizes) {
		
		synchronized (my_viz) {
			
			Iterator i = my_viz.items(LayoutUtils.NODES);
			
			while (i.hasNext()) {
				
				VisualItem item = (VisualItem) i.next();
				
				String node_id = item.getString(LayoutUtils.ID);
				
				if (sizes.containsKey(node_id)) {
					
					// double defaultH = item.getDouble(OptVisualExtensions.VISUAL_WIDTH);
					// double defaultW = item.getDouble(OptVisualExtensions.VISUAL_WIDTH);
					
					/**
					 * NOTE: modified by pmaia - if the default node sizes are
					 * to be overridden by user specified values, then, in order
					 * for the sizes to be consistent, these must
					 * always use the same default values for both metabolites
					 * and currency nodes.
					 */
					double defaultH = LayoutUtils.defaultNodeHeight;
					double defaultW = LayoutUtils.defaultNodeHeight;
					
					item.set(OptVisualExtensions.VISUAL_HEIGHT, sizes.get(node_id).getA() * defaultH);
					item.set(OptVisualExtensions.VISUAL_WIDTH, sizes.get(node_id).getB() * defaultW);
				}
			}
		}
	}
	
	public void resetNodesSizes() {
		
		synchronized (my_viz) {
			
			Iterator i = my_viz.items(LayoutUtils.NODES);
			
			while (i.hasNext()) {
				
				VisualItem item = (VisualItem) i.next();
				
				if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.MET_T)) {
					item.set(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.defaultNodeHeight);
					item.set(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.defaultMetaboliteWidth);
				} else if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T)) {
					item.set(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.reactionNodeSize);
					item.set(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.reactionNodeSize);
				} else if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.HUBS)) {
					item.set(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.defaultNodeHeight);
					item.set(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.defaultNodeHeight);
				} else if (item.getString(LayoutUtils.TYPE).equals(LayoutUtils.INFO)) {
					item.set(OptVisualExtensions.VISUAL_HEIGHT, LayoutUtils.defaultInfoHeight);
					item.set(OptVisualExtensions.VISUAL_WIDTH, LayoutUtils.defaultInfoWidth);
				}
			}
		}
	}
	
	public void overlapEdgesAndNodes(Map<String, Integer> nodeColors, Map<String, Integer> nodeShapes, Map<String, Pair<Integer, Integer>> reactionColors, Map<String, Integer> reactionShapes) {
		
		synchronized (my_viz) {
			
			Iterator i = my_viz.items(LayoutUtils.NODES);
			
			while (i.hasNext()) {
				
				VisualItem item = (VisualItem) i.next();
				
				String item_id = item.getString(LayoutUtils.ID);
				if (nodeColors.containsKey(item_id)) {
					item.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, true);
					item.setInt(LayoutUtils.SPECIAL_COLOR, nodeColors.get(item_id));
				} else if (reactionColors.containsKey(item_id)) {
					if (reactionColors.containsKey(item_id)) {
						item.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, true);
						
						if (reactionColors.get(item_id).getA().equals(reactionColors.get(item_id).getB())) {
							item.setInt(LayoutUtils.SPECIAL_COLOR, reactionColors.get(item_id).getA());
						} else
							item.setInt(LayoutUtils.SPECIAL_COLOR, ColorLib.rgba(0, 0, 0, 127));
					}
				}
				
				if (nodeShapes.containsKey(item_id)) {
					item.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, true);
					item.setInt(LayoutUtils.SPECIAL_SHAPE, nodeShapes.get(item_id));
				} else if (reactionShapes.containsKey(item_id)) {
					item.setBoolean(LayoutUtils.HAS_SPECIAL_SHAPE, true);
					item.setInt(LayoutUtils.SPECIAL_SHAPE, reactionShapes.get(item_id));
				}
			}
			
			i = my_viz.items(LayoutUtils.EDGES);
			
			while (i.hasNext()) {
				
				VisualItem item = (VisualItem) i.next();
				
				String rNode_id = item.getString(LayoutUtils.ID);
				if (reactionColors.containsKey(rNode_id) && !item.getString(LayoutUtils.TYPE).equals(LayoutUtils.LINK_INFO)) {
					
					item.setBoolean(LayoutUtils.HAS_SPECIAL_COLOR, true);
					if (item.getBoolean(LayoutUtils.IS_REACTANT))
						item.setInt(LayoutUtils.SPECIAL_COLOR, reactionColors.get(rNode_id).getA());
					else
						item.setInt(LayoutUtils.SPECIAL_COLOR, reactionColors.get(rNode_id).getB());
				}
			}
		}
	}
	
	public void stopDecorators() {
		
		//		if(my_viz != null){
		System.out.println("Stop Decorators " + my_viz);
		my_viz.removeGroup("reactionNodes_deco");
		my_viz.removeGroup("nodes_deco");
		my_viz.removeGroup("hubNodes_deco");
		my_viz.removeGroup("inforNodes_deco");
		//		}
	}
	
	public void clear() {
		
		if (reactionPredicate != null) reactionPredicate.clear();
		if (metabolitePredicate != null) metabolitePredicate.clear();
		if (hubPredicate != null) hubPredicate.clear();
		if (infoPredicate != null) infoPredicate.clear();
		
		if (reactionPredicate != null) {
			infoDecorator.removeAllTableListeners();
			infoDecorator.clear();
		}
		
		if (hubDecorator != null) {
			hubDecorator.removeAllTableListeners();
			hubDecorator.clear();
		}
		
		if (metaboliteDecorator != null) {
			metaboliteDecorator.removeAllTableListeners();
			metaboliteDecorator.clear();
		}
		
		if (reactionDecorator != null) {
			reactionDecorator.removeAllTableListeners();
			reactionDecorator.clear();
		}
		
		reactionPredicate = null;
		metabolitePredicate = null;
		hubPredicate = null;
		infoPredicate = null;
		
		infoDecorator = null;
		hubDecorator = null;
		metaboliteDecorator = null;
		reactionDecorator = null;
		
	}
	
	public void removeDecorators() {
		my_viz.removeGroup("reactionNodes_deco");
		my_viz.removeGroup("nodes_deco");
		my_viz.removeGroup("hubNodes_deco");
		my_viz.removeGroup("inforNodes_deco");
	}
	
	private void constructPredicates() {
		//		new AndPredicate(LayoutUtils.pred_reac, visibility_Predicate);
	}
	
	public void runDecorators() {
		/**
		 * Decorators for the labels
		 */
		
		//		if(visibility_Predicate != m)
		visibility_Predicate = new VisiblePredicate();
		
		//		Predicate or = new OrPredicate(LayoutUtils.pred_met, LayoutUtils.pred_hub);
		
		if (reactionPredicate == null) reactionPredicate = new AndPredicate(LayoutUtils.pred_reac, visibility_Predicate);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, this.reaction_Label_Color);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.FONT, reactionLabelFont);
		reactionDecorator = my_viz.addDecorators("reactionNodes_deco", LayoutUtils.NODES, reactionPredicate, LayoutUtils.DECORATOR_SCHEMA);
		
		if (metabolitePredicate == null) metabolitePredicate = new AndPredicate(LayoutUtils.pred_met, visibility_Predicate);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, metabolite_label_Color);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.FONT, metaboliteLabelFont);
		metaboliteDecorator = my_viz.addDecorators("nodes_deco", LayoutUtils.NODES, metabolitePredicate, LayoutUtils.DECORATOR_SCHEMA);
		
		if (hubPredicate == null) hubPredicate = new AndPredicate(LayoutUtils.pred_hub, visibility_Predicate);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, currency_Label_Color);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.FONT, currencyLabelFont);
		hubDecorator = my_viz.addDecorators("hubNodes_deco", LayoutUtils.NODES, hubPredicate, LayoutUtils.DECORATOR_SCHEMA);
		
		if (infoPredicate == null) infoPredicate = new AndPredicate(LayoutUtils.pred_info, visibility_Predicate);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, information_label_Color);
		LayoutUtils.DECORATOR_SCHEMA.setDefault(VisualItem.FONT, informationFont);
		infoDecorator = my_viz.addDecorators("inforNodes_deco", LayoutUtils.NODES, infoPredicate, LayoutUtils.DECORATOR_SCHEMA);
	}
	
	public void runNewProperties(VisualizationProperties vps) {
		
		stopDecorators();
		
		updateNodeShapes(vps);
		updateFillandStroke(vps);
		setDefaultLabels(vps);
		updateFont(vps);
		updateShapeThickness(vps);
		updateEdgeThickness(vps);
		updateEdgeColor(vps);
		
		runDecorators();
	}
	
	private void updateShapeThickness(VisualizationProperties vps) {
		
		Float[] strokes = new Float[4];
		
		strokes[0] = vps.getReactionStrokeThickness();
		strokes[1] = vps.getMetaboliteStrokeThickness();
		strokes[2] = vps.getCurrencyStrokeThickness();
		strokes[3] = vps.getInformationStrokeThickness();
		
		shapeStrokeThicknessAction.setReactStroke(strokes[0]);
		shapeStrokeThicknessAction.setMetStroke(strokes[1]);
		shapeStrokeThicknessAction.setCurrencyStroke(strokes[2]);
		shapeStrokeThicknessAction.setInfotroke(strokes[3]);
		
		setShapeThickness(strokes);
	}
	
	private void setShapeThickness(Float[] strokes) {
		this.reaction_stroke = strokes[0];
		this.metabolite_stroke = strokes[1];
		this.currency_stroke = strokes[2];
		this.information_stroke = strokes[3];
	}
	
	private void updateEdgeColor(VisualizationProperties vps) {
		int edgeColor = vps.getReactionStrokeColor();
		
		edgeInfoStrokeColorAction.setDefaultColor(edgeColor);
		edgeHubStrokeColorAction.setDefaultColor(edgeColor);
		edgeConvStrokeColorAction.setDefaultColor(edgeColor);
		arrowInfoColorAction.setDefaultColor(edgeColor);
		arrowHubColorAction.setDefaultColor(edgeColor);
		arrowConvColorAction.setDefaultColor(edgeColor);
		
		setEdgeColor(edgeColor);
	}
	
	private void setEdgeColor(int edgeColor) {
		hub_edgeColor = edgeColor;
		info_edgeColor = edgeColor;
		conversion_edgeColor = edgeColor;
	}
	
	private void updateNodeShapes(VisualizationProperties vps) {
		
		Integer x = vps.getMetaboliteShape();
		if (x != null) setMetaboliteNodeShape(x);
		x = vps.getCurrencyShape();
		if (x != null) setCurrencyNodeShape(x);
		x = vps.getReactionShape();
		if (x != null) setReactionNodeShape(x);
		x = vps.getInformationShape();
		if (x != null) setInfoNodeShape(x);
		
	}
	
	private void updateEdgeThickness(VisualizationProperties vps) {
		Float thickness = vps.getEdgeThickness();
		if (thickness != null) setInfoEdgeStroke(thickness);
	}
	
	private void setInfoEdgeStroke(float edgeThickness) {
		this.edge_thickness = edgeThickness;
		shapeStrokeThicknessAction.setReactStroke(edgeThickness);
		edgeStrokeAction.setDefaultSize(edgeThickness);
	}
	
	private void updateFont(VisualizationProperties vps) {
		
		Integer x;
		
		if (vps.getMetaboliteFont() != null) metaboliteLabelFont = FontLib.getFont(vps.getMetaboliteFont().getName(), vps.getMetaboliteFont().getStyle(), vps.getMetaboliteFont().getSize());
		if (vps.getCurrencyFont() != null) currencyLabelFont = FontLib.getFont(vps.getCurrencyFont().getName(), vps.getCurrencyFont().getStyle(), vps.getCurrencyFont().getSize());
		if (vps.getReactionFont() != null) reactionLabelFont = FontLib.getFont(vps.getReactionFont().getName(), vps.getReactionFont().getStyle(), vps.getReactionFont().getSize());
		if (vps.getInformationFont() != null) informationFont = FontLib.getFont(vps.getInformationFont().getName(), vps.getInformationFont().getStyle(), vps.getInformationFont().getSize());
		
		//label colors
		x = vps.getMetaboliteFontSize();
		if (x != null) metabolite_label_Color = vps.getMetaboliteFontSize();
		x = vps.getCurrencyFontSize();
		if (x != null) currency_Label_Color = vps.getCurrencyFontSize();
		x = vps.getReactionFonSize();
		if (x != null) reaction_Label_Color = vps.getReactionFonSize();
		x = vps.getInformationFontColor();
		if (x != null) information_label_Color = vps.getInformationFontColor();
		
	}
	
	private void updateFillandStroke(VisualizationProperties vps) {
		//Fill and stroke of metabolites
		Integer x = vps.getMetaboliteFillColor();
		if (x != null) setMetNodesColor(x);
		x = vps.getMetaboliteStrokeColor();
		if (x != null) setMetStrokeColor(x);
		
		//Fill and stroke of currency
		x = vps.getCurrencyFillColor();
		if (x != null) setHUBNodesColor(x);
		x = vps.getCurrencyStrokeColor();
		if (x != null) setHubStrokeColor(x);
		
		//Fill and stroke of reaction
		x = vps.getReactionFillColor();
		if (x != null) setReaccNodesColor(x);
		x = vps.getReactionStrokeColor();
		if (x != null) setReacStrokeColor(x);
		
		//Fill and stroke of info nodes
		x = vps.getInformationFillColor();
		if (x != null) setInfoNodesColor(x);
		x = vps.getInformationStrokeColor();
		if (x != null) setInfoStrokeColor(x);
		
	}
	
	public void setDefaultLabels(VisualizationProperties vps) {
		
		metaboliteLabels = vps.getMetaboliteLabels();
		reactionLabels = vps.getReactionLabels();
		currencyLabels = vps.getCurrencyLabels();
		infoLabels = vps.getInformationLabels();
		changeAllLabes();
		
	}
	
	private void updateLabelNode(VisualItem item) {
		String label = "";
		String id = item.getString(LayoutUtils.ID);
		String type = item.getString(LayoutUtils.TYPE);
		
		if (type.equals(LayoutUtils.MET_T) && metaboliteLabels != null && metaboliteLabels.containsKey(id))
			label = metaboliteLabels.get(id);
		else if (type.equals(LayoutUtils.REA_T) && reactionLabels != null && reactionLabels.containsKey(id))
			label = reactionLabels.get(id);
		else if (type.equals(LayoutUtils.HUBS) && currencyLabels != null && currencyLabels.containsKey(id))
			label = currencyLabels.get(id);
		else if (type.equals(LayoutUtils.INFO) && infoLabels != null && infoLabels.containsKey(id)) label = infoLabels.get(id);
		
		if (!label.equals("")) item.setString(LayoutUtils.LABEL, label);
	}
	
	public void updateLabel(VisualItem nodeItem) {
		
		//		FIXME: Is this necessary
		synchronized (my_viz) {
			updateLabelNode(nodeItem);
		}
	}
	
	private void changeAllLabes() {
		synchronized (my_viz) {
			
			Iterator items = my_viz.items(LayoutUtils.NODES);
			
			oldLabelsToNewNodes = new HashMap<String, String>();
			while (items.hasNext()) {
				
				VisualItem item = (VisualItem) items.next();
				updateLabelNode(item);
			}
		}
	}
	
	private void setMetNodesColor(Integer x) {
		colors[2] = x;
		node_colorMet.setDefaultColor(x);
	}
	
	private void setMetStrokeColor(Integer x) {
		met_stroke_color = x;
		node_strokeMet.setDefaultColor(x);
	}
	
	private void setInfoNodesColor(Integer x) {
		colors[0] = x;
		node_colorInfo.setDefaultColor(x);
	}
	
	private void setInfoStrokeColor(Integer x) {
		this.info_stroke_color = x;
		node_strokeInfo.setDefaultColor(x);
	}
	
	private void setReaccNodesColor(Integer x) {
		colors[4] = x;
		node_colorReact.setDefaultColor(x);
	}
	
	private void setReacStrokeColor(Integer x) {
		reac_stroke_color = x;
		node_strokeReact.setDefaultColor(x);
	}
	
	private void setHUBNodesColor(Integer x) {
		colors[3] = 3;
		node_colorHUB.setDefaultColor(x);
	}
	
	private void setHubStrokeColor(Integer x) {
		this.info_stroke_color = x;
		node_strokeHUB.setDefaultColor(x);
	}
	
	private void setInfoNodeShape(Integer x) {
		shapes[0] = x;
		shapeAction.upDateInfoShape(x);
		
	}
	
	private void setReactionNodeShape(Integer x) {
		shapes[3] = x;
		shapeAction.upDateReactionShape(x);
		
	}
	
	private void setCurrencyNodeShape(Integer x) {
		shapes[2] = x;
		shapeAction.upDateCurrencyShape(x);
		
	}
	
	private void setMetaboliteNodeShape(Integer x) {
		shapes[1] = x;
		shapeAction.upDateMetaboliteShape(x);
		
	}
	
	public void reset() {
		stopDecorators();
		clear();
		//		my_viz = null;
	}
	
}
