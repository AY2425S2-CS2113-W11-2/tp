package cashflow.analytics.command;

import cashflow.analytics.AnalyticsManager;
import cashflow.model.FinanceData;

public class CategoryInsightCommand extends AnalyticGeneralCommand{
    private int year;
    private int month;

    public CategoryInsightCommand(int month, int year) {
        this.month = month;
        this.year = year;
    }
    @Override
    public void execute(FinanceData data) {
        AnalyticsManager analytics = data.getAnalyticsManager();
        if (analytics != null) {
            analytics.showCategoryBreakdown(month, year);
        } else {
            System.out.println("Analytics module is not available.");
        }
    }
}
