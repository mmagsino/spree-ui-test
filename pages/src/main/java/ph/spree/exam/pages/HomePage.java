package ph.spree.exam.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import ph.spree.exam.core.Config;

/**
 * Example page object for the Playwright homepage.
 */
public final class HomePage extends BasePage {

  public HomePage(Page page) {
    super(page);
  }

  public HomePage open() {
    page.navigate(Config.getExampleUrl());
    return this;
  }

  public String getPageTitle() {
    return page.title();
  }

  public void assertTitleContains(String expected) {
    PlaywrightAssertions.assertThat(page).hasTitle(expected);
  }
}
