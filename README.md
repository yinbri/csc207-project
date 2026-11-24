# Budget Abroad

A desktop budgeting app for international students, built with Java Swing and Clean Architecture
for CSC207.

## Team Information

### Team Members
- Jinyeong Soul
- Kevin Liu
- Brian Yin
- Alexandra Nguyen

---

## Project Description

### Domain
**Personal Finance**

Moving abroad to study means managing money in a new currency, often on a fixed budget. Budget Abroad
lets a student record their expenses, set a spending limit for each month, see how much is left, and
view the same numbers in their home currency using a live exchange rate. Expenses and budgets are
entered in the local currency (CAD) and will be saved to a local JSON file so they are still there the
next time the app is opened.

---

## User Stories

1. **Add an expense** — As an international student, I want to record an expense with its amount,
   category, description and date, so that I can keep track of where my money goes.
2. **Set a monthly budget** — As an international student, I want to set a spending limit for a month,
   so that I know how much I am allowed to spend.
3. **See my monthly summary** — As an international student, I want to see how much I have spent this
   month, how much is left in my budget, and how my spending is split across categories, so that I can
   adjust my spending before I run out of money.
4. **See my spending in my home currency** — As an international student, I want to see my budget and
   spending converted into my home currency, so that I can understand the amounts in terms I am used to
   (and explain them to my family).

---

## Use Cases

### Use Case 1: Add Expense
- **Main flow:** The student enters an amount, chooses a category, optionally types a description, and
  enters the date. The expense is saved and the monthly summary updates to include it.
- **Alternative flows:**
  - The amount is not a number, or is not greater than zero → the student is told what is wrong and
    nothing is saved.
  - The category is not a valid category → the student is asked to choose a valid one.
  - The date is not a valid `YYYY-MM-DD` date → the student is told the expected format.

### Use Case 2: Set Monthly Budget
- **Main flow:** The student enters a month and a spending limit. The budget is saved (replacing any
  previous budget for that month) and the summary shows the new budget and remaining amount.
- **Alternative flows:**
  - The month is not in `YYYY-MM` format → the student is told the expected format.
  - The limit is not a number, or is not greater than zero → the student is told what is wrong.

### Use Case 3: View Monthly Summary
- **Main flow:** The student chooses a month. The app shows that month's expenses, the total spent, the
  budget, the money remaining, and the total for each category.
- **Alternative flows:**
  - No budget has been set for the month → spending is shown and the student is told no budget is set.
  - Spending is above the budget → the remaining amount is negative and highlighted in red.
  - The month is not in `YYYY-MM` format → the student is told the expected format.

### Use Case 4: View Spending in Home Currency
- **Main flow:** The student chooses their home currency. The app shows today's exchange rate and the
  month's budget, spending, remaining amount, and category totals in that currency.
- **Alternative flows:**
  - The exchange rate cannot be retrieved (for example no internet) → the student is told the
    conversion is unavailable.
  - The chosen currency is the same as the local currency → the student is told there is nothing to
    convert.

---

## Minimal Viable Product (MVP)

All four use cases are part of the MVP. Each team member takes the lead on one: its interactor, the
interfaces it needs, its controller and presenter, and the part of the UI that triggers it.

| Team Member | MVP Use Case |
|-------------|--------------|
| Brian Yin | Add Expense |
| Kevin Liu | Set Monthly Budget |
| Jinyeong Soul | View Monthly Summary |
| Alexandra Nguyen | View Spending in Home Currency |

---

## Entities

All five entities are implemented and unit tested.

### Expense
- **Instance variables:** `id: String`, `amount: double`, `category: Category`, `description: String`,
  `date: LocalDate`
- **Methods:** getters; `isIn(YearMonth month): boolean`
- **Rule:** the amount must be positive.

### Category (enum)
- **Values:** `RENT`, `FOOD`, `TRANSPORTATION`, `TUITION`, `ENTERTAINMENT`, `OTHER`
- **Methods:** `getDisplayName()`, `fromDisplayName(String)`

### MonthlySpending
- **Instance variables:** `month: YearMonth`, `expenses: List<Expense>` (only the expenses in that month)
- **Methods:** `getTotal(): double`, `getTotalsByCategory(): Map<Category, Double>`

### MonthlyBudget
- **Instance variables:** `month: YearMonth`, `limit: double`
- **Methods:** `getRemaining(MonthlySpending): double`, `isOverBudget(MonthlySpending): boolean`
- **Rule:** the limit must be positive.

