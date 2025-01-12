package store;

import exception.MessageException;
import modal.Account;
import modal.InterestRule;
import modal.Transaction;
import modal.enums.TransactionType;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {

    private static final Map<String, Account> accountStoreMap;

    private static List<InterestRule> interestRulesStore;

    static {
        accountStoreMap = new HashMap<>();
        interestRulesStore = new ArrayList<>();

/*var acc = new Account("AC001");

        try {
            acc.addTransaction(new Transaction("20230505 AC001 D 100.00".split(" ")));
            acc.addTransaction(new Transaction("20230601 AC001 D 150.00".split(" ")));
       //     acc.addTransaction(new Transaction("20230615 AC001 D 10.00".split(" ")));
            acc.addTransaction(new Transaction("20230626 AC001 W 20.00".split(" ")));
            acc.addTransaction(new Transaction("20230626 AC001 W 100.00".split(" ")));
        } catch (MessageException e) {
            throw new RuntimeException(e);
        }
        accountStoreMap = new HashMap<>(
                Map.of("AC001", acc)
        );
        try {
            interestRulesStore =
                    List.of(new InterestRule("20230520 RULE02 1.90".split(" ")),
                            new InterestRule("20230615 RULE03 2.20".split(" "))
                       //     new InterestRule("20230630 RULE04 3.20".split(" "))


            );
        } catch (MessageException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static void addTransactionToAccount(String accountNumber, Transaction transaction) throws MessageException {
        var account = accountStoreMap.get(accountNumber);
        if(account == null && transaction.getTransactionType().equals(TransactionType.W)){
            throw new MessageException("First transaction cannot be Withdraw");
        }
        if(account == null){
            account = new Account(accountNumber);
        }
        account.addTransaction(transaction);
        accountStoreMap.put(accountNumber, account);
    }

    public static List<Transaction> getTransactions(String accountNumber) throws MessageException {
        var account = accountStoreMap.get(accountNumber);
        if(account == null){
            throw new MessageException("Account not found");
        }
        return account.getTransactions().stream()
                .sorted(Comparator.comparing(Transaction::getDate)).collect(Collectors.toList());
    }

    public static List<Transaction> getTransactionsForYearMonth(String accountNumber, YearMonth ym) throws MessageException {
        var account = accountStoreMap.get(accountNumber);
        if(account == null){
            throw new MessageException("Account not found");
        }
        return account.getTransactions().stream()
                .filter(transaction -> transaction.getDate().getYear() == ym.getYear() && transaction.getDate().getMonthValue() == ym.getMonthValue())
                .sorted(Comparator.comparing(Transaction::getDate)).collect(Collectors.toList());
    }

    public static double getFinalBalance(String accountNumber) throws MessageException {
        var account = accountStoreMap.get(accountNumber);
        if(account == null){
            throw new MessageException("Account not found");
        }
        return account.getBalance();
    }


    public static void addInterestRule(InterestRule interestRule) {
        Optional<InterestRule> optionalInterestRule = interestRulesStore.stream()
                .filter(existing -> existing.getDate().isEqual(interestRule.getDate()))
                .peek(existing -> existing.overrideRule(interestRule))
                .findFirst();
        if(!optionalInterestRule.isPresent()){
            interestRulesStore.add(interestRule);
        }
    }


    public static List<InterestRule> getInterestRules() {
        return interestRulesStore.stream().sorted(Comparator.comparing(InterestRule::getDate)).collect(Collectors.toList());
    }
}
