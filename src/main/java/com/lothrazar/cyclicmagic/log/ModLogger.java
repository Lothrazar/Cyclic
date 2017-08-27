package com.lothrazar.cyclicmagic.log;
import org.apache.logging.log4j.Logger;

public class ModLogger {
  private Logger logger;
  public boolean sendInfo = true;
  public boolean sendLogs = false;// disable this for release
  public ModLogger(Logger l) {
    logger = l;
  }
  public void info(String string) {
    if (sendInfo)
      logger.info(string);
  }
  public void log(String string) {
    if (sendLogs)
      logger.info(string);
  }
  public void error(String string) {
    logger.error(string);
  }
}
