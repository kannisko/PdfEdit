package org.liny.lulu;

import java.io.InputStream;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class PdfEditParams {
    private String serialNo;
    private String value;
    private String expirationDate;
    private String recipient;
    private byte qrCode[];

    public PdfEditParams(){}
    public PdfEditParams(VoucherData voucherData, byte qrCode[]){
        this.serialNo = voucherData.getVllNumber();
        this.value = Integer.toString(voucherData.getValue())+"PLN";
        this.expirationDate = voucherData.getExpirationDate().format(ISO_LOCAL_DATE);
        this.recipient = voucherData.getRecipient();
        this.qrCode = qrCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode( byte[] qrCode) {
        this.qrCode = qrCode;
    }
}
