package ph.spree.exam.pages;

import com.microsoft.playwright.Page;
import ph.spree.exam.core.Config;

/**
 * Page object for the Spree Commerce products listing page.
 */
public final class SpreeProductsPage extends BasePage {

  public SpreeProductsPage(Page page) {
    super(page);
  }

  public SpreeProductsPage open() {
    page.navigate(Config.getProductsUrl());
    return this;
  }

  /** Clicks on a product link by name to open its detail page. */
  public void openProduct(String productName) {
    page.getByRole(com.microsoft.playwright.options.AriaRole.LINK).filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(productName)).first().click();
  }
}
