package budgetsaving.budget.utils;

import budgetsaving.budget.command.AddToBudgetCommand;
import budgetsaving.budget.command.ModifyBudgetCommand;
import budgetsaving.budget.command.CheckBudgetCommand;
import budgetsaving.budget.command.DeductFromBudgetCommand;
import budgetsaving.budget.command.ListBudgetCommand;
import budgetsaving.budget.command.SetBudgetCommand;
import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

import java.time.LocalDate;

public class BudgetParser {

    public static Command parseSetBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        BudgetAttributes attributes = new BudgetAttributes(input);
        String name = attributes.getName();
        double amount = attributes.getAmount();
        String category = attributes.getCategory();
        LocalDate endDate = attributes.getEndDate();
        return new SetBudgetCommand(budgetManager, name, amount, endDate, category);
    }

    public static Command parseCheckBudgetCommand(String input, BudgetManager budgetManager) {
        // Expected format: check-budget (no extra parameters)
        BudgetAttributes attributes = new BudgetAttributes(input);
        int index = attributes.getIndex();
        return new CheckBudgetCommand(index, budgetManager);
    }

    public static Command parseListBudgetCommand(BudgetManager budgetManager) {
        return new ListBudgetCommand(budgetManager);
    }

    public static Command parseDeductBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        BudgetAttributes attributes = new BudgetAttributes(input);
        int index = attributes.getIndex();
        double amount = attributes.getAmount();
        return new DeductFromBudgetCommand(budgetManager, index, amount);
    }

    public static Command parseAddBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        // Expected format: add-budget n/BUDGET_NAME a/AMOUNT
        BudgetAttributes attributes = new BudgetAttributes(input);
        int index = attributes.getIndex();
        double amount = attributes.getAmount();
        return new AddToBudgetCommand(budgetManager, index, amount);
    }

    public static Command parseModifyBudgetCommand(String input, BudgetManager budgetManager)
            throws NumberFormatException {
        // Expected format: set-budget n/BUDGET_NAME a/AMOUNT
        BudgetAttributes attributes = new BudgetAttributes(input);
        int index = attributes.getIndex();
        String name = attributes.getName();
        double amount = attributes.getAmount();
        LocalDate endDate = attributes.getEndDate();
        String category = attributes.getCategory();
        return new ModifyBudgetCommand(budgetManager, index, amount, name, endDate, category);
    }

}
