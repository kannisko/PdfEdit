package org.liny.lulu;

import java.time.LocalDate;

public class FullOrderData extends VoucherData {


    private  LocalDate expirationDate;
    private  String vllNumber;
    private byte[] qrCode;
    private byte[] pdf;

    public FullOrderData(VoucherData voucherData){
        this.orderNo = voucherData.orderNo;
        this.value = voucherData.value;
        this.purchaseDate = voucherData.purchaseDate;
        this.buyer = voucherData.buyer;
        this.email = voucherData.email;
        this.phone = voucherData.phone;
        this.recipient = voucherData.recipient;
    }

}
