package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.awt.Color;
import java.text.DecimalFormat;

public class OverlapUtilities {

	public static double normalizeBetween(double a, double b, double min, double max, double v) {
		double nv = ((b - a) * (v - min)) / (max - min) + a;		
		return nv;
	}
	
	public static Color mixColors(Color color1, Color color2, double percent) {
		double inverse_percent = 1.0 - percent;
		int redPart = (int) Math.ceil(color1.getRed() * percent + color2.getRed() * inverse_percent);
		int greenPart = (int) Math.ceil(color1.getGreen() * percent + color2.getGreen() * inverse_percent);
		int bluePart = (int) Math.ceil(color1.getBlue() * percent + color2.getBlue() * inverse_percent);
		int alpha = (int) Math.ceil(color1.getAlpha() * percent + color2.getAlpha() * inverse_percent);
		redPart = normalizeValue(redPart);
		greenPart = normalizeValue(greenPart);
		bluePart = normalizeValue(bluePart);
		alpha = normalizeValue(alpha);
		return new Color(redPart, greenPart, bluePart, alpha);
	}
	
	public static int normalizeValue(int value) {
		if (value > 255)
			return 255;
		else if (value < 0)
			return 0;
		else
			return value;
	}
	
	public static double normalizeToPercent(double v, double max) {
		double nv = v * 100 / max;
		return (nv / 100);
	}
	
	public static double formatDigits(double in){
		DecimalFormat df = new DecimalFormat("#.00000"); //five decimal places
		return Double.parseDouble(df.format(in));
	}
	
	public static void main(String[] args) {
		double a = 0.0;
		double b = 10.0;
		double min= 0.0;
		double max = 350.0;
		double val = 350.0;
		
		System.out.println(normalizeBetween(a, b, min, max, val));
				
	}
}
