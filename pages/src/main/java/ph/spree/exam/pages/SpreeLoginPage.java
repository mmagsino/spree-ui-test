package ph.spree.exam.pages;

import com.microsoft.playwright.Page;
import ph.spree.exam.core.Config;

/**
 * Page object for the Spree Commerce login page.
 */
public final class SpreeLoginPage extends BasePage {

  public SpreeLoginPage(Page page) {
    super(page);
  }

  public SpreeLoginPage open() {
    page.navigate(Config.getLoginUrl());
    return this;
  }

  public boolean hasLoginForm() {
    return page.getByLabel("Email").or(page.locator("input[name*='email']")).first().isVisible(new com.microsoft.playwright.Locator.IsVisibleOptions().setTimeout(3000));
  }

  public void login(String email, String password) {
    page.getByLabel("Email").or(page.locator("input[name*='email'], input[type='email']")).first().fill(email);
    page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).or(page.locator("input[name*='password'][name*='user']")).first().fill(password);
    page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).first().click();
  }
}
