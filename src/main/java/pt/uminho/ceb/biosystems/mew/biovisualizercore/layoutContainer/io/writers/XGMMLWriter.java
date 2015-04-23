package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;

/**
 * Writer of layouts into <i>XGMML</i> files.
 * Supports writing with custom biovisualizer setting.<br>
 * @author Alberto Noronha
 */
public class XGMMLWriter {
	
	/** GRAPHICS */
	public final static String XAXIS = "x";
	public final static String YAXIS = "y";
	public final static String METABOLIC_ID = "Opt_MetabolicId";
	public final static String NODETYPE = "Opt_NodeType";
	public final static String METABOLITE = "Opt_Metabolite";
	public final static String HUB = "Opt_HUB";
	public final static String INFO = "Opt_INFO";
	public final static String REACTION = "Opt_Reaction";
	public final static String REVERSIBLE = "Reversible";
	public final static String INFOFIELD = "Opt_Information";
	
//	Map<String, String> listForNodeCreation;
	Map<String, String> idtoLabel;
	
	public  XGMMLWriter(){	
	}
	
	
//	public void writeDocumentWithFluxes(LayoutContainer container , Map<String, Double> fluxDistribution, String file){
//
//
//		Document document = container.getDocument();
//		Map<String,Set<String>> nodeIdtoMetId = container.getNodeIdtoMetId();
//
//
//		Element docEle = document.getDocumentElement();
//		NodeList el = docEle.getElementsByTagName("edge");
//
//		if(el!=null){
//			for(int i =0; i< el.getLength(); i++){
//
//				Element elm = (Element)el.item(i);
//
//				Set<String> reactantIds = nodeIdtoMetId.get(elm.getAttribute("source"));
//				Set<String> productIds = nodeIdtoMetId.get( elm.getAttribute("target"));
//
//				Double fluxValue = null;
//
//				if(containsAny(fluxDistribution.keySet(),reactantIds)){
//					fluxValue = getFluxFromDis(fluxDistribution,reactantIds);
//				}
//				else if(containsAny(fluxDistribution.keySet(), productIds)){
//					fluxValue = getFluxFromDis(fluxDistribution, productIds);
//				}
//
//				if(fluxValue!= null){
//
//					NodeList nl = elm.getElementsByTagName("graphics");
//					Node n = nl.item(0).getAttributes().getNamedItem("width");
//					n.setTextContent(fluxValue.toString());
//
//				}
//			}
//		}	
//
//		TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		Transformer transformer;
//		try {
//
//			transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(document);
//			StreamResult result = new StreamResult(new File(file));
//			transformer.transform(source, result);
//
//		} catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}	

//	private Double getFluxFromDis(Map<String, Double> fluxDistribution,
//			Set<String> reactionIds) {
//
//		Double result = 0.0;
//
//		for(String s : fluxDistribution.keySet()){
//
//			if(reactionIds.contains(s)) result += fluxDistribution.get(s);
//		}
//		return result;
//
//	}
//
//	private boolean containsAny(Set<String> keySet, Set<String> reactionIds) {
//
//		boolean found = false;
//		for(String s : keySet){
//
//			if(reactionIds.contains(s)) found = true;
//		}
//		return found;
//	}

