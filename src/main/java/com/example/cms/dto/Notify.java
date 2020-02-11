package com.example.cms.dto;

import java.io.Serializable;

public class Notify implements Serializable {
    private Long transactionId;
    private State state;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
