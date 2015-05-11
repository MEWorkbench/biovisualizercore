package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.LayoutVisualizerGUI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.AbstractLabelsConstructor;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite.MetLabelMethodIDs;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.metabolite.MetaboliteFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction.ReactionFactoryLabel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.constructor.reaction.ReactionLabelMethodIDs;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.LayoutVisualizator;

/**
 * Class that carries the properties of the visualization to the
 * {@link LayoutVisualizerGUI} and from there to the {@link LayoutVisualizator}.
 * It stores the colors and shapes for each type of nodes and edges. It can also 
 * carry new labels, allowing for the user to change them according to a template. 
 * @author Noronha
 *
 */
public class VisualizationProperties {

	
	private int metaboliteShape = 1;
	private int currencyShape = 1;
	private int reactionShape = 2;
	private int informationShape = 0;
	
	private int metaboliteFillColor = new Color(153,255,102,255).getRGB(); 
	private int currencyFillColor = new Color(255,255,51,255).getRGB();
	private int reactionFillColor = new Color(102,102,102,255).getRGB(); 
	private int informationFillColor = new Color(0,102,102,255).getRGB();
	
	private int metaboliteStrokeColor = new Color(102, 102, 102, 255).getRGB();
	private int currencyStrokeColor = new Color(102, 102, 102, 255).getRGB();
	private int reactionStrokeColor = new Color(102, 102, 102, 255).getRGB();
	private int informationStrokeColor = new Color(102, 102, 102, 255).getRGB();
	
	private float edgeThickness = 3;
	private int edgeColor = Color.BLUE.getRGB();
	
	private Map<String, String> metaboliteLabels;
	private Map<String, String> currencyLabels;
	private Map<String, String> reactionLabels;
	private Map<String, String> informationLabels;
	
	private Font metaboliteFont;
	private Font currencyFont;
	private Font reactionFont;
	private Font informationFont;
	
	private int metaboliteFontColor = new Color(0, 0, 0, 255).getRGB();
	private int currencyFontColor = new Color(0, 0, 0, 255).getRGB();
	private int reactionFonColor = new Color(0, 0, 0, 255).getRGB();
	private int informationFontColor = new Color(0, 0, 0, 255).getRGB();
	
	private float metaboliteStrokeThickness =2f;
	private float currencyStrokeThickness = 2f;
	private float informationStrokeThickness = 2f;
	private float reactionStrokeThickness = 2f;
	
	public static AbstractLabelsConstructor<Container> getMetaboliteConstructor(){
		AbstractLabelsConstructor<Container> labelConstructor = new AbstractLabelsConstructor<Container>(new MetaboliteFactoryLabel());
		labelConstructor.addLabelInfo(MetLabelMethodIDs.ID, "");
		return labelConstructor;
	}
	
	public static AbstractLabelsConstructor<Container> getReactionsConstructor(){
		AbstractLabelsConstructor<Container> labelConstructor = new AbstractLabelsConstructor<Container>(new ReactionFactoryLabel());
		labelConstructor.addLabelInfo(ReactionLabelMethodIDs.ID, "");
		return labelConstructor;
	}
	
	/** Metabolite node label constructor */
	protected AbstractLabelsConstructor<Container> metLabelConstructor = getMetaboliteConstructor();
	/** Currency node label constructor */
	protected AbstractLabelsConstructor<Container> curLabelConstructor = getMetaboliteConstructor();
	/** Inforamtion node label constructor */
	protected AbstractLabelsConstructor<Container> infoLabelConstructor = getMetaboliteConstructor();
	/** Reaction node label constructor */
	protected AbstractLabelsConstructor<Container> reacLabelConstructor = getReactionsConstructor();
	
	
	public VisualizationProperties() {
		metaboliteLabels = new HashMap<String, String>();
		currencyLabels = new HashMap<String, String>();
		reactionLabels = new HashMap<String, String>();
		informationLabels = new HashMap<String, String>();
		metaboliteFont = new Font("Lucida Bright Regular", Font.BOLD, 9);
		currencyFont = new Font("Lucida Bright Regular", Font.BOLD, 9);
		reactionFont = new Font("Lucida Bright Regular", Font.BOLD, 9);
		informationFont = new Font("Lucida Bright Regular", Font.BOLD, 9);
}
	
		
	/** Update the node labels from the container information */
	public void updateLabels(Container container){
		for(String metId : container.getMetabolites().keySet())
		{
			String metLabel = metId;
			try {
				metLabel = metLabelConstructor.getLabel(container, metId);
				System.out.println(metId + "\t" + metLabel + "\t" + container);
				metaboliteLabels.put(metId, metLabel);
			} catch (Exception e) {
//				e.printStackTrace();
			}
			
		}
		
		for(String curId : container.getMetabolites().keySet())
		{
			String curLabel = curId;
			try {
				curLabel = curLabelConstructor.getLabel(container, curId);
				currencyLabels.put(curId, curLabel);
			} catch (Exception e) {e.printStackTrace();}
			
		}
		
		for(String infoId : container.getMetabolites().keySet())
		{
			String infoLabel = infoId;
			try {
				infoLabel = infoLabelConstructor.getLabel(container, infoId);
				informationLabels.put(infoId, infoLabel);
			} catch (Exception e) {e.printStackTrace();}
			
		}
		
		for(String rId : container.getReactions().keySet())
		{
			String rLabel = rId;
			try {
				rLabel = reacLabelConstructor.getLabel(container, rId);
				reactionLabels.put(rId, rLabel);
			} catch (Exception e) {e.printStackTrace();}
			
		}
		System.out.println("Updating....");
		System.out.println("Reactions: " + reactionLabels);
		System.out.println("Metabolites: " + metaboliteLabels);
		System.out.println("Currency: " + currencyLabels);
		System.out.println("Information: " + informationLabels);
	}
	
