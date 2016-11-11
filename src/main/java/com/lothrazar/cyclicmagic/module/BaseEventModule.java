package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;

public abstract class BaseEventModule extends BaseModule {
  public void onPreInit() {
    ModCyclic.instance.events.register(this);
  }
}
