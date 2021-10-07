package com.lothrazar.cyclic.block.wireless.fluid;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockWirelessFluid extends BlockBase {

  private static final double BOUNDS = 4;
  public static final VoxelShape AABB = Block.box(BOUNDS, BOUNDS, BOUNDS, 16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);

  public BlockWirelessFluid(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    this.setHasGui();
    this.registerDefaultState(defaultBlockState().setValue(LIT, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
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
    MenuScreens.register(ContainerScreenRegistry.wireless_fluid, ScreenWirelessFluid::new);
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileWirelessFluid();
  }
}
