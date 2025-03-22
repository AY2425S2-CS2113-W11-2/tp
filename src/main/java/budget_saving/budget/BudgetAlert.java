package budget_saving.budget;

public class BudgetAlert {
    public static final String BUDGET_ALERT = "Your expenses has exceeded the budget limit!\n" +
            "Please manage your budget wisely, or type 'suggestion' for financial advises.";

    public static void exceedBudgetAlert(){
        System.out.println(BUDGET_ALERT);
    }
}
