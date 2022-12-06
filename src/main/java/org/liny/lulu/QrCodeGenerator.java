package org.liny.lulu;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
0123456789
ABCDEFGHIJ
KLMNOPQRST
*/

public class QrCodeGenerator {

    public static byte[] createQR(String data,
                                       int height, int width) throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                = new HashMap<EncodeHintType,
                                ErrorCorrectionLevel>();

        hashMap.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.H);



        BitMatrix matrix = new MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(
                matrix,"png",outputStream);

        return outputStream.toByteArray();
    }

    public static String encodeNumber(int no){
        if( no <= 0 || no >99999){
            return null;
        }
        String noS = Integer.toString(no);
        while(noS.length() <5 ){
            noS ="0" + noS;
        }
        int tab[] = new int[5];
        for( int i=0; i<5; i++){
            tab[i] = noS.charAt(i) - '0';
        }
        String ss = "";
        int prev = 1;
        for(int i =4; i>=0; i-- ){
            int val = tab[i];
            val += prev;
            val %= 10;;
            prev = val;
            ss += val;
        }
        return ss;
    }

    public static void main(String[] args) {
        for( int no = 1; no < 100; no ++){
            String ss = encodeNumber(no);
            System.out.println(ss);

        }

    }

}
