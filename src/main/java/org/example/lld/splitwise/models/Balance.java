package org.example.lld.splitwise.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
@Builder
public class Balance {
    private User user;
    private BigDecimal amount;
    
    public void addAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }
    
    public void subtractAmount(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }
}
