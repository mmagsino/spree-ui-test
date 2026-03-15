package ph.spree.exam.pages;

import com.microsoft.playwright.Page;
import ph.spree.exam.core.Config;

/**
 * Page object for a Spree Commerce product detail page.
 */
public final class SpreeProductPage extends BasePage {

  public SpreeProductPage(Page page) {
    super(page);
  }

  public SpreeProductPage open(String productSlug) {
    page.navigate(Config.getProductDetailUrl(productSlug));
    return this;
  }

  public void addToCart() {
    page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add To Cart")).first().click();
  }
}
