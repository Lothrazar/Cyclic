package com.lothrazar.cyclicmagic.component.peat;
import java.util.Random;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPeat extends BlockBase {
  private boolean isBaked;
  public BlockPeat(boolean baked) {
    super(Material.GROUND);
    this.setSoundType(SoundType.GROUND);
    this.setHarvestLevel(Const.ToolStrings.shovel, 2);
    this.setTickRandomly(true);
    isBaked = baked;
  }
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (this.isBaked) {
      return Item.getByNameOrId(Const.MODRES + "peat_fuel");
    }
    return super.getItemDropped(state, rand, fortune);
  }
  @Override
  public int quantityDropped(Random random) {
    if (this.isBaked) {
      return 4;
    }
    return 1;
  }
  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    // ModCyclic.logger.log("updateTick");
    if (this.isBaked) {
      return;
    }
    Block bSide;
    for (EnumFacing f : EnumFacing.VALUES) {
      bSide = world.getBlockState(pos.offset(f)).getBlock();
      if (bSide == Blocks.WATER || bSide == Blocks.FLOWING_WATER) {
        tryBake(world, pos);
        return;
      }
    }
  }
  private void tryBake(World world, BlockPos pos) {
    //TODO: is water nearby
    //ModCyclic.logger.log("tryBake");
    if (this.isBaked == false && world.rand.nextDouble() < 0.1) {
      world.setBlockToAir(pos);
      world.setBlockState(pos, Block.getBlockFromName(Const.MODRES + "peat_baked").getDefaultState());
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_BUBBLE, pos);
    }
  }
  @Override
  public int tickRate(World worldIn) {
    return 900;
  }
}
