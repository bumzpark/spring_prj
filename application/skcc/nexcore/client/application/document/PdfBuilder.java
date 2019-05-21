package skcc.nexcore.client.application.document;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;

public abstract class PdfBuilder {

	private HttpServletResponse response;
	private String fileName;

	private Document document;
	protected PdfWriter writer;
	private BaseFont bf;

	private Font chapterFont;
	private Font sectionFont;
	private Font headerFont;
	private Font defaultFont;

	private String author;
	private String subject;
	private String title;
	private String keywords;

	public PdfBuilder(HttpServletResponse response, String fileName) {
		this.response = response;
		this.fileName = fileName;

		// bf = BaseFont.createFont(fontBaseName, fontBaseEncoding,
		// BaseFont.NOT_EMBEDDED);
		// chapterFont = new Font(bf, 18, Font.BOLDITALIC, Color.black);
		// sectionFont = new Font(bf, 16, Font.BOLDITALIC, Color.black);
		// defaultFont = new Font(bf, 10, Font.NORMAL, Color.black);
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void build() throws DocumentException, IOException {
		// response.setContentType("application/vnd.ms-excel; charset=euc-kr");
		response.setContentType("application/pdf; charset=euc-kr");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		OutputStream ostream = response.getOutputStream();

		initFont("HYGoThic-Medium", "UniKS-UCS2-H");

		document = new Document(PageSize.A4, 50, 50, 50, 50);
		writer = PdfWriter.getInstance(document, ostream);
		document.addAuthor(author);
		document.addSubject(subject);
		document.addTitle(title);
		document.addKeywords(keywords);
		document.open();

		buildImpl();

		document.close();
		writer.flush();
		writer.close();
		ostream.close();
	}

	protected abstract void buildImpl() throws DocumentException;

	protected void add(Element element) throws DocumentException {
		document.add(element);
	}

	protected void initFont(String fontBaseName, String fontBaseEncoding) throws DocumentException, IOException {
		bf = BaseFont.createFont(fontBaseName, fontBaseEncoding, BaseFont.NOT_EMBEDDED);

		chapterFont = new Font(bf, 18, Font.BOLD, Color.BLACK);
		sectionFont = new Font(bf, 16, Font.BOLD, Color.BLACK);
		headerFont = new Font(bf, 12, Font.BOLD, Color.BLACK);
		defaultFont = new Font(bf, 10, Font.NORMAL, Color.BLACK);
	}

	protected Chapter createChapter(String text, int index) {
		Chapter chapter = new Chapter(new Paragraph(text, chapterFont), index);
		return chapter;
	}

	protected Section createSection(Chapter chapter, String text) {
		Paragraph paragraph = new Paragraph(text, sectionFont);
		Section section = chapter.addSection(paragraph);
		return section;
	}

	protected Paragraph createParagraph(String text) {
		return createParagraph(text, defaultFont);
	}

	protected Paragraph createParagraph(String text, Font font) {
		if (font == null) {
			font = defaultFont;
		}
		Paragraph p = new Paragraph(text, font);
		return p;
	}

	protected Table createTable(int columns, int rows) throws BadElementException {
		Table t = new Table(columns, rows);
		t.setBorderColor(Color.BLACK);
		t.setPadding(5);
		t.setSpacing(0);
		t.setBorderWidth(1);
		return t;
	}

	protected Cell createTableHeaderCell(Table table, String text) throws BadElementException {
		Cell cell = new Cell(createParagraph(text, headerFont));
		cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setBorderColor(Color.BLACK);
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		// cell.setUseBorderPadding(true);
		table.addCell(cell);
		return cell;
	}

	protected Cell createTableBodyCell(Table table, String text) throws BadElementException {
		return createTableBodyCell(table, text, Cell.ALIGN_CENTER);
	}

	protected Cell createTableBodyCell(Table table, String text, int halign) throws BadElementException {
		Cell cell = new Cell(createParagraph(text));
		cell.setBorderColor(Color.BLACK);
		cell.setHorizontalAlignment(halign);
		cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		table.addCell(cell);
		return cell;
	}

	protected PdfFormField createFormField(String name) {
		PdfFormField formField = PdfFormField.createEmpty(writer);
		formField.setFieldName(name);
		return formField;
	}

	protected void addAnnotation(PdfAnnotation anno) {
		writer.addAnnotation(anno);
	}

}
