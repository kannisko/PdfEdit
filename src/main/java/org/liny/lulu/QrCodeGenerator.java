package org.liny.lulu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


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
                matrix, "png", outputStream);

        return outputStream.toByteArray();
    }





    public static void main(String[] args) throws IOException {

    }




}
