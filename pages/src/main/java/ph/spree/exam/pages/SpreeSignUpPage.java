package ph.spree.exam.pages;

import com.microsoft.playwright.Page;
import ph.spree.exam.core.Config;

/**
 * Page object for the Spree Commerce sign up page.
 */
public final class SpreeSignUpPage extends BasePage {

  public SpreeSignUpPage(Page page) {
    super(page);
  }

  public SpreeSignUpPage open() {
    page.navigate(Config.getSignUpUrl());
    return this;
  }

  public void signUp(String email, String password) {
    page.getByLabel("Email").fill(email);
    page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill(password);
    page.getByLabel("Password Confirmation").fill(password);
    page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign Up")).click();
  }
}
