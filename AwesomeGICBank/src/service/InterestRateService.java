package service;

import constants.TextMessageConstants;
import exception.MessageException;
import modal.InterestRule;
import store.DataStore;
import util.Utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class InterestRateService {


    public static void interestRateConsole(Scanner scanner){
        var backOptionEnabled = false;
        while(!backOptionEnabled) {
            System.out.println(TextMessageConstants.INTEREST_RULE_MESSAGE);
            String input = scanner.nextLine();
            if("".equals(input)){
                backOptionEnabled = true;
            }else{
                defineInterestRateRule(input);
            }
        }
    }

    private static void defineInterestRateRule(String input) {
        try{
            String[] inputArr = input.split(" ");
            InterestRule interestRule = new InterestRule(inputArr);
            DataStore.addInterestRule(interestRule);

            System.out.println("Interest rules:");
            System.out.println("| Date     | RuleId | Rate (%) |");
            for(InterestRule rule: DataStore.getInterestRules()){
                var placeHolder = "| Date     | RuleId | Rate (%) |";
                var value = placeHolder.replace("Date", Utils.convertDateToString(rule.getDate(), "yyyyMMdd"))
                        .replace("RuleId", rule.getRuleId())
                        .replace("Rate (%)", Utils.formatDecimal(rule.getInterest(), "0.00"));
                System.out.println(value);
            }
            System.out.println("");
        }catch (MessageException e) {
            System.err.println(e.getMessage());
        }
    }

}
