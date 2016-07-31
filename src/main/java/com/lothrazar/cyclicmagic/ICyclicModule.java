package com.lothrazar.cyclicmagic;
import net.minecraftforge.common.config.Configuration;

public interface ICyclicModule extends IHasConfig{
  public void onPreInit();//events must be added here
  public void onInit();
  public void onPostInit();
  public void onServerStarting();
  public void syncConfig(Configuration config);
}
