/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.SpeciesReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.CDContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

/**
 * A writer for Cytoscape SBML Files, carrying flux distribution information
 * 
 * @author Alberto Noronha
 */
public class CellDesignerWriter{
	
	private SBMLDocument document;
	private String path;

	public CellDesignerWriter(){}

	public CellDesignerWriter(String path,CDContainer layoutContainer){
		this.path = path;
		this.document = layoutContainer.getDocument();
	}

	
	public SBMLDocument getDocument() {
		return document;
	}
	
	public void setDocument(SBMLDocument document) {
		this.document = document;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public void writeToFile() throws Exception {
		SBMLWriter writer = new SBMLWriter();
		OutputStream out = new FileOutputStream(path);
		writer.write(document, out);
	}

	public void writeToFile(SBMLDocument document) throws SBMLException, XMLStreamException, FileNotFoundException{
		SBMLWriter writer = new SBMLWriter();
		OutputStream out = new FileOutputStream(path);
		writer.write(document, out);
	}

	public void writeSolutionResult(Map<String, Double> values, double minBound, double maxBound) throws FileNotFoundException{
		SBMLDocument documentCopy = document.clone();
		Map<String, Double> normalized_values = LayoutUtils.normalizeFluxes(values, minBound, maxBound, 1);


		Model jsbmlModel = documentCopy.getModel();

		for(String s : normalized_values.keySet()){
			if(jsbmlModel.getReaction(s) != null){
				String annotation="";
				try {
					annotation = jsbmlModel.getReaction(s).getAnnotationString();
				} catch (XMLStreamException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				//Updates the width
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();

					Document documentXML = builder.parse(new InputSource(new StringReader(annotation)));
					Element root = documentXML.getDocumentElement();
					if(jsbmlModel.getReaction(s).getReversible()){
						//turns irreversible to design a 1 directional arrow
						jsbmlModel.getReaction(s).setReversible(false);

						//switches the arrow direction (switching the products and reactants)
						if(values.get(s) < 0){
							int tamanho;
							NodeList baseReactantsList = root.getElementsByTagName("celldesigner:baseReactants");
							tamanho = baseReactantsList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(baseReactantsList.item(0), "", "celldesigner:baseProductsAux");
							}

							NodeList baseProductsList = root.getElementsByTagName("celldesigner:baseProducts");
							tamanho = baseProductsList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(baseProductsList.item(0), "", "celldesigner:baseReactants");
							}

							NodeList baseProductsAuxList = root.getElementsByTagName("celldesigner:baseProductsAux");
							tamanho = baseProductsAuxList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(baseProductsAuxList.item(0), "", "celldesigner:baseProducts");
							}

							NodeList baseReactantList = root.getElementsByTagName("celldesigner:baseReactant");
							tamanho = baseReactantList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(baseReactantList.item(0), "", "celldesigner:baseProductAux");
							}

							NodeList baseProductList = root.getElementsByTagName("celldesigner:baseProduct");
							tamanho = baseProductList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(baseProductList.item(0), "", "celldesigner:baseReactant");
							}

							NodeList baseProductAuxList = root.getElementsByTagName("celldesigner:baseProductAux");
							tamanho = baseProductAuxList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(baseProductAuxList.item(i), "", "celldesigner:baseProduct");
							}

							NodeList reactantLinksList = root.getElementsByTagName("celldesigner:listOfReactantLinks");
							tamanho = reactantLinksList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(reactantLinksList.item(0), "", "celldesigner:listOfProductLinksAux");
							}

							NodeList productLinksList = root.getElementsByTagName("celldesigner:listOfProductLinks");
							tamanho = productLinksList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(productLinksList.item(0), "", "celldesigner:listOfReactantLinks");
							}

							NodeList productLinksAuxList = root.getElementsByTagName("celldesigner:listOfProductLinksAux");
							tamanho = productLinksAuxList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(productLinksAuxList.item(0), "", "celldesigner:listOfProductLinks");
							}

							NodeList reactantLinkList = root.getElementsByTagName("celldesigner:reactantLink");
							tamanho = reactantLinkList.getLength();
							for(int i=0; i<tamanho; i++){
								String reactant = ((Element) reactantLinkList.item(0)).getAttribute("reactant");
								((Element) reactantLinkList.item(0)).removeAttribute("reactant");
								((Element) reactantLinkList.item(0)).setAttribute("product", reactant);
								documentXML.renameNode(reactantLinkList.item(0), "", "celldesigner:productLinkAux");
							}

							NodeList productLinkList = root.getElementsByTagName("celldesigner:productLink");
							tamanho = productLinkList.getLength();
							for(int i=0; i<tamanho; i++){
								String product = ((Element) productLinkList.item(0)).getAttribute("product");
								((Element) productLinkList.item(0)).removeAttribute("product");
								((Element) productLinkList.item(0)).setAttribute("reactant", product);
								documentXML.renameNode(productLinkList.item(0), "", "celldesigner:reactantLink");
							}

							NodeList productLinkAuxList = root.getElementsByTagName("celldesigner:productLinkAux");
							tamanho = productLinkAuxList.getLength();
							for(int i=0; i<tamanho; i++){
								documentXML.renameNode(productLinkAuxList.item(i), "", "celldesigner:productLink");
							}

							ListOf<SpeciesReference> reactants = jsbmlModel.getReaction(s).getListOfReactants();
							ListOf<SpeciesReference> products = jsbmlModel.getReaction(s).getListOfProducts();
							jsbmlModel.getReaction(s).setListOfReactants(products);
							jsbmlModel.getReaction(s).setListOfProducts(reactants);

						}
					}

					NodeList lineList = root.getElementsByTagName("celldesigner:line");
					for(int i=0; i<lineList.getLength(); i++){
						Element line = (Element) lineList.item(i);
						if(!line.getParentNode().getNodeName().equals("celldesigner:modification")){
							line.setAttribute("width", normalized_values.get(s).toString());
							if(values.get(s) == 0) line.setAttribute("color", "cccccccc");
						}

					}

					DOMSource domSource = new DOMSource(documentXML);
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(domSource, result);

					annotation = writer.toString();

					int start_index = annotation.indexOf("<annotation>") + "<annotation>".length();
					int end_index = annotation.indexOf("</annotation>");
					String new_annotation = annotation.substring(start_index, end_index);
					new_annotation = new_annotation.replaceAll("^\\s*?<", "<");
					try {
						jsbmlModel.getReaction(s).setAnnotation(new Annotation(new_annotation));
					} catch (XMLStreamException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			writeToFile(documentCopy);
		} catch (SBMLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
	}
	
//	}
}
