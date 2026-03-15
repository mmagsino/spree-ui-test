package ph.spree.exam.core;

import com.microsoft.playwright.*;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Manages Playwright lifecycle: Playwright instance, Browser, and BrowserContext.
 * Use {@link #start()} to initialize and {@link #stop()} to clean up.
 */
public final class PlaywrightManager {

  private Playwright playwright;
  private Browser browser;
  private BrowserContext context;

  private final String browserName;
  private final String channel;
  private final boolean headless;
  private final String baseUrl;

  private PlaywrightManager(String browserName, String channel, boolean headless, String baseUrl) {
    this.browserName = browserName;
    this.channel = channel;
    this.headless = headless;
    this.baseUrl = baseUrl;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void start() {
    playwright = Playwright.create();
    BrowserType browserType = switch (browserName.toLowerCase()) {
      case "firefox" -> playwright.firefox();
      case "webkit" -> playwright.webkit();
      default -> playwright.chromium();
    };
    var launchOpts = new BrowserType.LaunchOptions().setHeadless(headless);
    if (channel != null && !channel.isBlank()) {
      launchOpts.setChannel(channel);
    }
    browser = browserType.launch(launchOpts);
    context = browser.newContext(new Browser.NewContextOptions()
        .setViewportSize(1280, 720)
        .setBaseURL(baseUrl != null ? baseUrl : ""));
  }

  public void stop() {
    if (context != null) {
      context.close();
      context = null;
    }
    if (browser != null) {
      browser.close();
      browser = null;
    }
    if (playwright != null) {
      playwright.close();
      playwright = null;
    }
  }

  public Page newPage() {
    ensureStarted();
    return context.newPage();
  }

  public BrowserContext newContext(Consumer<Browser.NewContextOptions> options) {
    ensureStarted();
    var opts = new Browser.NewContextOptions().setViewportSize(1280, 720);
    if (options != null) {
      options.accept(opts);
    }
    return browser.newContext(opts);
  }

  public BrowserContext getContext() {
    ensureStarted();
    return context;
  }

  public Browser getBrowser() {
    ensureStarted();
    return browser;
  }

  public void startTracing(Path tracePath) {
    ensureStarted();
    context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
  }

  public void stopTracing(Path tracePath) {
    if (context != null) {
      context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
    }
  }

  private void ensureStarted() {
    if (playwright == null || browser == null || context == null) {
      throw new IllegalStateException("PlaywrightManager not started. Call start() first.");
    }
  }

  public static final class Builder {
    private String browserName = "chromium";
    private String channel = null;
    private boolean headless = true;
    private String baseUrl = "";

    public Builder browser(String name) {
      this.browserName = name != null ? name : "chromium";
      return this;
    }

    /** Use installed browser channel: "chrome" for Google Chrome, "msedge" for Edge. */
    public Builder channel(String channel) {
      this.channel = channel;
      return this;
    }

    public Builder headless(boolean headless) {
      this.headless = headless;
      return this;
    }

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl != null ? baseUrl : "";
      return this;
    }

    public PlaywrightManager build() {
      return new PlaywrightManager(browserName, channel, headless, baseUrl);
    }
  }
}
