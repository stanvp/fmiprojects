package test;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import util.DOMValidator;
import apps.Statistics;

public class StatisticsTest {
	@Test
	public void testGenerate() {
		System.out.println("\n--testGenerate--");
		URL url = Statistics.class.getResource("/resources/RationalIntegral.xml");
		
		Statistics statistics = new Statistics(url);
		statistics.generate();
		statistics.serialize(System.out);
		System.out.print("\n");
	}
	
	@Test
	public void testDTDValide() {
		System.out.println("\n-- testDTDValide --");
		URL url = Statistics.class.getResource("/resources/ReferenceStatistics.xml");

		DOMValidator.validate(new File(url.getFile()));
		System.out.println("DTD Validation finished!");		
	}
	
	@Test
	public void testXSDValide(){
		System.out.print("\n-- testXSDValide --");

		DOMValidator.validateXScheme(
			new File(Statistics.class.getResource("/resources/ReferenceStatistics.xml").getFile()), 
			Statistics.class.getResource("/resources/Statistics.xsd").getFile()
		);
		System.out.println("XSD Validation finished!");
	}
}