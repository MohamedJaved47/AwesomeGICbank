package modal;

import exception.MessageException;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Account {

    public Account(String accNo) {
        this.accNo = accNo;
        this.balance = 0d;
        this.transactions = new ArrayList<>();
    }

    private String accNo;
    private double balance;
    private List<Transaction> transactions;

    public String getAccNo() {
        return accNo;
    }
    public double getBalance() {
        return balance;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) throws MessageException {
        var caclculatedBalance = this.balance;
        switch (transaction.getTransactionType()) {
            case D -> caclculatedBalance += transaction.getAmount();
            case W -> caclculatedBalance -= transaction.getAmount();
            default -> throw new MessageException("Invalid transaction");
        }
        if(caclculatedBalance < 0) throw new MessageException("Invalid transaction, No Sufficient Balance");

        this.balance = caclculatedBalance;
        transaction.setInterimBalance(caclculatedBalance);
        transactions.add(transaction);
        StringBuilder trxId = new StringBuilder(Utils.convertDateToString(transaction.getDate(), "yyyyMMdd"));
        trxId.append("-");
        trxId.append(transactions.stream().filter(tr -> tr.getDate().isEqual(transaction.getDate())).count());
        transaction.setTransactionId(trxId.toString());
    }
}
