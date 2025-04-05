
package expenseincome.income;

import cashflow.model.FinanceData;
import cashflow.model.interfaces.Finance;
import cashflow.model.interfaces.IncomeDataManager;
import expenseincome.income.exceptions.IncomeException;
import utils.money.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Currency;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;

public class IncomeManager implements IncomeDataManager {
    private static final Logger logger = Logger.getLogger(IncomeManager.class.getName());
    private final ArrayList<Income> incomes;
    private final FinanceData data;
    private final String currency;

    public IncomeManager(FinanceData data, String currency) {
        assert currency != null && !currency.isEmpty() : "Currency must not be null or empty.";
        this.incomes = new ArrayList<>();
        this.data = data;
        this.currency = currency;
    }

    public ArrayList<Finance> getIncomeList() {
        return new ArrayList<>(incomes);
    }

    public int getIncomeCount() {
        return incomes.size();
    }

    public Income getIncome(int i) {
        return incomes.get(i);
    }

    public void addIncome(String source, double amount, LocalDate date, String category) {
        try {
            validateIncomeDetails(source, amount, category);
            Currency currency = data.getCurrency();
            Money money = new Money(currency, amount);
            Income income = new Income(source, money, date, category);
            incomes.add(income);
            logger.log(Level.INFO, "Added income: {0}", income);
            System.out.println("Added: " + income);
        } catch (IncomeException e) {
            logger.log(Level.WARNING, "Failed to add income", e);
            System.out.println("Failed to add income. " + e.getMessage());
        }
    }

    public void editIncome(int index, String newSource, double newAmount, LocalDate newDate, String newCategory) {
        try {
            validateIndex(index);
            validateIncomeDetails(newSource, newAmount, newCategory);
            Income income = incomes.get(index - 1);
            Currency currency = data.getCurrency();
            Money money = new Money(currency, newAmount);
            income.setSource(newSource);
            income.setAmount(money);
            income.setDate(newDate);
            income.setCategory(newCategory);
            logger.log(Level.INFO, "Updated income: {0}", income);
            System.out.println("Updated: " + income);
        } catch (IncomeException e) {
            logger.log(Level.WARNING, "Failed to edit income", e);
            System.out.println("Failed to edit income. " + e.getMessage());
        }
    }

    public void deleteIncome(int index) {
        try {
            validateIndex(index);
            Income removed = incomes.remove(index - 1);
            logger.log(Level.INFO, "Deleted income: {0}", removed);
            System.out.println("Deleted: " + removed);
        } catch (IncomeException e) {
            logger.log(Level.WARNING, "Failed to delete income", e);
            System.out.println("Failed to delete income. " + e.getMessage());
        }
    }

    public void listIncomes() {
        if (incomes.isEmpty()) {
            System.out.println("No incomes recorded.");
            return;
        }
        System.out.println("Income List:");
        for (int i = 0; i < incomes.size(); i++) {
            System.out.println((i + 1) + ". " + incomes.get(i));
        }
    }

    public void listIncomesByCategory(String category) {
        if (incomes.isEmpty()) {
            System.out.println("No incomes recorded.");
            return;
        }

        boolean found = false;
        System.out.println("Incomes in category: " + category);
        for (int i = 0; i < incomes.size(); i++) {
            Income income = incomes.get(i);
            if (income.getCategory().equalsIgnoreCase(category)) {
                System.out.println((i + 1) + ". " + income);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No incomes found in this category.");
        }
    }

    public void sortIncomesByDate(boolean mostRecentFirst) {
        if (incomes.isEmpty()) {
            System.out.println("No incomes to sort.");
            return;
        }

        incomes.sort((i1, i2) -> mostRecentFirst ? i2.getDate().compareTo(i1.getDate())
                : i1.getDate().compareTo(i2.getDate()));

        listIncomes();
    }

    public void printTopCategory() {
        printCategorySummary(true);
    }

    public void printBottomCategory() {
        printCategorySummary(false);
    }

    private void printCategorySummary(boolean top) {
        if (incomes.isEmpty()) {
            System.out.println("No incomes recorded.");
            return;
        }

        Map<String, BigDecimal> totals = new HashMap<>();
        for (Income income : incomes) {
            String category = income.getCategory();
            BigDecimal amount = BigDecimal.valueOf(income.getAmount());
            totals.put(category, totals.getOrDefault(category, BigDecimal.ZERO).add(amount));
        }

        String targetCategory = null;
        BigDecimal targetAmount = null;

        for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
            if (targetAmount == null ||
                    (top && entry.getValue().compareTo(targetAmount) > 0) ||
                    (!top && entry.getValue().compareTo(targetAmount) < 0)) {
                targetAmount = entry.getValue();
                targetCategory = entry.getKey();
            }
        }

        if (targetCategory != null) {
            System.out.printf("%s Income Category: %s ($%.2f)%n",
                    top ? "Top" : "Lowest", targetCategory, targetAmount);
        }
    }

    private void validateIndex(int index) throws IncomeException {
        if (index < 1 || index > incomes.size()) {
            throw new IncomeException("Invalid index: must be between 1 and " + incomes.size());
        }
    }

    private void validateIncomeDetails(String source, double amount, String category) throws IncomeException {
        if (source == null || source.trim().isEmpty()) {
            throw new IncomeException("Source cannot be empty.");
        }
        if (amount <= 0) {
            throw new IncomeException("Amount must be greater than zero.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IncomeException("Category cannot be empty.");
        }
    }
}
