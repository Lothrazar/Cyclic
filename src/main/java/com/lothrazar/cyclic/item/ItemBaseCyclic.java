package com.lothrazar.cyclic.item;

import com.lothrazar.library.item.ItemFlib;

/**
 * All shared properties have moved into ItemFlib.
 */
public class ItemBaseCyclic extends ItemFlib {

  /**
   * sets tooltip = true if no settings is passed, overriding the default
   */
  public ItemBaseCyclic(Properties properties) {
    super(properties, new Settings().tooltip());
  }

  /**
   * Makes no changes to settings, passes them up
   */
  public ItemBaseCyclic(Properties properties, Settings settings) {
    super(properties, settings);
  }

}
