package skcc.nexcore.client.applicationext.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.UserDAO;
import skcc.nexcore.client.applicationext.entity.UserVO;

@Transactional(readOnly = true)
public class UserService extends BaseService {

	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public List<UserVO> selectAll() throws SQLException {
		return userDAO.selectAll();
	}

	public UserVO select(String userId) throws SQLException {
		return userDAO.select(userId);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(UserVO entity) throws SQLException {
		return userDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int update(UserVO entity) throws SQLException {
		return userDAO.update(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int updatePassword(String userId, String password, String passwordReset) throws SQLException {
		return userDAO.updatePassword(userId, password, passwordReset);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int updateStatus(String userId, String status) throws SQLException {
		return userDAO.updateStatus(userId, status);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int updateMyInfo(UserVO entity) throws SQLException {
		return userDAO.updateMyInfo(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String userId) throws SQLException {
		return userDAO.delete(userId);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String[] userIds) throws SQLException {
		int row = 0;
		if (userIds != null) {
			for (String id : userIds) {
				row += userDAO.delete(id);
			}
		}
		return row;
	}

}
