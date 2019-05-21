package skcc.nexcore.client.applicationext.document;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import skcc.nexcore.client.application.document.ExcelBuilder;
import skcc.nexcore.client.applicationext.entity.UserVO;

import com.lowagie.text.DocumentException;

public class UserExcelBuilder extends ExcelBuilder {

	private List<UserVO> userList;

	public UserExcelBuilder(HttpServletResponse response, String fileName, List<UserVO> userList) {
		super(response, fileName);
		this.userList = userList;
	}

	@Override
	protected void buildImpl() throws DocumentException {
		HSSFSheet sheet = createSheet("사용자 목록");
		HSSFRow titleRow = createRow(sheet, 0);
		createCell(titleRow, 0, "아이디");
		createCell(titleRow, 1, "이름");
		createCell(titleRow, 2, "상태");
		createCell(titleRow, 3, "그룹");
		createCell(titleRow, 4, "이메일");

		int i = 1;
		for (UserVO entity : userList) {
			HSSFRow row = createRow(sheet, i);
			createCell(row, 0, entity.userId);
			createCell(row, 1, entity.userName);
			createCell(row, 2, entity.status);
			createCell(row, 3, entity.groupId);
			createCell(row, 4, entity.email);
			i++;
		}
		autoSizeColumn(sheet, 0, 4);
	}

}
