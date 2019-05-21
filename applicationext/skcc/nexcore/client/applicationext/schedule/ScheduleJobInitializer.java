package skcc.nexcore.client.applicationext.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import skcc.nexcore.client.applicationext.entity.ScheduleVO;
import skcc.nexcore.client.applicationext.service.ScheduleService;
import skcc.nexcore.client.foundation.scheduling.ScheduleJobManager;
import skcc.nexcore.client.foundation.util.BytesTokenizer;

public class ScheduleJobInitializer {

	protected final Log logger = LogFactory.getLog(getClass());

	private ScheduleJobManager scheduleJobManager;
	private ScheduleService scheduleService;

	public void setScheduleJobManager(ScheduleJobManager scheduleJobManager) {
		this.scheduleJobManager = scheduleJobManager;
	}

	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void init() {
		logger.info("ScheduleJobInitializer init.");
		List<ScheduleVO> list = scheduleService.selectAll();
		if (list != null) {
			for (ScheduleVO entity : list) {
				try {
					add(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void add(ScheduleVO entity) throws Exception {
		if ("true".equalsIgnoreCase(entity.jobEnabled) || "Y".equalsIgnoreCase(entity.jobEnabled)) {
			Map<String, String> params = makeParams(entity);
			scheduleJobManager.add(entity.jobId, entity.jobCronExpression, HttpPostScheduleJob.class, params);
		}
	}

	public void remove(String jobId) throws Exception {
		scheduleJobManager.remove(jobId);
	}

	private Map<String, String> makeParams(ScheduleVO entity) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", entity.jobUrl);
		params.put("encoding", System.getProperty("file.encoding"));

		if (entity.jobParams != null) {
			BytesTokenizer bt = new BytesTokenizer(entity.jobParams.getBytes(), "|".getBytes());
			while (bt.hasMoreElements()) {
				String str = bt.nextToString();
				int index = str.indexOf("=");
				if (index > 0) {
					params.put(str.substring(0, index), str.substring(index + 1));
				}
			}
		}

		return params;
	}

	public void destroy() {
		logger.info("ScheduleJobInitializer destroy.");

	}

}
