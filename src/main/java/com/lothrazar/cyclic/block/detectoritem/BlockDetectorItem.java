package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDetectorItem extends BlockBase {

  private static final double BOUNDS = 1;
  private static final VoxelShape AABB = Block.box(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 10, 16 - BOUNDS);

  public BlockDetectorItem(Properties properties) {
    super(properties.strength(1.8F).noOcclusion());
    this.setHasGui();
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    MenuScreens.register(ContainerScreenRegistry.DETECTOR_ITEM, ScreenDetectorItem::new);
  }

  @Override
  @Deprecated
  public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    TileDetectorItem te = (TileDetectorItem) blockAccess.getBlockEntity(pos);
    if (te == null) {
      return 0;
    }
    return te.isPowered() ? 15 : 0;
  }

  @Override
  public boolean isSignalSource(BlockState state) {
    return true;
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileDetectorItem(pos,state);
  }


  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type,TileRegistry.DETECTOR_ITEM, world.isClientSide ? TileDetectorItem::clientTick : TileDetectorItem::serverTick);
  }
}
