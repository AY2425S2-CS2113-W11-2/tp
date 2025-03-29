package budgetsaving.budget;

public class BudgetException extends Exception {

    public static final String ERROR_NOTIFIER = "Error initialising a command from the budget manager.";

    public BudgetException(String message) {
        super(message);
    }
}

