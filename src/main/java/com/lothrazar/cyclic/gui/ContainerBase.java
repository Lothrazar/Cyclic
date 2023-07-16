package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.library.gui.ContainerFlib;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;

public abstract class ContainerBase extends ContainerFlib {

  protected ContainerBase(MenuType<?> type, int id) {
    super(type, id);
  }

  protected void trackAllIntFields(TileBlockEntityCyclic tile, int fieldCount) {
    for (int f = 0; f < fieldCount; f++) {
      trackIntField(tile, f);
    }
  }

  protected void trackIntField(TileBlockEntityCyclic tile, int fieldOrdinal) {
    addDataSlot(new DataSlot() {

      @Override
      public int get() {
        return tile.getField(fieldOrdinal);
      }

      @Override
      public void set(int value) {
        tile.setField(fieldOrdinal, value);
      }
    });
  }
}
