package budget_saving.budget;

import budget_saving.budget.command.*;
import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

public class BudgetParser {

    public static Command parseSetBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        // Expected format: set-budget n/BUDGET_NAME a/AMOUNT
        int nIndex = input.indexOf("n/");
        int aIndex = input.indexOf("a/");
        if (nIndex == -1 || aIndex == -1) {
            throw new IllegalArgumentException("Invalid set-budget command format. " +
                    "Expected: set-budget n/BUDGET_NAME a/AMOUNT");
        }
        String name = input.substring(nIndex + 2, aIndex).trim();
        String amountStr = input.substring(aIndex + 2).trim();
        double amount = Double.parseDouble(amountStr);
        return new SetBudgetCommand(budgetManager, name, amount);
    }

    public static Command parseCheckBudgetCommand(String input, BudgetManager budgetManager) {
        // Expected format: check-budget (no extra parameters)
        int iIndex = input.indexOf("i/");
        if (iIndex == -1) {
            throw new IllegalArgumentException("Invalid check-budget command format.");
        }
        String indexStr = input.substring(iIndex + 2).trim();
        int index = Integer.parseInt(indexStr) - 1;
        return new CheckBudgetCommand(index, budgetManager);
    }

    public static Command parseListBudgetCommand(BudgetManager budgetManager) {
        return new ListBudgetCommand(budgetManager);
    }

    public static Command parseDeductBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        // Expected format: deduct-budget i/INDEX a/AMOUNT
        int iIndex = input.indexOf("i/");
        int aIndex = input.indexOf("a/");
        if (iIndex == -1 || aIndex == -1) {
            throw new IllegalArgumentException("Invalid deduct-budget command format.");
        }
        String indexStr = input.substring(iIndex + 2, aIndex).trim();
        String amountStr = input.substring(aIndex + 2).trim();
        int index = Integer.parseInt(indexStr);
        double amount = Double.parseDouble(amountStr);
        return new DeductFromBudgetCommand(budgetManager, index, amount);
    }

    public static Command parseAddBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        // Expected format: add-budget n/BUDGET_NAME a/AMOUNT
        int iIndex = input.indexOf("i/");
        int aIndex = input.indexOf("a/");
        if (iIndex == -1 || aIndex == -1) {
            throw new IllegalArgumentException("Invalid add-budget command format.");
        }
        String indexStr = input.substring(iIndex + 2, aIndex).trim();
        String amountStr = input.substring(aIndex + 2).trim();
        int index = Integer.parseInt(indexStr);
        double amount = Double.parseDouble(amountStr);
        return new AddToBudgetCommand(budgetManager, index, amount);
    }
}
