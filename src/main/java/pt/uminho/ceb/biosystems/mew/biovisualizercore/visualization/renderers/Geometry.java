package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class Geometry {

	
	static public Pair<Double, Double> getMandB(Point2D a, Double m) {
		return getMandB(a.getX(), a.getY(), m);
	}

	static public Pair<Double, Double> getMandB(Point2D a, Point2D b) {
		return getMandB(a.getX(), a.getY(), b.getX(), b.getY());
		
	}

	static public Pair<Double, Double> getMandB(Double x1, Double y1, Double x2, Double y2 ){

		Double m = (y2-y1)/(x2-x1);
		return getMandB(x1, y1, m);
	}

	static public Pair<Double, Double> getMandB(Double x1, Double y1, Double m){

		Double b = y1 - m*x1;

		return new Pair<Double, Double>(m, b);
	}

	static public Double perpendicularM(Double m){
		return -(1/m);
	}
	
	static public Pair<Double, Double> rectaParalela(Pair<Double, Double> recta, double x, double y){
		return getMandB(x, y, recta.getA());
	}
	
	static public Pair<Double, Double> rectaParalela(Pair<Double, Double> recta, Point2D p){
		return getMandB(p.getX(), p.getY(), recta.getA());
	}

	static public Point2D lineIntersection(Pair<Double, Double> r1, Pair<Double, Double> r2){
		Double m1 = r1.getA();
		Double b1 = r1.getB();

		Double m2 = r2.getA();
		Double b2 = r2.getB();

		Double x = (b2-b1)/(m1-m2);
		Double y = m2*x+b2;
		return new Point2D.Double(x,y);
	}
}
