package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers.XGMMLReader;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers.EscherLayoutWriter;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.exceptions.InvalidLayoutFileException;

public class ConvertXGMMLToEscher {

	@Test
	public void test() throws InvalidLayoutFileException, IOException {
		
		String xgmmlFile = ConvertXGMMLToEscher.class.getClassLoader().getResource("singleReaction.xgmml").getFile();
		XGMMLReader reader = new XGMMLReader(xgmmlFile);
		ILayout l = reader.buildLayout();
		
		
		String escherFile = "singleReaction.json";
		FileOutputStream f = new FileOutputStream(escherFile);
		BufferedOutputStream out = new BufferedOutputStream(f);
		
		EscherLayoutWriter writer = new EscherLayoutWriter(out, 2.0);
		writer.write(l);
		out.close();
	}
}
