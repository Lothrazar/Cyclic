package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;

public abstract class BaseEventModule extends BaseModule {
  public void onPreInit() {
    ModMain.instance.events.register(this);
  }
}
