package budget_saving.budget.command;

import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

public class ListBudgetCommand implements Command {
    private BudgetManager budgetManager;

    public ListBudgetCommand(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    @Override
    public void execute() {
        budgetManager.listBudgets();
    }
}

