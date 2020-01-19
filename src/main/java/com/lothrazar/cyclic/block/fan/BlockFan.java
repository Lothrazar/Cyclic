package com.lothrazar.cyclic.block.fan;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockFan extends BlockBase {

  public static final BooleanProperty IS_LIT = BooleanProperty.create("lit");

  public BlockFan(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  public BlockRenderType getRenderType(BlockState bs) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileFan();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.FACING, UtilStuff.getFacingFromEntity(pos, entity)), 2);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(IS_LIT);
  }
}