	public void createDocumentWithFluxes(Map<String, Double> fluxDistribution, ILayout layout, String file) throws TransformerException, ParserConfigurationException, FileNotFoundException{

//		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setNamespaceAware(true);
			DocumentBuilder parser;
			parser = docFactory.newDocumentBuilder();
			Document doc = parser.newDocument();

			Element root = doc.createElement("graph");

			root.setAttribute("xmlns", "http://www.cs.rpi.edu/XGMML");
			root.setAttribute("xmlns:cy", "http://www.cytoscape.org");
			root.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
			root.setAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			root.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			root.setAttribute("directed", "1");
			root.setAttribute("label", "OptFlux Metabolic Network");

			Element att = doc.createElement("att");
			att.setAttribute("name", "documentVersion");
			att.setAttribute("value", "1.1");
			root.appendChild(att);

			Element att2 = doc.createElement("att");
			att2.setAttribute("name", "networkMetadata");
			
			root.appendChild(att2);
			createMetadataInformation(doc, att2);
			
			Element att3 = doc.createElement("att");
			att3.setAttribute("name", "backgroundColor");
			att3.setAttribute("type", "string");
			att3.setAttribute("value", "#ffffff");
			root.appendChild(att3);
			
			Element att4 = doc.createElement("att");
			att4.setAttribute("name", "GRAPH_VIEW_ZOOM");
			att4.setAttribute("type", "real");
			att4.setAttribute("value", "1.0");
			root.appendChild(att4);
			
			Element att5 = doc.createElement("att");
			att5.setAttribute("name", "GRAPH_VIEW_CENTER_X");
			att5.setAttribute("type", "real");
			att5.setAttribute("value", "0.0");
			root.appendChild(att5);
			
			Element att6 = doc.createElement("att");
			att6.setAttribute("name", "GRAPH_VIEW_CENTER_Y");
			att6.setAttribute("type", "real");
			att6.setAttribute("value", "0.0");
			root.appendChild(att6);
			
			Element att7 = doc.createElement("att");
			att7.setAttribute("name", "NODE_SIZE_LOCKED");
			att7.setAttribute("type", "boolean");
			att7.setAttribute("value", "true");
			root.appendChild(att7);
			
			buildNodes(root, doc, layout, true);
			buildEdges(root, doc, layout, true, fluxDistribution);

			doc.appendChild(root);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			FileOutputStream fout = new FileOutputStream(new File(file));
			StreamResult result =  new StreamResult(fout);
			transformer.transform(source, result);


	}

	private void buildEdges(Element root, Document doc, ILayout layout, boolean reactionNodes, Map<String,Double> fluxes) {

		for(String rId : layout.getReactions().keySet()){

//			if(visibles.get(rId)){

				for(String mId : layout.getReactions().get(rId).getReactants().keySet()){

//					if(visibles.get(mId)){
						
//						String rNodeId = this.listForNodeCreation.get(rId);
						String rNodeId = rId;
						String rNodeLabel = this.idtoLabel.get(rNodeId);
//						String mNodeId = this.listForNodeCreation.get(mId);
						String mNodeId = mId;
						String mNodeLabel = this.idtoLabel.get(mNodeId);

						Element edge = doc.createElement("edge");
						edge.setAttribute("label", mNodeLabel +" (DirectedEdge) " + rNodeLabel);
						edge.setAttribute("source", mNodeId);
						edge.setAttribute("target", rNodeId);

						Element edgeAtt = doc.createElement("att");
						edgeAtt.setAttribute("cy:editable", "false");
						edgeAtt.setAttribute("name", "interaction");
						edgeAtt.setAttribute("type", "string");
						edgeAtt.setAttribute("value", "DirectedEdge");
						edge.appendChild(edgeAtt);
						setEdgeGraphics(edge, doc, fluxes.get(rId));
						root.appendChild(edge);
//					}
				}

				for(String mId : layout.getReactions().get(rId).getProducts().keySet()){
					
//					if(visibles.get(mId)){

//						String rNodeId = this.listForNodeCreation.get(rId);
						String rNodeId = rId;
						String rNodeLabel = this.idtoLabel.get(rNodeId);
//						String mNodeId = this.listForNodeCreation.get(mId);
						String mNodeId = mId;
						String mNodeLabel = this.idtoLabel.get(mNodeId);

						Element edge = doc.createElement("edge");
						edge.setAttribute("label",  rNodeLabel + " (DirectedEdge) " + mNodeLabel);
						edge.setAttribute("source", rNodeId);
						edge.setAttribute("target", mNodeId);

						Element edgeAtt = doc.createElement("att");
						edgeAtt.setAttribute("cy:editable", "false");
						edgeAtt.setAttribute("name", "interaction");
						edgeAtt.setAttribute("type", "string");
						edgeAtt.setAttribute("value", "DirectedEdge");
						edge.appendChild(edgeAtt);
						setEdgeGraphics(edge,doc,fluxes.get(rId));
						root.appendChild(edge);
//					}
				}

				for(String infoId : layout.getReactions().get(rId).infos().keySet()){

//					if(visibles.get(infoId)){
//						String rNodeId = this.listForNodeCreation.get(rId);
						String rNodeId = rId;
						String rNodeLabel = this.idtoLabel.get(rNodeId);
//						String infoNodeId = this.listForNodeCreation.get(infoId);
						String infoNodeId = infoId;
						String infoNodeLabel = this.idtoLabel.get(infoNodeId);

						Element edge = doc.createElement("edge");
						edge.setAttribute("label",  rNodeLabel + " (DirectedEdge) " + infoNodeLabel);
						edge.setAttribute("source", rNodeId);
						edge.setAttribute("target", infoNodeId);

						Element edgeAtt = doc.createElement("att");
						edgeAtt.setAttribute("cy:editable", "false");
						edgeAtt.setAttribute("name", "interaction");
						edgeAtt.setAttribute("type", "string");
						edgeAtt.setAttribute("value", "DirectedEdge");

						edge.appendChild(edgeAtt);

						/**Graphics*/
						Element graph = doc.createElement("graphics");
						graph.setAttribute("cy:curved","STRAIGHT_LINES");
						graph.setAttribute("cy:edgeLabel","");
						graph.setAttribute("cy:edgeLabelFont","Default-0-10");
						graph.setAttribute("cy:edgeLineType","SOLID");
						graph.setAttribute("cy:sourceArrow","0");
						graph.setAttribute("cy:sourceArrowColor","#000000");
						graph.setAttribute("cy:targetArrow","0");
						graph.setAttribute("cy:targetArrowColor","#000000");
						graph.setAttribute("fill","#0000ff");
						graph.setAttribute("widht", "1");
						/***/

						root.appendChild(edge);
//					}
				}
//			}
		}
	}

