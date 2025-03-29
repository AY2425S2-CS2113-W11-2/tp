package budgetsaving.saving.command;

import cashflow.command.Command;
import utils.money.Money;
import budgetsaving.saving.SavingList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SetGoalCommand implements Command {
    public static final String DATE_FORMAT_ERROR = "Invalid date format, the correct format is: yyyy-MM-dd";

    private SavingList savingList;
    private String goalName;
    private Money amount;
    private String deadline;

    public SetGoalCommand(SavingList savingList, String goalName, Money amount, String deadline) {
        this.savingList = savingList;
        this.goalName = goalName;
        this.amount = amount;
        this.deadline = deadline;
    }

    @Override
    public void execute() throws DateTimeParseException {
        LocalDate deadlineDate = LocalDate.parse(deadline);
        String message = savingList.setGoal(goalName, amount, deadlineDate);
        System.out.println(message);
    }
}
