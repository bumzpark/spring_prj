package skcc.nexcore.client.application.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

public class ConfigurationFilePoller extends Thread {

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * A Default interval time.
	 */

	public final int DEFAULT_INTERVAL = 60000;

	/**
	 * Determines if the thread should continue to run or be allowed to die.
	 */

	private boolean toContinue = true;

	/**
	 * Holds the lastModified timestamp of the file being watched. This value is
	 * then compared with the files actual lastModified timestamp.
	 */

	private long lastModified;

	/**
	 * The sleep interval for the thread.
	 */

	private long interval;

	/**
	 * Polling File
	 */
	private Resource resource;
	private File file;

	private ConfigurationLoader loader;

	/**
	 * Sets the polling interval to the passed in value. This value is passed in
	 * from the manager object which gets the value from the
	 * Configuration.properties file.
	 * 
	 * @param _interval
	 *            The sleep interval that the thread should sleep for when
	 *            looping over the file checking functionality.
	 * @throws IOException
	 */

	public ConfigurationFilePoller(long _interval, Resource _resource, ConfigurationLoader loader) throws IOException {
		interval = _interval;
		resource = _resource;
		file = resource.getFile();
		this.loader = loader;
	}

	public void pleaseStop() {
		this.toContinue = false;
	}

	/**
	 * implementation of the Threads run method. This method is called to start
	 * the running of the new thread. This thread loops based on the toContinue
	 * variable checking the lastModified timestamp of the file. If the
	 * timestamp changes the poller notifies the configuration manager. This
	 * thread will sleep in each loop iteration based on the sleep value set in
	 * the Configuration.properties file.
	 */

	public void run() {
		// File checkFile = new File(configurationURI);
		lastModified = file.lastModified();
		while (toContinue) {
			if (lastModified != file.lastModified()) {
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("Configuration file modified [");
					sb.append(loader.getName());
					sb.append("] [");
					sb.append(file.getAbsolutePath());
					sb.append("]");
					logger.info(sb.toString());

					loader.modified(resource);
					lastModified = file.lastModified();
				} catch (Exception e) {
					StringBuilder sb = new StringBuilder();
					sb.append("Configuration reload error [");
					sb.append(loader.getName());
					sb.append("] [");
					sb.append(file.getAbsolutePath());
					sb.append("]");
					logger.error(sb.toString(), e);
					// break;
				}
			}

			try {
				Thread.sleep(interval);
			} catch (InterruptedException ie) {
				continue;
			}
		}
	}
}
