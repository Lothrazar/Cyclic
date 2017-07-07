package com.lothrazar.cyclicmagic.component.pylonexp;
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
public class XpPylonTESR extends BaseMachineTesr<TileEntityXpPylon> {
  private int lowerSlot;
  public XpPylonTESR(int slot, int ls) {
    super(slot);
    lowerSlot = ls;
  }
  @Override
  public void render(TileEntityBaseMachineInvo te) {
    renderItem(te, te.getStackInSlot(this.itemSlotAbove), 0, 0.5F, 1);
    renderItem(te, te.getStackInSlot(this.lowerSlot), 1, 0.5F, 0);
  }
}
