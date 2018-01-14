package com.lothrazar.cyclicmagic.component.enchanter;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnchanter extends GuiBaseContainer {
 
  private TileEntityEnchanter tile;
 
 
  public GuiEnchanter(InventoryPlayer inventoryPlayer, TileEntityEnchanter tileEntity) {
    super(new ContainerEnchanter(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
 
    this.fieldRedstoneBtn = TileEntityEnchanter.Fields.REDSTONE.ordinal();
  }
 
 
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
 
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + 30-1, 
          this.guiTop + ContainerEnchanter.SLOTY - 1, 
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      
      

      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + 110-1, 
          this.guiTop + ContainerEnchanter.SLOTY-1, 
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    
    this.drawFluidBar();
  }
  private void drawFluidBar() {
    //??EH MAYBE https://github.com/BuildCraft/BuildCraft/blob/6.1.x/common/buildcraft/core/gui/GuiBuildCraft.java#L121-L162
    int u = 0, v = 0;
    int currentFluid = tile.getField(TileEntityEnchanter.Fields.EXP.ordinal()); // ( fluid == null ) ? 0 : fluid.amount;//tile.getCurrentFluid();
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h = pngHeight / f;//f is scale factor. original is too big
    int x = this.guiLeft + 150, y = this.guiTop + 16;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    h -= 2;// inner texture is 2 smaller, one for each border
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_EXP);
    float percent = ((float) currentFluid / ((float) TileEntityEnchanter.TANK_FULL));
    int hpct = (int) (h * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1 + h - hpct,
        u, v,
        16, hpct,
        16, h);
  }
 
}