	private void setEdgeGraphics(Element edge, Document doc, Double width) {
		// TODO Auto-generated method stub
		Element graph = doc.createElement("graphics");
		graph.setAttribute("cy:curved","STRAIGHT_LINES");
		graph.setAttribute("cy:edgeLabel","");
		graph.setAttribute("cy:edgeLabelFont","Default-0-10");
		graph.setAttribute("cy:edgeLineType","SOLID");
		graph.setAttribute("cy:sourceArrow","0");
		graph.setAttribute("cy:sourceArrowColor","#000000");
		graph.setAttribute("cy:targetArrow","0");
		graph.setAttribute("cy:targetArrowColor","#000000");
		graph.setAttribute("fill","#0000ff");

		if(width!=null)
			graph.setAttribute("widht", "" + width);
		else
			graph.setAttribute("widht", "1");

		edge.appendChild(graph);
	}

	private void buildNodes(Element root, Document doc, ILayout layout, boolean reactionNodes) {
//		this.listForNodeCreation = new HashMap<String, String>();
		this.idtoLabel = new HashMap<String, String>();

		int counter = 1;

		if(reactionNodes){
			Map<String, IReactionLay> reactions = layout.getReactions();


			for(String rId : reactions.keySet()){

//				if(visibles.get(rId)){
					IReactionLay r = reactions.get(rId);
					Element node = doc.createElement("node");
					String idvalue = r.getUniqueId();

					node.setAttribute("id", idvalue);
					node.setAttribute("label", "node"+(counter));

					Set<String> metabolic_Ids = r.getIDs();
					setReactionNodeAtts(node, doc, r, metabolic_Ids);
					root.appendChild(node);
//					listForNodeCreation.put(rId, idvalue);
					idtoLabel.put(idvalue, "node"+(counter));
					counter++;
//				}
			}
			for(String mId : layout.getNodes().keySet()){
				
				if(/*visibles.get(mId) &&**/ !layout.getNodes().get(mId).getNodeType().equals(NodeTypeLay.INFORMATION)){
					INodeLay n = layout.getNodes().get(mId);
					buildMetaboliteNode(root, n, doc, counter);
					counter++;
				}
				else if(/*visibles.get(mId) && **/layout.getNodes().get(mId).getNodeType().equals(NodeTypeLay.INFORMATION)){
					
					/**
					 * Build info node!
					 */
					INodeLay n = layout.getNodes().get(mId);
					Element node = doc.createElement("node");
					String idvalue = n.getUniqueId();
					node.setAttribute("id", idvalue);
					node.setAttribute("label", "node" + counter+1);
					
					createAtt(node, doc, NODETYPE, "string", INFO);
					createAtt(node, doc, INFOFIELD, "string", n.getLabel());
					createInfoGraphics(node, doc, n, n.getLabel());
					
					root.appendChild(node);
//					listForNodeCreation.put(mId, idvalue);
					idtoLabel.put(idvalue, "node"+(counter));
					counter++;
				}
			}
		}

	}


