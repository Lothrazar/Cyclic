package com.lothrazar.cyclicmagic;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class ModLogger {
  private Logger logger;
  public boolean sendLogs = false;// disable this for release
  public ModLogger(Logger l) {
    logger = l;
  }
  public void info(String string) {
    if (sendLogs)
      logger.info(string);
  }
  public void warn(String string) {
    if (sendLogs)
      logger.log(Level.WARN, string);
  }
  public void error(String string) {
    logger.error(string);
  }
}
