package ph.spree.exam.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and provides access to configuration from application.properties.
 * Values can be overridden by system properties or environment variables.
 */
public final class Config {

  private static final String PROPERTIES_FILE = "application.properties";
  private static final Properties PROPS = loadProperties();

  private Config() {}

  private static Properties loadProperties() {
    var props = new Properties();
    try (InputStream in = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
      if (in != null) {
        props.load(in);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load " + PROPERTIES_FILE, e);
    }
    return props;
  }

  private static String get(String key, String defaultValue) {
    String value = System.getProperty(key);
    if (value != null && !value.isBlank()) {
      return value.trim();
    }
    value = PROPS.getProperty(key);
    return value != null ? value.trim() : defaultValue;
  }

  private static String get(String key) {
    return get(key, null);
  }

  private static int getInt(String key, int defaultValue) {
    String value = get(key);
    if (value == null || value.isBlank()) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /** Base URL for the application under test (e.g. https://demo.spreecommerce.org) */
  public static String getBaseUrl() {
    return get("app.base.url", "https://demo.spreecommerce.org");
  }

  /** Browser name: chromium, chrome, firefox, webkit. Use "chrome" for installed Google Chrome. */
  public static String getBrowserName() {
    return get("app.browser.name", "chromium");
  }

  /** Run browser in headless mode (true) or with visible window (false). */
  public static boolean isBrowserHeadless() {
    String value = get("app.browser.headless", "true");
    return !"false".equalsIgnoreCase(value) && !"0".equals(value);
  }

  /** Full URL for a path relative to base (e.g. /cart -> https://demo.spreecommerce.org/cart) */
  public static String url(String path) {
    String base = getBaseUrl().replaceAll("/$", "");
    String p = path != null ? path : "";
    if (!p.startsWith("/")) {
      p = "/" + p;
    }
    return base + p;
  }

  /** Example page URL (e.g. https://playwright.dev) */
  public static String getExampleUrl() {
    return get("app.example.url", "https://playwright.dev");
  }


  public static String getHomeUrl() {
    return url(get("app.path.home", "/"));
  }

  public static String getProductsUrl() {
    return url(get("app.path.products", "/products"));
  }

  public static String getCartUrl() {
    return url(get("app.path.cart", "/cart"));
  }

  public static String getLoginUrl() {
    return url(get("app.path.login", "/user/sign_in"));
  }

  public static String getSignUpUrl() {
    return url(get("app.path.signup", "/user/sign_up"));
  }

  public static String getCheckoutPath() {
    return get("app.path.checkout", "/checkout");
  }

  /** Full URL for a product detail page (e.g. /products/silver-chain) */
  public static String getProductDetailUrl(String productSlug) {
    String productsPath = get("app.path.products", "/products");
    String path = productsPath.endsWith("/") ? productsPath + productSlug : productsPath + "/" + productSlug;
    return url(path);
  }

  public static String getTestCardNumber() {
    return get("test.card.number", "4111111111111111");
  }

  public static String getTestCardCvv() {
    return get("test.card.cvv", "123");
  }

  public static String getTestCardExpiry() {
    return get("test.card.expiry", "12/28");
  }

  public static String getTestCardHolderName() {
    return get("test.card.holder.name", "Test User");
  }

  public static int getViewportWidth() {
    return getInt("app.viewport.width", 1280);
  }

  public static int getViewportHeight() {
    return getInt("app.viewport.height", 720);
  }

  /** Delay in milliseconds between each Cucumber step (0 to disable). Default 2000 (2 seconds). */
  public static int getStepDelayMs() {
    return getInt("app.execution.step.delay.ms", 2000);
  }
}
