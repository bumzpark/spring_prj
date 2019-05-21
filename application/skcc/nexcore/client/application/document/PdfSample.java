package skcc.nexcore.client.application.document;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class PdfSample {
	private static PdfSample sample = null;
	private Document document = null;
	private PdfWriter writer = null;
	// private PdfPTable table = null;
	// private PdfPCell cell = null;
	private BaseFont bf = null;
	private Font defaultFont = null;

	public static PdfSample getInstance() {
		if (sample == null) {
			synchronized (PdfSample.class) {
				if (sample == null)
					sample = new PdfSample();
			}
		}
		return sample;
	}

	public PdfSample() {
		try {
			document = new Document(PageSize.A4, 50, 50, 50, 50);
			bf = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED);
			defaultFont = new Font(bf, 10, Font.NORMAL, Color.black);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Paragraph getParagraph(String text) {
		return new Paragraph(text, defaultFont);
	}

	private void toPdf(String fileName) {
		try {
			if (fileName == null) {
				return;
			}
			writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.addAuthor("jihwan.cha");
			document.addSubject("PDF Example");
			document.addTitle("PDF Example");
			document.addKeywords("PDF Example");
			document.open();
			// document.add(setParagraph("첫번째 페이지 첫번째 글."));
			// document.add(setParagraph("첫번째 페이지 두번째 글."));

			Paragraph title = new Paragraph("Chapter 1", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));
			Chapter chapter1 = new Chapter(title, 1);
			chapter1.setNumberDepth(0);
			Paragraph section = new Paragraph("Section 1 in Chapter 1", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
					new Color(255, 0, 0)));
			Section section1 = chapter1.addSection(section);
			section1.add(getParagraph("테스트 글"));
			section1.add(getParagraph("Example Text Line"));
			section1.add(getParagraph("다음은 3 X 2 테이블 예제."));

			int columns = 5;
			int rows = 5;
			Table t = new Table(columns, rows);
			t.setBorderColor(new Color(220, 255, 100));
			t.setPadding(5);
			t.setSpacing(0);
			t.setBorderWidth(1);
			section1.add(t);

			Paragraph text = null;
			Cell cell = null;
			for (int i = 0; i < columns; i++) {
				cell = new Cell(getParagraph("col_" + i));
				cell.setBackgroundColor(new Color(220, 255, 100));
				cell.setBorderColor(new Color(220, 255, 100));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
				cell.setUseBorderPadding(true);
				t.addCell(cell);
			}

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					cell = new Cell(getParagraph("value_" + i + "_" + j));
					cell.setBorderColor(new Color(220, 255, 100));
					cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
					cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);

					t.addCell(cell);
				}
			}

			document.add(chapter1);

			document.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * *
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PdfSample.getInstance().toPdf("c:\\temp\\example.pdf");
			System.out.println("파일 생성 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
