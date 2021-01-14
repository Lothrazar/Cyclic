package com.lothrazar.cyclic;

import org.apache.logging.log4j.Logger;

public class CyclicLogger {

  private Logger logger;

  public CyclicLogger(Logger logger) {
    this.logger = logger;
  }

  public void error(String string) {
    logger.error(string);
  }

  public void error(String string, Object e) {
    logger.error(string, e);
  }

  public void info(String string) {
    //default for all releases is false to prevent spam-logs slipping out
    if (ConfigRegistry.LOGINFO.get()) {
      logger.info(string);
    }
  }
}
