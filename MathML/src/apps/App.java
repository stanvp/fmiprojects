package apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import util.DOMValidator;

public class App {
	public static void main(String[] args) throws MalformedURLException, FileNotFoundException, URISyntaxException {
		String basePath = App.class.getResource("/").getFile() + "../data";
		String outputPath = basePath + "/output";
		String inputPath = basePath + "/input";
		
		String[] inputChildren = new File(inputPath).list();
		
		System.out.println("= Generating statistics =");

		for (String filename : inputChildren) {
			if (filename.indexOf(".xml") == -1) continue;
			System.out.println("* Processing " + filename);
			Statistics statistics = new Statistics(new File(inputPath + "/" + filename).toURI().toURL());
			statistics.generate();
			statistics.serialize(
				new FileOutputStream(outputPath + "/Statistics" + filename)
			);
		}
		
		System.out.println("Statistics is generated for all mathml documents!");
		
		System.out.println("\n= Validation =");
		
		
		String[] outputChildren = new File(outputPath).list();
		for (String filename : outputChildren) {
			if (filename.indexOf(".xml") == -1) continue;
			System.out.println("* DTD Validating " + filename);
			
			DOMValidator.validateDTD(
					new File(outputPath + "/" + filename), App.class.getResource("/resources/Statistics.dtd").getFile()
			);
			
			System.out.println("* XScheme Validating " + filename);
			
			DOMValidator.validateXScheme(
					new File(outputPath + "/" + filename), App.class.getResource("/resources/Statistics.xsd").getFile()
			);			
		}
		
		System.out.println("Validation finished!");
		
	}
}
