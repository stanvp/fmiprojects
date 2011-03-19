package apps;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.DocumentUtil;
import util.HashMultiMap;
import util.HexStringUtil;
import util.Pair;

public class Statistics {
	private URL url;
	private Document mathmlDoc;
	private Document statDoc;

	public Statistics(URL url) {
		this.url = url;
		try {
			mathmlDoc = DocumentUtil.getDocument(url.openStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		statDoc = DocumentUtil.newDocument();
		statDoc.appendChild(statDoc.createElement("statistics"));
	}

	public void serialize(OutputStream out) {
		DocumentUtil.writeToStream(statDoc, out);
	}

	public void generate() {
		url();
		rows();
		fractions();
		scripts("sub");
		scripts("sup");
		count("mi", "identifiers", "identifier");
		count("mo", "operators", "operator");
		count("mn", "numbers", "number");
	}

	private void url() {
		Element element = statDoc.createElement("url");
		element.setTextContent(url.toExternalForm());
		statDoc.getChildNodes().item(0).appendChild(element);
	}
	
	private String getTypeOrValue(Node node) {
		if (node.getNodeName().equals("#text") && node.getNextSibling() != null) {
			return getTypeOrValue(node.getNextSibling());
		} else if (node.getNodeName().equals("mrow")) {
			return "row";
		} else {
			return node.getTextContent();
		}
	}
	
	private String getType(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return "string";
		}
		return "number";
	}

	private void scripts(String type) { 
		Element element = statDoc.createElement(type + "scripts");
		statDoc.getChildNodes().item(0).appendChild(element);

		NodeList nodes = mathmlDoc.getElementsByTagName("m" + type);

		element.setAttribute("total", "" + nodes.getLength());	
		
		HashMultiMap<Pair<String, String>, Node> group = new HashMultiMap<Pair<String, String>, Node>();

		for (int i = 0; i < nodes.getLength(); i++) {
			NodeList childs = nodes.item(i).getChildNodes();
			
			Pair<String, String> pair = Pair.of(
					getTypeOrValue(childs.item(0)),
					getTypeOrValue(childs.item(1))
					);
			
			group.putMulti(pair, nodes.item(i));
		}

		for (Entry<Pair<String, String>, List<Node>> entry : group.entrySet()) {
			Element subElement = statDoc.createElement(type + "script");
			subElement.setAttribute("total", "" + entry.getValue().size());
			
			Element subFirst = statDoc.createElement("first");
			subFirst.setTextContent(entry.getKey().first);
			subFirst.setAttribute("type", getType(entry.getKey().first));
			
			Element subSecond = statDoc.createElement("second");
			subSecond.setTextContent(entry.getKey().second);
			subSecond.setAttribute("type", getType(entry.getKey().second));
			
			subElement.appendChild(subFirst);
			subElement.appendChild(subSecond);
			
			element.appendChild(subElement);
		}
	}	

	private void rows() {
		Element element = statDoc.createElement("rows");
		statDoc.getChildNodes().item(0).appendChild(element);

		NodeList nodes = mathmlDoc.getElementsByTagName("mrow");

		element.setAttribute("total", "" + nodes.getLength());
	}

	private void fractions() {
		Element element = statDoc.createElement("fractions");
		statDoc.getChildNodes().item(0).appendChild(element);
		element.setAttribute("total", ""
				+ mathmlDoc.getElementsByTagName("mfrac").getLength());
	}

	private void count(String tagName, String mainNode, String subNode) {
		Element element = statDoc.createElement(mainNode);
		statDoc.getChildNodes().item(0).appendChild(element);

		NodeList nodes = mathmlDoc.getElementsByTagName(tagName);

		element.setAttribute("total", "" + nodes.getLength());

		HashMultiMap<String, Node> group = new HashMultiMap<String, Node>();

		for (int i = 0; i < nodes.getLength(); i++) {
			String key = nodes.item(i).getTextContent();
			
			if (!HexStringUtil.isPureAscii(key)) {
				key = String.format("\\x%s", HexStringUtil.toHex(key));
			}
			
			group.putMulti(key, nodes.item(i));
		}

		for (Entry<String, List<Node>> entry : group.entrySet()) {
			Element subElement = statDoc.createElement(subNode);
			subElement.setAttribute("total", "" + entry.getValue().size());
			subElement.setTextContent(entry.getKey());
			element.appendChild(subElement);
		}
	}

}