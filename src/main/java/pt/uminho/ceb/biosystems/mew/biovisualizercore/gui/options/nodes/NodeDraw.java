package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.nodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.JFrame;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.Constants;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.ShapeManager;


public class NodeDraw extends JComponent{

	private static final long serialVersionUID = 1L;

	private int sCode = Constants.SHAPE_NONE;
	private Color cLine = Color.black;
	private Color cfill = Color.GRAY;
	
	private double width = 50;
	private double height = 50;
	
	
	public NodeDraw(){
		super();
	}
	
	public NodeDraw(double width, double height){
		super();
		this.width = width;
		this.height = height;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		setSize(50, 50);
	    super.paintComponent(g);
	    g.setColor(cfill);
//	    Shape shape = question(0,0,100);
	    Shape shape = ShapeManager.getShape(sCode, 0, 0, width, height);
	    if(shape!=null){
	    	g2.fill(shape);
	    	g.setColor(cLine);
		    g2.draw(shape);
	    }
	    updateUI();
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension((int) width, (int) height + 2);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getSize();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getSize();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
	
	public void setShape(int sCode) {
		this.sCode = sCode;
	}

	public Color getC() {
		return cfill;
	}

	public void setColor(Color c) {
		this.cfill = c;
	}

	public void setShapeWidth(double width) {
		this.width = width;
	}

	public double getShapeWidth() {
		return width;
	}
	
	public double getShapeHeight() {
		return height;
	}


	public void setShapeHeight(double height) {
		this.height = height;
	}

	public static void main(String args[]) {
		 NodeDraw nd = new NodeDraw();
		 nd.setShape(Constants.SHAPE_QUESTION);
		 JFrame mainFrame = new JFrame("Graphics demo");
	     mainFrame.getContentPane().add(nd);
	     mainFrame.pack();
	     mainFrame.setVisible(true);
	}
}
