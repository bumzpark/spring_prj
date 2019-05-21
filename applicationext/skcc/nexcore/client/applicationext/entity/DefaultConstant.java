package skcc.nexcore.client.applicationext.entity;

import org.springframework.web.servlet.ModelAndView;

public final class DefaultConstant {

	public static ModelAndView ERROR_AUTH_PAGE = new ModelAndView("redirect:/system/error/errorRole.do");
	public static ModelAndView ERROR_DOMAIN_PAGE = new ModelAndView("redirect:/system/error/errorDomain.do");

//	public static final String SYSTEM_DOMAIN_ID = "SYSTEM";

	public static final String STATUS_NEW_HIRE = "New Hire";
	public static final String STATUS_ACTIVE = "Active";
	public static final String STATUS_INACTIVE = "Inactive";
	public static final String STATUS_TERMINATED = "Terminated";
	public static final String[] USER_STATUS_LIST = { STATUS_NEW_HIRE, STATUS_ACTIVE, STATUS_INACTIVE, STATUS_TERMINATED };
//	public static final String[] DOMAIN_STATUS_LIST = { STATUS_NEW_HIRE, STATUS_ACTIVE, STATUS_INACTIVE, STATUS_TERMINATED };

	public static final String URL_KIND_VIEW = "View";
	public static final String URL_KIND_ACTION = "Action";
	public static final String[] URL_KIND_LIST = { URL_KIND_VIEW, URL_KIND_ACTION };

	public static final String GENDER_MALE = "Male";
	public static final String GENDER_FEMALE = "Female";

	public static final int GROUP_LEVEL_SYSTEM_ADMIN = 10;
	public static final int GROUP_LEVEL_FUNCTION_ADMIN = 9;
	public static final int GROUP_LEVEL_DOMAIN_ADMIN = 5;
	public static final int GROUP_LEVEL_DOMAIN_FUNCTION_ADMIN = 4;
	public static final int GROUP_LEVEL_DOMAIN_NORMAL = 1;

//	public static boolean isSystemUser(String domainId) {
//		return SYSTEM_DOMAIN_ID.equals(domainId);
//	}

//	public static boolean isSystemAdminLevel(int level) {
//		return GROUP_LEVEL_SYSTEM_ADMIN == level;
//	}
//
//	public static boolean isFunctionAdminLevel(int level) {
//		return GROUP_LEVEL_FUNCTION_ADMIN == level;
//	}
//
//	public static boolean isSystemOrFunctionAdminLevel(int level) {
//		return isSystemAdminLevel(level) || isFunctionAdminLevel(level);
//	}

//	public static void removeSystemDomainId(List<DomainVO> domains) {
//		if (domains != null) {
//			DomainVO domain = null;
//			for (DomainVO entity : domains) {
//				if (DefaultConstant.SYSTEM_DOMAIN_ID.equals(entity.domainId)) {
//					domain = entity;
//					break;
//				}
//			}
//			if (domain != null) {
//				domains.remove(domain);
//			}
//		}
//	}
	// public static boolean isSystemAdminLevel(String level) {
	// return String.valueOf(GROUP_LEVEL_SYSTEM_ADMIN).equals(level);
	// }

}
