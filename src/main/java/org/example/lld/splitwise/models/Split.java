package org.example.lld.splitwise.models;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class Split {
    private User user;
    private BigDecimal amount;
}
