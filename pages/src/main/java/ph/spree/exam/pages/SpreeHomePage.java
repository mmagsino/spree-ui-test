package ph.spree.exam.pages;

import com.microsoft.playwright.Page;

import ph.spree.exam.core.Config;
/**
 * Page object for the Spree Commerce demo store home page.
 */
public final class SpreeHomePage extends BasePage {

  public SpreeHomePage(Page page) {
    super(page);
  }

  public SpreeHomePage open() {
    page.navigate(Config.getHomeUrl());
    return this;
  }

  public void goToProducts() {
    page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("Shop All")).click();
  }

  public void goToSignUp() {
    page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign Up")).first().click();
  }

  public void goToLogin() {
    page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("Login")).first().click();
  }

  /** Clicks the user/account icon in the header (My Account). */
  public void clickUserIcon() {
    page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("My Account")).first().click();
  }
}
