package budget_saving.budget.command;

import budget_saving.budget.Budget;
import budget_saving.budget.BudgetException;
import cashflow.ui.command.Command;
import cashflow.model.interfaces.BudgetManager;

import java.time.LocalDate;

public class ModifyBudgetCommand implements Command {

    private static final String MODIFY_SUCCESS_MESSAGE = "Successfully modified budget.";

    private BudgetManager budgetManager;
    private int index;
    private double amount;
    private String name;
    private LocalDate endDate;
    private String category;

    private void printSuccess(Budget budget) {
        System.out.println(MODIFY_SUCCESS_MESSAGE);
        System.out.println("The updated budget is: \n"
                            + budget.toString());
    }

    public ModifyBudgetCommand(BudgetManager budgetManager, int index, double amount,
                               String name, LocalDate endDate, String category) {
        this.index = index;
        this.amount = amount;
        this.name = name;
        this.endDate = endDate;
        this.category = category;
        this.budgetManager = budgetManager;
    }

    @Override
    public void execute() {
        try {
            budgetManager.modifyBudget(index, name, amount, endDate, category);
            printSuccess(budgetManager.getBudget(index));
        } catch (BudgetException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error modifying the budget: " + e.getMessage());
        }
    }

}
