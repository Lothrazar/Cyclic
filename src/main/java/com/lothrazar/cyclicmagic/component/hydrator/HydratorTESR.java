package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTESR;
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
public class HydratorTESR extends BaseMachineTESR<TileEntityHydrator> {
  private static final float height = 0.5F;
  public HydratorTESR(int slot, int ls) {
    super(slot);
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    renderItem(te, te.getStackInSlot(0), 0, height, 1);
    renderItem(te, te.getStackInSlot(1), 1, height, 1);
    renderItem(te, te.getStackInSlot(2), 1, height, 0);
    renderItem(te, te.getStackInSlot(3), 0, height, 0);
  }
}
