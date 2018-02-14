package com.lothrazar.cyclicmagic.component.peat.farm;
import com.lothrazar.cyclicmagic.block.EnergyStore; 
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ITileFuel;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GuiPeatFarm extends GuiBaseContainer {
  public GuiPeatFarm(InventoryPlayer inventoryPlayer, TileEntityPeatFarm te) {
    super(new ContainerPeatFarm(inventoryPlayer, te), te);
    this.progressBar = new ProgressBar(this, 10, 72, TileEntityPeatFarm.Fields.TIMER.ordinal(), TileEntityPeatFarm.TIMER_FULL);
    
  }
  @Override
  public void initGui() {
    super.initGui();
    // BUTTONS! 
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    //    Gui.drawModalRectWithCustomSizedTexture(
    //        this.guiLeft + ContainerItemPump.SLOTX_START + (j - 1) * Const.SQ - 1,
    //        this.guiTop + ContainerItemPump.SLOTY - 1,
    //        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    
    //DRAW ENERGY BAR
//    ITileFuel tileFuel = (ITileFuel) this.tile;
//    int u = 0, v = 0;
//     IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
//     
//     
//    float percent = ((float) energy.getEnergyStored()) / ((float) energy.getMaxEnergyStored());
//    int outerLength = 66, outerWidth = 16;
//    int innerLength = 64, innerWidth = 14;
//     
// int  fuelX = this.guiLeft + screenSize.width() -25;
// int  fuelXE = fuelX + innerWidth;
// int  fuelY = this.guiTop + Const.PAD;
// int  fuelYE = fuelY + innerLength;
//    this.mc.getTextureManager().bindTexture(Const.Res.ENERGY_CTR);
//    Gui.drawModalRectWithCustomSizedTexture(
//        this.guiLeft + screenSize.width() -26,
//        this.guiTop+4, u, v,
//        outerWidth, outerLength,
//        outerWidth, outerLength);
//    this.mc.getTextureManager().bindTexture(Const.Res.ENERGY_INNER);
//    Gui.drawModalRectWithCustomSizedTexture(
//        fuelX,
//        fuelY-3, u, v,
//        innerWidth, (int) (innerLength * percent),
//        innerWidth, innerLength);
    this.drawFluidBar();
  }
  private void drawFluidBar() {
    //??EH MAYBE https://github.com/BuildCraft/BuildCraft/blob/6.1.x/common/buildcraft/core/gui/GuiBuildCraft.java#L121-L162
    int u = 0, v = 0;
    int currentFluid = tile.getField(TileEntityPeatFarm.Fields.FLUID.ordinal()); // ( fluid == null ) ? 0 : fluid.amount;//tile.getCurrentFluid();
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h = pngHeight / f;//f is scale factor. original is too big
    int x = this.guiLeft + 98, y = this.guiTop + 16;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    h -= 2;// inner texture is 2 smaller, one for each border
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_WATER);
    float percent = ((float) currentFluid / ((float) TileEntityPeatFarm.TANK_FULL));
    int hpct = (int) (h * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1 + h - hpct,
        u, v,
        16, hpct,
        16, h);
  }
}
