package com.lothrazar.cyclicmagic.gui;
public interface ITileFuel {
  public boolean hasFuel();
  public int getFuelCurrent();
  public int getSpeed();
  public void incrementSpeed();
  public void decrementSpeed();
}
