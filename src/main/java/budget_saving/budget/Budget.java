package budget_saving.budget;

import expense_income.expense.Expense;
import utils.money.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Budget {
    private String name;
    private Money totalBudget;
    private Money remainingBudget;
    private ArrayList<Expense> expenses;
    private LocalDate startDate;
    private LocalDate endDate;

    public Budget(String name, Money totalBudget) {
        this.name = name;
        this.totalBudget = totalBudget;
        this.expenses = new ArrayList<>();
        this.startDate = LocalDate.now();
        // Initialize remainingBudget with the same currency and amount as totalBudget
        this.remainingBudget = new Money(totalBudget.getCurrency(), totalBudget.getAmount());
    }

    public Budget(String name, Money totalBudget, LocalDate endDate) {
        this.name = name;
        this.totalBudget = totalBudget;
        this.expenses = new ArrayList<>();
        this.startDate = LocalDate.now();
        this.endDate = endDate;
        // Initialize remainingBudget with the same currency and amount as totalBudget
        this.remainingBudget = new Money(totalBudget.getCurrency(), totalBudget.getAmount());
    }

    // Getter for budget name
    public String getName() {
        return name;
    }

    public BigDecimal getMoneySpent(){
        return totalBudget.getAmount().subtract(remainingBudget.getAmount());
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    // Setters now accept a double and convert it to a BigDecimal internally
    public void setTotalBudget(double amount) {
        BigDecimal amt = BigDecimal.valueOf(amount);
        totalBudget.setAmount(amt);
    }

    public void setRemainingBudget(double amount) {
        BigDecimal amt = BigDecimal.valueOf(amount);
        remainingBudget.setAmount(amt);
    }

    // Deducts an amount from the remaining budget
    public void deduct(double amount) {
        BigDecimal deduction = BigDecimal.valueOf(amount);
        BigDecimal current = remainingBudget.getAmount();
        if (current.compareTo(deduction) < 0) {
            System.out.println("Not enough budget to deduct " + amount);
        } else {
            remainingBudget.setAmount(current.subtract(deduction));
        }
    }

    // Adds an amount to both the total and remaining budget
    public void add(double amount) {
        BigDecimal addition = BigDecimal.valueOf(amount);
        remainingBudget.setAmount(remainingBudget.getAmount().add(addition));
        totalBudget.setAmount(totalBudget.getAmount().add(addition));
    }

    //to be used by expense, when adding a new expense
    public void deductFromExpense(Expense expense) {
        if (expense == null){
            throw new IllegalArgumentException("Invalid expense.");
        }
        if (expenses.add(expense)) {
            deduct(expense.getAmount());
        }
    }

    public void removeExpenseFromBudget(Expense expense) {
        if (expense == null){
            throw new IllegalArgumentException("Invalid expense.");
        }
        //returns a true value if the method successfully removed the budget
        if (expenses.remove(expense)) {
            BigDecimal amount = BigDecimal.valueOf(expense.getAmount());
            remainingBudget.setAmount(remainingBudget.getAmount().add(amount));
        }
    }

    //If do not modify one of the attributes, call the method with
    //totalAmount = 0, and name = null
    public void modifyBudget(double totalAmount, String name) {
        if (name != null){ this.name = name; }
        if (totalAmount > 0){
            BigDecimal spent = getMoneySpent();
            setTotalBudget(totalAmount);
            double updatedRemaining = totalAmount - spent.doubleValue();
            setRemainingBudget(updatedRemaining);
        }
    }

    @Override
    public String toString() {
        return "Budget { Name='" + name + "', TotalBudget=" + totalBudget.toString() +
                ", RemainingBudget=" + remainingBudget.toString() + " }";
    }

    public String toStringWithExpenses(){
        StringBuilder sb = new StringBuilder();
        sb.append(this);
        for (Expense expense : expenses) {
            sb.append(expense.toString());
        }
        return sb.toString();
    }
}
