package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class BlockGeneratorFuel extends BlockBase {

  public BlockGeneratorFuel(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    setDefaultState(getDefaultState().with(LIT, false));
    this.setHasGui();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(LIT);
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    ScreenManager.registerFactory(ContainerScreenRegistry.generator_fuel, ScreenGeneratorFuel::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileGeneratorFuel();
  }

  @Override
  public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
    final TileEntity tileEntity = world.getTileEntity(pos);
    if (tileEntity instanceof TileGeneratorFuel) {
      for (final Direction side : Direction.values()) {
        if (pos.offset(side).equals(neighbor)) {
          ((TileGeneratorFuel) tileEntity).neighborHasEnergyStorage.remove(side);
          break;
        }
      }
    }
    super.onNeighborChange(state, world, pos, neighbor);
  }

  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    final TileEntity tileEntity = world.getTileEntity(currentPos);
    if (tileEntity instanceof TileGeneratorFuel) {
      ((TileGeneratorFuel) tileEntity).neighborHasEnergyStorage.remove(facing);
    }
    return super.updatePostPlacement(stateIn, facing, facingState, world, currentPos, facingPos);
  }
}
