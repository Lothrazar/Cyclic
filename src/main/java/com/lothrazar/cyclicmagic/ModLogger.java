package com.lothrazar.cyclicmagic;
import org.apache.logging.log4j.Logger;

public class ModLogger {
  private Logger logger;
  public boolean sendInfo = false;// disable this for release
  public ModLogger(Logger l) {
    logger = l;
  }
  public void info(String string) {
    if (sendInfo)
      logger.info(string);
  }
  public void error(String string) {
    logger.error(string);
  }
}
