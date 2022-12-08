package org.liny.lulu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncryptionDecryption {

    private final char[][][] numberEncryptionTab;

    public static void main(String argv[]) throws IOException {
        canDecodeAllEncoded();
        cannotDecodeNotEncoded();
//        EncryptionDecryption encec = new EncryptionDecryption();
//
//        for( int i = 1; i< 1000; i++){
//            String encoded = encec.encodeNumber(i);
//            System.out.println(i + " " + encoded);
//        }
    }

    //numer kolejny
    //wartość
    //data zakupu -> expiration
    //kupujący imię, nazwisko
    //email kupujacego
    //telefon kupujacego
    //obdarowany
    //z tego
    // kolejny;VLLencryptedNumer;Base64(json przeciurany





    public EncryptionDecryption() throws IOException {
        this.numberEncryptionTab = getDefaultEncryptionTab();
    }


    public String encodeNumber(int no) {
        if (no <= 0 || no > 99999) {
            return null;
        }
        int digitTab[] = new int[5];
        for( int i=4; i>=0; i--){
            digitTab[i] = no % 10;
            no /= 10;
        }
        char resultChars[] = new char[5];
        int prev = 0;
        for (int digitPos = 4; digitPos >= 0; digitPos--) {
            int digit = digitTab[digitPos];
            char encryptedChar = numberEncryptionTab[digitPos][prev][digit];
            resultChars[digitPos] = encryptedChar;
            prev += digit;
            prev %= 10;
        }
        return new String(resultChars);
    }

    private int decodeOneDigit(char tabToDecode[],char charToDecode ){
        for (int digit = 0; digit < 10; digit++) {
            if (tabToDecode[digit] == charToDecode) {
                return digit;
            }
        }
        return -1;
    }

    public int decodeNumber(String n) {
        if (n.length() != 5) {
            return -1;
        }
        int POW10[] = {10000,1000,100,10,1};
        int prev = 0;
        int result = 0;
        for (int digitPos = 4; digitPos >= 0; digitPos--) {
            int decodedDigit = decodeOneDigit(numberEncryptionTab[digitPos][prev],n.charAt(digitPos));
            if( decodedDigit <0 ){
                return -1-digitPos;
            }
            result += POW10[digitPos] * decodedDigit;
            prev += decodedDigit;
            prev %= 10;
        }
        return result;
    }

    private static void canDecodeAllEncoded() throws IOException {
        System.out.println("canDecodeAllEncoded");
        EncryptionDecryption encoderDecoder = new EncryptionDecryption();
        for( int i = 1; i< 100000; i++){
            String encoded = encoderDecoder.encodeNumber(i);
            int decoded = encoderDecoder.decodeNumber(encoded);
            if(decoded != i)
            {
                System.out.println(i + " encoded as " + encoded + " decoded as " + decoded);
            }
        }
    }

    private static void cannotDecodeNotEncoded() throws IOException {
        System.out.println("cannotDecodeNotEncoded");
        EncryptionDecryption encoderDecoder = new EncryptionDecryption();
        Map<String,Integer> map = new HashMap();

        for( int i = 1; i< 100000; i++){
            String encoded = encoderDecoder.encodeNumber(i);
            map.put(encoded,i);
        }

        Kombination comm = new Kombination();
        do {
            String iter = comm.get();
            int decoded = encoderDecoder.decodeNumber(iter);
            if (decoded >0 ) {
                if (!map.containsKey(iter)) {
                    System.out.println(iter + " not in map decoded as " + decoded);
                } else {
                    int fromMap = map.get(iter);
                    if (fromMap != decoded) {
                        System.out.println(iter + " different vals  " + fromMap +" "+ decoded);
                    }
                }
            }
        } while (comm.next());
    }

    static class Kombination {
        List<Character> charTab = createCharTab();
        int kombi[] = new int[5];
        int modulo = charTab.size() - 1;

        Kombination() {
        }

        boolean next() {
            for (int i = 0; i < 5; i++) {
                if (kombi[i]++ < modulo) {
                    return true;
                } else {
                    kombi[i] = 0;
                }
            }
            return false;
        }

        String get() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(charTab.get(kombi[i]));
            }
            return sb.toString();
        }
    }


/*

        Map<String, Integer> map = new HashMap();
        for (int no = 1; no < 100000; no++) {
            String ss = encodeNumber(no);
            map.put(ss, no);
        }

        Kombination comm = new Kombination();
        do {
            String iter = comm.get();
            String decoded = decodeNumber(iter);
            if (!"-1".equals(decoded)) {
                int dec = Integer.parseInt(decoded);
                if (!map.containsKey(iter)) {
                    System.out.println(iter + " not in map decoded as " + decoded);
                } else {
                    int fromMap = map.get(iter);
                    if (fromMap != dec) {
                        System.out.println(iter + " different vals  " + fromMap +" "+ decoded);
                    }
                }
            }
        } while (comm.next());

//            String decoded =decodeNumber(ss);
//            int dec = Integer.parseInt(decoded);
//            System.out.println(no + " " + ss + " " + decoded);
////            if( dec != no) {
////                System.out.println(no + " " + ss + " " + decoded);
////            }
//        }

    }


    static class Kombination {
        List<Character> charTab = createCharTab();
        int kombi[] = new int[5];
        int modulo = charTab.size() - 1;

        Kombination() {
        }

        boolean next() {
            for (int i = 0; i < 5; i++) {
                if (kombi[i]++ < modulo) {
                    return true;
                } else {
                    kombi[i] = 0;
                }
            }
            return false;
        }

        String get() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(charTab.get(kombi[i]));
            }
            return sb.toString();
        }


    }

 */


    private static char[][][] getDefaultEncryptionTab() throws IOException {
        InputStream stream = EncryptionDecryption.class.getResourceAsStream("/encodeNumberTab.txt");
        return readEncryptionTab(stream);
    }

    private static char[][][] readEncryptionTab(InputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        ObjectReader reader = objectMapper.reader();
        return reader.readValue(stream, char[][][].class);
    }

    private static char[][][] readEncryptionTab(String encryptionTabString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        ObjectReader reader = objectMapper.reader();
        return reader.readValue(encryptionTabString, char[][][].class);
    }

    private static String writeEncryptionTabJson( char[][][] encryptionTab) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        ObjectWriter writer = objectMapper.writer();
        return writer.writeValueAsString(encryptionTab);
    }

    private static String createRandomEncryptionTabJson() throws JsonProcessingException {
        return writeEncryptionTabJson(createRandomEncryptionTab());
    }

    private static char[][][] createRandomEncryptionTab() {
        char result[][][] = new char[5][][];
        for (int i = 0; i < 5; i++) {
            result[i] = new char[10][];
            for (int j = 0; j < 10; j++) {
                result[i][j] = createRandomEnryptionForOneDigit();
            }
        }
        return result;
    }

    private static char[] createRandomEnryptionForOneDigit() {
        char result[] = new char[10];
        List<Character> tab = new ArrayList();
        for (char c = '0'; c <= '9'; c++) {
            tab.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            tab.add(c);
        }

        for (int i = 0; i < 10; i++) {
            int len = tab.size();
            int idx = (int) (Math.random() * len);
            result[i] = tab.remove(idx);
        }
        return result;
    }

    private static List<Character> createCharTab() {
        List<Character> tab = new ArrayList();
        for (char c = '0'; c <= '9'; c++) {
            tab.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            tab.add(c);
        }
        return tab;
    }

}
