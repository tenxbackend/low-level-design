package org.example.lld.splitwise.models;

public enum ExpenseType {
    TRAVEL("TRAVEL"),
    FOOD("FOOD"),
    ACCOMMODATION("ACCOMMODATION"),
    ENTERTAINMENT("ENTERTAINMENT"),
    OTHER("OTHER");

    private final String type;

    ExpenseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
