package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.module.BaseModule.ModuleType;
import net.minecraftforge.common.config.Configuration;

public interface ICyclicModule {
  public ICyclicModule setType(ModuleType t);
  public ModuleType getType();
  public void register();
  public void syncConfig(Configuration config);
  public boolean isEnabled();
}
