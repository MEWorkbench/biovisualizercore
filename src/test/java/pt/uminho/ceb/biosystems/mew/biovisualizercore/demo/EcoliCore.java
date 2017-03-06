package pt.uminho.ceb.biosystems.mew.biovisualizercore.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.ErrorsException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLReader;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.io.JSBMLValidationException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.StandaloneVisualization;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers.BiGGLayoutReader;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;

public class EcoliCore {
	
	
	public static void main(String[] args) throws Exception {
		
		
		JSBMLReader reader = new JSBMLReader(EcoliCore.class.getClassLoader().getResourceAsStream("models/ecoli_core_model.xml"), "1",false);
		
		Container cont = new Container(reader);
		Set<String> met = cont.identifyMetabolitesIdByPattern(Pattern.compile(".*_b"));
		
		BiGGLayoutReader layreder = new BiGGLayoutReader(new InputStreamReader(EcoliCore.class.getClassLoader().getResourceAsStream("BiGG1/ecoli_core_model_layout.txt")));
		
		StandaloneVisualization ole = new StandaloneVisualization(cont);
		ole.addLayout("Ecoli", layreder.buildLayout());
		
		
	}

}
