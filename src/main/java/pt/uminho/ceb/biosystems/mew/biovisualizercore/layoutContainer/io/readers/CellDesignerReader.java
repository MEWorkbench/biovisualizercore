package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.io.JSBMLValidationException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.ErrorsException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLReader;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.CDContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.NodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components.ReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayoutBuilder;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;

/**
 * Layout builder for cell designer SBML. Extends JSBMLReader, and 
 * uses JSBML.
 * @author Alberto Noronha
 *
 */
public class CellDesignerReader extends JSBMLReader implements ILayoutBuilder{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String filePath;
	private Model jsbmlmodel;
	private String header;
	private String organismName;

	protected SBMLDocument document;
	
	protected boolean checkConsistency = true;
	
	Map<String, INodeLay> nodes;
	Map<String, IReactionLay> reactions;
	
	public CellDesignerReader(String filePath, String organismName, Double lb, Double ub) throws InvalidLayoutFileException, XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException{
		this(filePath, organismName, true, lb, ub);
	}
	

	public CellDesignerReader(String filePath, String organismName, boolean checkConsistency, Double lb, Double ub) throws InvalidLayoutFileException, XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException{
		super(filePath, organismName, checkConsistency, lb, ub);

		try {

			this.filePath = filePath;
			this.getJSBMLModel();
			this.organismName = organismName;
			readInfo(document.getModel());
			
		} catch (InvalidLayoutFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
			
	/**
	 * Reads the SBML model.
	 * 
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws ErrorsException
	 */
	private void getJSBMLModel() throws InvalidLayoutFileException {
	    
		try {
			FileReader f = new FileReader(filePath);
			BufferedReader r = new BufferedReader(f);
			boolean found = false;

			while(r.ready() && !found){
				String str = r.readLine().trim();
				str.replaceAll("^\\s*?<", "<");
				if(str.contains("<sbml")){
					header = str;
					found = true;
				}
			}


			r.close();
			f.close();

			SBMLReader reader  = new SBMLReader();

			document = reader.readSBML(filePath);


			if(document.getNumErrors() > 0){
				throw new InvalidLayoutFileException(new ErrorsException(document).getMessage());
			}

			Model model = document.getModel();
			this.jsbmlmodel = model;

		} catch (XMLStreamException | IOException e) {
			throw new InvalidLayoutFileException("Problem reading the file.\nCause: " + e.getMessage());
		}
	}

	/**
	 * Returns the model name.
	 */
	public String getModelName() {			
		return jsbmlmodel.getName();
	}


	/**
	 * Returns the organism name.
	 */
	public String getOrganismName() {
		return organismName;
	}

	/**
	 * Version of the sbml.
	 */
	public Integer getVersion() {
		return Integer.valueOf((int)jsbmlmodel.getVersion());
	}
	
	/**
	 * Returns the header of the sbml.
	 * @return
	 */
	public String getHeader() {
		return header;
	}
	
	/**
	 * Sets a new header.
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}
	
	/**
	 * Returns the {@link SBMLDocument}
	 * @return
	 */
	public SBMLDocument getDocument() {
		return document;
	}
	
	/**
	 * Sets the {@link SBMLDocument}
	 * @param document
	 */
	public void setDocument(SBMLDocument document) {
		this.document = document;
	}
	
	/**
	 * Reads information from the SBML model, and creates the
	 * maps with the information of the layout.
	 * 
	 * @param jsbmlmodel
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void readInfo(Model jsbmlmodel) throws InvalidLayoutFileException{
		String model_annotations="";
		try {
			model_annotations = jsbmlmodel.getAnnotationString();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		nodes = new HashMap<String, INodeLay>();
		reactions = new HashMap<String, IReactionLay>();

		//		int comp_start_index = model_annotations.indexOf("<celldesigner:listOfCompartmentAliases>");
		//		int comp_end_index = model_annotations.indexOf("</celldesigner:listOfCompartmentAliases>") + "</celldesigner:listOfCompartmentAliases>".length();
		//		String compartments = header+model_annotations.substring(comp_start_index, comp_end_index)+"</sbml>";

		int species_start_index = model_annotations.indexOf("<celldesigner:listOfSpeciesAliases>");
		int species_end_index = model_annotations.indexOf("</celldesigner:listOfSpeciesAliases>") + "</celldesigner:listOfSpeciesAliases>".length();
		String metabolites = header+model_annotations.substring(species_start_index, species_end_index)+"</sbml>";

		Map<String, String> metabolite_classes = new HashMap<String, String>();
		ListOf<Species> sbmlspecies = jsbmlmodel.getListOfSpecies();
		for(int i =0;i<sbmlspecies.size();i++){
			Species species = sbmlspecies.get(i);
			String specie_id = species.getId();
			String specie_annotation="";
			try {
				specie_annotation = species.getAnnotationString();
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int class_start_index = specie_annotation.indexOf("<celldesigner:class>") + "<celldesigner:class>".length();
			int class_end_index = specie_annotation.indexOf("</celldesigner:class>");
			String _class = specie_annotation.substring(class_start_index, class_end_index);

			metabolite_classes.put(specie_id, _class);
		}

		Map<String, String> reactions = new HashMap<String, String>();
		Map<String, Boolean> reactions_reversibility = new HashMap<String, Boolean>();

		ListOf<Reaction> sbmlreactions = jsbmlmodel.getListOfReactions();
		for(int i =0;i<sbmlreactions.size();i++){

			Reaction sbmlreaction = sbmlreactions.get(i);
			String reactionId = sbmlreaction.getId();
			String reaction_annotation="";
			try {
				reaction_annotation = sbmlreaction.getAnnotationString();
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			boolean isReversible = sbmlreaction.getReversible();
			reactions.put(reactionId, reaction_annotation);
			reactions_reversibility.put(reactionId, isReversible);

		}

		//		populateCompartmentsCD(compartments);
		populateSpeciesCD(metabolites, metabolite_classes);
		populateReactonsCD(reactions, reactions_reversibility);
	}
	
//	public void populateCompartmentsCD(String compartments) throws ParserConfigurationException, SAXException, IOException{
//		
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		Document document = builder.parse(new InputSource(new StringReader(compartments)));
//		
//		
//		Element root = document.getDocumentElement();
//		NodeList compAliasList = root.getElementsByTagName("celldesigner:compartmentAlias");
//		
//		String aliasId, aliasName, cdClass, color, scheme;
//		Double x=null, y=null, w=null, h=null, tickness, outerwidth, innerwidth;
//		Point position;
//		Bound size;
//		
//		for(int i=0; compAliasList != null && i < compAliasList.getLength(); i++) {
//			Element compAlias = (Element) compAliasList.item(i);
//			
//			aliasId = compAlias.getAttribute("id");
//
//			aliasName = compAlias.getAttribute("compartment");
//			
//			NodeList classList = compAlias.getElementsByTagName("celldesigner:class");
//			
//			Element _class = (Element) classList.item(0);
//			
//			cdClass = _class.getTextContent();
//			
//			NodeList boundsList = compAlias.getElementsByTagName("celldesigner:bounds");
//			
//			Element bounds = (Element) boundsList.item(0);
//
//			if(bounds != null){
//				x = Double.parseDouble(bounds.getAttribute("x"));
//				y = Double.parseDouble(bounds.getAttribute("y"));
//				
//				w = Double.parseDouble(bounds.getAttribute("w"));
//				h = Double.parseDouble(bounds.getAttribute("h"));
//			}
//			size = new Bound(w, h);
//			position = new Point(x, y);
//
//			NodeList doubleLineList = compAlias.getElementsByTagName("celldesigner:doubleLine");
//			
//			Element doubleLine = (Element) doubleLineList.item(0);
//			
//			if(doubleLine != null){
//				tickness = Double.parseDouble(doubleLine.getAttribute("thickness"));
//				outerwidth = Double.parseDouble(doubleLine.getAttribute("outerWidth"));
//				innerwidth = Double.parseDouble(doubleLine.getAttribute("innerWidth"));
//			}
//			else{
//				tickness = 12.0;
//				outerwidth = 2.0;
//				innerwidth = 1.0;
//			}
//			
//			
//			NodeList paintList = compAlias.getElementsByTagName("celldesigner:paint");
//			
//			Element paint = (Element) paintList.item(0);
//			if(paint != null){
//				color = paint.getAttribute("color");
//				scheme = paint.getAttribute("scheme");
//			}
//			else{
//				color = "00000000";
//				scheme = "Color";
//			}
//			
//			compartments_cd.put(aliasId, new CompartmentCD(aliasId, aliasName, cdClass, position, size, tickness, outerwidth, innerwidth, color, scheme));
//		}
//	}
	
	/**
	 * Populates the nodes.
	 * @param metabolites
	 * @param metabolite_classes
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void populateSpeciesCD(String metabolites, Map<String, String> metabolite_classes) throws InvalidLayoutFileException{

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(metabolites)));

			Element root = document.getDocumentElement();
			NodeList speAliasList = root.getElementsByTagName("celldesigner:speciesAlias");

			String aliasId, aliasName, activity, cdClass, color, scheme;
			double x, y, w, h;
			//			Point position;
			//			Bound size;

			for(int i=0; speAliasList != null && i < speAliasList.getLength(); i++) {
				Element speAlias = (Element) speAliasList.item(i);

				aliasId = speAlias.getAttribute("id");
				aliasName = speAlias.getAttribute("species");

				NodeList activityList = speAlias.getElementsByTagName("celldesigner:activity");
				Element activ = (Element) activityList.item(0);
				activity = activ.getTextContent();
				NodeList boundsList = speAlias.getElementsByTagName("celldesigner:bounds");

				Element bounds = (Element) boundsList.item(0);

				x = Double.parseDouble(bounds.getAttribute("x"));
				y = Double.parseDouble(bounds.getAttribute("y"));
				//				position = new Point(x, y);

				w = Double.parseDouble(bounds.getAttribute("w"));
				h = Double.parseDouble(bounds.getAttribute("h"));
				//				size = new Bound(w, h);

				NodeList paintList = speAlias.getElementsByTagName("celldesigner:paint");

				Element paint = (Element) paintList.item(0);
				if(paint != null){
					color = paint.getAttribute("color");
					scheme = paint.getAttribute("scheme");
				}
				else{
					color = "00000000";
					scheme = "Color";
				}


				cdClass = metabolite_classes.get(aliasName);

				Set<String> ids = new HashSet<String>();
				ids.add(aliasName);
				NodeLay node = new NodeLay(aliasId, aliasName, ids, null, x, y);
				nodes.put(aliasId, node);
			}
		} catch (ParserConfigurationException e) {
			throw new InvalidLayoutFileException("Problem parsing the CellDesigner file:\n" + e.getMessage());
		} catch (SAXException e) {
			throw new InvalidLayoutFileException("Problem parsing the CellDesigner file:\n" + e.getMessage());
		} catch (IOException e) {
			throw new InvalidLayoutFileException("Problem parsing the CellDesigner file.\nCause:" + e.getClass()+ " :" + e.getMessage());
		}
	}

	/**
	 * Populates the reactions.
	 * @param reactions
	 * @param reactions_reversibility
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void populateReactonsCD(Map<String, String> reactions, Map<String, Boolean> reactions_reversibility) throws InvalidLayoutFileException{
				
		for(String reactionId : reactions.keySet()){
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			
			try {
				builder = factory.newDocumentBuilder();
				
				Document document = builder.parse(new InputSource(new StringReader(reactions.get(reactionId))));
				
				String reactionType, linecolor;
				double lineWidth;
				Map<String, INodeLay> reactants = new HashMap<String, INodeLay>(), products = new HashMap<String, INodeLay>(), modifiers = new HashMap<String, INodeLay>();
				
				Element root = document.getDocumentElement();
				NodeList reactionTypeList = root.getElementsByTagName("celldesigner:reactionType");
				Element eReactionType = (Element) reactionTypeList.item(0);
				reactionType = eReactionType.getTextContent();
				
				NodeList baseReactantsList = root.getElementsByTagName("celldesigner:baseReactant");
				for(int i=0; baseReactantsList != null && i < baseReactantsList.getLength(); i++){
					Element baseReactant = (Element) baseReactantsList.item(i);
					NodeLay aux = (NodeLay) nodes.get(baseReactant.getAttribute("alias"));
					aux.setNodeType(NodeTypeLay.METABOLITE);
					nodes.put(baseReactant.getAttribute("alias"), aux);
					reactants.put(((NodeLay) nodes.get(baseReactant.getAttribute("alias"))).getUniqueId(), nodes.get(baseReactant.getAttribute("alias")));
				}
				
				NodeList baseProductsList = root.getElementsByTagName("celldesigner:baseProduct");
				for(int i=0; baseProductsList != null && i < baseProductsList.getLength(); i++){
					Element baseProduct = (Element) baseProductsList.item(i);
					NodeLay aux = (NodeLay) nodes.get(baseProduct.getAttribute("alias"));
					aux.setNodeType(NodeTypeLay.METABOLITE);
					nodes.put(baseProduct.getAttribute("alias"), aux);
					products.put(((NodeLay) nodes.get(baseProduct.getAttribute("alias"))).getUniqueId(), nodes.get(baseProduct.getAttribute("alias")));
				}
				
				NodeList reactantLinksList = root.getElementsByTagName("celldesigner:reactantLink");
				for(int i=0; reactantLinksList != null && i < reactantLinksList.getLength(); i++){
					Element reactantLink = (Element) reactantLinksList.item(i);
					NodeLay aux = (NodeLay) nodes.get(reactantLink.getAttribute("alias"));
					aux.setNodeType(NodeTypeLay.METABOLITE);
					nodes.put(reactantLink.getAttribute("alias"), aux);
					reactants.put(((NodeLay) nodes.get(reactantLink.getAttribute("alias"))).getUniqueId(), nodes.get(reactantLink.getAttribute("alias")));
				}
				
				NodeList productLinksList = root.getElementsByTagName("celldesigner:productLink");
				for(int i=0; productLinksList != null && i < productLinksList.getLength(); i++){
					Element productLink = (Element) productLinksList.item(i);
					NodeLay aux = (NodeLay) nodes.get(productLink.getAttribute("alias"));
					aux.setNodeType(NodeTypeLay.METABOLITE);
					nodes.put(productLink.getAttribute("alias"), aux);
					products.put(((NodeLay) nodes.get(productLink.getAttribute("alias"))).getUniqueId(), nodes.get(productLink.getAttribute("alias")));
				}
				
				NodeList lineList = root.getElementsByTagName("celldesigner:line");
				
				Element line = (Element) lineList.item(0);
				if(line != null){
					lineWidth = Double.parseDouble(line.getAttribute("width"));
					linecolor = line.getAttribute("color");
				}
				else{
					lineWidth = 1.0;
					linecolor = "00000000";
				}
				
				NodeList modificationList = root.getElementsByTagName("celldesigner:modification");
				for(int i=0; modificationList != null && i < modificationList.getLength(); i++){
					Element modifier = (Element) modificationList.item(0);
					((NodeLay) nodes.get(modifier.getAttribute("aliases"))).setNodeType(NodeTypeLay.INFORMATION);
					modifiers.put(((NodeLay) nodes.get(modifier.getAttribute("aliases"))).getUniqueId(), nodes.get(modifier.getAttribute("aliases")));
				}
				
				boolean isReversible = reactions_reversibility.get(reactionId);
				Set<String> ids = new HashSet<String>();
				ids.add(reactionId);
				ReactionLay reaction = new ReactionLay(reactants, products, modifiers, isReversible, null, null, ids, reactionId, reactionId);
				this.reactions.put(reactionId, reaction);
//				reactions_cd.put(reactionId, new ReactionCD(reactionId, reactionType, isReversible, reactants, products, modifiers, lineWidth, linecolor));
		
			} catch (ParserConfigurationException e) {
				new InvalidLayoutFileException("Problem reading reaction id: " + reactionId + "\nCause: " + e.getMessage());
			} catch (SAXException e) {
				new InvalidLayoutFileException("Problem reading reaction id: " + reactionId + "\nCause: " + e.getMessage());
			} catch (IOException e) {
				new InvalidLayoutFileException("Problem reading reaction id: " + reactionId + "\nCause: " + e.getMessage());
			}
			
		}
	}


	@Override
	public LayoutContainer buildLayout(){
		
		CDContainer container = new CDContainer(nodes, reactions, document);
		return container;
	}
	
}
