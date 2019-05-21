package skcc.nexcore.client.application.document.pdf;

import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;

public class PDFRegisterField implements PdfPCellEvent {

	protected PdfFormField parent;

	protected PdfFormField kid;

	protected float padding;

	public PDFRegisterField(PdfFormField parent, PdfFormField kid, float padding) {
		this.kid = kid;
		this.parent = parent;
		this.padding = padding;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
	 *      com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {
		kid.setWidget(new Rectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)),
				PdfAnnotation.HIGHLIGHT_INVERT);
		try {
			parent.addKid(kid);
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

}
