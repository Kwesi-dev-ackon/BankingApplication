package com.example.bank.service;

import java.math.BigDecimal;

public interface AccountService {
    BigDecimal getBalance(Long accountId);
    void withdraw(Long accountId, BigDecimal amount);
}
