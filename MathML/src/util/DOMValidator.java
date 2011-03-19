package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.DefaultHandler;

public final class DOMValidator {
	public static void validate(File x) {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(true); // Default is false
			
			DocumentBuilder b = f.newDocumentBuilder();
			
			// ErrorHandler h = new DefaultHandler();
			ErrorHandler h = new MyErrorHandler();
			b.setErrorHandler(h);
			Document d = b.parse(x);
		} catch (ParserConfigurationException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	
	public static void validateDTD(File x, String dtdPath) {
		try {
			TransformerFactory tf = TransformerFactory
			    .newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(
			    OutputKeys.DOCTYPE_SYSTEM, dtdPath);
			
			String transformedPath = x.getAbsolutePath() + "_tmp";
			File transformedFile = new File(transformedPath);
			try {
				transformer.transform(new StreamSource(
				    x.getAbsolutePath()), new StreamResult(new FileOutputStream(transformedFile)));
			} catch (TransformerException e) {
				e.printStackTrace();
			}			
			
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(true); // Default is false
			DocumentBuilder b = f.newDocumentBuilder();
			
			// ErrorHandler h = new DefaultHandler();
			ErrorHandler h = new MyErrorHandler();
			b.setErrorHandler(h);
			
			Document d = b.parse(transformedFile);
			transformedFile.delete();
		} catch (ParserConfigurationException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}		
	
	public static void validateXScheme(File x, String xschemePath) {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(false);
			f.setNamespaceAware(true);
			
			SchemaFactory schemaFactory = 
			    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

			f.setSchema(schemaFactory.newSchema(
			    new Source[] {new StreamSource(xschemePath)}));			
			
			DocumentBuilder b = f.newDocumentBuilder();
			
			// ErrorHandler h = new DefaultHandler();
			ErrorHandler h = new MyErrorHandler();
			b.setErrorHandler(h);
			Document d = b.parse(x);
		} catch (ParserConfigurationException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}	

	private static class MyErrorHandler implements ErrorHandler {
		public void warning(SAXParseException e) throws SAXException {
			System.out.println("Warning: ");
			printInfo(e);
		}

		public void error(SAXParseException e) throws SAXException {
			System.out.println("Error: ");
			printInfo(e);
		}

		public void fatalError(SAXParseException e) throws SAXException {
			System.out.println("Fattal error: ");
			printInfo(e);
		}

		private void printInfo(SAXParseException e) {
			System.out.println("   Public ID: " + e.getPublicId());
			System.out.println("   System ID: " + e.getSystemId());
			System.out.println("   Line number: " + e.getLineNumber());
			System.out.println("   Column number: " + e.getColumnNumber());
			System.out.println("   Message: " + e.getMessage());
		}
	}
}
