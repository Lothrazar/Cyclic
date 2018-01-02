package com.lothrazar.cyclicmagic.component.trash;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import net.minecraft.util.ITickable;

public class TileEntityTrash extends TileEntityBaseMachineFluid implements ITickable {
  public TileEntityTrash() {
    super(1, 1000);
    this.setSlotsForBoth();
  }
  @Override
  public void update() {
    this.removeStackFromSlot(0);
    this.drain(100, true);
  }
}
