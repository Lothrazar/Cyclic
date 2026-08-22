package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.render.overlay.HorseCarrotOverlay;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.renderer.Rect2i;

import java.util.Collections;
import java.util.List;

public class HorseScreenGuiHandler implements IGuiContainerHandler<HorseInventoryScreen> {

  @Override
  public List<Rect2i> getGuiExtraAreas(HorseInventoryScreen screen) {
    Rect2i bounds = HorseCarrotOverlay.getPanelBounds(screen);
    return bounds == null ? Collections.emptyList() : List.of(bounds);
  }
}