	private void buildMetaboliteNode(Element root, INodeLay m, Document doc,  int counter){

			Element node = doc.createElement("node");
			String idvalue = m.getUniqueId();
			node.setAttribute("id", idvalue);
			node.setAttribute("label", "node"+counter);
			
			Set<String> met_IDs = m.getIds();
			setMetaboliteNodeAtts(node,doc,m,met_IDs);
			root.appendChild(node);
//			listForNodeCreation.put(idvalue, idvalue);
			idtoLabel.put(idvalue, "node"+(counter));
			
	}
	
	private void setMetaboliteNodeAtts(Element node, Document doc, INodeLay m,
			Set<String> mIds) {
		
		for(String s : mIds)
			createAtt(node, doc, METABOLIC_ID, "string", s);
		
		if(m.getNodeType().equals(NodeTypeLay.METABOLITE))
			createAtt(node, doc, NODETYPE, "string", METABOLITE);
		else if(m.getNodeType().equals(NodeTypeLay.CURRENCY))
			createAtt(node, doc, NODETYPE, "string", HUB);
		createMetaboliteGraphics(node, doc, m, m.getLabel());
	}

	private void setReactionNodeAtts(Element node, Document doc, IReactionLay r, Set<String> rIds) {
		
		for(String s : rIds)
			createAtt(node, doc, METABOLIC_ID, "string", s);
		createAtt(node, doc, NODETYPE, "string", REACTION);
		createAtt(node, doc, REVERSIBLE, "boolean", "" + r.isReversible());
		createReactionGraphics(node, doc,r, r.getLabel());
	}

	private void createMetaboliteGraphics(Element node, Document doc,
			INodeLay m, String mId) {
		
		Element graphics = doc.createElement("graphics");
		graphics.setAttribute("type", "ELLIPSE");
		
//		Double w = m.getWidth();
//		Double h = m.getHeight();
//		
//		if(w != null && h != null){
//			graphics.setAttribute("h", h.toString());
//			graphics.setAttribute("w", w.toString());
//		}
//		else{
		if(m.getNodeType().equals(NodeTypeLay.METABOLITE)){
			graphics.setAttribute("h", "20.0");
			graphics.setAttribute("w", "40.0");
		}
		else{
			graphics.setAttribute("h", "20.0");
			graphics.setAttribute("w", "20.0");
		}
		
		
		
		Double x = m.getX();
		Double y = m.getY();
		
		graphics.setAttribute("x", ""+x);
		graphics.setAttribute("y", ""+y);
		graphics.setAttribute("fill", "#00CC00");
		graphics.setAttribute("width", "1");
		graphics.setAttribute("outline", "#666666");
		graphics.setAttribute("cy:nodeTransparency", "1.0");
		graphics.setAttribute("cy:nodeLabelFont", "Default-0-12");
		graphics.setAttribute("cy:nodeLabel", mId);
		graphics.setAttribute("cy:boderLineType", "solid");
		
		node.appendChild(graphics);
	}
	
