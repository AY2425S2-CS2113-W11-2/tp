package budgettest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import budgetsaving.budget.Budget;
import budgetsaving.budget.BudgetList;
import budgetsaving.budget.BudgetException;
import budgetsaving.budget.command.BudgetGeneralCommand;
import cashflow.model.interfaces.BudgetManager;
import expenseincome.expense.Expense;
import utils.money.Money;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BudgetListTest {

    private BudgetManager budgetManager;

    @BeforeEach
    public void setup() {
        budgetManager = new BudgetList("USD");
    }

    @Test
    public void testUnknownCommand() {
        // Input that does not match any known command keyword
        BudgetGeneralCommand command = new BudgetGeneralCommand("unknown", budgetManager);
        command.execute();

        // No budget should be added
        assertThrows(IndexOutOfBoundsException.class, () -> {
            ((BudgetList) budgetManager).getBudget(0);
        });
    }

    @Test
    public void testSetCommandViaGeneralCommand() {
        // Example input for setting a budget
        String input = "set n/TestBudget a/1000 e/2025-12-31 c/TestCategory";
        BudgetGeneralCommand command = new BudgetGeneralCommand(input, budgetManager);
        command.execute();

        assertEquals("TestBudget", ((BudgetList) budgetManager).getBudget(0).getName());
    }

    @Test
    public void testAddNewBudget() throws BudgetException {
        BudgetList budgetList = new BudgetList("USD");
        Money money = new Money("USD", BigDecimal.valueOf(1000));
        LocalDate futureDate = LocalDate.now().plusDays(10);
        Budget budget1 = new Budget("Budget1", money, futureDate, "Category1");

        budgetList.addNewBudget(budget1);
        assertEquals(budget1, budgetList.getBudget(0));

        // Adding a budget with a duplicate category => BudgetException
        Budget budgetDuplicate = new Budget("Budget2", new Money("USD", BigDecimal.valueOf(500)), futureDate, "Category1");
        assertThrows(BudgetException.class, () -> budgetList.addNewBudget(budgetDuplicate));
    }

    @Test
    public void testSetBudget() {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);

        // setBudget => adds a new budget
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");
        assertEquals("Budget1", budgetList.getBudget(0).getName());

        // Attempt to add duplicate category => assertion in setBudget
        assertThrows(AssertionError.class, () ->
                budgetList.setBudget("BudgetDuplicate", 500, futureDate, "Category1")
        );
    }

    @Test
    public void testListBudgets() {
        BudgetList budgetList = new BudgetList("USD");

        // Capture output with empty list
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        budgetList.listBudgets();
        assertTrue(outContent.toString().contains("No budgets available."));

        // Reset output, add a budget, and list again
        outContent.reset();
        LocalDate futureDate = LocalDate.now().plusDays(10);
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");
        budgetList.listBudgets();
        assertTrue(outContent.toString().contains("Budget list:"));

        System.setOut(originalOut);
    }

    @Test
    public void testDeductFromBudget() {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Valid deduction
        budgetList.deductFromBudget(0, 200);
        String output = outContent.toString();
        assertTrue(output.contains("Budget deducted."));

        // Deduct an amount that pushes remaining below zero => triggers alert
        outContent.reset();
        budgetList.deductFromBudget(0, 1000);
        output = outContent.toString();
        assertTrue(output.contains("Budget deducted."));

        // Invalid indices
        outContent.reset();
        budgetList.deductFromBudget(-1, 100);
        output = outContent.toString();
        assertTrue(output.contains("Budget index out of range."));

        outContent.reset();
        budgetList.deductFromBudget(5, 100);
        output = outContent.toString();
        assertTrue(output.contains("Budget index out of range."));

        System.setOut(originalOut);
    }

    @Test
    public void testDeductBudgetFromExpense() {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");

        // Valid deduction from expense
        Expense expense = new Expense("Expense1", 100, LocalDate.now(), "Food");
        boolean result = budgetList.deductBudgetFromExpense(0, expense);
        assertTrue(result);

        // Invalid index => IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () ->
                budgetList.deductBudgetFromExpense(5, expense)
        );
    }

    @Test
    public void testAddToBudget() {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Valid addition
        budgetList.addToBudget(0, 500);
        String output = outContent.toString();
        assertTrue(output.contains("Budget added"));

        // Invalid indices
        outContent.reset();
        budgetList.addToBudget(-1, 100);
        output = outContent.toString();
        assertTrue(output.contains("Budget index out of range."));

        outContent.reset();
        budgetList.addToBudget(5, 100);
        output = outContent.toString();
        assertTrue(output.contains("Budget index out of range."));

        System.setOut(originalOut);
    }

    @Test
    public void testGetBudget() {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");

        Budget retrieved = budgetList.getBudget(0);
        assertEquals("Budget1", retrieved.getName());

        // Invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> budgetList.getBudget(5));
    }

    @Test
    public void testModifyBudgetInBudgetList() throws BudgetException {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");
        budgetList.setBudget("Budget2", 500, futureDate, "Category2");

        // Valid modification (same category)
        assertDoesNotThrow(() ->
                budgetList.modifyBudget(0, "UpdatedBudget1", 1200, futureDate.plusDays(5), "Category1")
        );
        assertEquals("UpdatedBudget1", budgetList.getBudget(0).getName());

        // Change to a new category not in use
        assertDoesNotThrow(() ->
                budgetList.modifyBudget(0, null, 0, futureDate.plusDays(10), "NewCategory")
        );
        assertEquals("NewCategory", budgetList.getBudget(0).getCategory());

        // Duplicate category => BudgetException
        assertThrows(BudgetException.class, () ->
                budgetList.modifyBudget(0, null, 0, futureDate.plusDays(10), "Category2")
        );

        // Invalid index
        assertThrows(BudgetException.class, () ->
                budgetList.modifyBudget(5, null, 0, futureDate.plusDays(10), "AnyCategory")
        );
    }

    @Test
    public void testCheckBudget() {
        BudgetList budgetList = new BudgetList("USD");
        LocalDate futureDate = LocalDate.now().plusDays(10);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Invalid index
        budgetList.checkBudget(-1);
        String output = outContent.toString();
        assertTrue(output.contains("Index out of range."));

        outContent.reset();
        // Valid check when no expenses
        budgetList.setBudget("Budget1", 1000, futureDate, "Category1");
        budgetList.checkBudget(0);
        output = outContent.toString();
        assertTrue(output.contains("There are no expenses in this budget yet"));

        outContent.reset();
        // Add an expense, then check again
        Budget budget = budgetList.getBudget(0);
        Expense expense = new Expense("Expense1", 100, LocalDate.now(), "Food");
        budget.deductFromExpense(expense);
        budgetList.checkBudget(0);
        output = outContent.toString();
        assertTrue(output.contains("Expense1 - $100.0 on"));

        System.setOut(originalOut);
    }

    @Test
    public void testCurrencyGetterSetter() {
        BudgetList budgetList = new BudgetList("USD");
        assertEquals("USD", budgetList.getCurrency());
        budgetList.setCurrency("EUR");
        assertEquals("EUR", budgetList.getCurrency());
    }
}
