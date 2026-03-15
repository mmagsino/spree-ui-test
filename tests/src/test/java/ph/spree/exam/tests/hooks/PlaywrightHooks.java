package ph.spree.exam.tests.hooks;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Video;
import ph.spree.exam.core.Config;
import ph.spree.exam.core.PlaywrightManager;
import ph.spree.exam.core.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlaywrightHooks {

  private static PlaywrightManager manager;
  private BrowserContext context;
  private Page page;

  @Before(order = 0)
  public void beforeAll() {
    if (manager == null) {
      var builder = PlaywrightManager.builder()
          .browser(Config.getBrowserName())
          .headless(Config.isBrowserHeadless())
          .baseUrl(Config.getBaseUrl());
      if ("chrome".equalsIgnoreCase(Config.getBrowserName())) {
        builder.channel("chrome");
      }
      manager = builder.build();
      manager.start();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        if (manager != null) {
          manager.stop();
        }
      }));
    }
  }

  @Before(order = 1)
  public void beforeScenario() {
    var videoDir = Path.of("target/videos");
    videoDir.toFile().mkdirs();
    context = manager.getBrowser().newContext(new Browser.NewContextOptions()
        .setViewportSize(Config.getViewportWidth(), Config.getViewportHeight())
        .setBaseURL(Config.getBaseUrl())
        .setRecordVideoDir(videoDir));
    page = context.newPage();
    TestContext.setPage(page);
  }

  @AfterStep
  public void afterStep() throws InterruptedException {
    int delayMs = Config.getStepDelayMs();
    if (delayMs > 0) {
      Thread.sleep(delayMs);
    }
  }

  @After(order = 1)
  public void afterScenario(Scenario scenario) {
    Video video = page != null ? page.video() : null;
    try {
      if (scenario.isFailed() && page != null) {
        var dir = Path.of("target/screenshots");
        dir.toFile().mkdirs();
        page.screenshot(new Page.ScreenshotOptions().setPath(
            dir.resolve(scenario.getName().replaceAll("[^a-zA-Z0-9_-]", "_") + ".png")));
      }
    } finally {
      if (context != null) {
        context.close(); // Saves video to recordVideoDir
        if (video != null) {
          Path videoPath = video.path();
          if (videoPath != null && videoPath.toFile().exists()) {
            try {
              byte[] videoBytes = Files.readAllBytes(videoPath);
              scenario.attach(videoBytes, "video/webm", scenario.getName());
            } catch (java.io.IOException ignored) { }
          }
        }
      }
      TestContext.clear();
      context = null;
      page = null;
    }
  }
}
