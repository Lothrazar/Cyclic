package com.lothrazar.cyclicmagic.module;
import net.minecraftforge.common.config.Configuration;

public interface ICyclicModule {
  public void onPreInit();//events must be added here
  public void onInit();
  public void onPostInit();
  public void onServerStarting();
  public void syncConfig(Configuration config);
  public boolean isEnabled();
}
