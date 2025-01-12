package service;

import constants.TextMessageConstants;
import exception.MessageException;
import modal.InterestRule;
import modal.Transaction;
import store.DataStore;
import util.Utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrintStatementService {

    public static void printStatementConsole(Scanner scanner){
        var backOptionEnabled = false;
        while(!backOptionEnabled) {
            System.out.println(TextMessageConstants.PRINT_STATEMENT_MESSAGE);
            String input = scanner.nextLine();
            if("".equals(input)){
                backOptionEnabled = true;
            }else{
                printAccountStatement(input);
            }
        }
    }

    protected static void printAccountStatement(String input) {
        try{
            String[] inputArr = input.split(" ");
            if(inputArr.length != 2){
                throw new MessageException("Invalid Print Statement Input!");
            }
            String accountNum = inputArr[0];
            YearMonth interestMonth;
            try {
                interestMonth =  Utils.convertStringToYearMonth(inputArr[1], "yyyyMM");
            }catch(Exception e){
                throw new MessageException("Date should be in YYYYMM format");
            }

            var placeHolder = "| Date     | Txn Id      | Type | Amount | Balance |";
            var transactions = DataStore.getTransactionsForYearMonth(accountNum, interestMonth);
            printTransactions(accountNum, transactions, placeHolder);
            printInterest(accountNum, interestMonth, calculateInterest(transactions, interestMonth), placeHolder);

        }catch (MessageException e) {
            System.err.println(e.getMessage());
        }
    }

    private static double calculateInterest(List<Transaction> transactions,  YearMonth interestMonth) throws MessageException {
        if(transactions.isEmpty()){
            return 0.0;
        }

        var filteredTransactions = transactions.stream().collect(Collectors.toMap(
                Transaction::getDate, Function.identity(), (v1, v2) -> v2, LinkedHashMap::new
        )).values().stream().toList();
        var interestRuleList = DataStore.getInterestRules();

        int i = 0, j = 0;
        double totalInterest = 0.0;
        LocalDate lastDayOfMonth = LocalDate.of(interestMonth.getYear(), interestMonth.getMonth(), 1).with(TemporalAdjusters.lastDayOfMonth());
        while (i < filteredTransactions.size() && j < interestRuleList.size()) {
            Transaction transaction = filteredTransactions.get(i);
            InterestRule rule = interestRuleList.get(j);

            LocalDate transactionDate = transaction.getDate();
            LocalDate nextTransactionDate = (i + 1 < filteredTransactions.size()) ? filteredTransactions.get(i + 1).getDate().minusDays(1) : lastDayOfMonth;

            LocalDate ruleDate = rule.getDate();
            LocalDate nextRuleDate = (j + 1 < interestRuleList.size()) ? interestRuleList.get(j + 1).getDate().minusDays(1) : lastDayOfMonth;

            LocalDate rangeStartDate = transactionDate.isAfter(ruleDate) ? transactionDate : ruleDate;
            LocalDate rangeEndDate = nextTransactionDate.isBefore(nextRuleDate) ? nextTransactionDate : nextRuleDate;

            if (!rangeStartDate.isAfter(rangeEndDate)) {
                long days = rangeStartDate.datesUntil(rangeEndDate.plusDays(1)).count();
                totalInterest += (transaction.getInterimBalance() * (rule.getInterest() / 100) * days);
            }

            if (rangeEndDate.equals(nextRuleDate)) {
                j++;
            } else {
                i++;
            }
        }

        return totalInterest / 365;
    }

//    private static double calculateInterest(List<Transaction> transactions, String accountNum) throws MessageException {
//        if(transactions.isEmpty()){
//            return 0.0;
//        }
//
//        double intermediateValue = 0.0;
//        var filteredTransactions = transactions.stream().collect(Collectors.toMap(
//                Transaction::getDate, Function.identity(), (v1, v2) -> v2, LinkedHashMap::new
//        )).values().stream().toList();
//
//        for(int i=1; i<filteredTransactions.size(); i++){
//            var transaction = filteredTransactions.get(i);
//            var prevTransaction = filteredTransactions.get(i-1);
//            Map.Entry<LocalDate, InterestRule> prevTranNearestInterestRate = DataStore.getInterestRulesStore().lowerEntry(prevTransaction.getDate());
//            Map.Entry<LocalDate, InterestRule> currentTranNearestInterestRate = DataStore.getInterestRulesStore().lowerEntry(transaction.getDate());
//
//            if(prevTranNearestInterestRate.getKey().isEqual(currentTranNearestInterestRate.getKey())){
//                intermediateValue += transaction.getInterimBalance()
//                        * ( currentTranNearestInterestRate.getValue().getInterest() / 100.0)
//                        *ChronoUnit.DAYS.between(prevTransaction.getDate(), transaction.getDate());
//            }
//            else{
//                intermediateValue += prevTransaction.getInterimBalance()
//                        * ( prevTranNearestInterestRate.getValue().getInterest() / 100.0)
//                        *ChronoUnit.DAYS.between(prevTransaction.getDate(), currentTranNearestInterestRate.getKey());
//                intermediateValue += prevTransaction.getInterimBalance()
//                        * ( currentTranNearestInterestRate.getValue().getInterest() / 100.0)
//                        *ChronoUnit.DAYS.between(currentTranNearestInterestRate.getKey(), transaction.getDate());
//            }
//        }
//
//        Transaction prevTransaction = transactions.get(transactions.size() - 1);
//        LocalDate lastDate = prevTransaction.getDate().with(TemporalAdjusters.lastDayOfMonth());
//        Map.Entry<LocalDate, InterestRule> prevTranNearestInterestRate = DataStore.getInterestRulesStore().lowerEntry(prevTransaction.getDate());
//        if(prevTransaction.getDate().isBefore(lastDate)){
//            intermediateValue +=  transactions.get(transactions.size() - 1).getInterimBalance()
//                    * ( prevTranNearestInterestRate.getValue().getInterest() / 100)
//                    * (ChronoUnit.DAYS.between(prevTransaction.getDate(), lastDate) + 1);
//        }
//        return intermediateValue / 365;
//    }

    private static void printInterest(String accountNum,  YearMonth interestMonth,  Double calculatedInterest, String placeHolder) throws MessageException {
        Double finalBalance = calculatedInterest + DataStore.getFinalBalance(accountNum);
        var interestRowText = placeHolder.replace("Date", Utils.convertDateToString(interestMonth.atEndOfMonth(), "yyyyMMdd"))
                .replace("Txn Id", "")
                .replace("Type", "I")
                .replace("Amount",  Utils.formatDecimal(calculatedInterest, "0.00"))
                .replace("Balance", Utils.formatDecimal(finalBalance, "0.00"));
        System.out.println(interestRowText);
    }


    protected static void printTransactions(String accNumber, List<Transaction> transctions, String placeHolder) throws MessageException {
        System.out.println("Account: " + accNumber);
        if(transctions.isEmpty()){
            System.out.println("No transactions found");
            return;
        }

        System.out.println(placeHolder);
        for (var trans: transctions){
            var value = placeHolder.replace("Date", Utils.convertDateToString(trans.getDate(), "yyyyMMdd"))
                    .replace("Txn Id", trans.getTransactionId())
                    .replace("Type", trans.getTransactionType().name())
                    .replace("Amount",  Utils.formatDecimal(trans.getAmount(), "0.00"))
                    .replace("Balance", Utils.formatDecimal(trans.getInterimBalance(), "0.00"));
            System.out.println(value);
        }
    }
}
