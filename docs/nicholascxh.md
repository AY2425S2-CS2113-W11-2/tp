# Nicholas Chong - Project Portfolio Page

## Overview

Our product, **CashFlow**, is a Command Line Interface (CLI) application that empowers users to record, categorize, and analyze their personal expenses and income efficiently.

I was in charge of the entire **Expense and Income** modules. This included end-to-end development from design, implementation, and testing, to documentation. I also played a key leadership role in coordinating the team’s workflow, ensuring standardization, and guiding team progress throughout the project lifecycle.

Below are the summary to my contributions.

### 🧑‍💻 Code Contributed
- [My code on Code Dashboard](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=nicholascxh&breakdown=true)
- Core logic for `Expense`, `Income`, `ExpenseManager`, `IncomeManager`, `ExpenseCommandParser`, `IncomeCommandParser` classes
  - All `Add`, `Edit`, `Delete`, `List`, `Sort`, `TopCategory`, `BottomCategory`, `Help`, and `ListCategory` command classes for Expense and Income
- Introduced `ExpenseParserResult` and `IncomeParserResult` for better user feedback separation
- Introduced integration logic between `Expense` and `Budget`:
  - Add expense calls `budgetManager` which checks for category and deducts amount from budget
  - Edit expense calls `budgetManager` which adds back budget and deducts again

### ✨ Enhancements Implemented

- Built the entire **Expense and Income subsystems**, including:
  - Command pattern infrastructure to separate parsing and execution
  - Comprehensive validation with meaningful user feedback
  - Structured parser result logic to cleanly separate command execution from user interface errors
- Implemented **category-based analytics** such as aggregation of entries to determine top and bottom spending/earning categories
- Built extensible command execution framework for expense and income
- Applied **logging architecture** using Java’s `Logger`, ensuring internal visibility without cluttering CLI by having a separate log file
- Implemented custom error handling via `ExpenseException` and `IncomeException`
- Designed and implemented CLI parser that converts user input into command classes
- Refactored command execution to support testability, SOC (Separation of Concerns), and SRP (Single Responsibility Principle)

### 📘 Contributions to the User Guide (UG)

- Added the following UG sections:
  - Expense commands: `add`, `edit`, `delete`, `list`, `list category`, `sort recent|oldest`, `top`, `bottom`, `help`
  - Income commands: same structure mirrored for income
  - FAQ section, command summary, quick start, introduction and possible enhancements
- Drafted a standardized UG template for the team to ease their workload

### 📗 Contributions to the Developer Guide (DG)

- Wrote DG sections for:
  - Expense and Income Management Design and Implementation
  - Error handling with exceptions and Logging with Java’s logger
  - Table of contents for ease of viewing
  - All appendixes' content and Acknowledgments
- Diagrams contributed:
  - Class Diagrams for Expense and Income packages, Sequence Diagrams for `AddExpenseCommand`, `EditIncomeCommand` (PlantUML)
- Drafted a standardized DG template for the team to ease their workload

### 👥 Contributions to Team-based Tasks

- **Leadership & Coordination**:
  - Proactively set **reminders and internal deadlines** when team engagement was low
  - Frequently updated and **kept the team accountable** for milestones and deliverables
  - **Standardized documentation** formats for both UG and DG to ensure consistency
  - Initiated a **round-robin review workflow**, where each PR must be reviewed by another team member before merging
  - **Chaired internal meetings** and design syncs to align on implementation priorities

- **Project Setup & Admin**:
  - Set up the GitHub repository, issue tracker, project board, and milestones
  - Configured repository access, PR templates, and default GitHub branch protection
  - Maintained a clean commit history and release tagging

### 🤝 Review / Mentoring Contributions

- Reviewed multiple pull requests related to:
  - Input validation, CLI parsing, command execution logic, and UI consistency
- Shared tutorials with teammates on:
  - How to write PlantUML diagrams
  - Proper Java logging practices
- Helped resolve merge conflicts during final week integration

### 🌐 Contributions Beyond the Team

- Filed 15 bug reports for other tP groups during PE dry run testing
- Scheduled a consultation session with the teaching team and encouraged the team to join in