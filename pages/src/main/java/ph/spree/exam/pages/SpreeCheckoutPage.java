package ph.spree.exam.pages;

import com.microsoft.playwright.Page;

/**
 * Page object for the Spree Commerce checkout flow.
 */
public final class SpreeCheckoutPage extends BasePage {

  public SpreeCheckoutPage(Page page) {
    super(page);
  }

  public void fillShippingAddress(String firstName, String lastName, String address, String city, String state, String zip, String phone) {
    // DOM from #shipping section at /checkout/{orderId}
    var shipping = page.locator("#shipping");
    shipping.locator("#order_ship_address_attributes_firstname").waitFor();
    shipping.locator("#order_ship_address_attributes_firstname").fill(firstName);
    shipping.locator("#order_ship_address_attributes_lastname").fill(lastName);
    shipping.locator("#order_ship_address_attributes_address1").fill(address);
    shipping.locator("#order_ship_address_attributes_city").fill(city);
    shipping.locator("#order_ship_address_attributes_state_id")
        .selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(state));
    shipping.locator("#order_ship_address_attributes_zipcode").fill(zip);
    shipping.locator("#order_ship_address_attributes_phone").fill(phone);
  }

  public void continueToNextStep() {
    page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).first().click();
  }

  public void selectFirstShippingMethod() {
    page.locator("input[type='radio'][name*='shipping'], input[type='radio'][name*='delivery']").first().check();
  }

  public boolean hasDeliveryOptions() {
    return page.getByText("Shipping").or(page.getByText("Delivery")).or(page.locator("[data-hook='shipping-method']")).first().isVisible();
  }

  public void fillPaymentDetails(String cardNumber, String expiry, String cvv, String cardholderName) {
    // DOM from #checkout_form_payment - Stripe Payment Element when Card is chosen
    // Card number, Expiration date, Security code are in iframe[title="Secure payment input frame"]
    var form = page.locator("#checkout_form_payment");
    var paymentEl = form.locator("[data-checkout-stripe-target='paymentElement']");
    paymentEl.locator("iframe[title='Secure payment input frame']").first().waitFor();
    var cardNum = cardNumber.replaceAll("\\s", "");
    var frame = page.frameLocator("#checkout_form_payment [data-checkout-stripe-target='paymentElement'] iframe[title='Secure payment input frame']").first();
    frame.locator("input").nth(0).fill(cardNum);
    frame.locator("input").nth(1).fill(expiry);
    frame.locator("input").nth(2).fill(cvv);
    // Cardholder name: often pre-filled from billing; parameter kept for API compatibility
  }

  public void placeOrder() {
    var button = page.locator("#checkout-payment-submit");
    button.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    button.click();
  }

  public boolean hasOrderConfirmation() {
    String orderConfirmation = page.getByText("Your order is confirmed!").innerText();
    return (orderConfirmation != null && !orderConfirmation.isEmpty());
  }

  public String getOrderNumber() {
    return page.locator("text=/R[0-9]{9}/").innerText();
  }

  public boolean hasSuccessMessage() {
    String successMessage = page.getByText("Paid").innerText();
    return (successMessage != null && !successMessage.isEmpty());
  }
}
