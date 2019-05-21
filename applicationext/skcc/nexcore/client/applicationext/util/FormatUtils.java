package skcc.nexcore.client.applicationext.util;

import java.sql.Timestamp;

import skcc.nexcore.client.foundation.util.DateTimeUtils;
import skcc.nexcore.client.foundation.util.StringUtils;

public class FormatUtils {

	public static void main(String[] args) {
		System.out.println("FormatUtils.formatSSN(111111111) : " + FormatUtils.formatSSN("111111111"));
	}

	public static String xml(String str) {
		str = StringUtils.replaceStr(str, "&", "&amp;");
		str = StringUtils.replaceStr(str, "<", "&lt;");
		str = StringUtils.replaceStr(str, ">", "&gt;");
		return str;
	}

	public static String dateToString(long mills) {
		return dateToString(DateTimeUtils.getFormatString(new Timestamp(mills), "yyyyMMddHHmmssSSS"));
	}

	public static String dateToString(String str) {
		if (str == null) {
			return "";
		}
		int len = str.length();
		if (len == 8) {
			return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8);
		}
		if (len == 10) {
			return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8) + " " + str.substring(8, 10);
		}
		if (len == 12) {
			return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12);
		}
		if (len == 14) {
			return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
		}
		if (len == 16) {
			return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14) + "," + str.substring(14, 16);
		}
		if (len >= 17) {
			return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14) + "," + str.substring(14, 17);
		}
		return str;
	}

	public static String timeToString(String str) {
		if (str == null) {
			return "";
		}
		int len = str.length();
		if (len == 2) {
			return str.substring(0, 2);
		}
		if (len == 4) {
			return str.substring(0, 2) + ":" + str.substring(2, 4);
		}
		if (len == 6) {
			return str.substring(0, 2) + ":" + str.substring(2, 4) + ":" + str.substring(4, 6);
		}
		return str;
	}

	public static String stringToDate(String str) {
		if (str == null) {
			return "";
		}
		str = StringUtils.replaceStr(str, "/", "");
		str = StringUtils.replaceStr(str, ":", "");
		str = StringUtils.replaceStr(str, " ", "");
		return str;
	}

	public static String formatSSN(String value) {
		// ###-**-*###
		if (value == null) {
			return "";
		}
		String result = "";
		int len = value.length();
		for (int i = 0; i < len; i++) {
			switch (i) {
			case 3:
				result += "-*";
				break;
			case 4:
				result += "*";
				break;
			case 5:
				result += "-*";
				break;
			default:
				result += value.substring(i, i + 1);
			}
		}
		return result;
	}

}
