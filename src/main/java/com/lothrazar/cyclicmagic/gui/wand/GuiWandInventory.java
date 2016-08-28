package com.lothrazar.cyclicmagic.gui.wand;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiBaseContainer {
  private final InventoryWand inventory;
  private static final ResourceLocation BACKGROUND = new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png"); // 176x156
  private static final ResourceLocation SLOT_CURRENT = new ResourceLocation(Const.MODID, "textures/gui/slot_current.png");
  // slot number, as i '3/9'
  int id = 777;
  final int padding = 4;
  ContainerWand container;
  private EntityPlayer player;
  public GuiWandInventory(ContainerWand containerItem, ItemStack wand) {
    super(containerItem);
    this.inventory = containerItem.inventory;
    this.container = containerItem;
    this.player = inventory.getPlayer();
  }
  @Override
  public void initGui() {
    super.initGui();
    int y = this.guiTop + padding;
    int x = this.guiLeft + 5;
    int width = 20;
    width = 50;
    ButtonBuildToggle btn = new ButtonBuildToggle(player, id, x, y, width);
    this.buttonList.add(btn);
    x += width + padding;
    ButtonWandReset b = new ButtonWandReset(player, id, x, y, width);
    if (ItemCyclicWand.BuildType.getSlot(UtilSpellCaster.getPlayerWandIfHeld(player)) == 0) {
      b.enabled = false;
    }
    this.buttonList.add(b);
  }
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    int guiLeft = (this.width - 176) / 2;
    int guiTop = (this.height - 166) / 2;
    GlStateManager.color(1F, 1F, 1F);
    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();
    int active = ItemCyclicWand.BuildType.getSlot(UtilSpellCaster.getPlayerWandIfHeld(player));
    for (Slot s : this.container.inventorySlots) {
      if (active == s.getSlotIndex()) {
        Minecraft.getMinecraft().renderEngine.bindTexture(SLOT_CURRENT);
        this.drawTexturedModalRect(guiLeft + s.xDisplayPosition, guiTop + s.yDisplayPosition, 0, 0, 16, 16);
        break;
      }
    }
    GlStateManager.popMatrix();
  }
  @Override
  public void onGuiClosed() {
    inventory.closeInventory(inventory.getPlayer());
    super.onGuiClosed();
  }
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(getBackground());
    this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
  }
  @Override
  public String getTitle() {
    return null;
  }
  public ResourceLocation getBackground() {
    return BACKGROUND;
  }
}
