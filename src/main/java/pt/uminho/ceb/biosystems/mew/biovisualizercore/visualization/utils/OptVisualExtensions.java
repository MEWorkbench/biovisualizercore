package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import prefuse.data.Schema;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;

public class OptVisualExtensions {
	
	public static final String VISUAL_WIDTH="optvisual.with";
    public static final String VISUAL_HEIGHT="optvisual.height";
	

    public static final Schema getVisualItemSchema() {
        Schema s = new Schema();
        
        // booleans
        s.addColumn(VisualItem.VALIDATED, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.VISIBLE, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.STARTVISIBLE, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.ENDVISIBLE, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.INTERACTIVE, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.EXPANDED, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.FIXED, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.HIGHLIGHT, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.HOVER, boolean.class, Boolean.FALSE);
        
        s.addInterpolatedColumn(VisualItem.X, double.class);
        s.addInterpolatedColumn(VisualItem.Y, double.class);
        
        // bounding box
        s.addColumn(VisualItem.BOUNDS, Rectangle2D.class, new Rectangle2D.Double());
        
        // color
        Integer defStroke = new Integer(ColorLib.rgba(0,0,0,0));
        s.addInterpolatedColumn(VisualItem.STROKECOLOR, int.class, defStroke);

        Integer defFill = new Integer(ColorLib.rgba(0,0,0,0));
        s.addInterpolatedColumn(VisualItem.FILLCOLOR, int.class, defFill);
//        s.addColumn(PrefuseLib.getStartField(VisualItem.FILLCOLOR), int.class,ColorLib.rgb(255,255,200));
//        s.addColumn(PrefuseLib.getEndField(VisualItem.FILLCOLOR), int.class,ColorLib.rgb(255,255,200));
        
        Integer defTextColor = new Integer(ColorLib.rgba(0,0,0,0));
        s.addInterpolatedColumn(VisualItem.TEXTCOLOR, int.class, defTextColor);

        // size
        s.addInterpolatedColumn(VisualItem.SIZE, double.class, new Double(1));
        
        // stroke
        s.addColumn(VisualItem.STROKE, Stroke.class, new BasicStroke());
        
        // font
        Font defFont = FontLib.getFont("SansSerif",Font.PLAIN,10);
        s.addInterpolatedColumn(VisualItem.FONT, Font.class, defFont);
        
        // degree-of-interest
        s.addColumn(VisualItem.DOI, double.class, new Double(Double.MIN_VALUE));

//        s.addColumn(VISUAL_HEIGHT, double.class);
//        s.addColumn(VISUAL_WIDTH, double.class);
        return s;
    }
    
    public static final Schema getVisualEdgeSc() {
        Schema s = new Schema();
        
        // booleans
        s.addColumn(VisualItem.VALIDATED, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.VISIBLE, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.STARTVISIBLE, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.ENDVISIBLE, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.INTERACTIVE, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.EXPANDED, boolean.class, Boolean.TRUE);
        s.addColumn(VisualItem.FIXED, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.HIGHLIGHT, boolean.class, Boolean.FALSE);
        s.addColumn(VisualItem.HOVER, boolean.class, Boolean.FALSE);
        
        s.addInterpolatedColumn(VisualItem.X, double.class);
        s.addInterpolatedColumn(VisualItem.Y, double.class);
        
        // bounding box
        s.addColumn(VisualItem.BOUNDS, Rectangle2D.class, new Rectangle2D.Double());
        
        // color
        Integer defStroke = new Integer(ColorLib.rgba(0,0,0,0));
        s.addInterpolatedColumn(VisualItem.STROKECOLOR, int.class, defStroke);

        Integer defFill = new Integer(ColorLib.rgba(0,0,0,0));
        s.addInterpolatedColumn(VisualItem.FILLCOLOR, int.class, defFill);

        Integer defTextColor = new Integer(ColorLib.rgba(0,0,0,0));
        s.addInterpolatedColumn(VisualItem.TEXTCOLOR, int.class, defTextColor);

        // size
        s.addInterpolatedColumn(VisualItem.SIZE, double.class, new Double(1));
        
        // stroke
        s.addColumn(VisualItem.STROKE, Stroke.class, new BasicStroke());
        
        // font
        Font defFont = FontLib.getFont("SansSerif",Font.PLAIN,10);
        s.addInterpolatedColumn(VisualItem.FONT, Font.class, defFont);
        
        // degree-of-interest
        s.addColumn(VisualItem.DOI, double.class, new Double(Double.MIN_VALUE));

//        s.addColumn(VISUAL_HEIGHT, double.class);
//        s.addColumn(VISUAL_WIDTH, double.class);
        return s;
    }
}
