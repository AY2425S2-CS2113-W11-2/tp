package cashflow.model.interfaces;

/**
 * Interface for budget features.
 */
public interface BudgetManager {

    void setBudget(String name, double amount);

    void listBudgets();

    void deductFromBudget(int index, double amount);

    void addToBudget(int index, double amount);

    void checkBudget(int index);
}
