package fmiprojects.weather.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public final class DocumentUtil {
	public static Document getDocument(InputStream in) throws IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		dbf.setNamespaceAware(true);

		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}

		Document document;

		try {
			document = db.parse(in);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}

		return document;
	}

	public static Document newDocument() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		dbf.setNamespaceAware(true);

		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}

		return db.newDocument();
	}

	public static void writeToStream(Document document, OutputStream out) {
		OutputFormat format = new OutputFormat(document);
		format.setIndent(1);
		format.setIndenting(true);
		
		XMLSerializer serializer = new XMLSerializer(out, format);

		try {
			serializer.serialize(document);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toString(Document document, OutputStream out) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		writeToStream(document, outputStream);

		try {
			return outputStream.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