### ExchangeRate
- **Instance variables:** `fromCurrency: String`, `toCurrency: String`, `rate: double`
- **Methods:** `convert(double amount): double`
- **Rule:** the rate must be positive.

---

## API Usage

**API Name:** ExchangeRate-API, Open Access endpoint (no API key needed)

**Documentation:** https://www.exchangerate-api.com/docs/free

**How it is used in the program:**
- `GET https://open.er-api.com/v6/latest/CAD` returns today's rates from CAD to about 160 currencies.
- `ExchangeRateApiDataAccessObject` calls it with OkHttp, reads the rate for the student's home currency
  with org.json, and returns an `ExchangeRate` entity to the *View Spending in Home Currency* use case.
- No internet, an unknown currency, or an unexpected response becomes an
  `ExchangeRateUnavailableException`, so the use case can show a message instead of crashing.
- The use case depends only on `ExchangeRateDataAccessInterface`, so its tests can use a fake rate source.

**Successfully called from Java:** Yes. `ExchangeRateApiDataAccessObject.getRate("CAD", "KRW")` returned
a rate of about 937 on Nov 16, and an unknown currency code produced `ExchangeRateUnavailableException`.

---

## Design Diagrams

### UML Class Diagram
To be added in `docs/` (PlantUML source) once the use cases are merged.

### Sequence Diagrams
To be added in `docs/`, one per use case.

---

## Design

### Clean Architecture

```
src/main/java
├── entity/             Expense, Category, MonthlySpending, MonthlyBudget, ExchangeRate
├── use_case/           one package per use case, each with InputBoundary, InputData, Interactor,
│                       OutputBoundary, OutputData and a DataAccessInterface
│                       (add_expense, set_budget, view_summary, convert_currency)
├── interface_adapter/  a Controller and Presenter per use case, plus the dashboard
│                       ViewModel and State
├── data_access/        FileBudgetDataAccessObject, InMemoryBudgetDataAccessObject,
│                       ExchangeRateApiDataAccessObject
├── view/               DashboardView and one panel per use case, e.g. AddExpensePanel (Swing)
└── app/                Main, AppBuilder (wires everything together)
```

Dependencies point inward: interactors only know about entities and the interfaces in their own
package, never about Swing, OkHttp or files. Presenters update the `DashboardViewModel`, and
`DashboardView` listens to it (Observer). `AppBuilder` assembles the app one use case at a time
(Builder) and chooses which data access implementations to plug in (Dependency Inversion).

---

## Current Progress

| Part | Status |
|------|--------|
| App skeleton: Maven project, `Main`, `AppBuilder`, dashboard view model, `DashboardView` with four placeholder panels | Merged (PR 1) |
| Expense entities: `Category`, `Expense` | Merged (PR 2) |
| `ExchangeRate` entity, exchange rate interface and API data access object | Merged (PR 3) |
| Budget entities: `MonthlyBudget`, `MonthlySpending` | Merged (PR 4) |
| Data storage: in-memory and JSON file data access objects | Merged (PR 5) |
| Use case: Add Expense (interactor, controller, presenter, `AddExpensePanel`, interactor tests) | Merged (PR 6) |
| Use case: Set Monthly Budget (interactor, controller, presenter, `BudgetPanel`, interactor tests) | Merged (PR 7) |
| Use case: View Monthly Summary (interactor, controller, presenter, `SummaryPanel`, interactor tests) | Merged (PR 8) |
| Use case: View Spending in Home Currency (interactor, controller, presenter, `CurrencyPanel`, interactor tests) | This PR (PR 9) |
| Unit tests for every interactor, 100% line coverage of entities and use cases | In progress: entities and all four interactors tested |
| UML class diagram and sequence diagrams | To do |

---

## How to Run

Requires Java 8 or newer and Maven (or open the project in IntelliJ and run `app.Main`).

```bash
mvn compile exec:java      # start the app
mvn test                   # run the unit tests
```

Add Expense and Set Monthly Budget save to `budget_data.json` and show a confirmation at the bottom
of the window. Invalid input shows an error there instead. View Monthly Summary loads the current
month at startup. Type a month and press **Show month** to see that month's expenses, budget, remaining
amount, and category totals. The remaining amount is red when spending is over the budget. Choose a
home currency and press **Convert** to see the shown month's budget, spending, and category totals in
that currency (needs internet for the live exchange rate).

To keep data somewhere else, pass a file path as the first program argument.

---

## Notes
- Amounts are entered in CAD (the local currency); home currency conversion is display-only.
- Each pull request should update the relevant section of this README.
