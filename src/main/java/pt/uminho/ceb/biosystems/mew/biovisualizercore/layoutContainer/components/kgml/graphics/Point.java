package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.graphics;

import java.io.Serializable;

public class Point implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected float x;
	protected float y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this.x = 0.0f;
		this.y = 0.0f;
	}
	
	public Point clone() {
		return new Point(x, y);
	}

	@Override
	public String toString() {
		return "x= " + x + "; y= " + y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float distanceTo(Point point) {
		float xd = x-point.getX();
		float yd = y-point.getY();
		return (float) Math.sqrt(xd*xd + yd*yd);
	}
	
	public void rotate(Point ref, float radian) {
		float tx = x - ref.getX();
		float ty = y - ref.getY();
		float trX = (float) (tx * Math.cos(radian) - ty * Math.sin(radian)); 
		float trY = (float) (tx * Math.sin(radian) + ty * Math.cos(radian));
		x = trX + ref.getX();
		y = trY + ref.getY();
	}
	
	public void translate(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public boolean isAt(int x, int y) {
		return this.x == x && this.y == y;
	}
		
	public int getIntX() {
		return (int) x;
	}

	public int getIntY() {
		return (int) y;
	}
	
}
