Feature: Send Payment
  As a user
  I want to send payments to recipients
  So that I can transfer money easily

  Scenario: User sends valid payment
    Given I am on the payment screen
    And I enter recipient email "test@example.com"
    And I enter amount "50.00"
    And I select currency "USD"
    When I click send payment button
    Then I should see success message "Payment sent successfully"
    And the payment should appear in transaction history

  Scenario: User enters invalid email
    Given I am on the payment screen
    And I enter recipient email "invalid-email"
    And I enter amount "100.00"
    And I select currency "EUR"
    When I click send payment button
    Then I should see error message "Invalid email format"

  Scenario: User enters zero amount
    Given I am on the payment screen
    And I enter recipient email "test@example.com"
    And I enter amount "0"
    And I select currency "USD"
    When I click send payment button
    Then I should see error message "Amount must be greater than 0"