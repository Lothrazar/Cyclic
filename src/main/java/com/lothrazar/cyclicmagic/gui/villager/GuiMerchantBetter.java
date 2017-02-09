package com.lothrazar.cyclicmagic.gui.villager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMerchantBetter extends GuiContainer {
  /** The GUI texture for the villager merchant GUI. */
  private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
  /** The current IMerchant instance in use for this specific merchant. */
  private final EntityVillager merchant;
  /** The button which proceeds to the next available merchant recipe. */
  private GuiMerchantBetter.MerchantButton nextButton;
  /** Returns to the previous Merchant recipe if one is applicable. */
  private GuiMerchantBetter.MerchantButton previousButton;
  /**
   * The integer value corresponding to the currently selected merchant recipe.
   */
  private int selectedMerchantRecipe;
  /** The chat component utilized by this GuiMerchant instance. */
  private final ITextComponent chatComponent;
  private List<MerchantJumpButton> merchButtons = new ArrayList<MerchantJumpButton>();
  public GuiMerchantBetter(InventoryPlayer ip, EntityVillager merch,InventoryMerchant im, World worldIn) {
    super(new ContainerMerchant(ip, merch, worldIn));
    this.merchant = merch;
    this.chatComponent = merch.getDisplayName();
    
  }
  /**
   * Adds the buttons (and other controls) to the screen in question. Called
   * when the GUI is displayed and when the window resizes, the buttonList is
   * cleared beforehand.
   */
  int padding = 4;
  public void initGui() {
    super.initGui();
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    int btnId = 1;
    int x = i + 158;
    int y = j + padding;
    this.nextButton = (GuiMerchantBetter.MerchantButton) this.addButton(new GuiMerchantBetter.MerchantButton(btnId, x, y, true));
    btnId = 2;
    x = i + padding;
    y = j + padding;
    this.previousButton = (GuiMerchantBetter.MerchantButton) this.addButton(new GuiMerchantBetter.MerchantButton(btnId, x, y, false));
    this.nextButton.enabled = false;
    this.previousButton.enabled = false;
    btnId = 3;
    MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
    int idx = 0;
    int h = 20, w = 60;
    x = i + padding - w - 2 * padding;
    y = j + padding ;
    for (MerchantRecipe r : merchantrecipelist) {
      MerchantJumpButton slotBtn = (MerchantJumpButton) this.addButton(new MerchantJumpButton(btnId, x, y, w, h, idx));
      y += h + padding;
 
      btnId++;
      idx++;
      merchButtons.add(slotBtn);
    }
  }
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    String s = this.chatComponent.getUnformattedText();
    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
    this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
    int idx, x, y, spacing = 18, k;
    MerchantRecipe r;
    for (MerchantJumpButton btn : merchButtons) {
      idx = btn.getRecipeIndex();
      r = merchantrecipelist.get(idx);
      k = 0;
      x = btn.xPosition - i + k * spacing;
      y = btn.yPosition - j + padding/2;

      ModCyclic.proxy.renderItemOnScreen(r.getItemToBuy(), x, y);
      k++;
      if (r.getSecondItemToBuy() != null) {
        x = btn.xPosition - i + k * spacing;
        ModCyclic.proxy.renderItemOnScreen(r.getSecondItemToBuy(), x, y);
      }
      k++;
      x = btn.xPosition - i + k * spacing;
      ModCyclic.proxy.renderItemOnScreen(r.getItemToSell(), x, y);
    }
  }
  public void updateScreen() {
    super.updateScreen();
    MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
    if (merchantrecipelist != null) {
      this.nextButton.enabled = this.selectedMerchantRecipe < merchantrecipelist.size() - 1;
      this.previousButton.enabled = this.selectedMerchantRecipe > 0;
    }
  }
  protected void actionPerformed(GuiButton button) throws IOException {
    boolean flag = false;
    MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
    if (button == this.nextButton) {
      ++this.selectedMerchantRecipe;
      if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size()) {
        this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
      }
      flag = true;
    }
    else if (button == this.previousButton) {
      --this.selectedMerchantRecipe;
      if (this.selectedMerchantRecipe < 0) {
        this.selectedMerchantRecipe = 0;
      }
      flag = true;
    }
    else if (button instanceof MerchantJumpButton) {//if (button.id == 3) {
      setRecipeIndex(((MerchantJumpButton) button).getRecipeIndex());
    }
    if (flag) {
      setRecipeIndex(this.selectedMerchantRecipe);
    }
  }
  private void setRecipeIndex(int i) {
    this.selectedMerchantRecipe = i;
    ((ContainerMerchant) this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
    PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
    packetbuffer.writeInt(this.selectedMerchantRecipe);
    this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|TrSel", packetbuffer));
  }
  /**
   * Draws the background layer of this container (behind the items).
   */
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
    if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
      if (this.selectedMerchantRecipe < 0 || this.selectedMerchantRecipe >= merchantrecipelist.size()) { return; }
      MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(this.selectedMerchantRecipe);
      if (merchantrecipe.isRecipeDisabled()) {
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
        this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
      }
    }
  }
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.thePlayer);
    if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
      int i = (this.width - this.xSize) / 2;
      int j = (this.height - this.ySize) / 2;
      //RENDER the selected recipe here //TODO: tooltips of jump buttons
      MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(this.selectedMerchantRecipe);
      ItemStack itemstack = merchantrecipe.getItemToBuy();
      ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
      ItemStack itemstack2 = merchantrecipe.getItemToSell();
      GlStateManager.pushMatrix();
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableColorMaterial();
      GlStateManager.enableLighting();
      this.itemRender.zLevel = 100.0F;
      this.itemRender.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
      this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i + 36, j + 24);
      if (itemstack1 != null) {
        this.itemRender.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, i + 62, j + 24);
      }
      this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
      this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i + 120, j + 24);
      this.itemRender.zLevel = 0.0F;
      GlStateManager.disableLighting();
      if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && itemstack != null) {
        this.renderToolTip(itemstack, mouseX, mouseY);
      }
      else if (itemstack1 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && itemstack1 != null) {
        this.renderToolTip(itemstack1, mouseX, mouseY);
      }
      else if (itemstack2 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && itemstack2 != null) {
        this.renderToolTip(itemstack2, mouseX, mouseY);
      }
      else if (merchantrecipe.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY))) {
        this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
      }
      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
    }
  }
  public IMerchant getMerchant() {
    return this.merchant;
  }
  @SideOnly(Side.CLIENT)
  static class MerchantJumpButton extends GuiButton {
    private int recipeIndex;
    public MerchantJumpButton(int buttonId, int x, int y, int widthIn, int heightIn, int r) {
      super(buttonId, x, y, widthIn, heightIn, "");
      setRecipeIndex(r);
    }
    public int getRecipeIndex() {
      return recipeIndex;
    }
    public void setRecipeIndex(int recipeIndex) {
      this.recipeIndex = recipeIndex;
    }
  }
  @SideOnly(Side.CLIENT)
  static class MerchantButton extends GuiButton {
    private final boolean forward;
    public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_) {
      super(buttonID, x, y, 12, 19, "");
      this.forward = p_i1095_4_;
    }
    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
        mc.getTextureManager().bindTexture(GuiMerchantBetter.MERCHANT_GUI_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        int i = 0;
        int j = 176;
        if (!this.enabled) {
          j += this.width * 2;
        }
        else if (flag) {
          j += this.width;
        }
        if (!this.forward) {
          i += this.height;
        }
        this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
      }
    }
  }
}
