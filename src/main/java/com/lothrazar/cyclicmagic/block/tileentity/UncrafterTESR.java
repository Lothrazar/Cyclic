package com.lothrazar.cyclicmagic.block.tileentity;
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
public class UncrafterTESR extends CyclicBaseTESR<TileMachineUncrafter> {
  public UncrafterTESR(){
    super("block/uncrafting_block_head",0);
  }
}
