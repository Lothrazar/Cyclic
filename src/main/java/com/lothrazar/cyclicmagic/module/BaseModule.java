package com.lothrazar.cyclicmagic.module;
import net.minecraftforge.common.config.Configuration;

public abstract class BaseModule implements ICyclicModule {
  public enum ModuleType {
    PREINIT, INIT, POSTINIT, SERVERSTART;
  }
  private ModuleType type;
  public ICyclicModule setType(ModuleType t) {
    type = t;
    return this;
  }
  public ModuleType getType() {
    return type;
  }
  public boolean isEnabled() {
    return true;
  }
  public abstract void register();
  public abstract void syncConfig(Configuration config);
}
