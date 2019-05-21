package skcc.nexcore.client.application.configuration;

import org.springframework.core.io.Resource;

public interface ConfigurationParser {

	public abstract void parse(Resource resource) throws Exception;

}
