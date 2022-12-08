package org.liny.lulu;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    public static void main(String argv[]) throws IOException {
            EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
            File file = new File("./orders.json");
            String json = readFileToString(file, StandardCharsets.UTF_8);
            List<VoucherData> orders = VoucherData.readListFromJson(json);
            //validate
            for( VoucherData order : orders){
                FullOrderData fullOrderData = new FullOrderData(order);
                fullOrderDatavll

                System.out.println(order.getBuyer());
            }


    }




}
