package com.lothrazar.cyclicmagic.block.base;

/**
 * generic tile and packet that sets text
 * TODO: if needed up this to use indices similar 
 * to the getField/setField in the IInventory
 * @author Sam
 *
 */
public interface ITileTextbox {
  public void setText(String s);
  public String getText();
}
