package org.liny.lulu;

import java.time.LocalDate;

public class FullOrderData  {

    private final VoucherData voucherData;
    private byte[] qrCode;
    private byte[] pdf;

    private byte[] pdfA6Front;

    public byte[] getPdfA6Front() {
        return pdfA6Front;
    }

    public void setPdfA6Front(byte[] pdfA6Front) {
        this.pdfA6Front = pdfA6Front;
    }

    public byte[] getPdfA6Back() {
        return pdfA6Back;
    }

    public void setPdfA6Back(byte[] pdfA6Back) {
        this.pdfA6Back = pdfA6Back;
    }

    private byte[] pdfA6Back;
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
