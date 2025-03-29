package budgetsaving.budget.command;

import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

public class CheckBudgetCommand implements Command {
    private BudgetManager budgetManager;
    private int index;

    public CheckBudgetCommand(int index, BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
        this.index = index;
    }

    @Override
    public void execute() {
        try {
            budgetManager.checkBudget(index);
        } catch (Exception e) {
            System.err.println("Error checking a budget: " + e.getMessage());
        }
    }

}
