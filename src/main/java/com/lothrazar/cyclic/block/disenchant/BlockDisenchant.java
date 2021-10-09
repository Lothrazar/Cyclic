package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.Random;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDisenchant extends BlockBase {

  protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

  public BlockDisenchant(Properties properties) {
    super(properties.strength(1.8F).noOcclusion());
    this.setHasGui();
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState state) {
    return true;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  /**
   * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and
   * will always be called regardless of whether the block can receive random update ticks
   */
  @Override
  public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
    super.animateTick(stateIn, worldIn, pos, rand);
    for (int i = -2; i <= 2; ++i) {
      for (int j = -2; j <= 2; ++j) {
        if (i > -2 && i < 2 && j == -1) {
          j = 2;
        }
        if (rand.nextInt(16) == 0) {
          for (int k = 0; k <= 1; ++k) {
            BlockPos blockpos = pos.offset(i, k, j);
            if (worldIn.getBlockState(blockpos).getEnchantPowerBonus(worldIn, blockpos) > 0) {
              if (!worldIn.isEmptyBlock(pos.offset(i / 2, 0, j / 2))) {
                break;
              }
              worldIn.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D,
                  i + rand.nextFloat() - 0.5D, k - rand.nextFloat() - 1.0F, j + rand.nextFloat() - 0.5D);
            }
          }
        }
      }
    }
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    MenuScreens.register(ContainerScreenRegistry.DISENCHANTER, ScreenDisenchant::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileDisenchant(pos,state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.disenchanter , world.isClientSide ? TileDisenchant::clientTick : TileDisenchant::serverTick);
  }
}
