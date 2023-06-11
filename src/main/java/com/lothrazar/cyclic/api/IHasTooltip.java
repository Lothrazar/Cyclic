package com.lothrazar.cyclic.api;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface IHasTooltip {

  /**
   * Get the translated list of tooltips for this
   * 
   * @return tooltips as a list
   */
  public List<Component> getTooltips();

  /**
   * Override the tooltip and set it to a new list containing this string
   * 
   * @param tooltip
   */
  public void setTooltip(String tooltip);

  /**
   * Add a tooltip to the list
   * 
   * @param tooltip
   *          tooltip string in
   */
  public void addTooltip(String tooltip);
}
