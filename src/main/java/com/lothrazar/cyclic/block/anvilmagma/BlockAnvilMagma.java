package com.lothrazar.cyclic.block.anvilmagma;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilBlockstates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockAnvilMagma extends BlockBase {

  public BlockAnvilMagma(Properties properties) {
    super(properties.strength(1.8F).sound(SoundType.ANVIL));
    this.setHasGui();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.ANVIL_MAGMA, ScreenAnvilMagma::new);
  }


  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    return direction.getAxis() == Direction.Axis.X ? BlockAnvilAuto.X_AXIS_AABB : BlockAnvilAuto.Z_AXIS_AABB;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileAnvilMagma(pos,state);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }
}
