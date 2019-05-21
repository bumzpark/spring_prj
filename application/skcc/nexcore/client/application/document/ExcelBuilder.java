package skcc.nexcore.client.application.document;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lowagie.text.DocumentException;

public abstract class ExcelBuilder {

	private HttpServletResponse response;
	private String fileName;

	private HSSFWorkbook workbook;

	public ExcelBuilder(HttpServletResponse response, String fileName) {
		this.response = response;
		this.fileName = fileName;
	}

	public void build() throws DocumentException, IOException {
		response.setContentType("application/vnd.ms-excel; charset=euc-kr");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		workbook = new HSSFWorkbook();
		buildImpl();
		
		OutputStream ostream = response.getOutputStream();
		workbook.write(ostream);
		ostream.close();
	}

	protected abstract void buildImpl() throws DocumentException;

	protected HSSFSheet createSheet(String name) {
		HSSFSheet sheet = workbook.createSheet(name);
		return sheet;
	}

	protected HSSFRow createRow(HSSFSheet sheet, int index) {
		HSSFRow row = sheet.createRow(index);
		return row;
	}

	protected HSSFCell createCell(HSSFRow row, int index) {
		HSSFCell cell = row.createCell((short) index);
		return cell;
	}

	protected HSSFCell createCell(HSSFRow row, int index, String value) {
		HSSFCell cell = row.createCell((short) index);
		cell.setCellValue(new HSSFRichTextString(value));
		return cell;
	}

	protected void autoSizeColumn(HSSFSheet sheet, int index) {
		sheet.autoSizeColumn((short) index);
	}

	protected void autoSizeColumn(HSSFSheet sheet, int start, int end) {
		for (int i = start; i <= end; i++) {
			sheet.autoSizeColumn((short) i);
		}
	}

}
