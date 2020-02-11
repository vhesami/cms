package com.example.cms;

import com.example.cms.models.Card;
import com.example.cms.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RepositoriesTest {
    private static final Long MIN_AMOUNT = 10000L;
    private static final Long MAX_AMOUNT = 30000000L;
    private static List<Card> cards = new ArrayList<>();
    @Autowired
    private CardRepository cardRepository;

    @Test
    void addCards() {
        cards.clear();
        Arrays.asList("6037997148591235",
                "6104337896214788",
                "6037695875951234",
                "6274121121587965",
                "5892101025879563").forEach(n -> {
            Card card = new Card();
            card.setNumber(n);
            card.setRemainAmount(MAX_AMOUNT);
            cards.add(card);
        });
        cards = cardRepository.saveAll(cards);
    }

    @Test
    void checkBalance() {
        Long balance = cardRepository.accountBalance(MIN_AMOUNT);
        assert balance == MAX_AMOUNT * cards.size();
    }

    @Test
    void resetCards() {
        Long accountBalance = cardRepository.accountBalance(MIN_AMOUNT);
        cardRepository.resetRemainAmounts(MAX_AMOUNT * 2);
        Long newBalance = cardRepository.accountBalance(MIN_AMOUNT);

        assert accountBalance != null && newBalance != null && !accountBalance.equals(newBalance);
    }

    @Test
    void removeCards() {
        cardRepository.deleteInBatch(cards);
        Card card = cards.get(0);
        cards.clear();
        assert !cardRepository.findOne(Example.of(card)).isPresent();
    }
}
