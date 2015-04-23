package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures;

import java.io.Serializable;

public class Shape implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;

	protected Integer width;
	protected Integer height;

	public Shape(Integer width, Integer height) {
		this.width = width;
		this.height = height;
	}
	
	public Shape(Shape shape) {
		this.width = shape.width;
		this.height = shape.height;
	}
	
	@Override
	public Shape clone() {
		return new Shape(this);
	}
	
}
