package SDMCommon;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private double funds;
    private List<Transaction> transactionsList;

    BankAccount(){
        funds = 0;
        transactionsList = new ArrayList<>();
    }


    public double getFunds() {
        return funds;
    }

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }
    public void addFunds(double amount){
        funds += amount;
    }
}
