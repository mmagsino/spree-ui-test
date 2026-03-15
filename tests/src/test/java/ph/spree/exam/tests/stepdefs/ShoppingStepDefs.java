package ph.spree.exam.tests.stepdefs;

import static org.junit.jupiter.api.Assertions.*;

import ph.spree.exam.core.TestContext;
import ph.spree.exam.pages.*;
import ph.spree.exam.tests.TestCardDetails;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.datafaker.Faker;

public class ShoppingStepDefs {

  private static final Faker FAKER = new Faker();

  private SpreeHomePage homePage;
  private SpreeSignUpPage signUpPage;
  private SpreeLoginPage loginPage;
  private SpreeProductsPage productsPage;
  private SpreeProductPage productPage;
  private SpreeCartPage cartPage;
  private SpreeCheckoutPage checkoutPage;

  private String signUpEmail;
  private String signUpPassword;

  @Given("I am on the Spree demo store home page")
  public void iAmOnTheSpreeDemoStoreHomePage() {
    homePage = new SpreeHomePage(TestContext.getPage());
    homePage.open();
  }

  @And("I sign up with a new account")
  public void iSignUpWithANewAccount() {
    signUpEmail = FAKER.internet().emailAddress();
    signUpPassword = FAKER.credentials().password();
    signUpPage = new SpreeSignUpPage(TestContext.getPage());
    signUpPage.open();
    signUpPage.signUp(signUpEmail, signUpPassword);
  }

  @When("I click on the user icon")
  public void iClickOnTheUserIcon() {
    homePage = new SpreeHomePage(TestContext.getPage());
    homePage.clickUserIcon();
  }

  @And("I log in with the newly registered user credentials")
  public void iLogInWithTheNewlyRegisteredUserCredentials() {
    loginPage = new SpreeLoginPage(TestContext.getPage());
    loginPage.open();
    if (loginPage.hasLoginForm()) {
      loginPage.login(signUpEmail, signUpPassword);
    }
  }

  @And("I browse products and open the product {string}")
  public void iBrowseProductsAndOpenTheProduct(String productName) {
    productsPage = new SpreeProductsPage(TestContext.getPage());
    productsPage.open();
    productsPage.openProduct(productName);
  }

  @And("I add the product to the cart")
  public void iAddTheProductToTheCart() {
    productPage = new SpreeProductPage(TestContext.getPage());
    productPage.addToCart();
  }

  @And("I go to the cart")
  public void iGoToTheCart() {
    cartPage = new SpreeCartPage(TestContext.getPage());
    cartPage.open();
  }

  @Then("I should see the product {string} with quantity {int} and the correct price")
  public void iShouldSeeTheProductWithQuantityAndPrice(String productName, int expectedQty) {
    assertTrue(cartPage.hasProduct(productName), "Expected product '" + productName + "' in cart");
    String quantity = cartPage.getProductQuantity();
    assertEquals(String.valueOf(expectedQty), quantity, "Expected quantity " + expectedQty);
    assertTrue(cartPage.hasPriceDisplayed(), "Expected product price to be displayed");
  }

  @And("I proceed to checkout")
  public void iProceedToCheckout() {
    cartPage = new SpreeCartPage(TestContext.getPage());
    cartPage.checkout();
  }

  @And("I add a shipping address")
  public void iAddAShippingAddress() {
    checkoutPage = new SpreeCheckoutPage(TestContext.getPage());
    checkoutPage.fillShippingAddress(
        FAKER.name().firstName(),
        FAKER.name().lastName(),
        FAKER.address().streetAddress(),
        FAKER.address().city(),
        "California",
        FAKER.address().zipCode(),
        FAKER.phoneNumber().cellPhone()
    );
    checkoutPage.continueToNextStep();
  }

  @And("I verify the delivery and pricing options are displayed")
  public void iVerifyTheDeliveryAndPricingOptionsAreDisplayed() {
    checkoutPage = new SpreeCheckoutPage(TestContext.getPage());
    assertTrue(checkoutPage.hasDeliveryOptions(), "Expected delivery/shipping options to be displayed");
  }

  @And("I select a shipping method")
  public void iSelectAShippingMethod() {
    checkoutPage.selectFirstShippingMethod();
    checkoutPage.continueToNextStep();
  }

  @And("I select a payment method and enter test card details")
  public void iSelectAPaymentMethodAndEnterTestCardDetails() {
    checkoutPage.fillPaymentDetails(
        TestCardDetails.CARD_NUMBER,
        TestCardDetails.EXPIRY,
        TestCardDetails.CVV,
        TestCardDetails.CARDHOLDER_NAME
    );
  }

  @And("I complete the order")
  public void iCompleteTheOrder() {
    checkoutPage.placeOrder();
  }

  @Then("I should see the order confirmation page with an order number and success message")
  public void iShouldSeeTheOrderConfirmationPageWithOrderNumberAndSuccessMessage() {
    assertTrue(checkoutPage.hasOrderConfirmation(), "Expected order confirmation to be displayed");
    assertTrue(checkoutPage.hasSuccessMessage(), "Expected success message to be displayed");
    String orderNumber = checkoutPage.getOrderNumber();
    assertFalse(orderNumber.isEmpty(), "Expected order number to be displayed");
  }
}
