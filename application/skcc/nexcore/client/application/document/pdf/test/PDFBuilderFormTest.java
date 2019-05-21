package skcc.nexcore.client.application.document.pdf.test;

import java.awt.Color;
import java.io.IOException;

import skcc.nexcore.client.application.document.pdf.PDFBuilder;
import com.lowagie.text.Chapter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.TextField;

public class PDFBuilderFormTest extends PDFBuilder {

	public PDFBuilderFormTest(String fileName) {
		super(null, fileName);
	}

	@Override
	protected void buildImpl() throws DocumentException, IOException {
		buildChapter1();
//		add(buildChapter1());
	}

	private Chapter buildChapter1() throws DocumentException, IOException {
		Chapter chapter = createChapter("Employees", 1);
		
		chapter.setNumberDepth(0);
		Section section1 = createSection(chapter, "Employees List");
		section1.add(createParagraph("XXXXXXXXXXXXXXXXXX"));
		section1.add(createParagraph("Employee List Table"));
		String[] columnNames = new String[] { "ID", "NAME", "SOCIAL SECURITY NUMBER", "E-MAIL", "TEMP" };
		int columns = columnNames.length;
		int rows = 5;

		
		TextField tf2 = new TextField(writer, new Rectangle(400, 720, 520, 810), "dog");
		tf2.setBackgroundColor(Color.YELLOW);
		tf2.setBorderColor(Color.RED);
		tf2.setBorderWidth(2);
		tf2.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
		tf2.setText("Quick brown fox jumps over the lazy dog");
		tf2.setAlignment(Element.ALIGN_RIGHT);
		tf2.setOptions(TextField.MULTILINE | TextField.REQUIRED);
		tf2.setRotation(90);

		writer.addAnnotation(tf2.getTextField());

		PdfFormField employees = createFormField("employees");
		
		PdfPTable t = createPTable(columns);
		section1.add(t);
		
		for (int i = 0; i < columns; i++) {
			createTableHeaderCell(t, columnNames[i]);
		}

		for (int i = 0; i < rows; i++) {
//			 t.addCell("id_" + i);
//			 t.addCell("name_" + i);
//			 t.addCell("ssn_" + i);
//			 t.addCell("email_" + i);

			createTableBodyCell(t, "id_" + i);
			createTableBodyCell(t, "name_" + i);
			createTableBodyCell(t, "ssn_" + i);
			createTableBodyCell(t, "email_" + i);
			createRegisterField(t, employees, "temp_" + i);
		}

		Section section2 = createSection(chapter, "Companys List");

		PdfFormField companys = createFormField("companys");
		
		PdfPTable tt = createPTable(columns);
		section2.add(tt);
		
		for (int i = 0; i < columns; i++) {
			createTableHeaderCell(tt, columnNames[i]);
		}

		for (int i = 0; i < rows; i++) {
//			 t.addCell("id_" + i);
//			 t.addCell("name_" + i);
//			 t.addCell("ssn_" + i);
//			 t.addCell("email_" + i);

			createTableBodyCell(tt, "id_" + i);
			createTableBodyCell(tt, "name_" + i);
			createTableBodyCell(tt, "ssn_" + i);
			createTableBodyCell(tt, "email_" + i);
			createRegisterField(tt, companys, "temp_" + i);
		}

		
		add(chapter);
		addAnnotation(employees);
		addAnnotation(companys);

		return chapter;
	}

	public static void main(String[] args) throws DocumentException, IOException {
		PDFBuilderFormTest t = new PDFBuilderFormTest("C:/Temp/employees_register.pdf");
		t.setAuthor("jihwan.cha");
		t.setSubject("PDF Example");
		t.setTitle("PDF Example");
		t.setKeywords("PDF Example");
		t.build();
	}

}
