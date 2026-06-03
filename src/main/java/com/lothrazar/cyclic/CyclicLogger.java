package com.lothrazar.cyclic;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.logging.log4j.Logger;

public class CyclicLogger {

  private Logger logger;
  public static ModConfigSpec.BooleanValue LOGINFO;

  public CyclicLogger(Logger logger) {
    this.logger = logger;
  }

  public void error(String string) {
    logger.error(string);
  }

  public void error(String string, Object e) {
    logger.error(string, e);
  }

  public void info(String string , Object... e) {
    //default for all releases is false to prevent spam-logs slipping out
    if (LOGINFO.get()) {
      logger.info(string, e);
    }
  }

  public void debug(String string , Object... e) {
    logger.debug(string, e);
  }
}
