package com.lothrazar.cyclic.block.tp;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.library.dim.DimensionTransit;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BlockTeleport extends BlockCyclic {

  public static IntValue POWERCONF;
  public static IntValue COSTDIM;
  public static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);

  public BlockTeleport(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    this.setHasGui();
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof ServerPlayer == false) {
      return;
    }
    ServerPlayer player = (ServerPlayer) entity;
    if (player.isCrouching() == false) {
      return;
    }
    //if im a player, im crouching. anyway just go 
    TileTeleport tile = (TileTeleport) worldIn.getBlockEntity(pos);
    BlockPosDim dimpos = tile.getTargetInSlot(0);
    if (dimpos != null && worldIn instanceof ServerLevel) {
      final int pay = LevelWorldUtil.dimensionIsEqual(dimpos, worldIn) ? POWERCONF.get() : COSTDIM.get();
      int sim = tile.energy.extractEnergy(pay, true);
      if (pay == 0 || sim == pay) {
        tile.energy.extractEnergy(pay, false);
        //tp now
        DimensionTransit transit = new DimensionTransit((ServerLevel) worldIn, dimpos);
        transit.teleport(player);
        player.changeDimension(transit.getTargetLevel(), transit);
      }
    }
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
    MenuScreens.register(MenuTypeRegistry.TELEPORT.get(), ScreenTeleport::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileTeleport(pos, state);
  }
}
