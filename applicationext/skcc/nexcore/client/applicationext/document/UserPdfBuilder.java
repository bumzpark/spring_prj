package skcc.nexcore.client.applicationext.document;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import skcc.nexcore.client.application.document.PdfBuilder;
import skcc.nexcore.client.applicationext.entity.UserVO;

import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Section;
import com.lowagie.text.Table;

public class UserPdfBuilder extends PdfBuilder {

	private List<UserVO> userList;

	public UserPdfBuilder(HttpServletResponse response, String fileName, String author, List<UserVO> userList) {
		super(response, fileName);
		super.setAuthor(author);
		super.setSubject("Employee List");
		super.setTitle("Employee List");
		super.setKeywords("Employee List");
		this.userList = userList;
	}

	@Override
	protected void buildImpl() throws DocumentException {
		add(buildChapter1());
	}

	private Chapter buildChapter1() throws DocumentException {
		Chapter chapter = createChapter("Users", 1);
		chapter.setNumberDepth(0);
		Section section1 = createSection(chapter, "User List");
		section1.add(createParagraph("XXXXXXXXXXXXXXXXXX"));
		section1.add(createParagraph("User List Table"));

		if (userList == null) {
			section1.add(createParagraph("User List Not Found."));
		} else {
			String[] columnNames = new String[] { "아이디", "이름", "상태", "그룹", "이메일" };

			int columns = columnNames.length;
			int rows = userList.size();

			Table t = createTable(columns, rows);
			section1.add(t);

			for (int i = 0; i < columns; i++) {
				createTableHeaderCell(t, columnNames[i]);
			}

			for (UserVO entity : userList) {
				createTableBodyCell(t, entity.userId);
				createTableBodyCell(t, entity.userName);
				createTableBodyCell(t, entity.status);
				createTableBodyCell(t, entity.groupId);
				createTableBodyCell(t, entity.email, Cell.ALIGN_LEFT);
			}
		}
		return chapter;
	}
}
