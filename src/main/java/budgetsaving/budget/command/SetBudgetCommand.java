package budgetsaving.budget.command;

import cashflow.command.Command;
import cashflow.model.interfaces.BudgetManager;

import java.time.LocalDate;

public class SetBudgetCommand implements Command {
    private BudgetManager budgetManager;
    private double amount;
    private String name;
    private LocalDate endDate;
    private String category;

    public SetBudgetCommand(BudgetManager budgetManager, String name,
                            double amount, LocalDate endDate, String category) {
        this.budgetManager = budgetManager;
        this.amount = amount;
        this.name = name;
        this.endDate = endDate;
        this.category = category;
    }

    @Override
    public void execute() {
        try {
            budgetManager.setBudget(name, amount, endDate, category);
        } catch (Exception e) {
            System.err.println("Error setting the budget: " + e.getMessage());
        }
    }

}
