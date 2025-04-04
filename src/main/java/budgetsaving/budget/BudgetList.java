package budgetsaving.budget;

import budgetsaving.budget.exceptions.BudgetException;
import budgetsaving.budget.utils.BudgetActiveStatus;
import budgetsaving.budget.utils.BudgetAlert;
import cashflow.model.interfaces.BudgetDataManager;
import cashflow.model.interfaces.BudgetManager;
import cashflow.model.interfaces.Finance;
import expenseincome.expense.Expense;
import utils.io.IOHandler;
import utils.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class BudgetList implements BudgetManager, BudgetDataManager {
    private ArrayList<Budget> budgets;
    private Currency currency;
    private HashMap<String, Budget> budgetByCategory;

    // Modified constructor to accept a currency
    public BudgetList(Currency currency) {
        assert currency != null : "Currency must not be null or empty.";
        this.currency = currency;
        budgets = new ArrayList<>();
        budgetByCategory = new HashMap<>();
    }

    // Added getter (and optionally setter) for currency
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    //update the budget completion/active status
    //refresh the status constantly when the user starts the app
    private void updateBudgetCompletionStatus(Budget budget) {
        if (budget == null) {
            IOHandler.writeOutput("Budget does not exist.");
            return;
        }
        LocalDate currDate = LocalDate.now();
        LocalDate budgetExpireDate = budget.getEndDate();
        if (currDate.isAfter(budgetExpireDate)){
            budget.updateBudgetActiveStatus(BudgetActiveStatus.EXPIRED);
        }
    }

    //please call this method when initialising the app
    public void refreshBudgetStatuses(){
        for (int i = 0; i < budgets.size(); i++) {
            updateBudgetCompletionStatus(budgets.get(i));
        }
        IOHandler.writeOutput("Budget statuses are refreshed!");
    }


    public void addNewBudget(Budget budget) throws BudgetException {
        if (budget == null) {
            throw new BudgetException("Cannot add a null budget.");
        }
        String category = budget.getCategory();
        if (budgetByCategory.containsKey(category)) {
            throw new BudgetException("Budget in category " + category + " already exists.");
        }
        assert !budgets.contains(budget) : "Budget already exists before addition.";
        budgets.add(budget);
        budgetByCategory.put(category, budget);
        assert budgets.contains(budget) : "Budget not added properly.";
    }


    public void setBudget(String name, double amount, LocalDate endDate, String category) {
        Money money = new Money(currency, BigDecimal.valueOf(amount));
        Budget newBudget = new Budget(name, money, endDate, category);
        int initialSize = budgets.size();
        try {
            addNewBudget(newBudget);
            IOHandler.writeOutput("New budget added, type 'check i/INDEX' to check the details");
        } catch (BudgetException e) {
            IOHandler.writeOutput("Error adding new budget: " + e.getMessage());
        }
        assert budgets.size() == initialSize + 1 :
                "Budget list size did not increase.";
        assert budgets.get(budgets.size() - 1).equals(newBudget) :
                "Last budget is not the newly added one.";
        assert budgetByCategory.get(newBudget.getCategory()).equals(newBudget) :
                "Budget hash mapping not updated properly.";
    }


    @Override
    public void listBudgets() {
        if (budgets.isEmpty()) {
            IOHandler.writeOutput("No budgets available.");
        } else {
            IOHandler.writeOutput("Budget list:");
            for (int i = 0; i < budgets.size(); i++) {
                Budget b = budgets.get(i);
                IOHandler.writeOutput("Budget " + (i + 1) + ". " + b.toString());
            }
        }
    }

    @Override
    public void deductFromBudget(int index, double amount) {
        if (index < 0 || index >= budgets.size()) {
            IOHandler.writeOutput("Budget index out of range.");
            return;
        }
        Budget b = budgets.get(index);
        Money before = b.getRemainingBudget(); // assuming this exists
        b.deduct(amount);
        Money after = b.getRemainingBudget();
        assert after.getAmount().compareTo(before.getAmount()) <= 0 : "Budget did not decrease after deduction.";
        IOHandler.writeOutput("Budget deducted.");
        if (after.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            BudgetAlert.exceedBudgetAlert();
        }
        IOHandler.writeOutput(b.toString());
    }

    public boolean deductBudgetFromExpense(Expense expense) {
        String category = expense.getCategory();
        //dont need to check if category is null
        //its done on expense side
        Budget targetBudget = null;
        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getCategory().equals(category)) {
                targetBudget = budgets.get(i);
            }
        }
        boolean hasExceededBudget = false;
        try{
            hasExceededBudget = targetBudget.deductFromExpense(expense);
            Money remainingBudget = targetBudget.getRemainingBudget();
            if (remainingBudget.compareTo(new Money(currency, 0)) < 0){
                hasExceededBudget = true;
            }
        } catch (NullPointerException e) {
            IOHandler.writeOutput("No budgets found.");
        }
        return hasExceededBudget;
    }

    public void addToBudget(int index, double amount) {
        if (index < 0 || index >= budgets.size()) {
            IOHandler.writeOutput("Budget index out of range.");
            return;
        }
        Budget b = budgets.get(index);
        Money before = b.getRemainingBudget(); // assuming getRemaining returns a Money object
        b.add(amount);
        Money after = b.getRemainingBudget();
        assert after.getAmount().compareTo(before.getAmount()) >= 0 : "Budget did not increase after addition.";
        IOHandler.writeOutput("Budget added");
        IOHandler.writeOutput(b.toString());
    }

    public Budget getBudget(int index) {
        if (index < 0 || index >= budgets.size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
        return budgets.get(index);
    }

    public void modifyBudget(int index, String name, double amount, LocalDate endDate, String category)
            throws BudgetException {
        if (budgets.isEmpty()){
            throw new BudgetException("No budgets available.");
        }
        if (index >= budgets.size()) {
            throw new BudgetException("Index must be within 1 to " + budgets.size() + ".");
        }
        Budget b = budgets.get(index);
        String oldCategory = b.getCategory();
        if (!oldCategory.equals(category)) {
            if (budgetByCategory.containsKey(category)) {
                throw new BudgetException("Budget in category " + category + " already exists.");
            }
            b.modifyBudget(amount, name, endDate, category);
            budgetByCategory.remove(oldCategory);
            budgetByCategory.put(category, b);
        } else {
            b.modifyBudget(amount, name, endDate, category);
        }
    }

    //to list out all the expenses within the budget
    //incorporate it in command
    public void checkBudget(int index) {
        if (index < 0 || index >= budgets.size()) {
            IOHandler.writeOutput("Index out of range.");
            return;
        }
        IOHandler.writeOutput(budgets.get(index).printExpenses());
    }

    @Override
    public ArrayList<Finance> getBudgetList() {
        return new ArrayList<>(budgets);
    }

}
