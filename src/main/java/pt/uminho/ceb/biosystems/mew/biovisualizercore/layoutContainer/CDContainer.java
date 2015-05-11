package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

/**
 * Container for layouts generated from CellDesigner. It's and extension
 * to {@link LayoutContainer}, the difference is the additional
 * {@link SBMLDocument} that is saved so it's possible to export it
 * with flux distribution information.
 * 
 * @author Alberto Noronha
 *
 */
public class CDContainer extends LayoutContainer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SBMLDocument document;
	private String sbmlString;

	public CDContainer(Map<String, INodeLay> nodes,
			Map<String, IReactionLay> reactions, SBMLDocument document) {
		super(nodes, reactions);
		
		this.document = document;
	}
	
	
	public CDContainer(Map<String, INodeLay> nodes, Map<String,IReactionLay> reactions, String sbmlString){
		
		super(nodes, reactions);
		this.sbmlString = sbmlString;
	}
	
	public SBMLDocument getDocument() {

		if(document == null){
			SBMLReader reader = new SBMLReader();
			try {

				document = reader.readSBMLFromString(getSbmlString());
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return document;
	}

	private String constructSbmlString(){
		ByteArrayOutputStream barr = new ByteArrayOutputStream();
		try {
			SBMLWriter.write(document, barr, "OptFlux", "3.0");
			sbmlString = barr.toString("UTF-8");
		} catch (SBMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return sbmlString;
		
	}
	
	public String getSbmlString(){
		if(sbmlString == null)
			constructSbmlString();
		return sbmlString;
	}
	
	public CDContainer clone(){
		
		Map<String,INodeLay> n = new HashMap<String, INodeLay>(nodes);
		Map<String, IReactionLay> r = new HashMap<String, IReactionLay>(reactions);
		String sbml = this.sbmlString;
		CDContainer ncontainer = new CDContainer(n, r, sbml);
		return ncontainer;
		
	}
}
