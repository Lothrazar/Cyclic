package com.lothrazar.cyclic;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class CyclicLogger {

  private Logger logger;
  public static BooleanValue LOGINFO;

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
    if (LOGINFO.get()) {
      logger.info(string);
    }
  }
}
