package skcc.nexcore.client.application.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.core.io.Resource;

public class ConfigurationLoader implements BeanNameAware {

	private Log logger = LogFactory.getLog(getClass());

	private String beanName;
	private boolean polling;
	private long pollingInterval;
	private Resource locations[];
	private ConfigurationParser parser;
	private List<ConfigurationFilePoller> filePollers;

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getName() {
		return beanName;
	}

	/**
	 * @param polling
	 *            the polling to set
	 */
	public void setPolling(boolean polling) {
		this.polling = polling;
	}

	/**
	 * @param pollingInterval
	 *            the pollingInterval to set
	 */
	public void setPollingInterval(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	/**
	 * @param parser
	 *            the parser to set
	 */
	public void setParser(ConfigurationParser parser) {
		this.parser = parser;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void init() throws Exception {
		logger.info(beanName + " init.");
		if (locations != null) {
			for (Resource location : locations) {
				try {
					parser.parse(location);
					logger.info(beanName + " loading from [" + location + "]");
					startFilePolling(location);
				} catch (Exception e) {
					logger.error(beanName + " loading fail. from [" + location + "]", e);
					throw e;
				}
			}
		}
	}

	public void destroy() {
		logger.info(beanName + " destroy.");
		if (filePollers != null) {
			for (ConfigurationFilePoller poller : filePollers) {
				poller.pleaseStop();
			}
		}
	}

	public void modified(Resource location) {
		logger.info(beanName + " modified. " + location);
		try {
			parser.parse(location);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startFilePolling(Resource location) throws IOException {
		if (!polling) {
			logger.info(beanName + " does not active configuration polling service because polling option is off in file. " + location);
			return;
		}
		if ("file".equalsIgnoreCase(location.getURL().getProtocol())) {
			logger.info(beanName + " does not active configuration polling service because resource protocol(" + location.getURL().getProtocol() + ") is not file. " + location);
			return;
		}
		if (filePollers == null) {
			filePollers = new ArrayList<ConfigurationFilePoller>();
		}
		ConfigurationFilePoller poller = new ConfigurationFilePoller(pollingInterval, location, this);
		poller.start();
		filePollers.add(poller);
		logger.info(beanName + " do active configuration polling service in file. " + location);
	}
}
