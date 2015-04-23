package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.sbgn.SbgnUtil;
import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Arc.End;
import org.sbgn.bindings.Arc.Start;
import org.sbgn.bindings.Bbox;
import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Glyph.Clone;
import org.sbgn.bindings.Label;
import org.sbgn.bindings.Sbgn;
import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;

/**
 * Writer of layouts in SBGN format.
 * @author Alberto Noronha
 *
 */
public class SBGNWriter {

	public static String SIMPCH = "simple chemical";
	public static String PROC = "process";
	public static String CONS = "consumption";
	public static String PRO = "production";
	
	public static void writeSBGNtoFile(String file, ILayout ilayout) throws JAXBException, SAXException, IOException{
		
		Sbgn sbgn = new Sbgn();		
		org.sbgn.bindings.Map sbgnMap = new org.sbgn.bindings.Map();
		sbgn.setMap(sbgnMap);
		sbgnMap.setLanguage("process description");
		Map<String, Glyph> uniqueIdToSBGNGlyph = new HashMap<String, Glyph>();
		
		Map<String, INodeLay> nodes = ilayout.getNodes();
		
		int counter = 1;
		
		
		/**
		 * Draw all the nodes
		 */
		
		for(String nodeId : nodes.keySet()){
			
			INodeLay nodelay = nodes.get(nodeId);
			
			Set<String> ids = nodes.get(nodeId).getIds();
			
			String label = "";
			
			String[] ids_v = new String[ids.size()];
			
			int count = 0;
			for(String id : ids){
				ids_v[count] = id;
				count++;
			}

			label = ids_v[0];
			
			if(ids_v.length > 1){
				for(int i = 1; i< ids_v.length; i++){

					label += ";" + ids_v[i];
				}
			}
			
			
			Double x = nodelay.getX();
			Double y = nodelay.getY();
			float xaxis = Float.valueOf(x.toString());
			float yaxis = Float.valueOf(y.toString());
		
			NodeTypeLay nodeType = nodelay.getNodeType();
			
			Glyph g = new Glyph();
			g.setId("glyph" + counter);
			
			

			g.setClazz(SIMPCH);
			if(nodeType.equals(NodeTypeLay.CURRENCY))
				g.setClone(new Clone());

			Label sbgnLabel = new Label();
			sbgnLabel.setText(label);
			g.setLabel(sbgnLabel);
			
			Bbox bbox1 = new Bbox();
			bbox1.setX(xaxis);
			bbox1.setY(yaxis);
			bbox1.setW(60);
			bbox1.setH(60);
			g.setBbox(bbox1);
			
			counter++;
			sbgnMap.getGlyph().add(g);
			uniqueIdToSBGNGlyph.put(nodeId, g);
		}
	
		Map<String, IReactionLay> reactions = ilayout.getReactions();
		for(String rId : reactions.keySet()){
			
			IReactionLay ireaction = reactions.get(rId);
			String id = "glyph" + counter;


			Set<String> ids = ireaction.getIDs();

			String label = "";

			String[] ids_v = new String[ids.size()];
			
			int count = 0;
			
			for(String i : ids){
				ids_v[count] = i;
				count++;
			}

			label = ids_v[0];
			if(ids_v.length > 1){
				for(int i = 1; i< ids_v.length; i++){

					label += ";" + ids_v[i];
				}
			}
			
			Double x = ireaction.getX();
			Double y = ireaction.getY();
			float xaxis = Float.valueOf(x.toString());
			float yaxis = Float.valueOf(y.toString());
			
			Glyph g = new Glyph();
			g.setId(id);
			Label sbgnLabel = new Label();
			sbgnLabel.setText(label);
			g.setLabel(sbgnLabel);
			g.setClazz(PROC);
			
			
			Bbox bbox1 = new Bbox();
			
			bbox1.setX(xaxis);
			bbox1.setY(yaxis);
			bbox1.setW(20);
			bbox1.setH(20);
			g.setBbox(bbox1);
			
			counter++;
			sbgnMap.getGlyph().add(g);
			
			
			Map<String, INodeLay> reactants = ireaction.getReactants();
			for(String reactantId : reactants.keySet()){
				
				String arc_id = "glyph" + counter;
				
				Arc arc = new Arc();
				
				arc.setId(arc_id);
				
				Double[] result;
				
				if(ireaction.isReversible()){
					arc.setClazz(PRO);
					result = calculatePoints(reactions.get(rId).getX(),reactions.get(rId).getY(), nodes.get(reactantId).getX(), nodes.get(reactantId).getY(), 10);
					
				}
				else
				{
					arc.setClazz(CONS);
					result = calculatePoints(nodes.get(reactantId).getX(), nodes.get(reactantId).getY(), reactions.get(rId).getX(), reactions.get(rId).getY(), 10);
				}
				
				Start s = new Start();
				float xf = Float.valueOf(result[0].toString());
				float yf = Float.valueOf(result[1].toString());
				s.setX(xf);
				s.setY(yf);
				arc.setStart(s);
				
				End e = new End();
				xf = Float.valueOf(result[2].toString());
				yf = Float.valueOf(result[3].toString());
				e.setX(xf);
				e.setY(yf);
				arc.setEnd(e);
				
				arc.setSource(g);
				arc.setTarget(uniqueIdToSBGNGlyph.get(reactantId));
				
				sbgnMap.getArc().add(arc);
				
				counter++;
			}
			
			Map<String, INodeLay> products = ireaction.getProducts();
			for(String productId : products.keySet()){
				
				String arc_id = "glyph" + counter;
				
				Arc arc = new Arc();
				arc.setId(arc_id);
				
				arc.setClazz(PRO);
				Double[] result = calculatePoints(reactions.get(rId).getX(),reactions.get(rId).getY(), nodes.get(productId).getX(), nodes.get(productId).getY(), 10);
				
				Start s = new Start();
				float xf = Float.valueOf(result[0].toString());
				float yf = Float.valueOf(result[1].toString());
				s.setX(xf);
				s.setY(yf);
				arc.setStart(s);
				
				End e = new End();
				xf = Float.valueOf(result[2].toString());
				yf = Float.valueOf(result[3].toString());
				e.setX(xf);
				e.setY(yf);
				arc.setEnd(e);
				
				arc.setSource(g);
				arc.setTarget(uniqueIdToSBGNGlyph.get(productId));
				
				sbgnMap.getArc().add(arc);
				
				counter++;
			}
		}
		
		
		File f = new File(file);
		SbgnUtil.writeToFile(sbgn,f);
	}
	
