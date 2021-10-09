package com.lothrazar.cyclic.block.terraglass;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockTerraGlass extends BlockBase {

  public BlockTerraGlass(Properties properties) {
    //https://en.wikipedia.org/wiki/Terra_preta
    super(properties.strength(1.8F).noOcclusion().lightLevel(state -> {
      return state.getValue(LIT) ? 6 : 0;
    }));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1.0F;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  @Deprecated
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 0;
  }

  @Override
  public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    return blockState.getValue(LIT) ? 15 : 0;
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    return blockState.getValue(LIT) ? 15 : 0;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state ) {
    return new TileTerraGlass(pos,state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.TERRAGLASS.get(), world.isClientSide ? TileTerraGlass::clientTick : TileTerraGlass::serverTick);
  }
  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }
}
