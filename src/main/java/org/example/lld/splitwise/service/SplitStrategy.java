package org.example.lld.splitwise.service;


import org.example.lld.splitwise.models.Split;
import org.example.lld.splitwise.models.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

interface SplitStrategy {
    List<Split> computeSplits(BigDecimal totalAmount, List<SplitInput> splitInputList);
}


// total amount, list of users , amount per user, percentage per usage

class SplitInput {
    User user;

    public SplitInput(User user) {
        this.user = user;
    }
}

class ExactSplitInput extends SplitInput {
    public BigDecimal amount;

    public ExactSplitInput(User user, BigDecimal amount) {
        super(user);
        this.amount = amount;
    }
}

// implementations of split strategy

class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> computeSplits(BigDecimal totalAmount, List<SplitInput> splitInputList) {
        int totalUsers = splitInputList.size();
        BigDecimal equalContributionAmount = totalAmount.divide(BigDecimal.valueOf(totalUsers));
        List<Split> splits = new ArrayList<>();
        for (SplitInput splitInput : splitInputList) {
            Split split = new Split();
            split.setUser(splitInput.user);
            split.setAmount(equalContributionAmount);
            splits.add(split);
        }

        return splits;
    }
}


class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> computeSplits(BigDecimal totalAmount, List<SplitInput> splitInputList) {
        // validate sum of percentage
        List<Split> splits = new ArrayList<>();
        BigDecimal percentageSum = BigDecimal.ZERO;
        for (SplitInput splitInput : splitInputList) {
            PercentageSplitInput percentageSplitInput = (PercentageSplitInput) splitInput;
            Split split = new Split();
            split.setUser(splitInput.user);
            BigDecimal amount = totalAmount.multiply(percentageSplitInput.percentage.divide(BigDecimal.valueOf(100)));
            split.setAmount(amount);
            splits.add(split);
            percentageSum = percentageSum.add(percentageSplitInput.percentage);
        }

        if (percentageSum.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new RuntimeException("Invalid percentage input");
        }

        return splits;

    }
}


class SplitStrategyFactory {

    public static SplitStrategy getSplitStrategyInstance(SplitStrategyType strategyType) {
        SplitStrategy splitStrategy = null;

        splitStrategy = switch (strategyType) {
            case EQUAL -> new EqualSplitStrategy();
            case PERCENTAGE -> new PercentageSplitStrategy();
            default -> new EqualSplitStrategy();
        };
        return splitStrategy;
    }
}