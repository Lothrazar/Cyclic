package com.lothrazar.cyclic.block.detectorentity;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.fml.client.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDetector extends BlockBase {

  private static final double BOUNDS = 1;
  private static final VoxelShape AABB = Block.box(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 10, 16 - BOUNDS);

  public BlockDetector(Properties properties) {
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
    ClientRegistry.bindTileEntityRenderer(TileRegistry.DETECTOR_ENTITY, RenderDetector::new);
    MenuScreens.register(ContainerScreenRegistry.DETECTOR_ENTITY, ScreenDetector::new);
  }

  @Override
  @Deprecated
  public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    TileDetector te = (TileDetector) blockAccess.getBlockEntity(pos);
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
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileDetector();
  }
}
