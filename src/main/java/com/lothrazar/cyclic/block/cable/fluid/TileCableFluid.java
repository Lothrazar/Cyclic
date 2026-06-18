package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TileCableFluid extends TileCableBase implements MenuProvider {

  public static ModConfigSpec.IntValue BUFFERSIZE;
  public static ModConfigSpec.IntValue TRANSFER_RATE;

  public TileCableFluid(BlockPos pos, BlockState state) {
    super(TileRegistry.FLUID_PIPE.get(), pos, state);
    setIsFluid();
  }


  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableFluid e) {
    e.tickFluid();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.FLUID_PIPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCableFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

}
