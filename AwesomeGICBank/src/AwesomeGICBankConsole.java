import constants.TextMessageConstants;
import service.InterestRateService;
import service.PrintStatementService;
import service.TransactionService;

import java.util.Scanner;

public class AwesomeGICBankConsole {

   public static void main(String[] args) {

       boolean quitConsole = false;
       Scanner scanner = new Scanner(System.in);
       while(!quitConsole) {
           System.out.println(TextMessageConstants.INTRODUCTION_MESSAGE);
           String option = scanner.nextLine().toLowerCase();
           switch(option) {
               case "t" -> TransactionService.transactionConsole(scanner);
               case "i" -> InterestRateService.interestRateConsole(scanner);
               case "p" -> PrintStatementService.printStatementConsole(scanner);
               case "q" -> {
                   System.out.println(TextMessageConstants.QUIT_MESSAGE);
                   quitConsole = true;
               }
               default -> System.err.println("Invalid option selected");
           }
       }
   }



}
