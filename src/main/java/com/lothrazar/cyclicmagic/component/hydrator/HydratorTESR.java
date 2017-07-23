package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTesr;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial
 * http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class HydratorTESR extends BaseMachineTesr<TileEntityHydrator> {
  private int lowerSlot;
  public HydratorTESR(int slot, int ls) {
    super(slot);
    lowerSlot = ls;
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    renderItem(te, te.getStackInSlot(this.itemSlotAbove), 0, 0.5F, 1);
    renderItem(te, te.getStackInSlot(this.lowerSlot), 1, 0.5F, 0);
  }
}
