package com.lothrazar.cyclicmagic.log;
import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraftforge.common.config.Configuration;

public class ModLogger implements IHasConfig {
  private Logger logger;
  private boolean sendInfo = true;//info are things we WANT to stay on release. by default
  private boolean sendLogs = false;//in config. only used for dev or live debugging
  private boolean runUnitTests;
  public ModLogger(Logger l) {
    logger = l;
  }
  /**
   * info defaults to TRUE in config file use this for logs you want to run in
   * release
   * 
   * @param string
   */
  public void info(String string) {
    if (sendInfo)
      logger.info(string);
  }
  /**
   * Defaults to FALSE in config file use for dev debugging, and then leave some
   * in place for release that will safely not spam out unless turned on
   * 
   * @param string
   */
  public void log(String string) {
    if (sendLogs)
      logger.info(string);
  }
  /**
   * Always send the log in every environment
   * 
   * @param string
   */
  public void error(String string) {
    logger.error(string);
  }
  /**
   * always check this before running a unit test
   * 
   * @return
   */
  public boolean runUnitTests() {
    return this.runUnitTests;
  }
  /**
   * logs only if runUnitTests() is true
   * 
   * @param string
   */
  public void logTestResult(String string) {
    if (runUnitTests())
      logger.info("[UnitTest]" + string);
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.logging;
    sendInfo = config.getBoolean("Information", category, true, "Log basic game startup information such as ore dictionary registration");
    runUnitTests = config.getBoolean("UnitTests", category, false, "Run unit tests on startup and log the result.  Still experimental and not widely used");
    sendLogs = config.getBoolean("Debug", category, false, "Log debug related information.  This can be very spammy, only used for debugging problems or new features, so just leave it off normally.");
  }
}
