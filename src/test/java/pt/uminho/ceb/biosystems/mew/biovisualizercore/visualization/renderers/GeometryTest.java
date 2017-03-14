package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.renderers;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class GeometryTest {

	@Test
	public void test() {
	
		
		Point2D a = new Point2D.Double(1d,2d);
		Point2D b = new Point2D.Double(4d,5d);
		Point2D r = new Point2D.Double(5d,2d);
		
		
		Pair<Double, Double> mAndB_AB = Geometry.getMandB(a, b);
		System.out.println(mAndB_AB);
		
		Double newM = Geometry.perpendicularM(mAndB_AB.getA());
		System.out.println(newM);
		Pair<Double, Double> mAndB2 = Geometry.getMandB(r, newM);
		
		System.out.println(mAndB2);
		Point2D newPoint = Geometry.lineIntersection(mAndB_AB, mAndB2);
		
		System.out.println(newPoint);
	}
	
	
	@Test
	public void test2() {
		
		Point2D a = new Point2D.Double(1d,2d);
		Point2D b = new Point2D.Double(4d,5d);
		Point2D r = new Point2D.Double(5d,2d);
		
		
		Pair<Double, Double> mAndB_AB = Geometry.getMandB(a, b);
		System.out.println(mAndB_AB);
		Pair<Double, Double> mAndB_AB_paralele = Geometry.rectaParalela(mAndB_AB, r);
		Double newM = Geometry.perpendicularM(mAndB_AB.getA());
		
		Pair<Double, Double> perpendicularB = Geometry.getMandB(a, newM);
		
		
		Point2D newPoint = Geometry.lineIntersection(perpendicularB, mAndB_AB_paralele);
//		Double newM = Geometry.perpendicularM(mAndB_AB.getA());
//		System.out.println(newM);
//		Pair<Double, Double> mAndB2 = Geometry.getMandB(r, newM);
		
//		System.out.println(mAndB2);
//		Point2D newPoint = Geometry.lineIntersection(mAndB_AB, mAndB2);
		
		System.out.println(newPoint);
	}


}
