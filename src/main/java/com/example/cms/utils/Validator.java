package com.example.cms.utils;

import com.example.cms.dto.Notify;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Validator {
    private static Validator instance;

    private Validator() {
    }

    public static Validator getInstance() {
        if (instance == null) instance = new Validator();
        return instance;
    }

    public void validateAmount(Long amount) {
        if (amount == null || amount < Configurations.getInstance().getMinAmount())
            throw new ValidationException(String.format("Request amount is less than %d rials.", Configurations.getInstance().getMinAmount()));

    }

    public void validateNotifications(List<Notify> notifications) {
        if (notifications == null || notifications.isEmpty())
            throw new ValidationException("Request body is empty!");

    }
}
