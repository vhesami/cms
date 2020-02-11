package com.example.cms.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("cms")
public class Configurations {
    private static Configurations instance;
    private Long minAmount;
    private Long maxAmount;

    public Configurations() {
        instance = this;
    }

    public static Configurations getInstance() {
        return instance;
    }

    public Long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Long minAmount) {
        this.minAmount = minAmount;
    }

    public Long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Long maxAmount) {
        this.maxAmount = maxAmount;
    }
}
