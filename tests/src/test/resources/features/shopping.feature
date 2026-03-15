Feature: Complete checkout on Spree Commerce demo store
  As a shopper on the Spree demo store
  I want to log in, add a product to cart, and complete checkout
  So that I can verify order placement end-to-end

  Background:
    Given I am on the Spree demo store home page
    And I sign up with a new account

  Scenario: Complete checkout with product verification
    When I click on the user icon
    And I log in with the newly registered user credentials
    And I browse products and open the product "Pink Scarf"
    And I add the product to the cart
    And I go to the cart
    Then I should see the product "Pink Scarf" with quantity 1 and the correct price
    When I proceed to checkout
    And I add a shipping address
    And I verify the delivery and pricing options are displayed
    And I select a shipping method
    And I select a payment method and enter test card details
    And I complete the order
    Then I should see the order confirmation page with an order number and success message
