package budget_saving.budget.command;

import budget_saving.budget.BudgetParser;
import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;
import java.util.Scanner;

public class BudgetGeneralCommand implements Command {
    public static final String LIST_BUDGET = "list";
    public static final String SET_BUDGET = "set";
    public static final String CHECK_BUDGET = "check";
    public static final String DEDUCT_BUDGET = "deduct";
    public static final String ADD_BUDGET = "add";


    private static final String BUDGET_COMMANDS =
                      "- " + SET_BUDGET + " n/BUDGET_NAME a/AMOUNT\n"
                    + "- " + CHECK_BUDGET + "\n"
                    + "- " + LIST_BUDGET + "\n"
                    + "- " + DEDUCT_BUDGET + " i/INDEX a/AMOUNT\n"
                    + "- " + ADD_BUDGET + " i/INDEX a/AMOUNT\n";

    private Command command;

    /**
     * Constructs a BudgetGeneralCommand by parsing the user input and initializing the corresponding command.
     * If the input is exactly "budget", the user is prompted for further details.
     *
     * Expected budget subcommand formats:
     * - set-budget n/BUDGET_NAME a/AMOUNT
     * - check-budget
     * - deduct-budget i/INDEX a/AMOUNT
     * - add-budget i/INDEX a/AMOUNT
     *
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
            System.err.println("An error has occurred when executing the budget command.");
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
