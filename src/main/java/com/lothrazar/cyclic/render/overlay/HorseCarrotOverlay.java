package com.lothrazar.cyclic.render.overlay;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.item.animal.ItemHorseQuartzStep;
import com.lothrazar.cyclic.net.PacketSyncHorseCarrots;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class HorseCarrotOverlay {

  public static final int ICON_SIZE = 16;
  public static final int ROW_GAP = 2;
  public static final int PANEL_PAD_X = 6;

  /**
   * Returns the screen-space rectangle occupied by the carrot indicator panel,
   * or null if no panel is being drawn for this screen. Used by JEI/REI/EMI
   * extra-area handlers so their sidebars don't overlap the panel.
   */
  public static Rect2i getPanelBounds(HorseInventoryScreen screen) {
    AbstractHorse horse = screen.horse;
    if (horse == null) {
      return null;
    }
    List<Entry> entries = collect(horse);
    if (entries.isEmpty()) {
      return null;
    }
    int x = screen.getGuiLeft() + screen.getXSize() + PANEL_PAD_X;
    int y = screen.getGuiTop();
    int rowH = ICON_SIZE + ROW_GAP;
    int h = entries.size() * rowH - ROW_GAP;
    return new Rect2i(x, y, ICON_SIZE, h);
  }

  @SubscribeEvent
  public void onClientLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
    HorseCarrotClientCache.clear();
  }

  @SubscribeEvent
  public void onScreenRenderPost(ScreenEvent.Render.Post event) {
    if (!(event.getScreen() instanceof HorseInventoryScreen horseScreen)) {
      return;
    }
    AbstractHorse horse = horseScreen.horse;
    if (horse == null) {
      return;
    }
    List<Entry> entries = collect(horse);
    if (entries.isEmpty()) {
      return;
    }
    AbstractContainerScreen<?> cs = horseScreen;
    int panelX = cs.getGuiLeft() + cs.getXSize() + PANEL_PAD_X;
    int panelY = cs.getGuiTop();
    GuiGraphicsExtractor g = event.getGuiGraphics();
    int mouseX = event.getMouseX();
    int mouseY = event.getMouseY();
    Entry hovered = null;
    int rowH = ICON_SIZE + ROW_GAP;
    for (int i = 0; i < entries.size(); i++) {
      Entry e = entries.get(i);
      int x = panelX;
      int y = panelY + i * rowH;
      g.renderItem(e.stack, x, y);
      if (e.count > 1) {
        g.renderItemDecorations(Minecraft.getInstance().font, e.stack, x, y, String.valueOf(e.count));
      }
      if (mouseX >= x && mouseX < x + ICON_SIZE && mouseY >= y && mouseY < y + ICON_SIZE) {
        hovered = e;
      }
    }
    if (hovered != null) {
      List<Component> lines = new ArrayList<>();
      lines.add(hovered.stack.getHoverName().copy().withStyle(ChatFormatting.AQUA));
      lines.add(Component.translatable(hovered.descriptionKey).withStyle(ChatFormatting.GRAY));
      g.setTooltipForNextFrame(Minecraft.getInstance().font, lines, Optional.empty(), mouseX, mouseY);
    }
  }

  private static List<Entry> collect(AbstractHorse horse) {
    List<Entry> out = new ArrayList<>();
    PacketSyncHorseCarrots state = HorseCarrotClientCache.get(horse.getId());
    // Persistent NBT is server-only in NeoForge 21.1.x, so counts and boolean flags
    // come from the synced packet cache. Quartz state is derived from STEP_HEIGHT
    // which IS auto-synced by vanilla attribute syncing.
    if (state != null) {
      if (state.redstone > 0) {
        out.add(new Entry(ItemRegistry.CARROT_REDSTONE.get(), state.redstone, "cyclic.horse_panel.speed"));
      }
      if (state.diamond > 0) {
        out.add(new Entry(ItemRegistry.CARROT_DIAMOND.get(), state.diamond, "cyclic.horse_panel.health"));
      }
      if (state.emerald > 0) {
        out.add(new Entry(ItemRegistry.CARROT_EMERALD.get(), state.emerald, "cyclic.horse_panel.jump"));
      }
      if (state.ender > 0) {
        out.add(new Entry(ItemRegistry.CARROT_ENDER.get(), state.ender, "cyclic.horse_panel.ender"));
      }
      if (state.copper()) {
        out.add(new Entry(ItemRegistry.CARROT_COPPER.get(), 0, "cyclic.horse_panel.radar"));
      }
      if (state.netherite()) {
        out.add(new Entry(ItemRegistry.CARROT_NETHERITE.get(), 0, "cyclic.horse_panel.heat"));
      }
      if (state.prismarine()) {
        out.add(new Entry(ItemRegistry.CARROT_PRISMARINE.get(), 0, "cyclic.horse_panel.water"));
      }
    }
    AttributeInstance step = horse.getAttribute(Attributes.STEP_HEIGHT);
    if (step != null && step.getBaseValue() >= ItemHorseQuartzStep.STEP_TARGET) {
      out.add(new Entry(ItemRegistry.CARROT_QUARTZ.get(), 0, "cyclic.horse_panel.step"));
    }
    return out;
  }

  private static final class Entry {
    final ItemStack stack;
    final int count;
    final String descriptionKey;

    Entry(Item item, int count, String descriptionKey) {
      this.stack = new ItemStack(item);
      this.count = count;
      this.descriptionKey = descriptionKey;
    }
  }
}
