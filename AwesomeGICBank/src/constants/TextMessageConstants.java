package constants;

public class TextMessageConstants {

    public static final String INTRODUCTION_MESSAGE = """
            ```
            Welcome to AwesomeGIC Bank! What would you like to do?
            [T] Input transactions
            [I] Define interest rules
            [P] Print statement
            [Q] Quit
            >
            ```
            """;

    public static final String TRANSACTION_MESSAGE = """
            ```
            Please enter transaction details in <Date> <Account> <Type> <Amount> format\s
            (or enter blank to go back to main menu):
            >
            ```
            """;

    public static final String INTEREST_RULE_MESSAGE = """
            ```
            Please enter interest rules details in <Date> <RuleId> <Rate in %> format\s
            (or enter blank to go back to main menu):
            >
            ```
            """;

    public static final String PRINT_STATEMENT_MESSAGE = """
            ```
            Please enter account and month to generate the statement <Account> <Year><Month>
            (or enter blank to go back to main menu):
            >
            ```
            """;


    public static final String QUIT_MESSAGE = """
            Thank you for banking with AwesomeGIC Bank.
            Have a nice day!
            """;
}
