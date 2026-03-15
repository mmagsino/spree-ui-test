package ph.spree.exam.pages;

import com.microsoft.playwright.Page;
import ph.spree.exam.core.Config;

/**
 * Page object for the Spree Commerce cart page.
 */
public final class SpreeCartPage extends BasePage {

  public SpreeCartPage(Page page) {
    super(page);
  }

  public SpreeCartPage open() {
    page.navigate(Config.getCartUrl());
    return this;
  }

  public void checkout() {
    // Link only: wait for href to be set (contains /checkout/) before clicking
    var checkoutLink = page.locator("a[data-cart-target='checkoutButton'][href*='/checkout/']");
    checkoutLink.waitFor();
    checkoutLink.click();
    page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
  }

  public String getProductQuantity() {
    var qtyLocator = page.locator(".cart-item input[type='number'], [data-hook='cart_item'] input, .line-item-quantity input");
    if (qtyLocator.count() > 0) {
      String val = qtyLocator.first().inputValue();
      return val != null && !val.isEmpty() ? val : "1";
    }
    return "1";
  }

  public String getProductPrice() {
    var priceLocator = page.locator(".cart-item .price, [data-hook='cart_item'] .price, .line-item-total .price, [class*='price']");
    return priceLocator.count() > 0 ? priceLocator.first().textContent().trim() : "";
  }

  public boolean hasPriceDisplayed() {
    return page.locator(".price, [class*='price'], [data-hook*='price']").count() > 0
        || page.getByText("$").count() > 0;
  }

  public boolean hasProduct(String productName) {
    return page.getByText(productName).first().isVisible();
  }
}
