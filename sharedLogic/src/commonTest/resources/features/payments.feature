Feature: Payment Management
  As a user
  I want to manage my payments
  So that I can send money and track my transactions

  Scenario: Successful payment creation
    Given a user enters valid payment details "user@example.com", 50.0, "USD"
    When they submit the payment
    Then the payment should be processed successfully
    And the transaction should be saved to the database

  Scenario: Failed payment due to invalid amount
    Given a user enters an invalid amount "-5.0"
    When they submit the payment
    Then the payment should fail
    And an error message "Invalid Amount" should be shown

  Scenario: Failed payment due to invalid email
    Given a user enters an invalid email "invalid-email"
    When they submit the payment
    Then the payment should fail
    And an error message "Invalid email address" should be shown

  Scenario: Listing past transactions
    Given the user has 2 past transactions in the database
    When they view their transaction history
    Then they should see 2 transactions in the list
