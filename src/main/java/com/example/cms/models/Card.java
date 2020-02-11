package com.example.cms.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Card implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, length = 16, nullable = false)
    private String number;
    private Long remainAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Long remainCapacityAmount) {
        this.remainAmount = remainCapacityAmount;
    }
}
