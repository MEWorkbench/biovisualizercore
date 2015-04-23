package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers.kgmmlParser;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLCompound;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLEntry;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLGraphics;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLPathway;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLReaction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.KGMLRelation;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.structures.Shape;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.EntryType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.KGMLShapeType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.RelationType;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.kgml.types.SubType;

public class KGMLParser {
	
	public static String shape = "type";
	public static String accession = "name";
	public static String pathwayName = "title";
	public static String reversibility = "type";
	
	
	public boolean debug = true;
	protected Document doc;
	
	public static KGMLPathway downloadAndParse(String mapId) throws ParserConfigurationException, SAXException, IOException {
		URL url = new URL("http://www.genome.jp/kegg-bin/download?entry=rn" + mapId + "&format=kgml");
		System.out.println(url.toString());
		KGMLParser fp = new KGMLParser(url);
		return fp.parseFile();
	}
	
	public KGMLParser(String kgmlString) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		InputSource source = new InputSource(new StringReader(kgmlString));
		doc = factory.newDocumentBuilder().parse(source); 
	}
	
	public KGMLParser(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(xmlFile);
		doc.getDocumentElement().normalize();
	}
	
	public KGMLParser(URL fileURL) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(fileURL.openStream());
		doc.getDocumentElement().normalize();
	}
	
	public KGMLPathway parseFile() {
		NodeList pathwayNodes = doc.getElementsByTagName("pathway");
//		if(debug){
//			System.out.println("Found " + pathwayNodes.getLength() + " nodes");
//		}
		
		Node pathwayNode = pathwayNodes.item(0);
		Element element = (Element) pathwayNode;
		String pathName = getTagValue("title", element);
		String pathId   = getTagValue("number", element);
//		if(debug){ 
//			System.out.println("Pathway: " + pathName);
//			System.out.println("Number: " + pathId);
//		}
		
		KGMLPathway pathway = new KGMLPathway(pathName, pathId);
		readEntries(pathway, element);
		readRelations(pathway, element);
		readReactions(pathway, element);
		
		return pathway;
	}
	
	protected void readReactions(KGMLPathway pathway, Element pathwayElement) {
		NodeList reactions = pathwayElement.getElementsByTagName("reaction");
		
//		if(debug)
//			System.out.println("Parsing " + reactions.getLength() + " reactions.");
		
		for (int i = 0; i < reactions.getLength(); i++) {
			 
		   Node reactionNode = reactions.item(i);
		   
		   if (reactionNode.getNodeType() == Node.ELEMENT_NODE) {
			   Element reactionElement = (Element) reactionNode;
			   Integer reactionId = Integer.valueOf(getTagValue("id", reactionElement));
			   String[] acc   = getTagValue(accession, reactionElement).split("\\s+");
			   Boolean reversible = "reversible".equalsIgnoreCase(getTagValue("type", reactionElement));
			   List<KGMLCompound> substrates = getCompounds(pathway, reactionElement, true);
			   List<KGMLCompound> products   = getCompounds(pathway, reactionElement, false);
			   List<String> accs = new ArrayList<String>();
			   for(String a : acc) {
				   accs.add(a.replaceAll("rn\\:", ""));
			   }
			   
//			   if(debug) {
//				   System.out.println("Reaction:");
//				   System.out.println("\tId: " + reactionId);
//				   System.out.println("\tAccession: " + CollectionUtils.join(accs, "; "));
//			   }
			   
			   pathway.addReaction(reactionId, new KGMLReaction(reactionId, accs, reversible, substrates, products));
		   }
		}
	}

	protected List<KGMLCompound> getCompounds(KGMLPathway pathway, Element reactionElement, boolean substrates) {
		String tag = substrates ? "substrate" : "product";
		List<KGMLCompound> compounds = new ArrayList<KGMLCompound>();
		NodeList compoundsNode = reactionElement.getElementsByTagName(tag);
		for (int i = 0; i < compoundsNode.getLength(); i++) {
			 
		   Node compoundNode = compoundsNode.item(i);
		   if (compoundNode.getNodeType() == Node.ELEMENT_NODE) {
			   Element compoundElement = (Element) compoundNode;
			   Integer id = Integer.valueOf(getTagValue("id", compoundElement));
			   String[] acc   = getTagValue(accession, compoundElement).split("\\s+");
			   
			   List<String> accs = new ArrayList<String>();
			   for(String a : acc) {
				   accs.add(a.replaceAll("cpd\\:|gl\\:|dr\\:", ""));
			   }
			   
			   
//			   if(debug) {
//				   System.out.println("Compound:");
//				   System.out.println("\tId:" + id);
//				   System.out.println("\tAccession: " + CollectionUtils.join(accs, "; "));
//			   }
			   KGMLCompound cpd = new KGMLCompound(id, accs);
			   compounds.add(cpd);
			   pathway.addCompound(id, cpd);
		   }
		}
		
		return compounds;
	}

	protected void readRelations(KGMLPathway pathway, Element pathwayElement) {
		NodeList relations = pathwayElement.getElementsByTagName("relation");
//		if(debug) 
//			System.out.println("Reading " + relations.getLength() + " relations");
		
		
		for (int i = 0; i < relations.getLength(); i++) {
			 
		   Node relationNode = relations.item(i);
		   if (relationNode.getNodeType() == Node.ELEMENT_NODE) {
			   Element relationElement = (Element) relationNode;
			   Integer entry1 = Integer.valueOf(getTagValue("entry1", relationElement));
			   Integer entry2 = Integer.valueOf(getTagValue("entry2", relationElement));
			   RelationType relationType = parseRelationType(relationElement);
			   SubType subType = parseSubType(relationElement);

			   
//			   if(debug) {
//				   System.out.println("Relation:");
//				   System.out.println("\tEntry 1: " + entry1);
//				   System.out.println("\tEntry 2: " + entry2);
//				   System.out.println("\tType: " + relationType);
//			   }
			   
			   pathway.addRelation(new KGMLRelation(entry1, entry2, relationType, subType));
		   }
			   
		}
		
	}

	private SubType parseSubType(Element relationElement) {
		NodeList subTypeList = relationElement.getElementsByTagName("subtype");
		Node subTypeNode = subTypeList.item(0);
		Element subTypeElement = (Element) subTypeNode;
		EntryType type = parseEntryType(subTypeElement, "name");
		Integer subTypeId = Integer.valueOf(getTagValue("value", subTypeElement));
		
//		if(debug) {
//			System.out.println("SubType:");
//			System.out.println("\tType: "+ type);
//			System.out.println("\tId: " + subTypeId);
//		}
		
		SubType subType = new SubType(type, subTypeId);
		return subType;
	}

	private RelationType parseRelationType(Element relationElement) {
		return RelationType.valueOf(getTagValue("type", relationElement).toUpperCase());
	}

	protected void readEntries(KGMLPathway pathway, Element pathwayElement) {
		NodeList entries = pathwayElement.getElementsByTagName("entry");
		
//		if(debug)
//			System.out.println("Reading " + entries.getLength() + " entries");
		
		for (int i = 0; i < entries.getLength(); i++) {
			 
		   Node entryNode = entries.item(i);
		   if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
			   Element entryElement = (Element) entryNode;
			   Integer entryId = Integer.valueOf(getTagValue("id", entryElement));
			   String entryName = getTagValue("name", entryElement);
			   EntryType entryType = parseEntryType(entryElement, "type");
			   String reactionId = getTagValue("reaction", entryElement);
			   KGMLGraphics graphics = getGraphics(entryElement);
			   
//			   if(debug) {
//				   System.out.println("Entry:");
//				   System.out.println("\tId: " + entryId);
//				   System.out.println("\tName: " + entryName);
//				   System.out.println("\tType: " + entryType);
//				   System.out.println("\tReaction: " + reactionId);
//			   }
			   
			   pathway.addEntry(entryId, new KGMLEntry(entryId, entryName, entryType, reactionId, graphics));
		   }
			   
		}
	}
	
	private KGMLGraphics getGraphics(Element entryElement) {
		NodeList graphicsList = entryElement.getElementsByTagName("graphics");
		Integer x = 0;
		Integer y = 0;
		Integer width = 0;
		Integer height = 0;
		KGMLShapeType shapeType = KGMLShapeType.CIRCLE;
		Color bgColor = Color.BLACK;
		Color fgColor = Color.BLACK;
		String name = "Unkown";
		Shape shapeValue = null;
		List<List<Point2D>> coordSet = new ArrayList<List<Point2D>>();
			for(int i = 0; i < graphicsList.getLength(); i++) {
				Node graphicsNode = graphicsList.item(i);
				Element graphicsElement = (Element) graphicsNode;
				if(graphicsElement.getNodeType() == Node.ELEMENT_NODE) {
				name = getTagValue("name", graphicsElement);
				fgColor = hexToRGB(getTagValue("fgcolor", graphicsElement));
				bgColor = hexToRGB(getTagValue("bgcolor", graphicsElement));
				shapeType = getShapeType(getTagValue(shape, graphicsElement)); 
				shapeValue = getShape(graphicsElement);
				x = getPosition("x", graphicsElement);
				y = getPosition("y", graphicsElement);
				width = getPosition("width", graphicsElement);
				height = getPosition("height", graphicsElement);
				List<Point2D> coords = getCoordinates(graphicsElement);
				coordSet.add(coords);
//				if(debug) {
//					System.out.println("\tGraphics:" + (i+1));
//					System.out.println("\tName: " + name);
//				    System.out.println("\tBackground Color: " + bgColor.toString());
//			        System.out.println("\tForeground Color: " + fgColor.toString());
//				    System.out.println("\tShapeType: " + shapeType.toString());
//				    System.out.println("\tX: " + x);
//				    System.out.println("\tY: " + y);
//				    System.out.println("\tWidth: " + width);
//				    System.out.println("\tHeight: " + height); 
//				}
			}
		}
		KGMLGraphics graphics = new KGMLGraphics(name, fgColor, bgColor, shapeType, shapeValue, x, y, coordSet, width, height);
		return graphics;
	}

	protected Integer getPosition(String coord, Element graphicsElement) {
		String value = getTagValue(coord, graphicsElement);
		Integer pos = null;
		try {
			pos = Integer.valueOf(value);
		}
		
		catch(NumberFormatException e) {
			System.err.println(e.getMessage());
		}
		return pos;
	}

	protected List<Point2D> getCoordinates(Element graphicsElement) {
		String list = getTagValue("coords", graphicsElement);
		if(debug) System.out.println("Coords:" + list + " (" + list.length() + ")");
		ArrayList<Point2D> coords = new ArrayList<Point2D>();
		if(list != null && list.length() > 0) {
			String[] coordList = list.split("\\,");
			for(int i = 0; i< coordList.length; i+= 2) {
				double x = Double.valueOf(coordList[i]);
				double y = Double.valueOf(coordList[i+1]);
				coords.add(new Point2D.Double(x, y));
				
			}
		}
		return coords;
			
	}

	protected Shape getShape(Element graphicsElement) {
		
		Integer width 	= Integer.valueOf(getTagValue("width", graphicsElement));
		Integer height  = Integer.valueOf(getTagValue("height", graphicsElement));
		
		Shape shape = new Shape(width, height);
		return shape;
	}

	protected KGMLShapeType getShapeType(String tagValue) {
//		if(debug)
//			System.out.println("Shape for: " + tagValue);
		
		return KGMLShapeType.valueOf(tagValue.toUpperCase());
	}

	protected Color hexToRGB(String colorStr) {
//		if(debug)
//			System.out.println("Hex color:" + colorStr);
		
		if(colorStr.compareToIgnoreCase("none") == 0) {
			return Color.BLACK;
		}
		return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}

	protected EntryType parseEntryType(Element element, String field) {
		return EntryType.valueOf(getTagValue(field, element).toUpperCase());
	}

	protected String getTagValue(String sTag, Element eElement) {
		return eElement.getAttribute(sTag);
	}
}