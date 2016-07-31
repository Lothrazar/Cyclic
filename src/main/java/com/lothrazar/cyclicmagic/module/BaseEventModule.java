package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ICyclicModule;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraftforge.common.config.Configuration;

public abstract class BaseEventModule implements ICyclicModule {
  public boolean isEnabled() {
    return true;
  }
  public void onPreInit() {
    ModMain.instance.events.addEvent(this);
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
