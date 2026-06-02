package com.lothrazar.cyclic.compat.jei;

import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclic.render.HorseCarrotOverlay;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.renderer.Rect2i;

public class HorseScreenGuiHandler implements IGuiContainerHandler<HorseInventoryScreen> {

  @Override
  public List<Rect2i> getGuiExtraAreas(HorseInventoryScreen screen) {
    Rect2i bounds = HorseCarrotOverlay.getPanelBounds(screen);
    return bounds == null ? Collections.emptyList() : List.of(bounds);
  }
}
