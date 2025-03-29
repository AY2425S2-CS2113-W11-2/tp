package budgetsaving.budget.utils;

import java.time.LocalDate;

public class BudgetAttributes {
    public static final String INDEX_IDENTIFIER = "i/";
    public static final String NAME_IDENTIFIER = "n/";
    public static final String AMOUNT_IDENTIFIER = "a/";
    public static final String END_DATE_IDENTIFIER = "e/";
    public static final String CATEGORY_IDENTIFIER = "c/";
    public static final int IDENTIFIER_OFFSET = 2;

    private int index;
    private String name;
    private double amount;
    private LocalDate endDate;
    private String category;

    public BudgetAttributes(String input) {
        int iPos = input.indexOf(INDEX_IDENTIFIER);
        int nPos = input.indexOf(NAME_IDENTIFIER);
        int aPos = input.indexOf(AMOUNT_IDENTIFIER);
        int ePos = input.indexOf(END_DATE_IDENTIFIER);
        int cPos = input.indexOf(CATEGORY_IDENTIFIER);

        // Extract index (convert to 0-index)
        if (iPos != -1) {
            int end = findNextIdentifier(input, iPos, nPos, aPos, ePos, cPos);
            String indexStr = input.substring(iPos + IDENTIFIER_OFFSET, end).trim();
            try {
                this.index = Integer.parseInt(indexStr) - 1;
            } catch(NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid index value: " + indexStr, ex);
            }
        } else {
            this.index = -1;
        }

        // Extract name
        if (nPos != -1) {
            int end = findNextIdentifier(input, nPos, aPos, ePos, cPos);
            this.name = input.substring(nPos + IDENTIFIER_OFFSET, end).trim();
        } else {
            this.name = null;
        }

        // Extract amount
        if (aPos != -1) {
            int end = findNextIdentifier(input, aPos, ePos, cPos);
            String amountStr = input.substring(aPos + IDENTIFIER_OFFSET, end).trim();
            try {
                this.amount = Double.parseDouble(amountStr);
            } catch(NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid amount value: " + amountStr, ex);
            }
        } else {
            this.amount = -1;
        }
        // Extract end date
        if (ePos != -1) {
            int end = findNextIdentifier(input, ePos, cPos);
            String endDateStr = input.substring(ePos + IDENTIFIER_OFFSET, end).trim();
            try {
                this.endDate = LocalDate.parse(endDateStr);
            } catch(Exception ex) {
                throw new IllegalArgumentException("Invalid date format: " + endDateStr, ex);
            }
        } else {
            this.endDate = null;
        }
        // Extract category (till end of string)
        if (cPos != -1) {
            this.category = input.substring(cPos + IDENTIFIER_OFFSET).trim();
        } else {
            this.category = null;
        }
    }

    // Helper method to find the next identifier position after 'current'
    // among a variable number of identifier positions.
    private int findNextIdentifier(String input, int current, int... positions) {
        int next = input.length();
        for (int pos : positions) {
            if (pos != -1 && pos > current && pos < next) {
                next = pos;
            }
        }
        return next;
    }

    // Getters
    public int getIndex(){
        return this.index;
    }
    public String getName(){
        return this.name;
    }
    public double getAmount(){
        return this.amount;
    }
    public LocalDate getEndDate(){
        return this.endDate;
    }
    public String getCategory(){
        return this.category;
    }
}
