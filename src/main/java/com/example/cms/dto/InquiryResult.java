package com.example.cms.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InquiryResult implements Serializable {
    private Date serverDateTime;
    private Long amount;
    private String message;
    private List<Payment> payments;

    public Date getServerDateTime() {
        return serverDateTime;
    }

    public void setServerDateTime(Date serverDateTime) {
        this.serverDateTime = serverDateTime;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
