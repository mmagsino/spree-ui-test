package ph.spree.exam.core;

import com.microsoft.playwright.Page;

/**
 * Thread-local holder for the current Page during a test scenario.
 * Used by Cucumber hooks and step definitions to share page state.
 */
public final class TestContext {

  private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

  private TestContext() {}

  public static void setPage(Page page) {
    PAGE.set(page);
  }

  public static Page getPage() {
    Page page = PAGE.get();
    if (page == null) {
      throw new IllegalStateException("No Page in TestContext. Ensure hooks create a page before steps run.");
    }
    return page;
  }

  public static void clear() {
    PAGE.remove();
  }
}
