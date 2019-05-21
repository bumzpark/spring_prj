package skcc.nexcore.client.application.util;

import skcc.nexcore.client.application.base.BaseAuthorityVO;

public final class AuthorityHelper {

	public static boolean isSelect(BaseAuthorityVO entity) {
		if (entity != null) {
			return entity.selectEnabled;
		}
		return false;
	}

	public static boolean isInsert(BaseAuthorityVO entity) {
		if (entity != null) {
			return entity.insertEnabled;
		}
		return false;
	}

	public static boolean isUpdate(BaseAuthorityVO entity) {
		if (entity != null) {
			return entity.updateEnabled;
		}
		return false;
	}

	public static boolean isDelete(BaseAuthorityVO entity) {
		if (entity != null) {
			return entity.deleteEnabled;
		}
		return false;
	}

	public static boolean isServer(BaseAuthorityVO entity) {
		if (entity != null) {
			return entity.serverEnabled;
		}
		return false;
	}

}
