package budget_saving.budget;

import cashflow.model.interfaces.BudgetManager;
import utils.money.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BudgetList implements BudgetManager {
    private List<Budget> budgets;
    private String currency;

    // Modified constructor to accept a currency
    public BudgetList(String currency) {
        this.currency = currency;
        budgets = new ArrayList<>();
    }

    // Added getter (and optionally setter) for currency
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void addBudget(Budget budget) throws BudgetException {
        if (budget == null) {
            throw new BudgetException("Cannot add a null budget.");
        }
        budgets.add(budget);
    }

    @Override
    public void setBudget(String name, double amount) {
        Money money = new Money(currency, BigDecimal.valueOf(amount));
        Budget newBudget = new Budget(name, money);
        try {
            addBudget(newBudget);
            System.out.println("New budget added: " + newBudget);
        } catch (BudgetException e) {
            System.err.println("Error adding new budget: " + e.getMessage());
        }
    }

    @Override
    public void listBudgets() {
        if (budgets.isEmpty()) {
            System.out.println("No budgets available.");
        } else {
            System.out.println("Budget list:");
            for (int i = 0; i < budgets.size(); i++) {
                Budget b = budgets.get(i);
                System.out.println("Budget " + (i + 1) + ". " + b.toString());
            }
        }
    }

    @Override
    public void deductFromBudget(int index, double amount) {
        if (index < 0 || index >= budgets.size()) {
            System.out.println("Budget index out of range.");
            return;
        }
        Budget b = budgets.get(index);
        b.deduct(amount);
        System.out.println("Budget deducted.");
        System.out.println(b.toString());

    }

    public void addToBudget(int index, double amount) {
        if (index < 0 || index >= budgets.size()) {
            System.out.println("Budget index out of range.");
            return;
        }
        Budget b = budgets.get(index);
        b.add(amount);
        System.out.println("Budget added");
        System.out.println(b.toString());
    }

    //returns the index of the budget, so it could be easily referenced
    public int findBudgetIndex(Budget budget) {
        for (int i = 0; i < budgets.size(); i++) {
            Budget b = budgets.get(i);
            if (b.equals(budget)) {
                return i;
            }
        }
        return -1;
    }

    public void modifyBudget(int index, String name, double amount) throws BudgetException {
        if (index < 0 || index >= budgets.size()) {
            throw new BudgetException("Index out of range.");
        }
        Budget b = budgets.get(index);
        b.modifyBudget(amount, name);
    }

    //to list out all the expenses within the budget
    //incorporate it in command
    public void checkBudget(int index) {
        if (index < 0 || index >= budgets.size()) {
            System.out.println("Index out of range.");
            return;
        }
        System.out.println(budgets.get(index).toStringWithExpenses());
    }
}
