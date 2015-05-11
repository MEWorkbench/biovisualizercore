package pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.properties;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.AbstractLabelsConstructor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.IFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite.MetLabelMethodIDs;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite.MetaboliteFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction.ReactionFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction.ReactionLabelMethodIDs;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.VisualizationProperties;
import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.FontException;
import pt.uminho.ceb.biosystems.mew.guiutilities.fonts.FontManager;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public class VPropertyConstants {
	
	public static final String TREE_PATH_NODES_LABEL = "Visualization.Metabolite_Label";
	public static final String TREE_PATH_NODES_SHAPE = "Visualization.Metabolite_Shape";
	public static final String TREE_PATH_EDGES_LABEL = "Visualization.Reaction_Label";
	public static final String TREE_PATH_EDGES_SHAPE = "Visualization.Reaction_Shape";
	
	// Nodes
	public static final String FONT_METABOLITE_PROP = TREE_PATH_NODES_LABEL + ".Font_Metabolite";
	public static final String FONTSTYLE_METABOLITE_PROP = TREE_PATH_NODES_LABEL + ".Style_Metabolite";
	public static final String FONTSIZE_METABOLITE_PROP = TREE_PATH_NODES_LABEL + ".Size_Metabolite";
	public static final String FONTCOLOR_METABOLITE_PROP = TREE_PATH_NODES_LABEL + ".FontColor_Metabolite";
	public static final String INFORMATION_METABOLITE_PROP = TREE_PATH_NODES_LABEL + ".Information_Metabolite";
	
	public static final String FILLCOLOR_METABOLITE_PROP = TREE_PATH_NODES_SHAPE + ".FillColor_Metabolite";
	public static final String SHAPE_METABOLITE_PROP = TREE_PATH_NODES_SHAPE + ".Shape_Metabolite";
	public static final String STROKECOLOR_METABOLITE_PROP = TREE_PATH_NODES_SHAPE + ".StrokeColor_Metabolite";
	public static final String STROKETHICKNESS_METABOLITE_PROP = TREE_PATH_NODES_SHAPE + ".StrokeThickness_Metabolite";
	
	
	public static final String FONT_CURRENCY_PROP = TREE_PATH_NODES_LABEL + ".Font_Currency";
	public static final String FONTSTYLE_CURRENCY_PROP = TREE_PATH_NODES_LABEL + ".Style_Currency";
	public static final String FONTSIZE_CURRENCY_PROP = TREE_PATH_NODES_LABEL + ".Size_Currency";
	public static final String FONTCOLOR_CURRENCY_PROP = TREE_PATH_NODES_LABEL + ".FontColor_Currency";
	public static final String INFORMATION_CURRENCY_PROP = TREE_PATH_NODES_LABEL + ".Information_Currency";
	
	public static final String FILLCOLOR_CURRENCY_PROP = TREE_PATH_NODES_SHAPE + ".FillColor_Currency";
	public static final String SHAPE_CURRENCY_PROP = TREE_PATH_NODES_SHAPE + ".Shape_Currency";
	public static final String STROKECOLOR_CURRENCY_PROP = TREE_PATH_NODES_SHAPE + ".StrokeColor_Currency";
	public static final String STROKETHICKNESS_CURRENCY_PROP = TREE_PATH_NODES_SHAPE + ".StrokeThickness_Currency";
	
	
	public static final String FONT_INFORMATION_PROP = TREE_PATH_NODES_LABEL + ".Font_Information";
	public static final String FONTSTYLE_INFORMATION_PROP = TREE_PATH_NODES_LABEL + ".Style_Information";
	public static final String FONTSIZE_INFORMATION_PROP = TREE_PATH_NODES_LABEL + ".Size_Information";
	public static final String FONTCOLOR_INFORMATION_PROP = TREE_PATH_NODES_LABEL + ".FontColor_Information";
	public static final String INFORMATION_INFORMATION_PROP = TREE_PATH_NODES_LABEL + ".Information_Information";
	
	public static final String FILLCOLOR_INFORMATION_PROP = TREE_PATH_NODES_SHAPE + ".FillColor_Information";
	public static final String SHAPE_INFORMATION_PROP = TREE_PATH_NODES_SHAPE + ".Shape_Information";
	public static final String STROKECOLOR_INFORMATION_PROP = TREE_PATH_NODES_SHAPE + ".StrokeColor_Information";
	public static final String STROKETHICKNESS_INFORMATION_PROP = TREE_PATH_NODES_SHAPE + ".StrokeThickness_Information";


	public static final String FONT_DEFAULTVALUE = "Lucida Bright Regular";
	public static final String FONTSTYLE_DEFAULTVALUE = "bold";
	public static final String FONTSIZE_DEFAULTVALUE = "10";
	public static final String FONTCOLOR_DEFAULTVALUE = "0_0_0_255"; // R_G_B_ALPHA
	public static final String INFORMATION_DEFAULTVALUE = "{" + MetLabelMethodIDs.ID + "}";
	public static final String METABOLITE_FILLCOLOR_DEFAULTVALUE = "153_255_102_255"; // R_G_B_ALPHA
	public static final String CURRENCY_FILLCOLOR_DEFAULTVALUE = "255_255_51_255"; // R_G_B_ALPHA
	public static final String INFORMATION_FILLCOLOR_DEFAULTVALUE = "0_102_102_255"; // R_G_B_ALPHA
	
	public static final String METABOBLITE_SHAPE_DEFAULTVALUE = "1";
	public static final String CURRENCY_SHAPE_DEFAULTVALUE = "1";
	public static final String INFORMATION_SHAPE_DEFAULTVALUE = "0";
	
	public static final String STROKECOLOR_DEFAULTVALUE = "102_102_102_255"; // R_G_B_ALPHA
	public static final String STROKETHICKNESS_DEFAULTVALUE = "2"; // R_G_B_ALPHA
	
	
	// Edges
	public static final String FONT_REACTION_PROP = TREE_PATH_EDGES_LABEL + ".Font_Reaction";
	public static final String FONTSTYLE_REACTION_PROP = TREE_PATH_EDGES_LABEL + ".Style_Reaction";
	public static final String FONTSIZE_REACTION_PROP = TREE_PATH_EDGES_LABEL + ".Size_Reaction";
	public static final String FONTCOLOR_REACTION_PROP = TREE_PATH_EDGES_LABEL + ".FontColor_Reaction";
	public static final String INFORMATION_REACTION_PROP = TREE_PATH_EDGES_LABEL + ".Information_Reaction";
	
	public static final String FILLCOLOR_REACTION_PROP = TREE_PATH_EDGES_SHAPE + ".Color_Reaction";
	public static final String STROKECOLOR_REACTION_PROP = TREE_PATH_EDGES_SHAPE + ".StrokeColor_Reaction";
	public static final String SHAPE_REACTION_PROP = TREE_PATH_EDGES_SHAPE + ".Shape_Reaction";
	public static final String STROKETHICKNESS_REACTION_PROP = TREE_PATH_EDGES_SHAPE + ".StrokeThickness_Reaction";
	

	public static final String FONT_REACTION_DEFAULTVALUE = "Lucida Bright Regular";
	public static final String FONTSTYLE_REACTION_DEFAULTVALUE = "bold";
	public static final String FONTSIZE_REACTION_DEFAULTVALUE = "10";
	public static final String FONTCOLOR_REACTION_DEFAULTVALUE = "0_0_0_255"; // R_G_B_ALPHA
	public static final String INFORMATION_REACTION_DEFAULTVALUE = "{" + ReactionLabelMethodIDs.ID + "}\n{" + ReactionLabelMethodIDs.EC_NUMBER + "}";
	public static final String FILLCOLOR_REACTION_DEFAULTVALUE = "102_102_102_255"; // R_G_B_ALPHA
	public static final String SHAPE_REACTION_DEFAULTVALUE = "2"; 
	public static final String STROKECOLOR_REACTION_DEFAULTVALUE = "102_102_102_255"; // R_G_B_ALPHA
	public static final String STROKETHICKNESS_REACTION_DEFAULTVALUE = "3"; // R_G_B_ALPHA
	
	
	public static final String COLOR_RGB_SEPARATOR = "_";
	
	
	public static final String FILE = "./conf/Properties/visualization.conf";
	
	static MetaboliteFactoryLabel metLabFactory = new MetaboliteFactoryLabel();
	
	public static MetaboliteFactoryLabel getMetaboliteFactory(){
		return metLabFactory;
	}
	
	public static String convertColor(Color colorPropValue){
		Color color = (Color) colorPropValue;
		String rgb = color.getRed() + VPropertyConstants.COLOR_RGB_SEPARATOR + 
				color.getGreen() + VPropertyConstants.COLOR_RGB_SEPARATOR + 
				color.getBlue() + VPropertyConstants.COLOR_RGB_SEPARATOR +
				color.getAlpha();
		return rgb;
	}
	
	public static Color decodeColor(String colorPropValue){
		String[] tokens = colorPropValue.split(VPropertyConstants.COLOR_RGB_SEPARATOR);
		if(tokens.length>2)
		{
			int alpha = (tokens.length>3) ? Integer.parseInt(tokens[3]) : 0;
			return new Color(Integer.parseInt(tokens[0]), 
					Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), alpha);
		}
		return null;
	}
	
	public static String convertFont(Font fontPropValue){
		return fontPropValue.getFontName();
	}
	
	public static Font decodeFont(String fontPropValue){
		try {
			return FontManager.getManager().getRenderingFont(fontPropValue);
		} catch (FontException e) {e.printStackTrace();}
		try {
			return FontManager.getManager().getRenderingFont(FONT_DEFAULTVALUE);
		} catch (FontException e) {e.printStackTrace();}
		return null;
	}
	
	public static Integer decodeFontSize(String sizePropValue){
		try {
			return Integer.decode(sizePropValue);
		} catch (Exception e) {e.printStackTrace();}
		return Integer.decode(FONTSIZE_DEFAULTVALUE);
	}
	
	public static Integer decodeStrokeThickness(String thickness){
		try {
			return Integer.decode(thickness);
		} catch (Exception e) {e.printStackTrace();}
		return Integer.decode(STROKETHICKNESS_DEFAULTVALUE);
	}
	
	public static Integer decodeShape(String shapeCode){
		try {
			return Integer.decode(shapeCode);
		} catch (Exception e) {e.printStackTrace();}
		return Integer.decode("1");
	}
	
	public static String convertLabelConstructor(AbstractLabelsConstructor<Object> lConstructorPropValue){
		String label = "";
		for(int i=0; i<lConstructorPropValue.countMethods(); i++)
		{
			Pair<String, String> lS = lConstructorPropValue.getMethodAndSep(i);
			label += "{" + lS.getA() + "}" + lS.getB();
		}
		return label;
	}
	
