package modal;

import exception.MessageException;
import modal.enums.TransactionType;
import util.Utils;

import java.time.LocalDate;

public class Transaction {

    public Transaction(String[] consoleInput) throws MessageException {
        if(consoleInput.length != 4){
            throw new MessageException("Invalid Transaction Input!");
        }

        try {
           this.date =  Utils.convertStringToDate(consoleInput[0], "yyyyMMdd");
        }catch(Exception e){
            throw new MessageException("Date should be in YYYYMMdd format");
        }

        try{
            this.transactionType = TransactionType.valueOf(consoleInput[2].toUpperCase());
        }catch (Exception e){
            throw new MessageException("Invalid Transaction Type");
        }

        try{
            this.amount = Double.parseDouble(consoleInput[3]);
            if(amount < 0)
                throw new MessageException("Amount should be greater than zero");

            String formattedNumber = Utils.formatDecimal(amount, "0.00");
            if(amount > Double.parseDouble(formattedNumber))
                throw new MessageException("Amount should be less than or equal to two decimal places");

        } catch (NumberFormatException e) {
            throw new MessageException("Invalid Amount!.");
        }
    }


    private LocalDate date;
    private String transactionId;
    private TransactionType transactionType;
    private double amount;
    private double interimBalance;

    public LocalDate getDate() {
        return date;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public double getInterimBalance() {
        return interimBalance;
    }

    public void setInterimBalance(double interimBalance) {
        this.interimBalance = interimBalance;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

}
