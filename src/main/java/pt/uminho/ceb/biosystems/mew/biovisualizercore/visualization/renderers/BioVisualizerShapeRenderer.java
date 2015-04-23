package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import prefuse.render.AbstractShapeRenderer;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.OptVisualExtensions;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.ShapeManager;


public class BioVisualizerShapeRenderer extends AbstractShapeRenderer {

    private double width=10;
    private double height = 10;
    
    private Ellipse2D   m_ellipse = new Ellipse2D.Double();
    private Rectangle2D m_rect = new Rectangle2D.Double();
    private GeneralPath m_path = new GeneralPath();

    /**
     * Creates a new ShapeRenderer with defnewtestault base size of 10 pixels.
     */
    public BioVisualizerShapeRenderer() {
    }
    
    /**
     * Creates a new ShapeRenderer with given base size.
     * @param size the base size in pixels
     */
    public BioVisualizerShapeRenderer(double width, double height) {
       setBaseSize(width, height);
    }
    
    /**
     * Sets the base size, in pixels, for shapes drawn by this renderer. The
     * base size is the width and height value used when a VisualItem's size
     * value is 1. The base size is scaled by the item's size value to arrive
     * at the final scale used for rendering.
     * @param size the base size in pixels
     */
    public void setBaseSize(double width, double height) {
        this.height = height;
        this.width = width;
    }
    
    /**
     * Returns the base size, in pixels, for shapes drawn by this renderer.
     * @return the base size in pixels
     */
//    public int getBaseSize() {
//        return m_baseSize;
//    }
    
    /**
     * @see prefuse.render.AbstractShapeRenderer#getRawShape(prefuse.visual.VisualItem)
     */
    protected Shape getRawShape(VisualItem item) {
    	
    	double visual_width=item.getDouble(OptVisualExtensions.VISUAL_WIDTH);
    	double visual_height=item.getDouble(OptVisualExtensions.VISUAL_HEIGHT);
    	
        int stype = item.getShape();
        double x = item.getX();
        if ( Double.isNaN(x) || Double.isInfinite(x) )
            x = 0;
        double y = item.getY();
        if ( Double.isNaN(y) || Double.isInfinite(y) )
            y = 0;
        double width = visual_width*item.getSize();
        double height = visual_height*item.getSize();
        // Center the shape around the specified x and y
        if ( width > 1 ) {
            x = x-height/2;
            y = y-width/2;
        }
        
        return ShapeManager.getShape(stype, x, y, width, height, m_path, m_ellipse, m_rect);
       
    }
	
} // end of class ShapeRenderer
