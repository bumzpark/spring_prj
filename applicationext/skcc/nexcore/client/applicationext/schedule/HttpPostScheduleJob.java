package skcc.nexcore.client.applicationext.schedule;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import skcc.nexcore.client.applicationext.helper.AuthCheckInterceptor;
import skcc.nexcore.client.foundation.scheduling.ScheduleJob;

public class HttpPostScheduleJob extends ScheduleJob {

	@Override
	protected void executeInternal(String jobName, Map<String, String> param) throws Exception {
		String url = param.get("url");
		String encoding = param.get("encoding");
		HttpClient client = null;
		try {
			client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			method.addParameter(new NameValuePair(AuthCheckInterceptor.INTERNAL_SESSION_KEY_NAME, AuthCheckInterceptor.INTERNAL_SESSION_KEY_VALUE));
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				//성공
			} else {
				throw new Exception(HttpStatus.getStatusText(statusCode));
			}
		} finally {
		}
	}

}
