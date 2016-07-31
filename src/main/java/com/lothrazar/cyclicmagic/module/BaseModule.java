package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ICyclicModule;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public abstract class BaseModule implements ICyclicModule {
  public void onPreInit() {
  }
  public void onInit() {
  }
  public void onPostInit() {
  }
  @Override
  public void onServerStarting(FMLServerStartingEvent event) {
  }
  public void syncConfig(Configuration config){
    
  }
}
