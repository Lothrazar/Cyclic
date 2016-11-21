package com.lothrazar.cyclicmagic;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class ModLogger {
  private Logger logger;
  public boolean sendLogs = true;// disable this for release
  public ModLogger(Logger l) {
    logger = l;
  }
  public void info(String string) {
    if (sendLogs)
      logger.info(string);
  }
  public void warn(String string) {
    logger.log(Level.WARN, string);
  }
  public void error(String string) {
    logger.error(string);
  }
}
