package org.liny.lulu;

import java.io.InputStream;

public class PdfEditParams {
    private String serialNo;
    private String value;
    private String expirationDate;
    private String recipient;
    private byte qrCode[];

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
