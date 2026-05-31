package com.lothrazar.cyclic.item;

import com.lothrazar.library.item.ItemFlib;

/**
 * shared properties have moved into ItemFlib
 */
public class ItemBaseCyclic extends ItemFlib {

  public ItemBaseCyclic(Properties properties) {
    super(properties);
  }
  public ItemBaseCyclic(Properties properties, Settings settings) {
    super(properties, settings);
  }

}
