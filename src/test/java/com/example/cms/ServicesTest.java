package com.example.cms;

import com.example.cms.dto.InquiryResult;
import com.example.cms.services.CardService;
import com.example.cms.utils.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ServicesTest {
    @Autowired
    private CardService cardService;

    @Test
    void inquiry() {
        AtomicReference<Long> amount = new AtomicReference<>();

        amount.set(999L);
        AtomicReference<InquiryResult> reference = new AtomicReference<>();
        assertThrows(ValidationException.class, () -> reference.set(cardService.preparePayments(amount.get())));

        amount.set(12350000L);
        assert cardService.preparePayments(amount.get()).getPayments().size() == 1;

        amount.set(50000000L);
        assert cardService.preparePayments(amount.get()).getPayments().size() == 3;

        amount.set(5000000000L);
        assert cardService.preparePayments(amount.get()).getPayments().size() == 0;
    }
}
