package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class ShapeManager {
	
	public static Shape question(GeneralPath m_path, float x, float y, float h) {
		
		float dec = h/10;
		float mid = h/2;
		
		m_path.reset();
		m_path.moveTo(x, y+dec);
		m_path.lineTo(x, y+4*dec);
		m_path.lineTo(x+ 2*dec, y+4*dec);
		m_path.lineTo(x+ 2*dec, y+3*dec);
		m_path.lineTo(x + 3*dec, y + 2*dec);
		m_path.lineTo(x+7*dec, y+2*dec);
		m_path.lineTo(x+8*dec, y+3*dec);
		m_path.lineTo(x+8*dec, y+4*dec);
		m_path.lineTo(x+mid, y+4*dec);
		m_path.lineTo(x+4*dec, y+mid);
		m_path.lineTo(x+4*dec, y+8*dec);
		m_path.lineTo(x+6*dec, y+8*dec);
		m_path.lineTo(x+6*dec, y+6*dec);
		m_path.lineTo(x+9*dec, y+6*dec);
		m_path.lineTo(x+h, y+mid);
		m_path.lineTo(x+h, y+dec);
		m_path.lineTo(x+ (h-dec), y);
		m_path.lineTo(x+dec, y);
		m_path.lineTo(x, y+dec);
		
		m_path.moveTo(x+4*dec, y+(h-dec));
		m_path.lineTo(x+4*dec, y+h);
		m_path.lineTo(x+6*dec, y+h);
		m_path.lineTo(x+6*dec, y+(h-dec));
		m_path.lineTo(x+4*dec, y+(h-dec));
		
		return m_path;
	}
	
	public static Shape arrow_up(GeneralPath m_path, float x, float y, float h) {
		
    	float h12 = h/2; float h34 = 3*h/4; float h14 = h/4;
    	
    	m_path.reset();
    	m_path.moveTo(x+h12, y);
    	m_path.lineTo(x, y+h12);
    	m_path.lineTo(x+h14, y+h12);
    	m_path.lineTo(x+h14, y+h);
    	m_path.lineTo(x+h34, y+h);
    	m_path.lineTo(x+h34, y+h12);
    	m_path.lineTo(x+h, y+h12);
    	m_path.closePath();
    	
    	return m_path;
	}
	
	public static Shape arrow_down(GeneralPath m_path, float x, float y, float h) {
		
		float h12 = h/2; float h14 = h/4; float h34 = 3*h/4;
		
		m_path.reset();
		m_path.moveTo(x+h14, y);
		m_path.lineTo(x+h14, y+h12);
		m_path.lineTo(x, y+h12);
		m_path.lineTo(x+h12, y+h);
		m_path.lineTo(x+h, y+h12);
		m_path.lineTo(x+h34, y+h12);
		m_path.lineTo(x+h34, y);
		m_path.closePath();
		
		return m_path;
	
	}
	
	/**
     * Returns a diamond shape of the given dimenisions.
     */
    public static Shape diamond(GeneralPath m_path, float x, float y, float height) {
        m_path.reset();
        m_path.moveTo(x,(y+0.5f*height));
        m_path.lineTo((x+0.5f*height),y);
        m_path.lineTo((x+height),(y+0.5f*height));
        m_path.lineTo((x+0.5f*height),(y+height));
        m_path.closePath();
        return m_path;
    }
    
    /**
     * Returns a hexagon shape of the given dimenisions.
     */
    public static Shape hexagon(GeneralPath m_path, float x, float y, float height) {
        float width = height/2;  
        
        m_path.reset();
        m_path.moveTo(x,            y+0.5f*height);
        m_path.lineTo(x+0.5f*width, y);
        m_path.lineTo(x+1.5f*width, y);
        m_path.lineTo(x+2.0f*width, y+0.5f*height);
        m_path.lineTo(x+1.5f*width, y+height);
        m_path.lineTo(x+0.5f*width, y+height);
        m_path.closePath();      
        return m_path;
    }
    
    /**
     * Returns a star shape of the given dimenisions.
     */
    public static Shape star(GeneralPath m_path, float x, float y, float height) {
        float s = (float)(height/(2*Math.sin(Math.toRadians(54))));
        float shortSide = (float)(height/(2*Math.tan(Math.toRadians(54))));
        float mediumSide = (float)(s*Math.sin(Math.toRadians(18)));
        float longSide = (float)(s*Math.cos(Math.toRadians(18)));
        float innerLongSide = (float)(s/(2*Math.cos(Math.toRadians(36))));
        float innerShortSide = innerLongSide*(float)Math.sin(Math.toRadians(36));
        float innerMediumSide = innerLongSide*(float)Math.cos(Math.toRadians(36));

        m_path.reset();
        m_path.moveTo(x, y+shortSide);            
        m_path.lineTo((x+innerLongSide),(y+shortSide));
        m_path.lineTo((x+height/2),y);
        m_path.lineTo((x+height-innerLongSide),(y+shortSide));
        m_path.lineTo((x+height),(y+shortSide));
        m_path.lineTo((x+height-innerMediumSide),(y+shortSide+innerShortSide));        
        m_path.lineTo((x+height-mediumSide),(y+height));
        m_path.lineTo((x+height/2),(y+shortSide+longSide-innerShortSide));
        m_path.lineTo((x+mediumSide),(y+height));
        m_path.lineTo((x+innerMediumSide),(y+shortSide+innerShortSide));
        m_path.closePath();
        return m_path;
    }
    
    public static Shape fat_cross(GeneralPath m_path, float x, float y, float h) {
		
    	float h14 = h/4;
    	float h12 = h/2;
    	float h34 = 3*h/4;
    	
    	m_path.reset();
    	m_path.moveTo(x+h14, y);
    	m_path.lineTo(x, y+h14);
    	m_path.lineTo(x+h14, y+h12);
    	m_path.lineTo(x, y+h34);
    	m_path.lineTo(x+h14, y+h);
    	m_path.lineTo(x+h12, y+h34);
    	m_path.lineTo(x+h34, y+h);
    	m_path.lineTo(x+h, y + h34);
    	m_path.lineTo(x+h34, y+h12);
    	m_path.lineTo(x+h, y+h14);
    	m_path.lineTo(x+h34, y);
    	m_path.lineTo(x+h12,y+h14);
    	m_path.closePath();
    	return m_path;
	}
    
    public static Shape rotated_cross(GeneralPath m_path, float x, float y, float h) {
    	
    	float h38 = 3*h/8;
    	float h12 = h/2;
    	float h14 = h/4;
    	float h58 = 5*h/8;
    	float h34 = 3*h/4;
    	
    	m_path.reset();

    	m_path.moveTo(x, y);
    	m_path.lineTo(x+h38, y+h12);
    	m_path.lineTo(x, y+h);
    	m_path.lineTo(x+h14, y+h);
    	m_path.lineTo(x+h12, y+h58);
    	m_path.lineTo(x+h34,y+h);
    	m_path.lineTo(x+h, y+h);
    	m_path.lineTo(x+h58, y+h12);
    	m_path.lineTo(x+h, y);
    	m_path.lineTo(x+h34, y);
    	m_path.lineTo(x+h12, y+h38);
    	m_path.lineTo(x+h14, y);
    	m_path.lineTo(x, y);
    	
    	m_path.closePath();
    	return m_path;
    }

    /**
     * Returns a cross shape of the given dimenisions.
     */
    public static Shape cross(GeneralPath m_path, float x, float y, float height) {
        float h14 = 3*height/8, h34 = 5*height/8;
        m_path.reset();
        m_path.moveTo(x+h14, y);
        m_path.lineTo(x+h34, y);
        m_path.lineTo(x+h34, y+h14);
        m_path.lineTo(x+height, y+h14);
        m_path.lineTo(x+height, y+h34);
        m_path.lineTo(x+h34, y+h34);
        m_path.lineTo(x+h34, y+height);
        m_path.lineTo(x+h14, y+height);
        m_path.lineTo(x+h14, y+h34);
        m_path.lineTo(x, y+h34);
        m_path.lineTo(x, y+h14);
        m_path.lineTo(x+h14, y+h14);
        m_path.closePath();
        return m_path;
    }
    
	/**
     * Returns a rectangle of the given dimenisions.
     */
    public static Shape rectangle(Rectangle2D m_rect, double x, double y, double width, double height) {
        m_rect.setFrame(x, y, width, height);
        return m_rect;
    }

    /**
     * Returns an ellipse of the given dimenisions.
     */
    public static Shape ellipse(Ellipse2D m_ellipse, double x, double y, double width, double height) {
        m_ellipse.setFrame(x, y, width, height);
        return m_ellipse;
    }
    
    /**
     * Returns a up-pointing triangle of the given dimenisions.
     */
    public static Shape triangle_up(GeneralPath m_path, float x, float y, float height) {
        m_path.reset();
        m_path.moveTo(x,y+height);
        m_path.lineTo(x+height/2, y);
        m_path.lineTo(x+height, (y+height));
        m_path.closePath();
        return m_path;
    }
    
    /**
     * Returns a down-pointing triangle of the given dimenisions.
     */
    public static Shape triangle_down(GeneralPath m_path, float x, float y, float height) {
        m_path.reset();
        m_path.moveTo(x,y);
        m_path.lineTo(x+height, y);
        m_path.lineTo(x+height/2, (y+height));
        m_path.closePath();
        return m_path;
    }
    
    /**
     * Returns a left-pointing triangle of the given dimenisions.
     */
    public static Shape triangle_left(GeneralPath m_path, float x, float y, float height) {
        m_path.reset();
        m_path.moveTo(x+height, y);
        m_path.lineTo(x+height, y+height);
        m_path.lineTo(x, y+height/2);
        m_path.closePath();
        return m_path;
    }
    
    /**
     * Returns a right-pointing triangle of the given dimenisions.
     */
    public static Shape triangle_right(GeneralPath m_path, float x, float y, float height) {
        m_path.reset();
        m_path.moveTo(x,y+height);
        m_path.lineTo(x+height, y+height/2);
        m_path.lineTo(x, y);
        m_path.closePath();
        return m_path;
    }
    
    
    public static Shape getShape(int stype, double x, double y, double width, double height){
    	
    	GeneralPath m_path = null;
    	Ellipse2D m_ellipse = null;
    	Rectangle2D m_rec = null;
    	
    	switch ( stype ) {
    		case Constants.SHAPE_RECTANGLE:
    			m_rec = new Rectangle2D.Double();
    			break;
    		case Constants.SHAPE_ELLIPSE:
    			m_ellipse = new Ellipse2D.Double();
    			break;
    		case Constants.SHAPE_NONE:
    			break;
    		default:
    			m_path = new GeneralPath();
    	}
    	
    	return getShape(stype, x, y, width, height, m_path, m_ellipse, m_rec);
    }
    
    public static Shape getShape(int stype, double x, double y, double width, double height, GeneralPath m_path, Ellipse2D m_ellipse, Rectangle2D m_rect){
    	switch ( stype ) {
        case Constants.SHAPE_NONE:
            return null;
        case Constants.SHAPE_RECTANGLE:
            return rectangle(m_rect,x, y, width, height);
        case Constants.SHAPE_ELLIPSE:
            return ellipse(m_ellipse, x, y, width, height);
        case Constants.SHAPE_TRIANGLE_UP:
            return triangle_up(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_TRIANGLE_DOWN:
            return triangle_down(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_TRIANGLE_LEFT:
            return triangle_left(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_TRIANGLE_RIGHT:
            return triangle_right(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_CROSS:
            return cross(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_STAR:
            return star(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_HEXAGON:
            return hexagon(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_DIAMOND:
            return diamond(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_X:
        	return rotated_cross(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_FATX:
        	return fat_cross(m_path,(float)x, (float)y, (float)width);
        case Constants.SHAPE_ARROW_DOWN:
        	return ShapeManager.arrow_down(m_path, (float)x, (float)y, (float)width);
        case Constants.SHAPE_ARROW_UP:
        	return ShapeManager.arrow_up(m_path, (float)x, (float)y, (float)width);
        case Constants.SHAPE_QUESTION:
        	return ShapeManager.question(m_path, (float)x, (float)y, (float)width);
        default:
            throw new IllegalStateException("Unknown shape type: "+stype);
        }
    }
    
    // TODO Implement this method properly...
    public static Integer[] getAvailableShapeCodes(){
    	Integer[] codes = new Integer[15];
    	for(int i=0; i<15; i++)
    		codes[i]=i;
    	return codes;
    }
}
