package skcc.nexcore.client.application.configuration.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import skcc.nexcore.client.application.configuration.ConfigurationParser;
import skcc.nexcore.client.application.configuration.cache.MessageConfigurationCache;

public class MessageConfigurationParser extends DefaultHandler implements ConfigurationParser {

	private String m_locale;
	private Map<String, String> m_messages = new HashMap<String, String>();

	public void parse(Resource resource) throws Exception {
		m_locale = null;
		m_messages.clear();

		InputStream is = null;
		try {
			is = resource.getInputStream();
			XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			parser.setContentHandler(this);
			parser.parse(new InputSource(is));

			MessageConfigurationCache.add(resource.getFilename(), m_locale, new HashMap<String, String>(m_messages));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void startElement(final String namespaceURI, final String localName, final String rawName, final Attributes attributes) throws SAXException {
		if ("messages".equals(rawName)) {
			setMessages(attributes);
		} else if ("message".equals(rawName)) {
			setMessage(rawName, attributes);
		}
	}

	public void endElement(final String namespaceURI, final String localName, final String rawName) throws SAXException {
		if ("message".equals(rawName)) {
			// String value = buff.toString().trim();
			// m_labels.put(lastId, value);
			// buff.setLength(0);
		}
	}

	// public void characters(char[] chars, int start, int leng) throws
	// SAXException {
	// for (int i = 0; i < leng; i++) {
	// buff.append(chars[start + i]);
	// }
	// }

	private void setMessages(Attributes attributes) {
		m_locale = attributes.getValue("locale");
		m_messages.clear();
	}

	private void setMessage(String rawName, Attributes attributes) {
		String id = attributes.getValue("id");
		if (m_messages.containsKey(id)) {
			throw new RuntimeException("Message(" + id + ") is duplicated.");
		}
		String value = attributes.getValue("value");
		m_messages.put(id, value);
		// lastId = id;
	}

}
