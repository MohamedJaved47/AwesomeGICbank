package modal;

import exception.MessageException;
import util.Utils;

import java.time.LocalDate;

public class InterestRule {

    public InterestRule(String[] consoleInput) throws MessageException {
        if(consoleInput.length != 3){
            throw new MessageException("Invalid Interest Rule!");
        }
        try {
            this.date =  Utils.convertStringToDate(consoleInput[0], "yyyyMMdd");
        }catch(Exception e){
            throw new MessageException("Date should be in YYYYMMdd format");
        }
        this.ruleId = consoleInput[1];
        try{
            this.interest = Double.parseDouble(consoleInput[2]);
            if(interest < 1 || interest > 99)
                throw new MessageException("Interest Rate should be greater than zero and less than 99");
        } catch (NumberFormatException e) {
            throw new MessageException("Invalid Interest Rate!.");
        }

    }

    private LocalDate date;
    private double interest;
    private String ruleId;

    public LocalDate getDate() {
        return date;
    }

    public double getInterest() {
        return interest;
    }


    public String getRuleId() {
        return ruleId;
    }

    public void overrideRule(InterestRule newRule) {
        this.date = newRule.date;
        this.ruleId = newRule.ruleId;
        this.interest = newRule.interest;
    }

}
