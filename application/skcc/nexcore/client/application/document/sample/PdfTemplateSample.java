package skcc.nexcore.client.application.document.sample;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class PdfTemplateSample {

	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader("C:/Temp/employees_register.pdf");
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream("c:/TEMP/employees_register_filled.pdf"));

			AcroFields form = stamp.getAcroFields();
			HashMap fields = form.getFields();
			String key;
			for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
				key = (String) i.next();
				System.out.print(key + "\n");

				if (key.startsWith("f1")) {

				} else {
					form.setField(key, "AAAAAAAAAAAAAAAA\r\nBBBBBBBBBBBBBB\r\n0123456789\r\n0123456789");
				}
			}
			// loByteContent.bezierArc(1, 2, 3, 4, 5, 6);
			// stamp.insertPage(1, PageSize.A4);

			stamp.setFormFlattening(false);
			 stamp.setFullCompression();
			stamp.close();
		} catch (Exception de) {
			de.printStackTrace();
		}

	}
}
