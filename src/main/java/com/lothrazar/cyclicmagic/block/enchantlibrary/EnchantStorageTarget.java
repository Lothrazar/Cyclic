package com.lothrazar.cyclicmagic.block.enchantlibrary;

import com.lothrazar.cyclicmagic.block.enchantlibrary.shelf.TileEntityLibrary;
import com.lothrazar.cyclicmagic.data.QuadrantEnum;

public class EnchantStorageTarget {

  public QuadrantEnum quad = null;
  public TileEntityLibrary library = null;

  public boolean isEmpty() {
    return quad == null || library == null;
  }
}