	private void createReactionGraphics(Element node, Document doc,
			IReactionLay r, String rId) {
		
		Element graphics = doc.createElement("graphics");
		graphics.setAttribute("type", "ELLIPSE");
		graphics.setAttribute("h", "20.0");
		graphics.setAttribute("w", "20.0");
		
		Double x = r.getX();
		Double y = r.getY();
		
		graphics.setAttribute("x", ""+x);
		graphics.setAttribute("y", ""+y);
		graphics.setAttribute("fill", "#000000");
		graphics.setAttribute("width", "1");
		graphics.setAttribute("outline", "#666666");
		graphics.setAttribute("cy:nodeTransparency", "1.0");
		graphics.setAttribute("cy:nodeLabelFont", "Default-0-12");
		graphics.setAttribute("cy:nodeLabel", rId);
		graphics.setAttribute("cy:boderLineType", "solid");
		
		node.appendChild(graphics);
	}

	private void createInfoGraphics(Element node, Document doc, INodeLay n,
			String label) {
		Element graphics = doc.createElement("graphics");
		graphics.setAttribute("type", "RECTANGLE");

//		if(n.getHeight() != null && n.getWidth()!=null){
//			graphics.setAttribute("h", n.getHeight().toString());
//			graphics.setAttribute("w", n.getWidth().toString());
//		}
//		else{
		graphics.setAttribute("h", "30.0");
		graphics.setAttribute("w", "30.0");
//		}

		Double x = n.getX();
		Double y = n.getY();
		
		graphics.setAttribute("x", ""+x);
		graphics.setAttribute("y", ""+y);
		graphics.setAttribute("fill", "#0000CC");
		graphics.setAttribute("width", "1");
		graphics.setAttribute("outline", "#666666");
		graphics.setAttribute("cy:nodeTransparency", "1.0");
		graphics.setAttribute("cy:nodeLabelFont", "Default-0-12");
		graphics.setAttribute("cy:nodeLabel", label);
		graphics.setAttribute("cy:boderLineType", "solid");
		
		node.appendChild(graphics);
		
	}
	
	public void createAtt(Element el, Document doc, String name, String type, String value){
		Element att = doc.createElement("att");
		att.setAttribute("name", name);
		att.setAttribute("type", type);
		att.setAttribute("value",value);
		el.appendChild(att);
	}
	
	private void createMetadataInformation(Document doc, Element att2) {
		
	
		Element rdf = doc.createElement("rdf:RDF");
		att2.appendChild(rdf);
		Element rdfDesc = doc.createElement("rdf:Description");
		rdfDesc.setAttribute("rdf:about", "http://optflux.org/");
		rdf.appendChild(rdfDesc);
		
		Element type = doc.createElement("dc:type");
		type.appendChild(doc.createTextNode("Metabolic Network"));
		
		Element desc = doc.createElement("dc:description");
		desc.appendChild(doc.createTextNode("N/A"));
		
		Element  identifier = doc.createElement("dc:identifier");
		identifier.appendChild(doc.createTextNode("N/A"));
		
		Element date = doc.createElement("dc:date");
		Date d = new Date();
		date.appendChild(doc.createTextNode(d.toString()));
		
		Element tittle = doc.createElement("dc:tittle");
		tittle.appendChild(doc.createTextNode("Network"));
		
		Element source = doc.createElement("dc:source");
		source.appendChild(doc.createTextNode("http://http://optflux.org/"));
		
		Element format = doc.createElement("dc:format");
		format.appendChild(doc.createTextNode("OptFlux - XGMML"));
		
		rdfDesc.appendChild(type);
		rdfDesc.appendChild(desc);
		rdfDesc.appendChild(identifier);
		rdfDesc.appendChild(date);
		rdfDesc.appendChild(tittle);
		rdfDesc.appendChild(source);
		rdfDesc.appendChild(format);
		
	}

	String getAttValue (Element ele, String attributeName) {
		NodeList nl = ele.getElementsByTagName("att");
		if (nl != null && nl.getLength() > 0){
			int imax = nl.getLength();
			for (int i = 0; i < imax; i++) {
				Element nEl = (Element) nl.item(i);
				if (nEl.getAttribute("name").equals(attributeName))
					return nEl.getAttribute("value");
			}
		}

		return null;
	}
	
}
