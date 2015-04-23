package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;

/**
 * Layout builder for XGMML files.
 * Implements {@link ILayoutBuilder}.
 * 
 * @author Alberto Noronha
 *
 */
public class XGMMLReader implements ILayoutBuilder{
	
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
	
	static private Double[] getCordinates(Element el){
		
		Double[] cord = new Double[2];
		try {
			cord[0] = Double.valueOf(getGraphics(el, XAXIS));
			cord[1] = Double.valueOf(getGraphics(el, YAXIS));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return cord;
		
	}
	
	static String getGraphics (Element ele, String nameAtt) {
		NodeList nl = ele.getElementsByTagName("graphics");
		if (nl != null && nl.getLength() > 0) {
			String t = ((Element) nl.item(0)).getAttribute(nameAtt);
			if (t.equals(""))
				return null;
			else
				return t;
		}
		return null;
	}
	
	private Document document;
	private String xgmml_file;
	private String id_attribute;
	
	private HashMap<String, IReactionLay> reactions;
	private HashMap<String, INodeLay> nodes;
	private Map<String, Set<String>> nodeIdtoMetId;
	
	public XGMMLReader(String xgmml_file) throws InvalidLayoutFileException {
		
		nodeIdtoMetId = new HashMap<String, Set<String>>();
		this.xgmml_file = xgmml_file;
		readFile();
		this.id_attribute = "Opt_MetabolicId";
		constructLayout();
		
	}
	
	
	private void constructLayout() throws InvalidLayoutFileException {
			buildGraphWithReactionNodes(id_attribute);
	}
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public String getXgmml_file() {
		return xgmml_file;
	}
	public void setXgmml_file(String xgmml_file) {
		this.xgmml_file = xgmml_file;
	}
	
	/**
	 * Reads a xgmml file using {@link DocumentBuilder}
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void readFile() throws InvalidLayoutFileException{
		
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		//		try {

		//Using factory get an instance of document builder
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new InvalidLayoutFileException(e.getMessage());
		}

		//parse using builder to get DOM representation of the XML file
		try {
			this.document = db.parse(new File(xgmml_file));
		} catch (SAXException e) {
			throw new InvalidLayoutFileException(e.getMessage());
		} catch (IOException e) {
			throw new InvalidLayoutFileException(e.getMessage());
		}

	}


	@Override
	public LayoutContainer buildLayout(){
		
		LayoutContainer layoutContainer = new LayoutContainer(nodes, reactions);
		return layoutContainer;
	}

	/**
	 * Builds the nodes and reactions maps.
	 * @param id_attribute2
	 * @param container2
	 * @throws XGMMLException
	 * @throws InvalidLayoutFileException 
	 */
	private void buildGraphWithReactionNodes(String id_attribute2) throws InvalidLayoutFileException {
		
		Element docEle = document.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("node");

		this.nodes = new HashMap<String, INodeLay>();
		this.reactions = new HashMap<String, IReactionLay>();

		if(nl != null && nl.getLength() > 0) {
			int imax = nl.getLength();
			for(int i = 0 ; i < imax;i++) {

				Element el = (Element)nl.item(i);
				String xgmml_id = el.getAttribute("id");
				String xgmml_label = el.getAttribute("label");
				String metId;

				Set<String> metabolic_ids = new HashSet<String>();

				if(id_attribute.equals("id")){
					metId = xgmml_id;
					metabolic_ids.add(metId);

				}
				else if(id_attribute.equals("label")){
					metId = xgmml_label;
					metabolic_ids.add(metId);
				}
				else{
					metabolic_ids = getAttsWithSameValue(el, id_attribute);
				}
				
				String nodeType = getAttValue(el, NODETYPE);
				if(nodeType == null){
					throw new InvalidLayoutFileException("The field " + NODETYPE + " is mandatory for all nodes!");
				}
				
				if(nodeType.equals(REACTION)){

					Map<String, INodeLay> reactants = new HashMap<String, INodeLay>();
					Map<String, INodeLay> products = new HashMap<String,INodeLay>();
					Map<String, INodeLay> infos = new HashMap<String, INodeLay>();
					
					boolean rev;
					
					if(getAttValue(el, REVERSIBLE)!=null)
						rev = Boolean.valueOf(getAttValue(el, REVERSIBLE));
					else
						throw new InvalidLayoutFileException("It's impossible to determine the reversibility of the reaction node : " + xgmml_id + ".\n"
													 + "The field " + REVERSIBLE + " must be specified in the reaction node.");
					
					String label = "";
					if(metabolic_ids.size()>0)
						label = getLabelFromSet(metabolic_ids);
					else{
						if(getGraphics(el, "cy:nodeLabel")!= null) label = getGraphics(el, "cy:nodeLabel");
						else
							label = xgmml_label;
					}

					Double[] cord = getCordinates(el);
					Double x = cord[0];
					Double y = cord[1];

					ReactionLay reaction = new ReactionLay(reactants, products, infos, rev, x, y,metabolic_ids, xgmml_id, label);
					reaction.setX(x);
					reaction.setY(y);
					
					this.nodeIdtoMetId.put(xgmml_id, metabolic_ids);
					this.reactions.put(xgmml_id ,reaction);
				}

				else if(nodeType.equals(METABOLITE) || nodeType.equals(HUB)){

					Double[] cord = getCordinates(el);
					Double x = cord[0];
					Double y = cord[1];
					
					
					String label;

					if(metabolic_ids.size() > 0){
						label = getLabelFromSet(metabolic_ids);
					}
					else{
						if(getGraphics(el, "cy:nodeLabel")!= null) label = getGraphics(el, "cy:nodeLabel");
						else
							label = xgmml_label;
					}
					
					NodeTypeLay type;
					if(nodeType.equals(METABOLITE)) 
						type = NodeTypeLay.METABOLITE;
					else
						type = NodeTypeLay.CURRENCY;
					
					NodeLay node = new NodeLay(xgmml_id, label , metabolic_ids, type, x, y);
					
//					Double w = Double.valueOf(getGraphics(el, "w"));
//					Double h = Double.valueOf(getGraphics(el, "h"));
//					
//					if(w!=null && h!=null){
//						node.setHeight(h);
//						node.setWidth(w);
//					}
					this.nodes.put(xgmml_id, node);
					this.nodeIdtoMetId.put(xgmml_id, metabolic_ids);
				}
				else if(nodeType.equals(INFO)){
					
					Double[] cord = getCordinates(el);
					Double x = cord[0];
					Double y = cord[1];
					
					String label = getAttValue(el, INFOFIELD);
					
					if(label!=null){
						
						NodeTypeLay nodeTLay = NodeTypeLay.INFORMATION;
						NodeLay node = new NodeLay(xgmml_id, label, new HashSet<String>(), nodeTLay, x, y);
						
//						Double w = Double.valueOf(getGraphics(el, "w"));
//						Double h = Double.valueOf(getGraphics(el, "h"));
//						
//						if(w!=null && h!=null){
//							node.setHeight(h);
//							node.setWidth(w);
//						}
						this.nodes.put(xgmml_id, node);
						this.nodeIdtoMetId.put(xgmml_id, metabolic_ids);
						
					}
					else
						throw new InvalidLayoutFileException("Info node " + xgmml_id + " doesn't have a " + INFOFIELD + ". This is a " +
								"mandatory field for all information nodes.");
				}
				else{
					if(nodeType!=null)
						throw new InvalidLayoutFileException("The node type: " + nodeType + " in the node: " + xgmml_id + " is not a valid nodeType" +
								"\n Valide node types are: " + METABOLITE + "\t" + HUB + "\t" + REACTION + "\t" + INFO);
				}

			}

		}

		/**
		 * the builder will will consider source nodes reactants, 
		 * and edges with source nodes as reactions will consider
		 * target nodes products.
		 */
		
		NodeList el = docEle.getElementsByTagName("edge");
		if(el != null && el.getLength() > 0) {
			for(int i = 0 ; i < el.getLength();i++) {
				
				Element elm = (Element)el.item(i);
				String xgmmlreactant_id = elm.getAttribute("source");
//				Set<String> reactantIds = nodeIdtoMetId.get(xgmmlreactant_id);
				String xgmmlproduct_id = elm.getAttribute("target");
//				Set<String> productIds = nodeIdtoMetId.get(xgmmlproduct_id);
				
				IReactionLay reaction;
				INodeLay node;
				boolean reactionIsProduct;
				
				if(nodes.containsKey(xgmmlreactant_id)){
					node = nodes.get(xgmmlreactant_id);
					reaction = reactions.get(xgmmlproduct_id);
					reactionIsProduct = true;
					//reactant metabolite
					
				}else if(nodes.containsKey(xgmmlproduct_id)){
					reaction = reactions.get(xgmmlreactant_id);
					node = nodes.get(xgmmlproduct_id);
					reactionIsProduct = false;
					//node metabolite
				}
				else{
//					try {
//						System.out.println(xgmmlreactant_id + nodes.get(xgmmlreactant_id));
//					} catch (Exception e) {
//						System.out.println("problem id " + xgmmlreactant_id +"\t" + nodes.get(xgmmlreactant_id));
//					}
//					
//					try {
//						System.out.println("id " + xgmmlproduct_id + nodes.get(xgmmlproduct_id));
//						
//					} catch (Exception e) {
//						System.out.println("problem id " + xgmmlproduct_id +"\t" + nodes.get(xgmmlproduct_id));
//					}
//					
//					continue;
					throw new InvalidLayoutFileException("Edge connecting : " + xgmmlreactant_id + " and "+ xgmmlproduct_id + " is incorrectly specified. \n" +
							"Edges must allways connect a metabolite/hub/info node to a reaction node! ");
				}

				if(node.getNodeType().equals(NodeTypeLay.METABOLITE) || node.getNodeType().equals(NodeTypeLay.CURRENCY)){

//					Set<String> reactionIds = reaction.getIDs();
//					Set<ReactionCI> reactions_of_node = getAllReactions(container, reactionIds);

//					boolean hasReactant = hasAnyReactant(reactions_of_node,  node.getIds());
//					boolean hasProduct = hasAnyProduct(reactions_of_node, node.getIds());

					if( reactionIsProduct ){
						reaction.getReactants().put(node.getUniqueId(), node);
					}
					else {
						reaction.getProducts().put(node.getUniqueId(), node);
					}
//					else{
//						throw new XGMMLException("Edge connecting " + xgmmlreactant_id + "\t" + xgmmlproduct_id + "incorrectly specified.");
//
//					}

				}
				else if(node.getNodeType().equals(NodeTypeLay.INFORMATION)){

					reaction.infos().put(node.getUniqueId(), node);
				}

//				if(hasAnyReaction(container, reactantIds)){	
//
//					INodeLay product = nodes.get(xgmmlproduct_id);
//					Set<ReactionCI> reactions_of_node = getAllReactions(container, reactantIds);
//					
//					if(hasAnyReactant(reactions_of_node,  productIds)){
//						reactions.get(xgmmlreactant_id).getReactants().put(product.getUniqueId(), product);
//					}
//					else
//						reactions.get(xgmmlreactant_id).getProducts().put(product.getUniqueId(), product);
//				}
//				
//				else if(hasAnyReaction(container, productIds)){
//					
//					INodeLay reactant = nodes.get(xgmmlreactant_id);
//					Set<ReactionCI> reactions_of_node = getAllReactions(container, productIds);
//
//					if(hasAnyProduct(reactions_of_node, reactantIds)){
//						reactions.get(xgmmlproduct_id).getProducts().put(reactant.getUniqueId(),reactant);
//					}else
//						reactions.get(xgmmlproduct_id).getReactants().put(reactant.getUniqueId(),reactant);
//				}
				
			}
		}
		
	}
	
//	/**
//	 * Checks if the reaction have any product
//	 * @param reactions_of_node
//	 * @param oroductsIds
//	 * @return
//	 */
//	private boolean hasAnyProduct(Set<ReactionCI> reactions_of_node,
//			Set<String> oroductsIds) {
//		boolean found = false;
//
//		for(ReactionCI reactionCI : reactions_of_node){
//
//			if(containsAny(reactionCI.getProducts(), oroductsIds)) found = true;
//		}
//
//		return found;
//	}
	
//	/**
//	 * Check if the reaction has any reactant
//	 * @param reactions_of_node
//	 * @param reactantIds
//	 * @return
//	 */
//	private boolean hasAnyReactant(Set<ReactionCI> reactions_of_node,
//			Set<String> reactantIds) {
//		
//		
//		boolean found = false;
//		
//		for(ReactionCI reactionCI : reactions_of_node){
//			
//			if(containsAny(reactionCI.getReactants(), reactantIds)) found = true;
//		}
//		
//		return found;
//	}
	
//	private boolean containsAny(Map<String, StoichiometryValueCI> products,
//			Set<String> productIds) {
//		
//		boolean contains = false;
//		
//		for(String s : productIds){
//			if(products.keySet().contains(s)) contains = true;
//		}
//		
//		return contains;
//	}

//	private Set<ReactionCI> getAllReactions(Container container,
//			Set<String> reactantIds) {
//		
//		Set<ReactionCI> reactions_result = new HashSet<ReactionCI>();
//		for(String s : container.getReactions().keySet()){
//			if(reactantIds.contains(s)){
//				reactions_result.add(container.getReaction(s));
//			}
//		}
//		
//		return reactions_result;
//	}
	
	private String getLabelFromSet(Set<String> metabolic_ids) {
		
		String result = "";
		
		result = (String) metabolic_ids.toArray()[0];
		if(metabolic_ids.size()>1){
			for(int i = 1 ; i<metabolic_ids.size(); i++){
				result += " " + metabolic_ids.toArray()[i];
			}
		}
		
		return result;
			
	}

//	private boolean getReversibility(Container container,
//			Set<String> metabolic_ids) {
//		
//		boolean reversible = true;
//		for(String s : metabolic_ids) if(!container.getReaction(s).isReversible()) reversible = false;
//		
//		return reversible;
//	}

//	private boolean hasAnyReaction(Container container,
//			Set<String> metabolic_ids) {
//
//		boolean hasReaction = false;
//
//		for(String s : metabolic_ids)
//			if(container.getReactions().keySet().contains(s)) hasReaction = true;
//
//		return hasReaction;
//	}
	
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
	
	Set<String> getAttsWithSameValue (Element ele, String attributeName) {

		NodeList nl = ele.getElementsByTagName("att");
		Set<String> result = new HashSet<String>();
		
		if (nl != null && nl.getLength() > 0){
			int imax = nl.getLength();
			for (int i = 0; i < imax; i++) {
				Element nEl = (Element) nl.item(i);
				if (nEl.getAttribute("name").equals(attributeName))
					result.add(nEl.getAttribute("value"));
			}
		}

		return result;
	}
	
	public Set<String> getNodeIds() {

		Element docEle = document.getDocumentElement();
		
		NodeList nl = docEle.getElementsByTagName("node");
		Set<String> ids = new HashSet<String>();

		if(nl != null && nl.getLength() > 0) {

			int imax = nl.getLength();
			for(int i = 0 ; i < imax;i++) {

				Element el = (Element)nl.item(i);
				String label;
				
				if(id_attribute.equals("id")||id_attribute.equals("label")){
					label = el.getAttribute(id_attribute);
					ids.add(label);
				}
				else{
					Set<String> mIds = getAttsWithSameValue(el, id_attribute);
					ids.addAll(mIds);
					
				}
				
			}
		}
		return ids;
	}
	
//	/**
//	 * Build a layout from Cytoscape SBML
//	 */
//	private void buildCYSBMLLayout() {
//
//		this.nodes = new HashMap<String, INodeLay>();
//		this.reactions = new HashMap<String, IReactionLay>();
//
//		Element docEle = document.getDocumentElement();
//
//		NodeList nl = docEle.getElementsByTagName("node");
//
//		if(nl != null && nl.getLength() > 0) {
//			for(int i = 0 ; i < nl.getLength();i++) {
//
//				Element el = (Element)nl.item(i);
//				String nodeType = getAttValue(el, "sbml:type");
//
//				if(nodeType.equals("reaction")){
//
//					String xgmml_id = el.getAttribute("id");
//					
//					//					String xgmml_label = el.getAttribute("label");
//					String metId = getAttValue(el, "sbml:reaction id");
//
//					Set<String> metabolic_ids = new HashSet<String>();
//					metabolic_ids.add(metId);
//
//					Map<String, INodeLay> reactants = new HashMap<String, INodeLay>();
//					Map<String, INodeLay> products = new HashMap<String,INodeLay>();
//					Map<String, INodeLay> infos = new HashMap<String, INodeLay>();
//
//					boolean rev = Boolean.valueOf(el.getAttribute("sbml:reversible"));
//
//					Double x = Double.valueOf(getGraphics(el, XAXIS));
//					Double y = Double.valueOf(getGraphics(el, YAXIS));
//
//
//					ReactionLay reaction = new ReactionLay(reactants, products, infos, rev, x, y, metabolic_ids, xgmml_id, metId);
//
//					this.nodeIdtoMetId.put(xgmml_id, metabolic_ids);
//					this.reactions.put(xgmml_id ,reaction);
//
//				}
//				else if(nodeType.equals("species")){
//
//					String xgmml_id = el.getAttribute("id");
//					
//					//					String xgmml_label = el.getAttribute("label");
//					String metId = getAttValue(el, "sbml:species id");
//
//					Set<String> metabolic_ids = new HashSet<String>();
//					metabolic_ids.add(metId);
//
//					Double x = Double.valueOf(getGraphics(el, XAXIS));
//					Double y = Double.valueOf(getGraphics(el, YAXIS));
//
//					NodeLay node = new NodeLay(xgmml_id, metId, metabolic_ids, NodeTypeLay.METABOLITE, x, y);
//					
//					this.nodes.put(xgmml_id, node);
//					this.nodeIdtoMetId.put(xgmml_id, metabolic_ids);
//				}
//			}
//		}
//
//		NodeList el = docEle.getElementsByTagName("edge");
//		if(el != null && el.getLength() > 0) {
//			for(int i = 0 ; i < el.getLength();i++) {
//
//				Element elm = (Element)el.item(i);
//				String xgmmlsource_id = elm.getAttribute("source");
//				String xgmmltargt_id = elm.getAttribute("target");
//
//				String reactionNode;
//				String metaboliteNode;
//				if(reactions.containsKey(xgmmlsource_id)){
//
//					reactionNode = xgmmlsource_id;
//					metaboliteNode = xgmmltargt_id;
//				}
//				else{
//					reactionNode = xgmmltargt_id;
//					metaboliteNode = xgmmlsource_id;
//				}
//
//				if(getAttValue(elm, "sbml:interaction").equals("reaction-reactant")){
//					INodeLay metaboliteNodeLay = nodes.get(metaboliteNode);
//					reactions.get(reactionNode).getReactants().put(metaboliteNodeLay.getUniqueId(), metaboliteNodeLay);
//				}
//				else if(getAttValue(elm, "sbml:interaction").equals("reaction-product")){
//					INodeLay metaboliteNodeLay = nodes.get(metaboliteNode);
//					reactions.get(reactionNode).getProducts().put(metaboliteNodeLay.getUniqueId(), metaboliteNodeLay);
//				}
//			}
//		}
//	}
	
	public static Set<String> getAttsFromDoc(String file) throws SAXException, IOException, ParserConfigurationException{
		
		Document document;
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		//				try {

		//Using factory get an instance of document builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		//parse using builder to get DOM representation of the XML file
		document = db.parse(new File(file));

		
		Set<String> result = new HashSet<String>();
		
		Element docEle = document.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("node");
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			NodeList attL = el.getElementsByTagName("att");
			if(attL != null && attL.getLength() > 0){
				for(int i = 0; i < attL.getLength(); i++){
					Element att = (Element) attL.item(i);
					result.add(att.getAttribute("name"));
				}
			}
		}
		return result;
	}
}
