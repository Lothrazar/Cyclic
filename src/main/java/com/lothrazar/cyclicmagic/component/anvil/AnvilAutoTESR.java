package com.lothrazar.cyclicmagic.component.anvil;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTESR;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class AnvilAutoTESR extends BaseMachineTESR<TileEntityAnvilAuto> {
  public AnvilAutoTESR(int slot) {
    super(slot);
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
    if (stack.isEmpty() == false) {
      renderItem(te, stack, 1.15F);
    }
  }
}
