package com.lothrazar.cyclicmagic.block.tileentity;
import net.minecraft.item.ItemStack;
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
public class MachineTESR extends BaseMachineTesr<TileEntityBaseMachineInvo> {
  public MachineTESR(String res, int slot) {
    super(res,slot);
  }
  @Override
  public void render(TileEntityBaseMachineInvo te) {
    renderAnimation(te);
    if(this.itemSlotAbove >= 0){
    ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
    if (stack != null) {
      renderItem(te, stack);
    }
    }
  }
}
