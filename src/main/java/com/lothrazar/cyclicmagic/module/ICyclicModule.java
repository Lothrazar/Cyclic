package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.module.BaseModule.ModuleType;
import net.minecraftforge.common.config.Configuration;

public interface ICyclicModule {
  public ICyclicModule setRegisterType(ModuleType t);
  public ModuleType getRegisterType();
  public abstract boolean isEnabled();
  public abstract void register();
  public abstract void syncConfig(Configuration config);
}
