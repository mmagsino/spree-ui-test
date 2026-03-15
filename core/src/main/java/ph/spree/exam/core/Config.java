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

  /** Example page URL (e.g. https://playwright.dev) */
  public static String getExampleUrl() {
    return get("app.example.url", "https://playwright.dev");
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

}