	/**
	 * Returns the metabolite label font size
	 * @return font size
	 */
	public int getMetaboliteFontSize() {
		return metaboliteFontColor;
	}
	
	/**
	 * Sets the property that defines the font size of the metabolite nodes.
	 * @param size
	 */
	public void setMetaboliteFontColor(int metaboliteFontSize) {
		this.metaboliteFontColor = metaboliteFontSize;
	}
	
	/**
	 * Returns the currency node label font size
	 * @return font size
	 */
	public int getCurrencyFontSize() {
		return currencyFontColor;
	}
	
	/**
	 * Sets the currency node label font size
	 * @param currencyFontSize
	 */
	public void setCurrencyFontColor(int currencyFontSize) {
		this.currencyFontColor = currencyFontSize;
	}
	
	/**
	 * Returns the reaction node label font size
	 * @return size
	 */
	public int getReactionFonSize() {
		return reactionFonColor;
	}
	
	/**
	 * Sets the reaction node label font size
	 * @param reactionFonSize
	 */
	public void setReactionFonSize(int reactionFonSize) {
		this.reactionFonColor = reactionFonSize;
	}
	
	/**
	 * Returns the information node label font size
	 * @return
	 */
	public int getInformationFontColor() {
		return informationFontColor;
	}
	
	/**
	 * Sets the information node label font size
	 * @param informationFontSize
	 */
	public void setInformationFontColor(int informationFontSize) {
		this.informationFontColor = informationFontSize;
	}
	
	/**
	 * Returns the shape of the metabolite nodes
	 * @return shape
	 */
	public int getMetaboliteShape() {
		return metaboliteShape;
	}
	
	/**
	 * Sets the shape of the metabolite nodes
	 * @param metaboliteShape
	 */
	public void setMetaboliteShape(int metaboliteShape) {
		this.metaboliteShape = metaboliteShape;
	}
	
	/**
	 * Returns the shape of the currency nodes
	 * @return shape
	 */
	public int getCurrencyShape() {
		return currencyShape;
	}
	
	/**
	 * Sets the shape of the currency nodes
	 * @param currencyShape
	 */
	public void setCurrencyShape(int currencyShape) {
		this.currencyShape = currencyShape;
	}
	
	/**
	 * Returns the reaction node shape
	 * @return shape
	 */
	public int getReactionShape() {
		return reactionShape;
	}
	
	/**
	 * Sets the shape of the reaction nodes
	 * @param reactionShape
	 */
	public void setReactionShape(int reactionShape) {
		this.reactionShape = reactionShape;
	}
	
	/**
	 * Returns the information nodes shape
	 * @return shape
	 */
	public int getInformationShape() {
		return informationShape;
	}
	
	/**
	 * Sets the shape of the information nodes
	 * @param informationShape
	 */
	public void setInformationShape(int informationShape) {
		this.informationShape = informationShape;
	}
	
	/**
	 * Returns the metabolite nodes fill color
	 * @return color
	 */
	public int getMetaboliteFillColor() {
		return metaboliteFillColor;
	}
	
	/**
	 * Sets the metabolite nodes fill color
	 * @param metaboliteFillColor
	 */
	public void setMetaboliteFillColor(int metaboliteFillColor) {
		this.metaboliteFillColor = metaboliteFillColor;
	}
	
