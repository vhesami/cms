package com.example.cms.models;

import com.example.cms.dto.State;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date initialDateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private Card sourceCard;
    private Long amount;
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private State state;

    public Transaction() {
    }

    public Transaction(Date initialDateTime, State state) {
        this.initialDateTime = initialDateTime;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInitialDateTime() {
        return initialDateTime;
    }

    public void setInitialDateTime(Date initialDateTime) {
        this.initialDateTime = initialDateTime;
    }

    public Card getSourceCard() {
        return sourceCard;
    }

    public void setSourceCard(Card sourceCard) {
        this.sourceCard = sourceCard;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
