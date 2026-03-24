package org.example.lld.splitwise;


import org.example.lld.splitwise.models.ExpenseType;
import org.example.lld.splitwise.models.Group;
import org.example.lld.splitwise.models.User;
import org.example.lld.splitwise.repository.ExpenseRepository;
import org.example.lld.splitwise.repository.GroupRepository;
import org.example.lld.splitwise.repository.UserRepository;
import org.example.lld.splitwise.service.*;

import java.math.BigDecimal;
import java.util.List;

public class Splitwise {
    public static void main(String[] args) {

        // user management done
        // group management done

        // split strategy
        // expense addition
        // balance
        UserRepository userRepository = new UserRepository();
        GroupRepository groupRepository = new GroupRepository();
        ExpenseRepository expenseRepository = new ExpenseRepository();

        UserService userService = new UserService(userRepository);
        GroupService groupService = new GroupService(groupRepository);
        ExpenseService expenseService = new ExpenseService(expenseRepository);


        User userA = userService.createUser("A", "test@email.com");
        User userB = userService.createUser("B", "test1@email.com");
        User userC = userService.createUser("C", "test2@email.com");

        Group goaTripGroup = groupService.createGroup("Goa Trip");
        groupService.addUserToGroup(goaTripGroup.getGroupId(), userA);
        groupService.addUserToGroup(goaTripGroup.getGroupId(), userB);
        groupService.addUserToGroup(goaTripGroup.getGroupId(), userC);


        expenseService.addExpense(BigDecimal.valueOf(300), userA, ExpenseType.TRAVEL, "cab booking", goaTripGroup,
                SplitStrategyType.EQUAL, List.of(
                        new EqualSplitInput(userA),
                        new EqualSplitInput(userB),
                        new EqualSplitInput(userC)
                ));

        expenseService.addExpense(BigDecimal.valueOf(200), userC, ExpenseType.FOOD, "cab booking", goaTripGroup,
                SplitStrategyType.PERCENTAGE, List.of(
                        new PercentageSplitInput(userC, BigDecimal.valueOf(60)),
                        new PercentageSplitInput(userB, BigDecimal.valueOf(40))
                ));
    }
}
