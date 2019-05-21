package skcc.nexcore.client.application.configuration.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import skcc.nexcore.client.application.configuration.ConfigurationParser;
import skcc.nexcore.client.application.configuration.cache.DataConfigurationCache;
import skcc.nexcore.client.application.util.Data;

public class DataConfigurationParser extends DefaultHandler implements ConfigurationParser {

	private String m_server;
	private Map<String, List<Data>> m_groups = new HashMap<String, List<Data>>();
	private String m_group_name = null;

	public void parse(Resource resource) throws Exception {
		m_server = null;
		m_groups.clear();
		m_group_name = null;

		InputStream is = null;
		try {
			is = resource.getInputStream();
			XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			parser.setContentHandler(this);
			parser.parse(new InputSource(is));

			DataConfigurationCache.add(resource.getFilename(), m_server, new HashMap<String, List<Data>>(m_groups));
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
		if ("datas".equals(rawName)) {
			setDatas(attributes);
		} else if ("group".equals(rawName)) {
			setGroup(rawName, attributes);
		} else if ("data".equals(rawName)) {
			setData(rawName, attributes);
		}
	}

	public void endElement(final String namespaceURI, final String localName, final String rawName) throws SAXException {
		if ("group".equals(rawName)) {
			m_group_name = null;
		} else if ("data".equals(rawName)) {
		}
	}

	// public void characters(char[] chars, int start, int leng) throws
	// SAXException {
	// for (int i = 0; i < leng; i++) {
	// buff.append(chars[start + i]);
	// }
	// }

	private void setDatas(Attributes attributes) {
		m_server = attributes.getValue("server");
		m_groups.clear();
	}

	private void setGroup(String rawName, Attributes attributes) {
		String id = attributes.getValue("id");
		List<Data> group = m_groups.get(id);
		if (group == null) {
			m_group_name = id;
			m_groups.put(id, new ArrayList<Data>());
		}
	}

	private void setData(String rawName, Attributes attributes) {
		String id = attributes.getValue("id");
		String value = attributes.getValue("value");
		if (m_group_name != null) {
			List<Data> m_group = m_groups.get(m_group_name);
			m_group.add(new Data(m_group_name, id, value));
			// m_datas.put(m_group_name + "." + id, new Data(id, value));
		}
	}

}
