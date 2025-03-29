package budgetsaving.budget.command;

import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

public class AddToBudgetCommand implements Command {
    private BudgetManager budgetManager;
    private int index;
    private double amount;

    public AddToBudgetCommand(BudgetManager budgetManager, int index, double amount) {
        this.budgetManager = budgetManager;
        this.index = index;
        this.amount = amount;
    }

    @Override
    public void execute() {
        try {
            budgetManager.addToBudget(index, amount);
        } catch (Exception e) {
            System.err.println("Error adding to a budget: " + e.getMessage());
        }
    }

}
