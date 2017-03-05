package com.lothrazar.cyclicmagic.gui.villager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketVillagerTrade;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMerchantBetter extends GuiBaseContainer {
  static final int texture_width = 250;
  static final int texture_height = 212;
  public static final ResourceLocation GUI = new ResourceLocation(Const.MODID, Const.Res.folder + "villager.png");
  public static final ResourceLocation TRADE_REDX = new ResourceLocation(Const.MODID, Const.Res.folder + "tradex.png");
  private int btnColCount = 4;
  private int yBtnStart;
  private int lastUnusedButtonId;
  private int xBtnStart;
  EntityPlayer player;
  private int selectedMerchantRecipe;
  private List<GuiButtonPurchase> merchButtons = new ArrayList<GuiButtonPurchase>();
  public GuiMerchantBetter(InventoryPlayer ip, EntityVillager merch, InventoryMerchantBetter im, World worldIn) {
    super(new ContainerMerchantBetter(ip, merch, im, worldIn));
    player = ip.player;
    this.xSize = texture_width;
    this.ySize = texture_height;
  }
  public ContainerMerchantBetter getContainer() {
    return (ContainerMerchantBetter) this.inventorySlots;
  }
  public void initGui() {
    super.initGui();
    //setup for the validate btns
    this.xBtnStart = this.guiLeft;
    this.yBtnStart = getMiddleY();
    this.lastUnusedButtonId = 1;
  }
  private void validateMerchantButtons() {
    MerchantRecipeList merchantrecipelist = getContainer().getTrades();
    int s = merchantrecipelist.size();
    int btnH = 20, btnW = 56;
    int x = this.xBtnStart;
    int y = this.yBtnStart;
    int currRow;
    int currCol;
    for (int i = 0; i < s; i++) {
      if (i >= merchButtons.size()) {
        currRow = i / btnColCount;
        currCol = i % btnColCount;
        x = this.xBtnStart + currCol * btnW;
        y = this.yBtnStart + currRow * btnH;
        GuiButtonPurchase slotBtn = (GuiButtonPurchase) this.addButton(new GuiButtonPurchase(lastUnusedButtonId, x, y, btnW, btnH, i, this));
        merchButtons.add(slotBtn);
        lastUnusedButtonId++;
      }
    }
  }
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}
  public void updateScreen() {
    super.updateScreen();
    getContainer().setCurrentRecipeIndex(this.selectedMerchantRecipe);//try to sync between both containers
    this.validateMerchantButtons();
  }
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button instanceof GuiButtonPurchase) {
      setAndTryPurchase(((GuiButtonPurchase) button));
    }
  }
  private void setAndTryPurchase(GuiButtonPurchase button) {
    this.selectedMerchantRecipe = button.getRecipeIndex();
    getContainer().setCurrentRecipeIndex(this.selectedMerchantRecipe);
    ModCyclic.network.sendToServer(new PacketVillagerTrade(this.selectedMerchantRecipe));
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(GUI);
    int thisX = this.getMiddleX();
    int thisY = this.getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, texture_width, texture_height, texture_width, texture_height);
  }
  /**
   * this HAS to be a nested class, because the Gui itemRender is not public
   */
  @SideOnly(Side.CLIENT)
  static class GuiButtonPurchase extends GuiButton implements ITooltipButton {
    final static int rowCount = 4;
    final static int spacing = 18;
    private int recipeIndex;
    private GuiMerchantBetter parent;
    public GuiButtonPurchase(int buttonId, int x, int y, int widthIn, int heightIn, int r, GuiMerchantBetter p) {
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
        int x = this.xPosition + 2;
        int y = this.yPosition + 1;
        GlStateManager.pushMatrix();
        ModCyclic.proxy.renderItemOnGui(r.getItemToBuy(), parent.itemRender, parent.fontRendererObj, x, y);
        x += spacing;
        ModCyclic.proxy.renderItemOnGui(r.getSecondItemToBuy(), parent.itemRender, parent.fontRendererObj, x, y);
        x += spacing;
        ModCyclic.proxy.renderItemOnGui(r.getItemToSell(), parent.itemRender, parent.fontRendererObj, x, y);
        GlStateManager.popMatrix();
        RenderHelper.enableGUIStandardItemLighting();//IMPORTANT: without this, any button with transparent item (glass) well have messed up shading
        if (r.isRecipeDisabled()) {
          parent.mc.getTextureManager().bindTexture(TRADE_REDX);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.disableLighting();
          int sizeOfX = 30 / 2;//real physical texture is 30x30 pixels, and we just reduce it a touch
          Gui.drawModalRectWithCustomSizedTexture(x - 18, y + 1, 0, 0, sizeOfX, sizeOfX,
              sizeOfX, sizeOfX);
        }
      }
    }
    @Override
    public List<String> getTooltips() {
      List<String> tt = new ArrayList<String>();
      MerchantRecipeList merchantrecipelist = parent.getContainer().getTrades();
      if (merchantrecipelist == null) { return tt; }
      MerchantRecipe r = merchantrecipelist.get(recipeIndex);
      if (r == null) { return tt; }
      if (r.isRecipeDisabled()) {
        tt.add(UtilChat.lang("merchant.deprecated"));
      }
      else {
        tt.add(UtilChat.lang("tile.tool_trade.button.tooltip"));
      }
      if (r.getItemToSell() != null) {// && r.getItemToSell().getItem() == Items.ENCHANTED_BOOK
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(r.getItemToSell());
        if (map != null) {
          for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            tt.add(entry.getKey().getTranslatedName(entry.getValue()));
          }
        }
      }
      return tt;
    }
  }
}
