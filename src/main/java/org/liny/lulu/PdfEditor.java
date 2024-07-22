package org.liny.lulu;

import com.google.zxing.WriterException;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.util.Matrix;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToString;

public class PdfEditor {
    private static final int DEFAULT_USER_SPACE_UNIT_DPI = 72;

    private static final float MM_TO_UNITS = 1 / (10 * 2.54f) * DEFAULT_USER_SPACE_UNIT_DPI;
    private static final float POINTS_PER_INCH = 72;
    private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;

    public static final String INPUT_PDF = "/voucher_A4.pdf";
    public static final String OUTPUT_PDF = "./output.pdf";

    private static final PDRectangle RECIP_BOX_MM = new PDRectangle(110, 45 + 18, 85, 18);
    private static final PDRectangle EXPIRATION_BOX_MM = new PDRectangle(110, 115 + 18, 85, 18);
    //private static final PDRectangle VALUE_BOX_MM = new PDRectangle(110, 80 + 18, 85, 18);
    private static final PDRectangle VALUE_BOX_MM = new PDRectangle(115, 80 + 18, 75, 18);
    private static final PDRectangle SERIAL_BOX_MM = new PDRectangle(5, 133 + 18, 20, 18);

    private static final PDRectangle RECIP_BOX_MM_A6 = new PDRectangle(78, 25 + 13, 60, 13);
    private static final PDRectangle COMMENTS_BOX_MM_A6 = new PDRectangle(78, 39 + 13, 60, 13);
    private static final PDRectangle VALUE_BOX_MM_A6 = new PDRectangle(78, 64 + 13, 60, 13);
    private static final PDRectangle EXPIRATION_BOX_MM_A6 = new PDRectangle(78, 89 + 13, 60, 13);

    private static final PDRectangle SERIAL_BOX_MM_A6 = new PDRectangle(3, 93 + 10, 25, 10);
    private final PDDocument doc;
    private final PDFont arialFont;
     private float pageHeight;

    static class Point {
        float x;
        float y;

        public Point() {
            this(0f, 0f);
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    static class RGB {
        float red;
        float green;
        float blue;

        public RGB() {
        }

        public RGB(int red, int green, int blue) {
            this.red = red / 255f;
            this.green = green / 255f;
            this.blue = blue / 255f;
        }
    }

    private Point mmToTextSpace(Point pt) {
        return new Point(getX(pt.x), getY(pt.y));
    }

    private PDRectangle mmToTextSpace(PDRectangle bx) {
        return new PDRectangle(getX(bx.getLowerLeftX()), getY(bx.getLowerLeftY()), getX(bx.getWidth()), getX(bx.getHeight()));
    }

    public static void main(String argv[]) throws IOException, WriterException {
        EncryptionDecryption encryptionDecryption = EncryptionDecryption.getInstance();
        File file = new File("./orders.json");
        String json = readFileToString(file, StandardCharsets.UTF_8);
        List<VoucherData> orders = VoucherData.readListFromJson(json);
        //validate
        VoucherData order =  orders.get(0);
        order.calcMissingFields();
        FullOrderData fullOrderData = new FullOrderData(order);
        String encryptedForQR = encryptionDecryption.encryptForQR(order);
        fullOrderData.setQrCode(QrCodeGenerator.createQR(encryptedForQR,200,200));
        PdfEditParams pdfEditParams = new PdfEditParams(order, fullOrderData.getQrCode());
        byte[] data = PdfEditor.prepareA6Front("/voucher_A6L.pdf",pdfEditParams);
        File zipFile = new File("a6front.pdf");
        FileOutputStream fos = new FileOutputStream(zipFile);
        fos.write(data);
        fos.close();
    }

    public static byte[] prepareA6Front(String tepmplateName,PdfEditParams editParams) throws IOException {
        PdfEditor editor = new PdfEditor(tepmplateName);
        PDPage page = editor.doc.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(editor.doc, page, PDPageContentStream.AppendMode.APPEND, false,true);

        prepareA6Front(editor,editParams,contentStream,1.0f);
        contentStream.close();
        return editor.saveDocument();
    }

    private static PDRectangle scale(PDRectangle box,double scale){
        if( scale == 1.0){
            return box;
        }
        return  new PDRectangle(
                (float)(box.getLowerLeftX()*scale),
                (float)(box.getLowerLeftY()*scale),
                (float)(box.getWidth()*scale),
                (float)(box.getHeight()*scale));
    }
    public static void prepareA6Front( PdfEditor editor,PdfEditParams editParams, PDPageContentStream contentStream, float scale ) throws IOException {
        RGB color = new RGB(100, 100, 100);

        editor.putTextAutoFormat(contentStream, editParams.getRecipient(), editor.arialFont, 13*scale, scale(RECIP_BOX_MM_A6,scale), color);
        if( !StringUtils.isEmpty(editParams.getComments())){
            editor.putTextAutoFormat(contentStream, editParams.getComments(), editor.arialFont, 13*scale, scale(COMMENTS_BOX_MM_A6,scale), color);
        }
        editor.putTextAutoFormat(contentStream, editParams.getValue(), editor.arialFont, 13*scale, scale(VALUE_BOX_MM_A6,scale), color);
        editor.putText(contentStream, editParams.getExpirationDate(), editor.arialFont, 13*scale, scale(EXPIRATION_BOX_MM_A6,scale), color);
        editor.putText(contentStream, editParams.getSerialNo(), editor.arialFont, 10*scale, scale(SERIAL_BOX_MM_A6,scale), new RGB());
    }

    PDPageContentStream getRotatedContent(PDPage page) throws IOException {
        page.setRotation(90);
        PDRectangle pageSize = page.getMediaBox();
        float h = pageSize.getHeight();
        float w = pageSize.getWidth();
        pageHeight = h;

        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false,true);

        float tx = h / 2;
        float ty = w / 2;
        contentStream.transform(Matrix.getTranslateInstance(tx, ty));
        contentStream.transform(Matrix.getRotateInstance(Math.toRadians(90), 0, 0));
        contentStream.transform(Matrix.getTranslateInstance(-ty, -tx));

        return contentStream;
    }

