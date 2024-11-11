package com.example.bank;

import com.example.bank.controller.BankAccountController;
import com.example.bank.service.AccountService;
import com.example.bank.service.JdbcAccountService;
import com.example.bank.publisher.EventPublisher;
import com.example.bank.publisher.SnsEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class BankApplication {
    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(); // Set up with a data source
        AccountService accountService = new JdbcAccountService(jdbcTemplate);
        EventPublisher eventPublisher = new SnsEventPublisher();
        BankAccountController controller = new BankAccountController(accountService, eventPublisher);

        // Simulate a withdrawal
        String response = controller.withdraw(12345L, new BigDecimal("100.00"));
        System.out.println(response);
    }
}
