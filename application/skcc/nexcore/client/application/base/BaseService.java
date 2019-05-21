package skcc.nexcore.client.application.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <p>
 * 비지니스 로직을 수행하는 서비스의 최상위 클래스
 * </p>
 */
@Transactional(readOnly = true)
public class BaseService {
	protected final Log logger = LogFactory.getLog(getClass());
}