    public static byte[] prepareA6Back(PdfEditParams editParams) throws IOException {
        PdfEditor editor = new PdfEditor("/empty_A6L.pdf");
        PDPage page = editor.doc.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(editor.doc, page, PDPageContentStream.AppendMode.APPEND, false,true);

        editor.putText(contentStream, "Numer Seryjny: " + editParams.getSerialNo(), editor.arialFont, 18, new PDRectangle(30, 30, 40, 20), new RGB());
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(editor.doc, editParams.getQrCode(), "qrcode.png");
        contentStream.drawImage(pdImage, editor.getX(20), editor.getY(100));

        contentStream.close();
        return editor.saveDocument();
    }

    public static byte[] preparePdf(PdfEditParams editParams) throws IOException {
        PdfEditor editor = new PdfEditor("/voucher_A4.pdf");
        PDPage page = editor.doc.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(editor.doc, page, PDPageContentStream.AppendMode.APPEND, false,true);
        prepareA6Front(editor,editParams,contentStream,1.41f);

        editor.editPage0(contentStream,editParams);
        contentStream.close();
        return editor.saveDocument();
    }

    private PdfEditor(String templateName) throws IOException {
        this.doc = loadDocument(templateName);
        this.arialFont = getFont("/arial.ttf");
        this.pageHeight = doc.getPage(0).getBBox().getHeight();
    }

    private PdfEditor(PDRectangle rectangle) throws IOException {
        this.doc = new PDDocument();
        doc.addPage(new PDPage(rectangle));
        this.arialFont = getFont("/arial.ttf");
        this.pageHeight = doc.getPage(0).getBBox().getHeight();
    }

    float getX(float x) {
        return x * MM_TO_UNITS;
    }

    float getY(float y) {
        return this.pageHeight - y * MM_TO_UNITS;
    }

    PDFont getFont(String name) throws IOException {
        InputStream input = PdfEditor.class.getResourceAsStream(name);
        return PDType0Font.load(doc, input);
    }


    Point getStringSize(String string, PDFont font, float fontSize) throws IOException {
        return new Point(font.getStringWidth(string) / 1000 * fontSize,
                font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize);
    }

    PDRectangle centerString(String string, PDFont font, float fontSize, PDRectangle boxInMM) throws IOException {
        Point ssize = getStringSize(string, font, fontSize);
        PDRectangle tsBox = mmToTextSpace(boxInMM);
        return new PDRectangle(tsBox.getLowerLeftX() + (tsBox.getWidth() - ssize.x) / 2,
                tsBox.getLowerLeftY() + (tsBox.getHeight() - ssize.y) / 2 - (font.getFontDescriptor().getDescent() / 1000 * fontSize),
                ssize.x, ssize.y);
    }

