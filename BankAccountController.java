package com.example.bank.controller;

import com.example.bank.service.AccountService;
import com.example.bank.publisher.EventPublisher;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    private final AccountService accountService;
    private final EventPublisher eventPublisher;

    public BankAccountController(AccountService accountService, EventPublisher eventPublisher) {
        this.accountService = accountService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount) {
        try {
            BigDecimal currentBalance = accountService.getBalance(accountId);
            if (currentBalance.compareTo(amount) < 0) {
                return "Insufficient funds for withdrawal";
            }

            accountService.withdraw(accountId, amount);
            eventPublisher.publishWithdrawalEvent(accountId, amount);

            return "Withdrawal successful";
        } catch (Exception e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
            return "Withdrawal failed";
        }
    }
}
