package com.lothrazar.cyclic.block.beaconpotion;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public class BlockPotion extends BlockCyclic implements BeaconBeamBlock {

  private static final float[] COLOR = new float[]{1, 1, 1};

  public BlockPotion(Properties properties) {
    super(properties.strength(1.8F)
        .noOcclusion() // transparency
    );
    this.setHasGui();
  }

  @Override
  public DyeColor getColor() {
    return DyeColor.WHITE;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state) {
    return true;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndLightGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TilePotionBeacon(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.BEACON.get(), world.isClientSide() ? TilePotionBeacon::clientTick : TilePotionBeacon::serverTick);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }
}
