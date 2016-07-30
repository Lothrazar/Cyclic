package com.lothrazar.cyclicmagic.module;
import net.minecraftforge.common.config.Configuration;

public abstract class BaseModule implements ICyclicModule {
  public boolean isEnabled() {
    return true;
  }
  public void onPreInit() {
  }
  public void onInit() {
  }
  public void onPostInit() {
  }
  public void onServerStarting() {
  }
  public void syncConfig(Configuration config){
    
  }
}
