package ph.spree.exam.tests;

import ph.spree.exam.core.Config;

/**
 * Test card details for Spree/Vendo checkout.
 * Values are loaded from application.properties via {@link Config}.
 * Refer to: https://docs.vendoservices.com/docs/how-to-run-test-transactions
 */
public final class TestCardDetails {

  /** Standard test card for basic transactions */
  public static final String CARD_NUMBER = Config.getTestCardNumber();
  /** Any 3-digit number for CVV */
  public static final String CVV = Config.getTestCardCvv();
  /** Any future expiration date (MM/YY) */
  public static final String EXPIRY = Config.getTestCardExpiry();
  public static final String CARDHOLDER_NAME = Config.getTestCardHolderName();

  private TestCardDetails() {}
}