//	public static final AbstractLabelsConstructor<Object> decodeLabelConstructor(String lConstructorPropValue){
//		Pattern p = Pattern.compile("(\\w+)(\\W*)");
//		AbstractLabelsConstructor<Object> labelConstructor = new AbstractLabelsConstructor<Object>(new FakeIMethods());
//
//		Matcher m = p.matcher(lConstructorPropValue);
//		while(m.find())
//			labelConstructor.addLabelInfo(m.group(1), m.group(2));
//		
//		return labelConstructor;
//	}
	
	
	public static AbstractLabelsConstructor<Container> decodeMetLabelConstructor(String lConstructorPropValue){
		IFactoryLabel<Container> metLabelConstructInfo = getMetaboliteFactory();
		return decodeLabelConstructor(lConstructorPropValue, metLabelConstructInfo);
	}
	
	public static AbstractLabelsConstructor<Container> decodeReacLabelConstructor(String lConstructorPropValue){
		IFactoryLabel<Container> reacLabelConstructorInfo = new ReactionFactoryLabel();
		return decodeLabelConstructor(lConstructorPropValue, reacLabelConstructorInfo);
	}
	
	public static <T> AbstractLabelsConstructor<T> decodeLabelConstructor(String lConstructorPropValue, IFactoryLabel<T> fatoryLabel){
		Pattern p = Pattern.compile("\\{([^\\}]+)\\}([^\\{\\}]*)");
		AbstractLabelsConstructor<T> labelConstructor = new AbstractLabelsConstructor<T>(fatoryLabel);

		Matcher m = p.matcher(lConstructorPropValue);
		while(m.find())
			labelConstructor.addLabelInfo(m.group(1), m.group(2));
		
		return labelConstructor;
	}
	
	
	public static VisualizationProperties createProperties(String file) throws FileNotFoundException, IOException{
		
		Properties p = new Properties();
		p.load(new FileInputStream(file));
		
		VisualizationProperties properties = new VisualizationProperties();
		Font font = decodeFont(p.getProperty(VPropertyConstants.FONT_METABOLITE_PROP));
		int style =FontManager.convertStyle(p.getProperty(VPropertyConstants.FONTSTYLE_METABOLITE_PROP));
		int size = decodeFontSize( p.getProperty(VPropertyConstants.FONTSIZE_METABOLITE_PROP));
		properties.setMetaboliteFont(new Font(font.getFontName(), style, size));
		properties.setMetaboliteFontColor((decodeColor( p.getProperty(VPropertyConstants.FONTCOLOR_METABOLITE_PROP))).getRGB());
		int shape = Integer.parseInt( p.getProperty(VPropertyConstants.SHAPE_METABOLITE_PROP));
		properties.setMetaboliteShape(shape);
		int fillColor = (decodeColor( p.getProperty(VPropertyConstants.FILLCOLOR_METABOLITE_PROP))).getRGB();
		properties.setMetaboliteFillColor(fillColor);
		int strokeColor = (decodeColor( p.getProperty(VPropertyConstants.STROKECOLOR_METABOLITE_PROP))).getRGB();
		properties.setMetaboliteStrokeColor(strokeColor);
		AbstractLabelsConstructor<Container> metLabelConstructor = decodeMetLabelConstructor( p.getProperty(VPropertyConstants.INFORMATION_METABOLITE_PROP));
		properties.setMetLabelConstructor(metLabelConstructor);
		int strokeThickness = Integer.parseInt( p.getProperty(VPropertyConstants.STROKETHICKNESS_METABOLITE_PROP));
		float sF = Float.valueOf(strokeThickness+"");
		properties.setMetaboliteStrokeThickness(sF);
		
		font = decodeFont( p.getProperty(VPropertyConstants.FONT_CURRENCY_PROP));
		style = FontManager.convertStyle( p.getProperty(VPropertyConstants.FONTSTYLE_CURRENCY_PROP));
		size = Integer.parseInt( p.getProperty(VPropertyConstants.FONTSIZE_CURRENCY_PROP));
		properties.setCurrencyFont(new Font(font.getFontName(), style, size));
		properties.setCurrencyFontColor((decodeColor( p.getProperty(VPropertyConstants.FONTCOLOR_CURRENCY_PROP))).getRGB());
		shape = Integer.parseInt( p.getProperty(VPropertyConstants.SHAPE_CURRENCY_PROP));
		properties.setCurrencyShape(shape);
		fillColor = (decodeColor( p.getProperty(VPropertyConstants.FILLCOLOR_CURRENCY_PROP))).getRGB();
		properties.setCurrencyFillColor(fillColor);
		strokeColor = (decodeColor( p.getProperty(VPropertyConstants.STROKECOLOR_CURRENCY_PROP))).getRGB();
		properties.setCurrencyStrokeColor(strokeColor);
		AbstractLabelsConstructor<Container> curLabelConstructor = decodeMetLabelConstructor( p.getProperty(VPropertyConstants.INFORMATION_CURRENCY_PROP));
		properties.setCurLabelConstructor(curLabelConstructor);
		strokeThickness = Integer.parseInt( p.getProperty(VPropertyConstants.STROKETHICKNESS_CURRENCY_PROP));
		sF = Float.valueOf(strokeThickness+"");
		properties.setCurrencyStrokeThickness(sF);
				
		font = decodeFont( p.getProperty(VPropertyConstants.FONT_INFORMATION_PROP));
		style = FontManager.convertStyle( p.getProperty(VPropertyConstants.FONTSTYLE_INFORMATION_PROP));
		size = Integer.parseInt( p.getProperty(VPropertyConstants.FONTSIZE_INFORMATION_PROP));
		properties.setInformationFont(new Font(font.getFontName(), style, size));
		properties.setInformationFontColor((decodeColor( p.getProperty(VPropertyConstants.FONTCOLOR_INFORMATION_PROP))).getRGB());
		shape = Integer.parseInt( p.getProperty(VPropertyConstants.SHAPE_INFORMATION_PROP));
		properties.setInformationShape(shape);
		fillColor = (decodeColor( p.getProperty(VPropertyConstants.FILLCOLOR_INFORMATION_PROP))).getRGB();
		properties.setInformationFillColor(fillColor);
		strokeColor = (decodeColor( p.getProperty(VPropertyConstants.STROKECOLOR_INFORMATION_PROP))).getRGB();
		properties.setInformationStrokeColor(strokeColor);
		AbstractLabelsConstructor<Container> infoLabelConstructor = decodeMetLabelConstructor( p.getProperty(VPropertyConstants.INFORMATION_INFORMATION_PROP));
		properties.setInfoLabelConstructor(infoLabelConstructor);
		strokeThickness = Integer.parseInt( p.getProperty(VPropertyConstants.STROKETHICKNESS_INFORMATION_PROP));
		sF = Float.valueOf(strokeThickness+"");
		properties.setInformationStrokeThickness(sF);
		
		font = decodeFont( p.getProperty(VPropertyConstants.FONT_REACTION_PROP));
		style = FontManager.convertStyle( p.getProperty(VPropertyConstants.FONTSTYLE_REACTION_PROP));
		size = Integer.parseInt( p.getProperty(VPropertyConstants.FONTSIZE_REACTION_PROP));
		properties.setReactionFont(new Font(font.getFontName(), style, size));
		properties.setReactionFonSize((decodeColor( p.getProperty(VPropertyConstants.FONTCOLOR_REACTION_PROP))).getRGB());
		shape = Integer.parseInt( p.getProperty(VPropertyConstants.SHAPE_REACTION_PROP));
		properties.setReactionShape(shape);
		fillColor = (decodeColor( p.getProperty(VPropertyConstants.FILLCOLOR_REACTION_PROP))).getRGB();
		properties.setReactionFillColor(fillColor);
		strokeColor = (decodeColor( p.getProperty(VPropertyConstants.STROKECOLOR_REACTION_PROP))).getRGB();
		properties.setReactionStrokeColor(strokeColor);
		AbstractLabelsConstructor<Container> reacLabelConstructor = decodeReacLabelConstructor( p.getProperty(VPropertyConstants.INFORMATION_REACTION_PROP));
		properties.setReacLabelConstructor(reacLabelConstructor);
		strokeThickness = Integer.parseInt( p.getProperty(VPropertyConstants.STROKETHICKNESS_REACTION_PROP));
		sF = Float.valueOf(strokeThickness+"");
		properties.setEdgeThickness(sF);
		properties.setReactionStrokeThickness(sF);
		
		return properties;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Pattern p = Pattern.compile("\\{([^\\}]+)\\}([^\\{\\}]*)");
		Matcher m = p.matcher("{id},{name}{ec number}\n{abc_def} {gh}");
		while(m.find())
		{
			System.out.println("<key>" + m.group(1) + "</key>" + "<separator>" + m.group(2) + "</separator>");
		}
		
		createProperties("/home/pvilaca/Work/invista/ko_study/visualization.conf");
	}
	
	
	
	
}
