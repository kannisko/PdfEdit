package org.liny.lulu;

import com.google.zxing.WriterException;
import org.apache.commons.io.FileUtils;

import java.io.File;
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

                String zipName = order.getVllNumber() + "_" + String.format("%05d",order.getOrderNo())+".zip";
                File zipFile = new File(zipName);
                FileOutputStream fos = new FileOutputStream(zipFile);

                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze= new ZipEntry(order.getVllNumber()+".pdf");
                zos.putNextEntry(ze);
                zos.write(fullOrderData.getPdf());
                zos.closeEntry();
                ze= new ZipEntry(order.getVllNumber()+".png");
                zos.putNextEntry(ze);
                zos.write(fullOrderData.getQrCode());
                zos.closeEntry();
                ze= new ZipEntry(order.getVllNumber()+".json");
                zos.putNextEntry(ze);
                zos.write(fullOrderData.getVoucherData().toJson().getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();

                zos.close();
                fos.close();

                //System.out.println(fullOrderData);
            }


    }
}
