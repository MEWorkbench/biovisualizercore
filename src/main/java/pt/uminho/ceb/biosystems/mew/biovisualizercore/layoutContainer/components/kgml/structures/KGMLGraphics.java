package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.graphics.Point;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.KGMLShapeType;

public class KGMLGraphics implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public static final Stroke defaultStroke = new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
	
	protected String name;
	protected Color fgColor;
	protected Color bgColor;
	protected KGMLShapeType shapeType;
	protected Shape shape;
	protected Integer x;
	protected Integer y;
	protected Integer width;
	protected Integer height;
	protected List<List<Point2D>> coordsSet;
	protected Stroke drawStroke;
	
	public KGMLGraphics(String name,
			Color fgColor, Color bgColor,
			KGMLShapeType shapeType, Shape shape,
			Integer x, Integer y, List<List<Point2D>> coordSet,
			Integer width, Integer height) {
		this.name = name;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.shapeType = shapeType;
		this.shape = shape;
		this.x = x;
		this.y = y;
		this.coordsSet = coordSet;
		this.width = width;
		this.height = height;
//		this.drawStroke = defaultStroke;
	}

	public KGMLGraphics(KGMLGraphics graphics) {
		this.name = graphics.name;
		this.fgColor = graphics.fgColor;
		this.shapeType = graphics.shapeType;
		this.shape = graphics.shape.clone();
		this.x = graphics.x;
		this.y = graphics.y;
		this.coordsSet = new ArrayList<List<Point2D>>();
		for(List<Point2D> coords : graphics.coordsSet) {
			List<Point2D> newCoords = new ArrayList<Point2D>();
			for(Point2D p : coords) {
				newCoords.add(new Point2D.Double(p.getX(), p.getY()));
			}
			coordsSet.add(newCoords);
		}
		this.width = graphics.width;
		this.height = graphics.height;
		this.drawStroke = graphics.drawStroke;
	}
	
	@Override
	public KGMLGraphics clone() {
		return new KGMLGraphics(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + " --> X=" + x + " y=" + y;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public KGMLShapeType getShapeType() {
		return shapeType;
	}

	public void setShapeType(KGMLShapeType shapeType) {
		this.shapeType = shapeType;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Integer getX() {
		if(x != null)
			return x;
		else {
			Point interset = findCoordsMid();
			return (int) interset.getX();
		}
	}

	public Point findCoordsMid() {
		if(coordsSet.size() == 1) {
			List<Point2D> points =  coordsSet.get(0);
			Point2D p = points.get(points.size()/2);
			return new Point((float)p.getX(), (float)p.getY());
		}
		else if(coordsSet.size() > 0){ 
			List<Point2D> list = coordsSet.get(0);
			if(list.size() > 0) {
				for(int i = 1; i < coordsSet.size(); i++) {
					list.retainAll(coordsSet.get(i));
				}
				Point2D p;
				if(list.size() > 0) {
					 p = list.get(0);
					return new Point((float)p.getX(), (float)p.getY());
				}
				else return(new Point());
			}
			else return(new Point());
		}
		else return(new Point());
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		if(y != null) 
			return y;
		else {
			Point interset = findCoordsMid();
			return (int) interset.getY();
		}
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public List<List<Point2D>> getCoords() {
		return coordsSet;
	}

	public KGMLGraphics merge(KGMLGraphics graphics) {
		KGMLGraphics newG = new KGMLGraphics(this);
		if(this.x != null && graphics.x != null) {
			newG.x = (this.x + graphics.x) / 2;
		}
		if(this.y != null && graphics.y != null) {
			newG.y = (this.y + graphics.y) / 2;
		}
		newG.coordsSet.addAll(graphics.coordsSet);
		return newG;
	}

	public void setStroke(Stroke stroke) {
		this.drawStroke = stroke;
		
	}

	public Stroke getStroke() {
		return drawStroke;
	}
	
}