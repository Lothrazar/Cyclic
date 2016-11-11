package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ICyclicModule;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public abstract class BaseModule implements ICyclicModule {
  //having these makes implementing each one optional
  public void onPreInit() {
  }
  public void onInit() {
  }
  public void onPostInit() {
  }
  public void onServerStarting(FMLServerStartingEvent event) {
  }
}
