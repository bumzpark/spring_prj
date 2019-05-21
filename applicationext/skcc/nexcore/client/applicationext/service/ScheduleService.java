package skcc.nexcore.client.applicationext.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.ScheduleDAO;
import skcc.nexcore.client.applicationext.entity.ScheduleVO;

@Transactional(readOnly = true)
public class ScheduleService extends BaseService {

	private ScheduleDAO scheduleDAO;

	public void setScheduleDAO(ScheduleDAO scheduleDAO) {
		this.scheduleDAO = scheduleDAO;
	}

	public List<ScheduleVO> selectAll() {
		return scheduleDAO.selectAll();
	}

	public ScheduleVO select(String jobId) throws SQLException {
		return scheduleDAO.select(jobId);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(ScheduleVO entity) throws SQLException {
		return scheduleDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int update(ScheduleVO entity) throws SQLException {
		return scheduleDAO.update(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String[] jobIds) throws SQLException {
		int row = 0;
		if (jobIds != null) {
			for (String jobId : jobIds) {
				row += scheduleDAO.delete(jobId);
			}
		}
		return row;
	}
}
