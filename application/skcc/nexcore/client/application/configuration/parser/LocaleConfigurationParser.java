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
import skcc.nexcore.client.application.configuration.cache.LocaleConfigurationCache;
import skcc.nexcore.client.application.util.LocaleObject;

public class LocaleConfigurationParser extends DefaultHandler implements ConfigurationParser {

	private Map<String, LocaleObject> m_locales = new HashMap<String, LocaleObject>();

	public void parse(Resource resource) throws Exception {
		m_locales.clear();

		InputStream is = null;
		try {
			is = resource.getInputStream();
			XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			parser.setContentHandler(this);
			parser.parse(new InputSource(is));
			
			LocaleConfigurationCache.add(resource.getFilename(), new HashMap<String, LocaleObject>(m_locales));
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
		if ("locales".equals(rawName)) {
			m_locales.clear();
		} else if ("locale".equals(rawName)) {
			setLocale(rawName, attributes);
		}
	}

	public void endElement(final String namespaceURI, final String localName, final String rawName) throws SAXException {
		if ("locale".equals(rawName)) {
		}
	}

	private void setLocale(String rawName, Attributes attributes) {
		// <locale id="en" name="English" default="true"/>
		String id = attributes.getValue("id");
		if (m_locales.containsKey(id)) {
			throw new RuntimeException("Label(" + id + ") is duplicated.");
		}
		LocaleObject lo = new LocaleObject();
		lo.id = id;
		lo.name = attributes.getValue("name");
		lo.dateFormat = attributes.getValue("dateFormat");
		lo.isDefault = "true".equals(attributes.getValue("default"));
		m_locales.put(id, lo);
	}

}
