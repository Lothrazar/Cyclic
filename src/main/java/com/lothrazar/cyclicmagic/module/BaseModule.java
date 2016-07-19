package com.lothrazar.cyclicmagic.module;
import net.minecraftforge.common.config.Configuration;

public abstract class BaseModule implements ICyclicModule {
  public enum ModuleType {
    PREINIT, INIT, POSTINIT, SERVERSTART;
  }
  private ModuleType type;
  public ICyclicModule setRegisterType(ModuleType t) {
    type = t;
    return this;
  }
  public ModuleType getRegisterType() {
    return type;
  }
  public abstract boolean isEnabled();
  public abstract void register();
  public abstract void syncConfig(Configuration config);
}
