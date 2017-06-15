package com.lothrazar.cyclicmagic.component.playerextensions;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiInvoButtons {
  @SideOnly(value = Side.CLIENT)
  @SubscribeEvent
  public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
    GuiScreen gui = event.getGui();
    boolean showInvToggle = false;
    boolean showCraftToggle = false;
    if (gui instanceof GuiInventory || gui instanceof GuiPlayerExtended
        || gui instanceof GuiPlayerExtWorkbench
        || gui instanceof GuiScreenHorseInventory) {
      // gui left and top are private, so are the sizes
      int xSize = 176;
      int ySize = 166;
      int guiLeft = (gui.width - xSize) / 2;
      int guiTop = (gui.height - ySize) / 2;
      int x = 44 + guiLeft;
      int y = guiTop;
      EntityPlayer player = Minecraft.getMinecraft().player;
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
      showInvToggle = data.hasInventoryExtended();// && !(gui instanceof GuiPlayerExtWorkbench);
      showCraftToggle = data.hasInventoryCrafting();// && !(gui instanceof GuiPlayerExtended);
      if (event.getButtonList() == null) {
        event.setButtonList(new ArrayList<GuiButton>());
      }
      if (showInvToggle) {
        event.getButtonList().add(new ButtonTabToggleInventory(gui, x, y));
      }
      if (showCraftToggle) {
        event.getButtonList().add(new ButtonTabToggleCrafting(gui, x - 17, y));//the 17 is width + 2
      }
    }
  }
}
