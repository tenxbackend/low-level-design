package org.example.lld.splitwise.service;

import org.example.lld.splitwise.models.User;

import java.math.BigDecimal;

public class PercentageSplitInput extends SplitInput {
    public BigDecimal percentage;

    public PercentageSplitInput(User user, BigDecimal percentage) {
        super(user);
        this.percentage = percentage;
    }

}
