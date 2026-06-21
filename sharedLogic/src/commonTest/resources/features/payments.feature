Feature: Send Payment
  As a user
  I want to send money to a recipient
  So that I can complete a transaction

  Scenario: Successful payment initiation
    Given a user enters valid payment details "user@example.com", 50.0, "USD"
    When they submit the payment
    Then the payment should be processed and saved to history

  Scenario: Invalid amount
    Given a user enters an amount of -5.0
    When they submit the payment
    Then they should see an error message "Amount must be greater than zero"
