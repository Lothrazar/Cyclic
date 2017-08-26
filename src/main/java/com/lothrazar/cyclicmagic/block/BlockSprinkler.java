package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockSprinkler extends BlockBase {
  private static final int RADIUS = 4;
  private static final int TICKS = 60;
  public BlockSprinkler() {
    super(Material.IRON);
    this.setTranslucent();
    setTickRandomly(true);
  }
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    BlockPos current;
    for (int x = -1 * RADIUS; x <= RADIUS; x++) {
      for (int y = -1 * RADIUS; y <= RADIUS; y++) {
        for (int z = -1 * RADIUS; z <= RADIUS; z++) {
          current = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
          IBlockState bState = world.getBlockState(current);
          Block block = bState.getBlock();
          if (block instanceof IPlantable || block instanceof IGrowable) {
            //no need to literally increase internal growth numbers, just force more  update ticks
            world.scheduleBlockUpdate(current, block, rand.nextInt(TICKS), 1);
            block.updateTick(world, current, bState, rand);
            UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, current);
          }
        }
      }
    }
    super.updateTick(world, pos, state, rand);
  }
}