	private static Double[] calculatePoints(double center_x1, double center_y1, double center_x2, double center_y2, double r){
		
		// rect between the two points
		// y = mx + b
		
		double m = (center_y2-center_y1)/(center_x2-center_x1);
		
		double b = center_y1 - m*center_x1;
		
		//circle eq:
		// (X-X1)^2 + (Y-Y1)^2 = r^2
		
		//system of 2 equations!
		
		double C1_circle1 = m + 1;
		double C1_circle2 = m + 1; 
		
		double C2_circle1 = (-2 * center_x1) + (2*m*b) - (2*center_y1*m);
		double C2_circle2 = (-2 * center_x2) + (2*m*b) - (2*center_y2*m);
		
		double C3_circle1 = - Math.pow(r, 2) + Math.pow(center_x1, 2) + Math.pow(center_y1, 2) + (b*b) + (2*center_y1*b);
		double C3_circle2 = - Math.pow(r, 2) + Math.pow(center_x2, 2) + Math.pow(center_y2, 2) + (b*b) + (2*center_y2*b);
		
		// x = -b +- sqrt(b^2 - 4ac) / 2a
		
		double c1_x1 = ((-C2_circle1) + Math.sqrt((Math.pow(C2_circle1, 2) - (4*C1_circle1*C3_circle1))))/(2*C1_circle1);
		//replace y = mx + b
		double c1_y1 = m*c1_x1 + b;
		
		double c1_x2 = ((-C2_circle1) - Math.sqrt((Math.pow(C2_circle1, 2) - (4*C1_circle1*C3_circle1))))/(2*C1_circle1);
		
		double c1_y2 = m*c1_x2 + b;
		
		//WE NOW HAVE TWO POINTS FOR THE CIRCLE 1 - still need for the other circle
		
		double c2_x1 = ((-C2_circle2) + Math.sqrt((Math.pow(C2_circle2, 2) - (4*C1_circle2*C3_circle2))))/(2*C1_circle2);
		//replace y = mx + b
		double c2_y1 = m*c2_x1 + b;
		
		double c2_x2 = ((-C2_circle2) - Math.sqrt((Math.pow(C2_circle2, 2) - (4*C1_circle2*C3_circle2))))/(2*C1_circle2);
		
		double c2_y2 = m*c2_x2 + b;
		
		//We have four points now 
		// A1 = (c1_x1, c1_y1); A2 = (c1_x2, c1_y2)
		// B1 = (c2_x1, c2_y1); B2 = (c2_x2, c2_y2)
		
		//NEED THE DISTANCE BETWEEN THEM
		
		//Distance between A1 and B1
		double d = distance(c1_x1, c1_y1, c2_x1, c2_y1);
		Double[] result = new Double[4];
		result[0] = c1_x1;
		result[1] = c1_y1;
		result[2] = c2_x1;
		result[3] = c2_y1;
		
		//distance between A1 and B2
		if(distance(c1_x1, c1_y1, c2_x2, c2_y2)<d){
			result[0] = c1_x1;
			result[1] = c1_y1;
			result[2] = c2_x2;
			result[3] = c2_y2;
		}
		
		//distance between A2 and B1
		if(distance(c1_x2,c1_y2, c2_x1, c2_y1)<d){
			result[0] = c1_x2;
			result[1] = c1_y2;
			result[2] = c2_x1;
			result[3] = c2_y1;
		}
		//Distance between A2 and B2
		if(distance(c1_x2,c1_y2, c2_x2, c2_y2)<d){
			result[0] = c1_x2;
			result[1] = c1_y2;
			result[2] = c2_x2;
			result[3] = c2_y2;
		}
		
		return result;
	}
	
	public static double distance(double x1, double y1, double x2, double y2){
		
		double a = x1-x2;
		double b = y1-y2;
		double d = Math.pow(a , 2) + Math.pow(b , 2);
		return Math.sqrt(d);
	}
	
}
