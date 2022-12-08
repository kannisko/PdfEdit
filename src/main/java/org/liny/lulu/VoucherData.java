package org.liny.lulu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VoucherData {
    int orderNo;
    int value;
    LocalDate purchaseDate;
    String buyer;
    String email;
    String phone;
    String recipient;

    public VoucherData() {
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }



    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static List<VoucherData> getGetSampleData() {
        VoucherData data = new VoucherData();
        data.setOrderNo(1);
        data.setValue(2137);
        data.setPurchaseDate(LocalDate.of(2010,04,10));
        data.setBuyer("Antoni Macierewicz");
        data.setEmail("bywalec@smolensk.ru");
        data.setPhone("11223344");
        data.setRecipient("Bartuś Misiewicz zawsze dziewica");

        List<VoucherData> list = new ArrayList();
        list.add(data);
        return list;
    }

    public static String getSampleJsonString(List<VoucherData> data) throws JsonProcessingException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        ObjectWriter writer = objectMapper.writer();
        return writer.writeValueAsString(data);
    }

    public static List<VoucherData> readListFromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        TypeReference<List<VoucherData>> typeReference = new TypeReference<List<VoucherData>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
        return objectMapper.readValue(json,typeReference);
    }

    public static void main(String argv[]) throws JsonProcessingException {
        List<VoucherData> data = getGetSampleData();
        String ss = getSampleJsonString(data);
        System.out.println(ss);
    }


}
