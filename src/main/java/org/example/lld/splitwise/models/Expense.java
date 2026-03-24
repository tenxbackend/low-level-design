package org.example.lld.splitwise.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@ToString
@Builder
public class Expense {
    private Long expenseId;
    private BigDecimal amount;
    private User paidBy;
    private Group group;
    private LocalDateTime paidAt;
    private ExpenseType expenseType;
    private List<Split> splits;
}
