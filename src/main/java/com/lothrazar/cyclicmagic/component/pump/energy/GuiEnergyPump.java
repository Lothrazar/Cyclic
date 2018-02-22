package com.lothrazar.cyclicmagic.component.pump.energy;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiEnergyPump extends GuiBaseContainer {
  public GuiEnergyPump(InventoryPlayer inventoryPlayer, TileEntityEnergyPump tileEntity) {
    super(new ContainerEnergyPump(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityEnergyPump.Fields.REDSTONE.ordinal();
  }
}
