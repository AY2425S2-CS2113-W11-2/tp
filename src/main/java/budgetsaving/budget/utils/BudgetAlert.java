package budgetsaving.budget.utils;

public class BudgetAlert {
    public static final String BUDGET_ALERT = BudgetTextColour.RED +
            "You have exceeded the budget! \n" +
            "Please manage your budget wisely or type 'suggestion' for financial advises."
             + BudgetTextColour.RESET;

    public static void exceedBudgetAlert(){
        System.out.println(BUDGET_ALERT);
    }
}
