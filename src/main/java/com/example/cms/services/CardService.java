package com.example.cms.services;

import com.example.cms.dto.InquiryResult;
import com.example.cms.dto.Notify;
import com.example.cms.dto.Payment;
import com.example.cms.dto.State;
import com.example.cms.models.Card;
import com.example.cms.models.Transaction;
import com.example.cms.repositories.CardRepository;
import com.example.cms.repositories.TransactionRepository;
import com.example.cms.utils.Configurations;
import com.example.cms.utils.Utilities;
import com.example.cms.utils.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CardService {
    final static Logger logger = Logger.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public InquiryResult preparePayments(Long amount) {
        checkWorkingDay();

        Validator.getInstance().validateAmount(amount);

        InquiryResult result = new InquiryResult();
        result.setAmount(amount);
        result.setServerDateTime(new Date(System.currentTimeMillis()));

        List<Transaction> definedTransactions = createTransactions(amount);

        if (!definedTransactions.isEmpty()) {
            List<Card> selectedCards = definedTransactions.stream().map(Transaction::getSourceCard).collect(Collectors.toList());
            definedTransactions = transactionRepository.saveAll(definedTransactions);
            cardRepository.saveAll(selectedCards);

            result.setPayments(new ArrayList<>());
            result.setMessage("OK");

            definedTransactions.forEach(t -> {
                Payment payment = new Payment();
                payment.setTransactionId(t.getId());
                payment.setCardNumber(t.getSourceCard().getNumber());
                payment.setAmount(t.getAmount());
                result.getPayments().add(payment);
            });
        } else {
            result.setMessage("There is no sufficient amount in the whole of cards.");
        }
        return result;
    }

    @Transactional
    public void notifyPayments(List<Notify> notifications) {
        Validator.getInstance().validateNotifications(notifications);

        List<Transaction> notifiedTransactions = updateTransactions(notifications);
        List<Transaction> failedTransactions = notifiedTransactions
                .stream()
                .filter(t -> t.getState().equals(State.FAILED)).collect(Collectors.toList());
        refund(failedTransactions);
    }


    private void checkWorkingDay() {
        try {
            LocalDateTime now = LocalDateTime.now();
            Date lastDate = transactionRepository.lastTransactionTime(Utilities.getTodayWithoutTime());

            if (lastDate == null) {
                cardRepository.resetRemainAmounts(Configurations.getInstance().getMaxAmount());
            } else {
                LocalDateTime lastTransactionDate = lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (lastTransactionDate == null || now.getMonthValue() > lastTransactionDate.getMonthValue() || now.getDayOfMonth() > lastTransactionDate.getDayOfMonth())
                    cardRepository.resetRemainAmounts(Configurations.getInstance().getMaxAmount());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<Transaction> createTransactions(Long amount) {
        List<Transaction> transactions = new ArrayList<>();

        Long accountBalance = cardRepository.accountBalance(Configurations.getInstance().getMinAmount());
        if (accountBalance != null && accountBalance >= amount) {
            List<Card> activeCards = cardRepository.findAllActiveCards(Configurations.getInstance().getMinAmount());

            Date now = new Date(System.currentTimeMillis());

            for (int i = 0; i < activeCards.size() && amount > 0; i++) {
                Card card = activeCards.get(i);

                Transaction transaction = new Transaction(now, State.UNKNOWN);
                transaction.setAmount(amount >= card.getRemainAmount() ? card.getRemainAmount() : amount);

                amount -= transaction.getAmount();
                card.setRemainAmount(card.getRemainAmount() - transaction.getAmount());

                transaction.setSourceCard(card);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private List<Transaction> updateTransactions(List<Notify> notifications) {
        Map<Long, State> transactionStateMap = notifications.stream().collect(Collectors.toMap(Notify::getTransactionId, Notify::getState));
        List<Transaction> notifiedTransactions = transactionRepository.findAllById(transactionStateMap.keySet());
        notifiedTransactions.forEach(t -> t.setState(transactionStateMap.get(t.getId())));
        notifiedTransactions = transactionRepository.saveAll(notifiedTransactions);
        return notifiedTransactions;
    }

    private List<Card> refund(List<Transaction> failedTransactions) {
        Map<Long, Long> failedTransactionsCardsMap = failedTransactions
                .stream()
                .collect(Collectors.toMap(t -> t.getSourceCard().getId(), Transaction::getAmount));
        List<Card> refundCards = cardRepository.findAllById(failedTransactionsCardsMap.keySet());
        refundCards.forEach(c -> {
            long newValue = c.getRemainAmount() + failedTransactionsCardsMap.get(c.getId());
            c.setRemainAmount(newValue > Configurations.getInstance().getMaxAmount() ?
                    Configurations.getInstance().getMaxAmount() :
                    newValue);
        });
        refundCards = cardRepository.saveAll(refundCards);
        return refundCards;
    }
}

