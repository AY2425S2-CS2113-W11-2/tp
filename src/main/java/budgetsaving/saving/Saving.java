package budgetsaving.saving;

import java.time.LocalDate;
import java.math.BigDecimal;
import utils.money.Money;

public class Saving {
    private String name;
    private Money goalAmount;
    private Money currentAmount;
    private LocalDate deadline;

    public Saving(String name, Money goalAmount, LocalDate deadline) {
        this.name = name;
        this.goalAmount = goalAmount;
        this.deadline = deadline;
        // Initialize current amount to zero with the same currency as goalAmount.
        this.currentAmount = new Money(goalAmount.getCurrency(), BigDecimal.ZERO);
    }

    public String getName() {
        return name;
    }

    public Money getGoalAmount() {
        return goalAmount;
    }

    public Money getCurrentAmount() {
        return currentAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void addContribution(Money contribution) {
        // Ensure the contribution is in the same currency
        if (!contribution.getCurrency().equals(goalAmount.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch: Expected "
                    + goalAmount.getCurrency() + " but got " + contribution.getCurrency());
        }
        // Add contribution amount using BigDecimal arithmetic.
        BigDecimal newAmount = currentAmount.getAmount().add(contribution.getAmount());
        // Cap the current amount at the goal amount if it exceeds it.
        if (newAmount.compareTo(goalAmount.getAmount()) > 0) {
            newAmount = goalAmount.getAmount();
        }
        currentAmount.setAmount(newAmount);
    }

    @Override
    public String toString() {
        return "Saving [name=" + name + ", goalAmount=" + goalAmount
                + ", currentAmount=" + currentAmount + ", deadline=" + deadline + "]";
    }
}
