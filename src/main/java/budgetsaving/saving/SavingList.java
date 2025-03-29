package budgetsaving.saving;

import cashflow.model.interfaces.SavingsManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import utils.money.Money;

public class SavingList implements SavingsManager {
    private Map<String, Saving> savings;
    private String currency;

    public SavingList(String currency) {
        this.currency = currency;
        savings = new HashMap<>();
    }

    // Aggregated savings for all goals
    @Override
    public double getCurrentSavings() {
        double sum = 0;
        for (Saving goal : savings.values()) {
            sum += goal.getCurrentAmount().getAmount().doubleValue();
        }
        return sum;
    }

    @Override
    public void setCurrentSavings(double amount) {
        throw new UnsupportedOperationException("Setting overall savings is not supported in multi-goal manager.");
    }

    // Aggregated savings goal for all goals
    @Override
    public double getSavingsGoal() {
        double sum = 0;
        for (Saving goal : savings.values()) {
            sum += goal.getGoalAmount().getAmount().doubleValue();
        }
        return sum;
    }

    @Override
    public void setSavingsGoal(double amount) {
        throw new UnsupportedOperationException("Setting overall savings goal is not supported in multi-goal manager.");
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Creates a new savings goal.
     * Format: set-goal n/GOAL_NAME a/AMOUNT b/BY
     */
    public String setGoal(String name, Money amount, LocalDate deadline) {
        Saving goal = new Saving(name, amount, deadline);
        savings.put(name, goal);
        return String.format("You have set a new saving goal with \nName: %s\nAmount: %s\nBy: %s",
                name, amount.toString(), deadline);
    }

    /**
     * Contributes an amount to an existing savings goal.
     * Format: contribute-goal n/GOAL_NAME a/AMOUNT
     */
    public String contributeGoal(String name, Money amount) {
        Saving goal = savings.get(name);
        if (goal == null) {
            return "Savings goal not found.";
        }
        goal.addContribution(amount);
        Money left = new Money(
                goal.getGoalAmount().getCurrency(),
                goal.getGoalAmount().getAmount().subtract(goal.getCurrentAmount().getAmount())
        );
        return String.format("You have funded your saving goal\nName: %s\nAmount: %s\nAmount left to save: %s\nBy: %s",
                name, amount.toString(), left.toString(), goal.getDeadline());
    }

    /**
     * Checks and displays all savings goals and their progress.
     * Format: check-goal
     */
    public String checkGoals() {
        if (savings.isEmpty()) {
            return "No savings goals set.";
        }
        StringBuilder sb = new StringBuilder();
        for (Saving goal : savings.values()) {
            sb.append(String.format("Name: %s\nTotal Amount: %s\nSaved: %s\nBy: %s\n\n",
                    goal.getName(), goal.getGoalAmount().toString(),
                    goal.getCurrentAmount().toString(), goal.getDeadline()));
        }
        return sb.toString();
    }

}