	/**
	 * Returns the currency nodes fill color.
	 * @return
	 */
	public int getCurrencyFillColor() {
		return currencyFillColor;
	}
	public void setCurrencyFillColor(int currencyFillColor) {
		this.currencyFillColor = currencyFillColor;
	}
	public int getReactionFillColor() {
		return reactionFillColor;
	}
	public void setReactionFillColor(int reactionFillColor) {
		this.reactionFillColor = reactionFillColor;
	}
	public int getInformationFillColor() {
		return informationFillColor;
	}
	public void setInformationFillColor(int informationFillColor) {
		this.informationFillColor = informationFillColor;
	}
	public int getMetaboliteStrokeColor() {
		return metaboliteStrokeColor;
	}
	public void setMetaboliteStrokeColor(int metaboliteStrokeColor) {
		this.metaboliteStrokeColor = metaboliteStrokeColor;
	}
	public int getCurrencyStrokeColor() {
		return currencyStrokeColor;
	}
	public void setCurrencyStrokeColor(int currencyStrokeColor) {
		this.currencyStrokeColor = currencyStrokeColor;
	}
	public int getReactionStrokeColor() {
		return reactionStrokeColor;
	}
	public void setReactionStrokeColor(int reactionStrokeColor) {
		this.reactionStrokeColor = reactionStrokeColor;
	}
	public int getInformationStrokeColor() {
		return informationStrokeColor;
	}
	public void setInformationStrokeColor(int informationStrokeColor) {
		this.informationStrokeColor = informationStrokeColor;
	}
	public float getEdgeThickness() {
		return edgeThickness;
	}
	public void setEdgeThickness(float edgeThickness) {
		this.edgeThickness = edgeThickness;
	}
	public int getEdgeColor() {
		return edgeColor;
	}
	public void setEdgeColor(int edgeColor) {
		this.edgeColor = edgeColor;
	}
	public Map<String, String> getMetaboliteLabels() {
		return metaboliteLabels;
	}
	public void setMetaboliteLabels(Map<String, String> metaboliteLabels) {
		this.metaboliteLabels = metaboliteLabels;
	}
	public Map<String, String> getCurrencyLabels() {
		return currencyLabels;
	}
	public void setCurrencyLabels(Map<String, String> currencyLabels) {
		this.currencyLabels = currencyLabels;
	}
	public Map<String, String> getReactionLabels() {
		return reactionLabels;
	}
	public void setReactionLabels(Map<String, String> reactionLabels) {
		this.reactionLabels = reactionLabels;
	}
	public Map<String, String> getInformationLabels() {
		return informationLabels;
	}
	public void setInformationLabels(Map<String, String> informationLabels) {
		this.informationLabels = informationLabels;
	}
	public Font getMetaboliteFont() {
		return metaboliteFont;
	}
	public void setMetaboliteFont(Font metaboliteFont) {
		this.metaboliteFont = metaboliteFont;
	}
	public Font getCurrencyFont() {
		return currencyFont;
	}
	public void setCurrencyFont(Font currencyFont) {
		this.currencyFont = currencyFont;
	}
	public Font getReactionFont() {
		return reactionFont;
	}
	public void setReactionFont(Font reactionFont) {
		this.reactionFont = reactionFont;
	}
	public Font getInformationFont() {
		return informationFont;
	}
	public void setInformationFont(Font informationFont) {
		this.informationFont = informationFont;
	}

	public float getMetaboliteStrokeThickness() {return metaboliteStrokeThickness;}
	public void setMetaboliteStrokeThickness(float metaboliteStrokeThickness) {this.metaboliteStrokeThickness = metaboliteStrokeThickness;}
	public float getCurrencyStrokeThickness() {return currencyStrokeThickness;}
	public void setCurrencyStrokeThickness(float currencyStrokeThickness) {this.currencyStrokeThickness = currencyStrokeThickness;}
	public float getInformationStrokeThickness() {return informationStrokeThickness;}
	public void setInformationStrokeThickness(float informationStrokeThickness) {this.informationStrokeThickness = informationStrokeThickness;}
	public float getReactionStrokeThickness() {return reactionStrokeThickness;}
	public void setReactionStrokeThickness(float reactionStrokeThickness) {this.reactionStrokeThickness = reactionStrokeThickness;}
	public AbstractLabelsConstructor<Container> getMetLabelConstructor() {return metLabelConstructor;}
	public void setMetLabelConstructor(AbstractLabelsConstructor<Container> metLabelConstructor) {this.metLabelConstructor = metLabelConstructor;}
	public AbstractLabelsConstructor<Container> getCurLabelConstructor() {return curLabelConstructor;}
	public void setCurLabelConstructor(AbstractLabelsConstructor<Container> curLabelConstructor) {this.curLabelConstructor = curLabelConstructor;}
	public AbstractLabelsConstructor<Container> getInfoLabelConstructor() {return infoLabelConstructor;}
	public void setInfoLabelConstructor(AbstractLabelsConstructor<Container> infoLabelConstructor) {this.infoLabelConstructor = infoLabelConstructor;}
	public AbstractLabelsConstructor<Container> getReacLabelConstructor() {return reacLabelConstructor;}
	public void setReacLabelConstructor(AbstractLabelsConstructor<Container> reacLabelConstructor) {this.reacLabelConstructor = reacLabelConstructor;}
}
