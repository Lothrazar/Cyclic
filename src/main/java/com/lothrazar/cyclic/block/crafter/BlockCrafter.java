/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.block.crafter;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCrafter extends BlockBase {

  public BlockCrafter(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity tileentity = worldIn.getBlockEntity(pos);
      if (tileentity != null) {
        TileCrafter tileCrafter = (TileCrafter) tileentity;
        tileCrafter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.PREVIEW).ifPresent(h -> {
          h.extractItem(0, 64, false);
        });
        tileCrafter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.GRID).ifPresent(h -> {
          for (int i = 0; i < h.getSlots(); i++) {
            h.extractItem(i, 64, false);
          }
        });
        tileCrafter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.INPUT).ifPresent(h -> {
          for (int i = 0; i < h.getSlots(); i++) {
            Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
          }
        });
        tileCrafter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.OUTPUT).ifPresent(h -> {
          for (int i = 0; i < h.getSlots(); i++) {
            Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
          }
        });
      }
    }
    worldIn.updateNeighbourForOutputSignal(pos, this);
    super.onRemove(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state) {
    super.destroy(worldIn, pos, state);
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileCrafter(pos,state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.crafter, world.isClientSide ? TileCrafter::clientTick : TileCrafter::serverTick);
  }
  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.CRAFTER, ScreenCrafter::new);
  }
}
