package com.lothrazar.cyclicmagic.component.itemsort;
import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiItemSort extends GuiBaseContainer {
  private Map<EnumFacing, ButtonTileEntityField> btnMapLock = Maps.newHashMap();
  private Map<EnumFacing, ButtonTileEntityField> btnMapDamageIgnore = Maps.newHashMap();
  TileEntityItemCableSort te;
  public GuiItemSort(InventoryPlayer inventoryPlayer, TileEntityItemCableSort tileEntity) {
    super(new ContainerItemSort(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
    te = tileEntity;
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawDefaultBackground();//dim the background as normal
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(Const.Res.TABLEFILTER);
    int thisX = getMiddleX();
    int thisY = getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v,
        screenSize.width(), screenSize.height(),
        screenSize.width(), screenSize.height());
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    ButtonTileEntityField btn;
    for (EnumFacing f : EnumFacing.values()) {
      btn = btnMapLock.get(f);
      if (btn != null) {
        btn.setTooltip(te.getLockType(f).nameLower());
        btn.setTextureIndex(5 + te.getLockType(f).ordinal());
      }
      btn = btnMapDamageIgnore.get(f);
      if (btn != null) {
        btn.setTooltip("button.filter.ignoredamage" + te.getField(f.ordinal() + EnumFacing.values().length));
        //        btn.setTextureIndex(5 + te.getLockType(f).ordinal());
      }
    }
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    ButtonTileEntityField btn;
    for (EnumFacing f : EnumFacing.values()) {
      btn = new ButtonTileEntityField(
          id++,
          this.guiLeft + Const.PAD - 1,
          this.guiTop + f.ordinal() * Const.SQ + 17,
          tile.getPos(), f.ordinal(), 1,
          Const.SQ, Const.SQ);
      this.addButton(btn);
      btnMapLock.put(f, btn);
    }
    int offset = EnumFacing.values().length;
    for (EnumFacing f : EnumFacing.values()) {
      id++;
      btn = new ButtonTileEntityField(
          id++,
          this.guiLeft + 2,
          this.guiTop + f.ordinal() * Const.SQ + 17,
          tile.getPos(), f.ordinal() + offset, 1,
          8, Const.SQ);
      this.addButton(btn);
      btnMapDamageIgnore.put(f, btn);
    }
  }
}