    private void putText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, PDRectangle mmBox, RGB color) throws IOException {
        PDRectangle expBox = centerString(text, font, fontSize, mmBox);
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color.red, color.green, color.blue);
        contentStream.newLineAtOffset(expBox.getLowerLeftX(), expBox.getLowerLeftY());
        contentStream.showText(text);
        contentStream.endText();
    }

    private void rawText(String text, PDPageContentStream contentStream,int xMM, int yMM,PDFont font, float fontSize,  RGB color) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color.red, color.green, color.blue);
        contentStream.newLineAtOffset(xMM, yMM);
        contentStream.showText(text);
        contentStream.endText();

    }

    void editPage0(PDPageContentStream contentStream,PdfEditParams editParams) throws IOException {
        putText(contentStream, "Numer Seryjny: " + editParams.getSerialNo(), arialFont, 18, new PDRectangle(30, 150+30, 40, 20), new RGB());
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, editParams.getQrCode(), "qrcode.png");
        contentStream.drawImage(pdImage, getX(20), getY(150+100));
    }

    void addForm(PDPage page) throws IOException {
        // Adobe Acrobat uses Helvetica as a default font and
        // stores that under the name '/Helv' in the resources dictionary
        PDFont font = PDType1Font.HELVETICA;
        PDResources resources = new PDResources();
        resources.put(COSName.getPDFName("Helv"), font);

        // Add a new AcroForm and add that to the document
        PDAcroForm acroForm = new PDAcroForm(doc);
        doc.getDocumentCatalog().setAcroForm(acroForm);

        // Add and set the resources and default appearance at the form level
        acroForm.setDefaultResources(resources);

        // Acrobat sets the font size on the form level to be
        // auto sized as default. This is done by setting the font size to '0'
        String defaultAppearanceString = "/Helv 0 Tf 0 g";
        acroForm.setDefaultAppearance(defaultAppearanceString);

        // Add a form field to the form.
        PDTextField textBox = new PDTextField(acroForm);
        textBox.setPartialName("SampleField");
        textBox.setMultiline(true);

        // Acrobat sets the font size to 12 as default
        // This is done by setting the font size to '12' on the
        // field level.
        // The text color is set to blue in this example.
        // To use black, replace "0 0 1 rg" with "0 0 0 rg" or "0 g".
        defaultAppearanceString = "/Helv 12 Tf 0.4 0.4 0.4 rg";
        textBox.setDefaultAppearance(defaultAppearanceString);

        // add the field to the acroform
        acroForm.getFields().add(textBox);

        // Specify the annotation associated with the field
        PDAnnotationWidget widget = textBox.getWidgets().get(0);
        PDRectangle rect = mmToTextSpace(RECIP_BOX_MM);
        widget.setRectangle(rect);
        widget.setPage(page);

        // set green border and yellow background
        // if you prefer defaults, just delete this code block
        PDAppearanceCharacteristicsDictionary fieldAppearance
                = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
//        fieldAppearance.setBorderColour(new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE));
        fieldAppearance.setBackground(new PDColor(new float[]{1, 1, 1}, PDDeviceRGB.INSTANCE));
        widget.setAppearanceCharacteristics(fieldAppearance);

        // make sure the annotation is visible on screen and paper
        widget.setPrinted(true);

        // Add the annotation to the page
        page.getAnnotations().add(widget);

        // set the field value
        textBox.setValue("Wpisz nazwisko beneficjenta");
    }


    private void editPage1(PdfEditParams editParams) throws IOException {

    }

    float getScale(String txt,PDFont font, float fontSize,PDRectangle box) throws IOException {
        Point ssize = getStringSize(txt, font, fontSize);
        return box.getWidth() / ssize.x;
    }

    private void putTextAutoFormat(PDPageContentStream contentStream, String text, PDFont font, float fontSize, PDRectangle mmBox, RGB color) throws IOException {
        if(StringUtils.isEmpty(text)){
            return;
        }
        text = text.strip();
        PDRectangle box = mmToTextSpace(mmBox);
        Point ssize = getStringSize(text, font, fontSize);
        if( ssize.x <= box.getWidth()){
            putText(contentStream,  text,  font,  fontSize,  mmBox,  color);
            return;
        }
        float scale =  box.getWidth() / ssize.x;
        if( scale > 0.8f ){
            putText(contentStream,  text,  font,  fontSize*scale*0.9f,  mmBox,  color);
            return;
        }
        int spaceIndex = text.indexOf(' ');
        String txt1,txt2;
        if( spaceIndex > 0 ){
            int ii;
            while( (ii=text.indexOf(' ',spaceIndex+1)) > 0){
                if( ii >=text.length()/2){
                    break;
                }
                spaceIndex = ii;
            }
            txt1 = text.substring(0,spaceIndex);
            txt2 = text.substring(spaceIndex+1);
        }else{
            spaceIndex = text.length()/2;
            txt1 = text.substring(0,spaceIndex);
            txt2 = text.substring(spaceIndex);
        }
        float scale1 = getScale(txt1,font,fontSize,box);
        float scale2 = getScale(txt2,font,fontSize,box);
        scale = scale1 > scale2 ? scale2 : scale1;
        if( scale > 1.0 ){
           scale = 1.0f;
        }
        fontSize *= scale;
        PDRectangle mmBox1 = new PDRectangle(mmBox.getLowerLeftX(),mmBox.getLowerLeftY()-mmBox.getHeight()/2
                ,mmBox.getWidth(),mmBox.getHeight()/2);
        PDRectangle mmBox2 = new PDRectangle(mmBox.getLowerLeftX(),mmBox.getLowerLeftY(),mmBox.getWidth(),mmBox.getHeight()/2);
        putText(contentStream,  txt1,  font,  fontSize,  mmBox1,  color);
        putText(contentStream,  txt2,  font,  fontSize,  mmBox2,  color);
    }


    private PDDocument loadDocument(String resourceName) throws IOException {
        InputStream input = PdfEditor.class.getResourceAsStream(resourceName);
        PDDocument doc = PDDocument.load(input);
        input.close();
        return doc;
    }

    private byte[] saveDocument() throws IOException {
        //doc.save(OUTPUT_PDF);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.save(outputStream);
        doc.close();
        return outputStream.toByteArray();
    }

}
