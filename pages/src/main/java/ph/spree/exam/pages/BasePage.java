package ph.spree.exam.pages;

import com.microsoft.playwright.Page;

/**
 * Base class for all page objects. Provides common actions and access to the underlying Page.
 */
public abstract class BasePage {

  protected final Page page;

  protected BasePage(Page page) {
    this.page = page;
  }

  public Page getPage() {
    return page;
  }

  public void navigate(String path) {
    page.navigate(path);
  }

  public String getTitle() {
    return page.title();
  }
}
