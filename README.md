# Elkin Hurtado
**Confidential. *Please do not make public this document or your solution.***

<p>
<img width="500" src="https://i.imgur.com/jsTbKua.png">
</p>

# Veritran's Code Exercise

# We will pay extreme attention to:
- **Object oriented design.**
- **Testable design & TDD.**
- **Modular architecture.**
- **Clean code, KISS, DRY**
- **Extensibility**
- **SOLID Principles.**
- **Attention to details.**

# Background

Your mission is to create a simple banking system. Think about your personal
bank account experience. When in doubt, go for the simplest solution. You don’t
need to create any kind of user interface or make these features available
through any server. It's not required to expose as a web service nor implement
a database.

The exercise evolves as a sequence of iterations. Try to complete each
iteration before reading the next one.

## What to do
### Iteration 1: Add the deposit feature

You can follow this user story:

```gherkin
Feature: Deposit money into an account
    As a client of the bank
    I want to deposit money into my account
    In order to increase my balance

    Scenario: An existing client deposits money into his account
        Given an existing client with id “francisco” with 100 USD in his account
        When he deposits 10 USD into his account
        Then the balance of his account is 110 USD
```

### Iteration 2: Update the deposit feature

Currently, users can deposit negative amounts of money, which does not make
sense. Add a new test case to fix this issue.

### Iteration 3: Add the withdrawal feature

You can follow this user story:

```gherkin
Feature: Withdraw money from an account
    As a client of the bank
    I want to withdraw money from my account
    In order to have cash

    Scenario: An existing client withdraws money from his account
        Given an existing client with id “francisco” with 100 USD in his account
        When he withdraws 10 USD from his account
        Then the new balance is 90 USD
```

### Iteration 4: Cover border cases for the withdrawal feature

Add a scenario in the withdrawal feature for the case when a withdrawal
generates an overdraft. Withdrawal of amounts bigger than the current account
balance must not be allowed.

### Iteration 5: Update the withdrawal feature

Check whether it is possible to withdraw a negative value. If it is possible,
fix it. Add the corresponding test case to fix this issue.

## Retrospective

- Are you keeping up with the requirements? Has any iteration been a big
  challenge?
- Do you feel good about your design? Is it scalable and easily adapted to new
  requirements that come up in further iterations?
- Is everything tested? Are you confident in your tests?

### Iteration 6: Add the transfer feature

From now on, users will be able to transfer money from their account to the
account of another user. Add this feature. First write a user story for it.
Write the scenarios for the main case, but also write scenarios for border
cases, such as: can one transfer money one doesn't have? There may be others.
