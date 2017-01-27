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
public class StructureTESR extends CyclicMachineBaseTESR<TileMachineStructureBuilder> {
  public StructureTESR() {
    super("tesr/structure_slice", 0);
  }
  @Override
  public void render(TileEntityBaseMachineInvo te) {
    renderAnimation(te);
    ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
    if (stack != null) {
      renderItem(te, stack);
    }
  }
}
