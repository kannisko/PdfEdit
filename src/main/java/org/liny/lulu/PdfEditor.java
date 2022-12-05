package org.liny.lulu;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PdfEditor {
    public static final String INPUT_PDF = "c:/pro666/inkscape/voucher_no_txt.pdf";
    public static final String OUTPUT_PDF = "./output.pdf";


    static PDDocument loadDocument() throws IOException {
        File file = new File(INPUT_PDF);
        PDDocument doc = PDDocument.load(file);
        System.out.println("PDF loaded");
        return doc;
    }

    static void saveDocument(PDDocument doc) throws IOException {
        //Saving the document
        doc.save(OUTPUT_PDF);

        //Closing the document
        doc.close();
    }

    static void addNapis(PDDocument doc) throws IOException {
        PDPage page = doc.getPage(0);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND,false);


        //Begin the Content stream

        contentStream.beginText();
        contentStream.setTextMatrix(new Matrix(1f, 0f, 0f, -1f, 0f, 0f));
        //contentStream.setTextMatrix(Matrix.getRotateInstance(0, (float) 1.0, (float) -1.0));
        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);

        //Setting the position for the line
        contentStream.newLineAtOffset(20, -20);


        String text1 = "Hi!!! https://facebook.com/haltery.wodze.liny";

        //Adding text in the form of string
        contentStream.showText(text1);

        //Ending the content stream
        contentStream.endText();

        System.out.println("New Text ");

        //Closing the content stream
        contentStream.close();
    }

    public static void main(String argv[]) throws IOException {

        PDDocument doc = loadDocument();
        addNapis(doc);
        saveDocument(doc);



        //Loading an existing document

//Adding a blank page to the document
//        doc.addPage(new PDPage());
/*
        PDPage page = doc.getPage(0);
        InputStream inputStream = page.getContents();
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        System.out.println(text);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND,false);


        //Begin the Content stream

        contentStream.beginText();
        contentStream.setTextMatrix(Matrix.getRotateInstance(0, (float) 1.0, (float) -1.0));
        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);

        //Setting the position for the line
        contentStream.newLineAtOffset(100, 100);


        String text1 = "Hi!!! This is the first sample PDF document.";

        //Adding text in the form of string
        contentStream.showText(text1);

        //Ending the content stream
        contentStream.endText();

        System.out.println("New Text Content is added in the PDF Document.");

        //Closing the content stream
        contentStream.close();
*/


    }
}
