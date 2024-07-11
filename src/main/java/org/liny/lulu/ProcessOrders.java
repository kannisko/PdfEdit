package org.liny.lulu;

import com.google.zxing.WriterException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.io.FileUtils.*;

public class ProcessOrders {

    //wczytaj z pliku listę danaych
    //validacja//po sztuce rób
        //wylicz expiration date
        //wylicz VLL numer
        // gerneruj encryptowane dane do QR
        //generuj QR
        //generuj pdf
        //zapisz to kurwa gdzieś
    public static void main(String argv[]) throws IOException, WriterException {
            EncryptionDecryption encryptionDecryption = EncryptionDecryption.getInstance();
            File file = new File("./orders.json");
            String json = readFileToString(file, StandardCharsets.UTF_8);
            List<VoucherData> orders = VoucherData.readListFromJson(json);
            //validate
            for( VoucherData order : orders){
                order.calcMissingFields();
                FullOrderData fullOrderData = new FullOrderData(order);

                String encryptedForQR = encryptionDecryption.encryptForQR(order);
                fullOrderData.setQrCode(QrCodeGenerator.createQR(encryptedForQR,200,200));
                String json1 = encryptionDecryption.decryptFromQR(encryptedForQR);

                PdfEditParams pdfEditParams = new PdfEditParams(order, fullOrderData.getQrCode());
                fullOrderData.setPdf(PdfEditor.preparePdf(pdfEditParams));
                fullOrderData.setPdfA6Front(PdfEditor.prepareA6Front("/empty_A6L.pdf",pdfEditParams));
                fullOrderData.setPdfA6Back(PdfEditor.prepareA6Back(pdfEditParams));

                String zipName = order.getVllNumber() + "_" + String.format("%05d",order.getOrderNo())+".zip";
//                File zipFile = new File(zipName);
//                FileOutputStream fos = new FileOutputStream(zipFile);
//                ZipOutputStream zos = new ZipOutputStream(fos);
//
//                addZipFile(zos,order.getVllNumber()+".pdf",fullOrderData.getPdf());
//                addZipFile(zos,order.getVllNumber()+"_frontA6.pdf",fullOrderData.getPdfA6Front());
//                addZipFile(zos,order.getVllNumber()+"_backA6.pdf",fullOrderData.getPdfA6Back());
//                addZipFile(zos,order.getVllNumber()+".png",fullOrderData.getQrCode());
//                addZipFile(zos,order.getVllNumber()+".json",fullOrderData.getVoucherData().toJson().getBytes(StandardCharsets.UTF_8));
//                zos.close();
//                fos.close();
                pdfToFile("a6Front.pdf",fullOrderData.getPdfA6Front());
                pdfToFile("a6Back.pdf",fullOrderData.getPdfA6Back());
                pdfToFile(order.getVllNumber()+".pdf",fullOrderData.getPdf());
            }
    }

    private static void pdfToFile(String fileName, byte data[]) throws IOException {
        File filePdf = new File(fileName);
        FileOutputStream pdfStream = new FileOutputStream(filePdf);
        pdfStream.write(data);
        pdfStream.close();
    }


    public static void addZipFile(ZipOutputStream zos,String fileName,byte[] data) throws IOException {
        ZipEntry ze= new ZipEntry(fileName);
        zos.putNextEntry(ze);
        zos.write(data);
        zos.closeEntry();
    }
}
