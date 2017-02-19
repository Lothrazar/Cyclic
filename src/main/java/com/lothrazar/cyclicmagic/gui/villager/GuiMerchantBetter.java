package com.lothrazar.cyclicmagic.gui.villager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.pattern.ContainerPattern;
import com.lothrazar.cyclicmagic.net.PacketSyncVillagerToServer;
import com.lothrazar.cyclicmagic.net.PacketVillagerTrade;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMerchantBetter extends GuiBaseContainer {
  private static final int btnIdAuto = 99;  
  static final int texture_width = 176;
  static final int texture_height = 212;
  //212-176=36
  private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation(Const.MODID, "textures/gui/villager.png");
  private static final ResourceLocation TRADE_REDX_TEXTURE = new ResourceLocation(Const.MODID, "textures/gui/tradex.png");
  public static final ResourceLocation GUI = new ResourceLocation(Const.MODID, Const.Res.folder + "pattern.png");
  private GuiMerchantBetter.MerchantButton nextButton;
  private GuiMerchantBetter.MerchantButton previousButton;
  int itemPadding = 4;
  int btnRowCount = 6;
  private int yLatestJump;
  private int lastUnusedButtonId;
  private int xJump;
  EntityPlayer player;
  private int selectedMerchantRecipe;
  private final ITextComponent chatComponent;
  private List<MerchantJumpButton> merchButtons = new ArrayList<MerchantJumpButton>();
//  private ButtonVillagerPurchase autoBuy;
  public GuiMerchantBetter(InventoryPlayer ip, EntityVillager merch, InventoryMerchantBetter im, World worldIn, List<EntityVillager> all) {
    super(new ContainerMerchantBetter(ip, merch, im, worldIn, all));
    this.chatComponent = merch.getDisplayName();
    player = ip.player;
    this.xSize = texture_width;
    this.ySize = texture_height;
  }
  private ContainerMerchantBetter getContainer() {
    return (ContainerMerchantBetter) this.inventorySlots;
  }
  public void initGui() {
    super.initGui();
    int xMiddle = getMiddleX();
    int yMiddle = getMiddleY();
    int btnId = 1;
    int x = xMiddle + 158;
    int y = yMiddle + itemPadding;
    this.nextButton = (GuiMerchantBetter.MerchantButton) this.addButton(new GuiMerchantBetter.MerchantButton(btnId, x, y, true));
    btnId = 2;
    x = xMiddle + itemPadding;
    y = yMiddle + itemPadding;
    this.previousButton = (GuiMerchantBetter.MerchantButton) this.addButton(new GuiMerchantBetter.MerchantButton(btnId, x, y, false));
    this.nextButton.enabled = false;
    this.previousButton.enabled = false;
    btnId = 3;
//    autoBuy = new ButtonVillagerPurchase(btnIdAuto, x + 1, y + 40);
//    this.buttonList.add(autoBuy);
    this.xJump = this.guiLeft +Const.padding;
    this.yLatestJump = yMiddle+Const.padding;
    this.lastUnusedButtonId = btnId;
  }
  private void validateMerchantButtons() {
    MerchantRecipeList merchantrecipelist = getContainer().getTrades();
    int s = merchantrecipelist.size();
    int h = 20, w = 60;
    int rowSize = w + itemPadding;
    int rowHeight = h + itemPadding;
    int y = this.yLatestJump;
    int currRow;
    for (int i = 0; i < s; i++) {
      if (i >= merchButtons.size()) {
        currRow = i % btnRowCount;
        //        System.out.println("i: " + i + "  rowNum: " + rowNum + " currRow " + currRow);
        y = yLatestJump + currRow * rowHeight;
        //row zero, do nothing else : move left and up
        if (i > 0 && i % btnRowCount == 0) {
          y = yLatestJump;
          xJump = xJump + rowSize + itemPadding / 4;
        }
        MerchantJumpButton slotBtn = (MerchantJumpButton) this.addButton(new MerchantJumpButton(lastUnusedButtonId, this.xJump, y, w, h, i, this));
        this.buttonList.add(slotBtn);
        merchButtons.add(slotBtn);
        lastUnusedButtonId++;
      }
    }
  }
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    String s = this.chatComponent.getUnformattedText();
    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
//    this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
  }
  public void updateScreen() {
    super.updateScreen();
    //update button enable/disable states, with lots of null checks
    MerchantRecipeList merchantrecipelist = this.getContainer().getTrades();//merchant.getRecipes(this.mc.thePlayer);
    if (merchantrecipelist != null) {
      this.nextButton.enabled = this.selectedMerchantRecipe < merchantrecipelist.size() - 1;
      this.previousButton.enabled = this.selectedMerchantRecipe > 0;
      MerchantRecipe trade = merchantrecipelist.get(selectedMerchantRecipe);
      if (trade != null) {
//        autoBuy.enabled = !trade.isRecipeDisabled();
      }
      for (MerchantJumpButton btn : this.merchButtons) {
//        btn.enabled = btn.getRecipeIndex() != this.selectedMerchantRecipe;
      }
    }
    getContainer().setCurrentRecipeIndex(this.selectedMerchantRecipe);
    this.validateMerchantButtons();
  }
  protected void actionPerformed(GuiButton button) throws IOException {
    MerchantRecipeList merchantrecipelist = getContainer().getTrades();
    if (button == this.nextButton) {
      ++this.selectedMerchantRecipe;
      if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size()) {
        this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
      }
      setRecipeIndex(this.selectedMerchantRecipe);
    }
    else if (button == this.previousButton) {
      --this.selectedMerchantRecipe;
      if (this.selectedMerchantRecipe < 0) {
        this.selectedMerchantRecipe = 0;
      }
      setRecipeIndex(this.selectedMerchantRecipe);
    }
    else if (button.id == btnIdAuto) {
      ModCyclic.network.sendToServer(new PacketVillagerTrade(this.selectedMerchantRecipe));
    }
    else if (button instanceof MerchantJumpButton) {
      setRecipeIndex(((MerchantJumpButton) button).getRecipeIndex());
    }
  }
  private void setRecipeIndex(int i) {
    this.selectedMerchantRecipe = i;
    getContainer().setCurrentRecipeIndex(this.selectedMerchantRecipe);
    ModCyclic.network.sendToServer(new PacketVillagerTrade(this.selectedMerchantRecipe));
//    ModCyclic.network.sendToServer(new PacketSyncVillagerToServer(this.selectedMerchantRecipe));
  } 

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
    int thisX = this.getMiddleX();
    int thisY = this.getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, texture_width, texture_height, texture_width, texture_height);
 
  }
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
//    MerchantRecipeList merchantrecipelist = getContainer().getTrades();//merchant.getRecipes(this.mc.thePlayer);
//    if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
//      int i = getMiddleX();
//      int j = getMiddleY();
//      //RENDER the selected recipe here //TODO: tooltips of jump buttons
//      MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(this.selectedMerchantRecipe);
//      ItemStack itemstack = merchantrecipe.getItemToBuy();
//      ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
//      ItemStack itemstack2 = merchantrecipe.getItemToSell();
//      GlStateManager.pushMatrix();
//      RenderHelper.enableGUIStandardItemLighting();
//      GlStateManager.disableLighting();
//      GlStateManager.enableRescaleNormal();
//      GlStateManager.enableColorMaterial();
//      GlStateManager.enableLighting();
//      this.itemRender.zLevel = 100.0F;
//      this.itemRender.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
//      this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i + 36, j + 24);
//      if (itemstack1 != null) {
//        this.itemRender.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
//        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack1, i + 62, j + 24);
//      }
//      this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
//      this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i + 120, j + 24);
//      this.itemRender.zLevel = 0.0F;
//      GlStateManager.disableLighting();
//      if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && itemstack != null) {
//        this.renderToolTip(itemstack, mouseX, mouseY);
//      }
//      else if (itemstack1 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && itemstack1 != null) {
//        this.renderToolTip(itemstack1, mouseX, mouseY);
//      }
//      else if (itemstack2 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && itemstack2 != null) {
//        this.renderToolTip(itemstack2, mouseX, mouseY);
//      }
//      else if (merchantrecipe.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY))) {
//        this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
//      }
//      GlStateManager.popMatrix();
//      GlStateManager.enableLighting();
//      GlStateManager.enableDepth();
//      RenderHelper.enableStandardItemLighting();
//    }
  }
  @SideOnly(Side.CLIENT)
  static class MerchantJumpButton extends GuiButton {
    final static int rowCount = 4;
    final static int spacing = 18;
    private int recipeIndex;
    private GuiMerchantBetter parent;
    public MerchantJumpButton(int buttonId, int x, int y, int widthIn, int heightIn, int r, GuiMerchantBetter p) {
      super(buttonId, x, y, widthIn, heightIn, "");
      recipeIndex = r;
      this.parent = p;
    }
    public int getRecipeIndex() {
      return recipeIndex;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      super.drawButton(mc, mouseX, mouseY);
      if (this.visible) {
        MerchantRecipeList merchantrecipelist = parent.getContainer().getTrades();
        if (merchantrecipelist == null) { return; }
        MerchantRecipe r = merchantrecipelist.get(recipeIndex);
        if (r == null) { return; }
        int x = this.xPosition + parent.itemPadding;
        int y = this.yPosition + parent.itemPadding / 2;
        GlStateManager.pushMatrix();
        ModCyclic.proxy.renderItemOnGui(r.getItemToBuy(), parent.itemRender, parent.fontRendererObj, x, y);
        x += spacing;
        ModCyclic.proxy.renderItemOnGui(r.getSecondItemToBuy(), parent.itemRender, parent.fontRendererObj, x, y);
        x += spacing;
        ModCyclic.proxy.renderItemOnGui(r.getItemToSell(), parent.itemRender, parent.fontRendererObj, x, y);
        GlStateManager.popMatrix();
        RenderHelper.enableGUIStandardItemLighting();//IMPORTANT: without this, any button with transparent item (glass) well have messed up shading
        //TRADE_REDX_TEXTURE
        if (r.isRecipeDisabled()) {
          parent.mc.getTextureManager().bindTexture(TRADE_REDX_TEXTURE);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.disableLighting();
          int sizeOfX = 30 / 2;//real physical texture is 30x30 pixels, and we just reduce it a touch
          Gui.drawModalRectWithCustomSizedTexture(x - 18, y, 0, 0, sizeOfX, sizeOfX,
              sizeOfX, sizeOfX);
          //          this.drawTexturedModalRect(x,y, 0, 0, 30, 30);
          //          this.drawTexturedModalRect(x,y, 212, 0, 28, 21);
        }
      }
    }
  }
  @SideOnly(Side.CLIENT)
  static class MerchantButton extends GuiButton {
    private final boolean forward;
    public MerchantButton(int buttonID, int x, int y, boolean f) {
      super(buttonID, x, y, 12, 19, "");
      this.forward = f;
    }
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
//        mc.getTextureManager().bindTexture(GuiMerchantBetter.MERCHANT_GUI_TEXTURE);
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
//        int i = 0;
//        int j = 176;
//        if (!this.enabled) {
//          j += this.width * 2;
//        }
//        else if (flag) {
//          j += this.width;
//        }
//        if (!this.forward) {
//          i += this.height;
//        }
//        this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
      }
    }
  }
}
