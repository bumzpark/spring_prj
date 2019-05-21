package skcc.nexcore.client.application.web;

import java.util.HashMap;
import java.util.Map;

import skcc.nexcore.client.application.base.BaseAuthorityVO;

/**
 * <p>
 * HTTP POST 방식으로 수신된 요청 데이타 표현 클래스
 * </p>
 * 
 */
public class HttpParameterData implements java.io.Serializable {

	private static final long serialVersionUID = 6498155387562073824L;
	
	private Map<String, Object> parameters;

	/**
	 * 생성자
	 * 
	 * @param channelType
	 *            채널유형
	 * @param parameters
	 *            요청데이타
	 */
	public HttpParameterData() {
		parameters = new HashMap<String, Object>();
	}

	/**
	 * 데이타 조회
	 * 
	 * @return 데이타(key, value)
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void add(String key, Object value) {
		parameters.put(key, value);
	}

	/**
	 * key에 해당하는 데이타를 String으로 조회
	 * 
	 * @param key
	 *            key값
	 * @return 조회된 String 데이타
	 */
	public String getParameter(String key) {
		if (parameters == null) {
			return null;
		}
		Object obj = parameters.get(key);
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof String[]) {
			String[] array = (String[]) obj;
			if (array.length > 0) {
				return array[0];
			}
		}
		return null;
	}

	/**
	 * key에 해당하는 데이타를 String[]으로 조회
	 * 
	 * @param key
	 *            key값
	 * @return 조회된 String[] 데이타
	 */
	public String[] getParameterValues(String key) {
		if (parameters == null) {
			return null;
		}
		Object obj = parameters.get(key);
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return new String[] { (String) obj };
		} else if (obj instanceof String[]) {
			return (String[]) obj;
		}
		return null;
	}

}
