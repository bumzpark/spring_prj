package skcc.nexcore.client.application.document.sample;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php http://www.manning.com/lowagie/
 */

public class FoxDogChunk1 {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogChunk1");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using Chunk objects.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_chunk.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("C:/Temp/fox_dog_chunk.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Font font = new Font(Font.COURIER, 10, Font.BOLD);
			font.setColor(new Color(0xFF, 0xFF, 0xFF));
			Chunk fox = new Chunk("quick brown fox", font);
			float superscript = 8.0f;
			fox.setTextRise(superscript);
			fox.setBackground(new Color(0xa5, 0x2a, 0x2a));
			Chunk jumps = new Chunk(" jumps over ", new Font());
			Chunk dog = new Chunk("the lazy dog", new Font(Font.TIMES_ROMAN, 14, Font.ITALIC));
			float subscript = -8.0f;
			dog.setTextRise(subscript);
			dog.setUnderline(new Color(0xFF, 0x00, 0x00), 3.0f, 0.0f, -5.0f + subscript, 0.0f, PdfContentByte.LINE_CAP_ROUND);
			document.add(fox);
			document.add(jumps);
			document.add(dog);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}