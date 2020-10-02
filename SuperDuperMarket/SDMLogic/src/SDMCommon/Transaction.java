package SDMCommon;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    private final String action;
    private final Date date;
    private final double amount;
    private final double balanceBeforeAction;
    private final double balanceAfterAction;

    public Transaction(String action, Date date, double amount, double balanceBeforeAction, double balanceAfterAction) {
        this.action = action;

        this.date = date;
        this.amount = amount;
        this.balanceBeforeAction = balanceBeforeAction;
        this.balanceAfterAction = balanceAfterAction;
    }

    public String getAction() {
        return action;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceBeforeAction() {
        return balanceBeforeAction;
    }

    public double getBalanceAfterAction() {
        return balanceAfterAction;
    }
}
