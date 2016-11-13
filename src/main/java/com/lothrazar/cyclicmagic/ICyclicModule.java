package com.lothrazar.cyclicmagic;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public interface ICyclicModule{// extends IHasConfig {
  public void onPreInit();//events must be added here
  public void onInit();
  public void onPostInit();
  public void onServerStarting(FMLServerStartingEvent event);
}
