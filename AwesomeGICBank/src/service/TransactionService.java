package service;

import constants.TextMessageConstants;
import exception.MessageException;
import modal.Transaction;
import store.DataStore;
import util.Utils;

import java.util.List;
import java.util.Scanner;

public class TransactionService {

    public static void transactionConsole(Scanner scanner){
        var backOptionEnabled = false;
        while(!backOptionEnabled) {
            System.out.println(TextMessageConstants.TRANSACTION_MESSAGE);
            String input = scanner.nextLine();
            if("".equals(input)){
                backOptionEnabled = true;
            }else{
                initiateTransaction(input);
            }
        }
    }


    private static void initiateTransaction(String input) {
        try{
            String[] inputArr = input.split(" ");
            Transaction t = new Transaction(inputArr);
            DataStore.addTransactionToAccount(inputArr[1], t);
            var placeHolder = "| Date     | Txn Id      | Type | Amount |";
            PrintStatementService.printTransactions(inputArr[1], DataStore.getTransactions(inputArr[1]), placeHolder);
            System.out.println("");
        } catch (MessageException e) {
            System.err.println(e.getMessage());
        }
    }
}
