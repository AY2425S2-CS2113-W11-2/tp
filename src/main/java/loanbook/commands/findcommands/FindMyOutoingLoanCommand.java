package loanbook.commands.findcommands;

import loanbook.LoanManager;
import loanbook.loan.Loan;
import loanbook.ui.LoanUI;

import java.util.ArrayList;

public class FindMyOutoingLoanCommand extends FindOutgoingLoanCommand {
    public FindMyOutoingLoanCommand(LoanManager loanManager) {
        super(loanManager, loanManager.getUsername());
    }

    @Override
    public void execute() {
        ArrayList<Loan> found = loanManager.findOutgoingLoan(lender);
        if (found.isEmpty()) {
            System.out.println("No results found");
        } else {
            System.out.println("Outgoing loan" + (found.size() == 1 ? "" : "s") +
                    (found.size() == 1 ? " is:" : " are:"));
            System.out.println(LoanUI.forPrint(found));
        }
    }
}
