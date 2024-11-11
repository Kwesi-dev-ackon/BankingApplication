package com.example.bank.service;

import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;

public class JdbcAccountService implements AccountService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{accountId}, BigDecimal.class);
        } catch (Exception e) {
            System.err.println("Error fetching balance: " + e.getMessage());
            throw new RuntimeException("Failed to get balance", e);
        }
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows updated, withdrawal failed");
            }
        } catch (Exception e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
            throw new RuntimeException("Failed to withdraw", e);
        }
    }
}
