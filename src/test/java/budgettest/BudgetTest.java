package budgettest;

import static org.junit.Assert.*;

import budget_saving.budget.Budget;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import expense_income.expense.Expense;
import utils.money.Money;

public class BudgetTest {

    private Budget budget;
    private Money initialMoney;

    @Before
    public void setUp() {
        initialMoney = new Money("USD", BigDecimal.valueOf(100));
        budget = new Budget("Test Budget", initialMoney);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Budget", budget.getName());
        assertEquals(BigDecimal.ZERO, budget.getMoneySpent());
    }

    @Test
    public void testDeduct() {
        budget.deduct(30);
        assertEquals(BigDecimal.valueOf(30), budget.getMoneySpent());
    }

    @Test
    public void testDeductInsufficient() {
        budget.deduct(150);
        assertEquals(BigDecimal.ZERO, budget.getMoneySpent());
    }

    @Test
    public void testAdd() {
        budget.add(50);
        assertEquals(BigDecimal.ZERO, budget.getMoneySpent());
        budget.deduct(30);
        assertEquals(BigDecimal.valueOf(30), budget.getMoneySpent());
    }

    @Test
    public void testModifyBudget() {
        budget.deduct(20);
        budget.modifyBudget(200, "New Budget");
        assertEquals("New Budget", budget.getName());
        assertEquals(BigDecimal.valueOf(20), budget.getMoneySpent());
    }

    @Test
    public void testDeductFromExpense() {
        Expense expense = new Expense("test A", 30) {
            @Override
            public double getAmount() {
                return 30;
            }

            @Override
            public String toString() {
                return "Expense: 30";
            }
        };
        budget.deductFromExpense(expense);
        assertEquals(1, budget.getExpenses().size());
        assertEquals(BigDecimal.valueOf(30), budget.getMoneySpent());
    }

    @Test
    public void testRemoveExpenseFromBudget() {
        Expense expense = new Expense("test B", 30) {
            @Override
            public double getAmount() {
                return 30;
            }

            @Override
            public String toString() {
                return "Expense: 30";
            }
        };
        budget.deductFromExpense(expense);
        assertEquals(BigDecimal.valueOf(30), budget.getMoneySpent());
        budget.removeExpenseFromBudget(expense);
        assertTrue(budget.getExpenses().isEmpty());
        assertEquals(BigDecimal.ZERO, budget.getMoneySpent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeductFromExpenseWithNull() {
        budget.deductFromExpense(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveExpenseFromBudgetWithNull() {
        budget.removeExpenseFromBudget(null);
    }

    @Test
    public void testToString() {
        String str = budget.toString();
        assertTrue(str.contains("Test Budget"));
        assertTrue(str.contains("TotalBudget"));
    }

    @Test
    public void testToStringWithExpenses() {
        Expense expense = new Expense("test c", 30) {
            @Override
            public double getAmount() {
                return 30;
            }

            @Override
            public String toString() {
                return "Expense: 30";
            }
        };
        budget.deductFromExpense(expense);
        String str = budget.toStringWithExpenses();
        assertTrue(str.contains("Expense: 30"));
    }
}
