package org.liny.lulu;

import java.time.LocalDate;

public class FullOrderData  {

    private final VoucherData voucherData;
    private byte[] qrCode;
    private byte[] pdf;

    public FullOrderData(VoucherData voucherData){
       this.voucherData = voucherData;
    }

    public VoucherData getVoucherData() {
        return voucherData;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }
}
