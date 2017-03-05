package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.readers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers.EscherLayoutWriter;

public class EscherLayoutReaderTest {


	@Test
	public void test1() throws JsonProcessingException, IOException{
		InputStream s = getClass().getClassLoader().getResourceAsStream("escher/ecoli.core.metabolism3.json");

		EscherLayoutReader r = new EscherLayoutReader(s);
		LayoutContainer cont = r.buildLayout();


		EscherLayoutWriter w = new EscherLayoutWriter(new BufferedOutputStream(System.out));
		w.write(cont);


		EscherLayoutWriter w1 = new EscherLayoutWriter(new FileOutputStream("ole2.json"));
		w1.write(cont);
	}
	
	@Test
	public void test2() throws JsonProcessingException, IOException{
		InputStream s = getClass().getClassLoader().getResourceAsStream("escher/ecoli.core.metabolism.json");

		EscherLayoutReader r = new EscherLayoutReader(s);
		LayoutContainer cont = r.buildLayout();


		EscherLayoutWriter w = new EscherLayoutWriter(new BufferedOutputStream(System.out));
		w.write(cont);


		EscherLayoutWriter w1 = new EscherLayoutWriter(new FileOutputStream("ole.json"));
		w1.write(cont);
	}
}
