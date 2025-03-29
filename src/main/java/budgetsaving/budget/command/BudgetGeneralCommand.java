package budgetsaving.budget.command;

import budgetsaving.budget.BudgetException;
import budgetsaving.budget.utils.BudgetParser;
import budgetsaving.budget.utils.BudgetTextColour;
import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;
import java.util.Scanner;


//this class contains all the budget commands
//ALL EXCEPTIONS should be handled inside their respective commands
//so any exception thrown out should not be coming from lower hierarchy
public class BudgetGeneralCommand implements Command {

    public static final String LIST_BUDGET = "list";
    public static final String SET_BUDGET = "set";
    public static final String CHECK_BUDGET = "check";
    public static final String DEDUCT_BUDGET = "deduct";
    public static final String ADD_BUDGET = "add";
    public static final String MODIFY_BUDGET = "modify";

    public static final String LINE_SEPARATOR = "-".repeat(70) + "\n";

    public static final String DASH = "- ";

    private static final String BUDGET_COMMANDS =
            LINE_SEPARATOR
        + BudgetTextColour.RED    + DASH + SET_BUDGET
                  + " n/BUDGET_NAME a/AMOUNT e/YYYY-MM-DD c/CATEGORY\n"  + BudgetTextColour.RESET
        + BudgetTextColour.GREEN  + DASH + CHECK_BUDGET  + " i/INDEX\n"                 + BudgetTextColour.RESET
        + BudgetTextColour.YELLOW + DASH + LIST_BUDGET   + "\n"                         + BudgetTextColour.RESET
        + BudgetTextColour.BLUE   + DASH + DEDUCT_BUDGET + " i/INDEX a/AMOUNT\n"        + BudgetTextColour.RESET
        + BudgetTextColour.PURPLE + DASH + ADD_BUDGET    + " i/INDEX a/AMOUNT\n"        + BudgetTextColour.RESET
        + BudgetTextColour.CYAN   + DASH + MODIFY_BUDGET
                  + " i/INDEX n/NAME a/AMOUNT e/YYYY-MM-DD c/CATEGORY\n" + BudgetTextColour.RESET
        + LINE_SEPARATOR;

    private Command command;

    /**
     * Constructs a BudgetGeneralCommand by parsing the user input and initializing the corresponding command.
     * If the input is exactly "budget", the user is prompted for further details.
     * @param input the full user input command string.
     * @param budgetManager the budget manager to operate on.
     */
    public BudgetGeneralCommand(String input, BudgetManager budgetManager) {
        try {
            if (input.startsWith(SET_BUDGET)) {
                command = BudgetParser.parseSetBudgetCommand(input, budgetManager);
            } else if (input.startsWith(DEDUCT_BUDGET)) {
                command = BudgetParser.parseDeductBudgetCommand(input, budgetManager);
            } else if (input.startsWith(ADD_BUDGET)) {
                command = BudgetParser.parseAddBudgetCommand(input, budgetManager);
            } else if (input.startsWith(LIST_BUDGET)) {
                command = BudgetParser.parseListBudgetCommand(budgetManager);
            } else if (input.startsWith(CHECK_BUDGET)) {
                command = BudgetParser.parseCheckBudgetCommand(input, budgetManager);
            } else if (input.startsWith(MODIFY_BUDGET)) {
                command = BudgetParser.parseModifyBudgetCommand(input, budgetManager);
            } else {
                System.out.println("Unknown budget command.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount entered.");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void execute() {
        try {
            command.execute();
        } catch (Exception e) {
            System.err.println(BudgetException.ERROR_NOTIFIER);
        }
    }

    //the 'main' function to all budget commands
    public static void handleBudgetCommand(Scanner scanner, BudgetManager budgetManager) {
        while (true){
            System.out.print("Here's a list of budget commands: \n" + BUDGET_COMMANDS + "Enter budget command: ");
            String input = scanner.nextLine().trim();
            if (input.startsWith("exit")) {
                break;
            }
            BudgetGeneralCommand command = new BudgetGeneralCommand(input, budgetManager);
            command.execute();
        }
    }
}
