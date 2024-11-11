package com.example.bank.publisher;

import java.math.BigDecimal;

public interface EventPublisher {
    void publishWithdrawalEvent(Long accountId, BigDecimal amount);
}
