package budgetsaving.budget.command;

import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

public class DeductFromBudgetCommand implements Command {
    private BudgetManager budgetManager;
    private int index;
    private double amount;

    public DeductFromBudgetCommand(BudgetManager budgetManager, int index, double amount) {
        this.budgetManager = budgetManager;
        this.index = index;
        this.amount = amount;
    }

    @Override
    public void execute() {
        try {
            budgetManager.deductFromBudget(index, amount);
        } catch (Exception e) {
            System.err.println("Error deducting from a budget: " + e.getMessage());
        }
    }

}

