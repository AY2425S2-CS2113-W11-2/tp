package budgetsaving.saving.command;

import cashflow.command.Command;
import budgetsaving.saving.SavingList;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import utils.money.Money;
import java.math.BigDecimal;

public class SavingGeneralCommand implements Command {
    private static final String SAVING_COMMANDS =
            "- set-goal n/GOAL_NAME a/AMOUNT b/BY\n"
                    + "- contribute-goal n/GOAL_NAME a/AMOUNT\n"
                    + "- check-goal\n";

    private Command command;

    /**
     * Constructs a SavingGeneralCommand by parsing the user input and initializing the corresponding command.
     * If the input is just "saving", the user is prompted for further details.
     *
     * Expected saving subcommand formats:
     * - set-goal n/GOAL_NAME a/AMOUNT b/BY
     * - contribute-goal n/GOAL_NAME a/AMOUNT
     * - check-goal
     *
     * @param input the full user input command string.
     * @param savingList the saving list to operate on.
     */
    public SavingGeneralCommand(String input, SavingList savingList){
        // If the command is exactly "saving", prompt the user for the specific saving subcommand.
        if (input.trim().equalsIgnoreCase("saving")) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(SAVING_COMMANDS + "Enter saving command: ");
            input = scanner.nextLine().trim();
        }
        input = input.trim();
        String lowerInput = input.toLowerCase();
        try{
            if (lowerInput.startsWith("set-goal")) {
                command = parseSetGoalCommand(input, savingList);
            } else if (lowerInput.startsWith("contribute-goal")) {
                command = parseContributeGoalCommand(input, savingList);
            } else if (lowerInput.startsWith("check-goal")) {
                command = parseCheckGoalCommand(savingList);
            } else {
                System.out.println("Unknown saving command.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid currency/amount entered.");
        }
    }

    private static Command parseSetGoalCommand(String input, SavingList savingList)
            throws NumberFormatException {
        // Expected format: set-goal n/GOAL_NAME a/AMOUNT b/BY
        int nIndex = input.indexOf("n/");
        int aIndex = input.indexOf("a/");
        int bIndex = input.indexOf("b/");
        if (nIndex == -1 || aIndex == -1 || bIndex == -1) {
            throw new IllegalArgumentException("Invalid set-goal command format.");
        }
        String name = input.substring(nIndex + 2, aIndex).trim();
        String amountStr = input.substring(aIndex + 2, bIndex).trim();
        String deadline = input.substring(bIndex + 2).trim();
        Money moneyAmount = new Money(savingList.getCurrency(), new BigDecimal(amountStr));
        return new SetGoalCommand(savingList, name, moneyAmount, deadline);
    }

    private static Command parseContributeGoalCommand(String input, SavingList savingList)
            throws NumberFormatException {
        // Expected format: contribute-goal n/GOAL_NAME a/AMOUNT
        int nIndex = input.indexOf("n/");
        int aIndex = input.indexOf("a/");
        if (nIndex == -1 || aIndex == -1) {
            throw new IllegalArgumentException("Invalid contribute-goal command format.");
        }
        String name = input.substring(nIndex + 2, aIndex).trim();
        String amountStr = input.substring(aIndex + 2).trim();
        Money moneyAmount = new Money(savingList.getCurrency(), new BigDecimal(amountStr));
        return new ContributeGoalCommand(savingList, name, moneyAmount);
    }

    private static Command parseCheckGoalCommand(SavingList savingList) {
        // Expected format: check-goal with no extra parameters.
        return new CheckGoalCommand(savingList);
    }

    @Override
    public void execute() {
        try {
            command.execute();
        } catch (DateTimeParseException e) {
            System.err.println(SetGoalCommand.DATE_FORMAT_ERROR);
        } catch (Exception e) {
            System.err.println("An error has occurred when executing the command.");
        }
    }
}
