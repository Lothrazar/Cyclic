package com.lothrazar.cyclic.block.anvilmagma;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilBlockstates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockAnvilMagma extends BlockBase {

  public BlockAnvilMagma(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.ANVIL));
    this.setHasGui();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.anvil_magma, ScreenAnvilMagma::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    Direction direction = state.get(BlockStateProperties.HORIZONTAL_FACING);
    return direction.getAxis() == Direction.Axis.X ? BlockAnvilAuto.X_AXIS_AABB : BlockAnvilAuto.Z_AXIS_AABB;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileAnvilMagma();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }
}
