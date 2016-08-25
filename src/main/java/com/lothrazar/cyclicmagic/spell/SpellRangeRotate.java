package com.lothrazar.cyclicmagic.spell;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketRotateBlock;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellRangeRotate extends BaseSpellRange {
  public SpellRangeRotate(int id, String name) {
    super.init(id, name);
  }
  @Override
  public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos pos, EnumFacing side) {
    if (world.isRemote) {
      // only client side can call this method. mouseover does not exist
      // on server
      BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);
      if (mouseover != null) {
        ModMain.network.sendToServer(new PacketRotateBlock(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
      }
    }
    return true;
  }
  public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {
    if (pos == null || p.worldObj.getBlockState(pos) == null || side == null) { return; }
    World worldObj = p.worldObj;
    IBlockState clicked = worldObj.getBlockState(pos);
    if (clicked.getBlock() == null) { return; }
    Block clickedBlock = clicked.getBlock();
    boolean isDone = UtilPlaceBlocks.rotateBlockValidState(worldObj, p, pos, side);
    if (isDone) {
      this.playSound(worldObj, p, clickedBlock, pos);
      this.spawnParticle(worldObj, p, pos);
    }
    return;
  }
}
