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
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockCrafter extends BlockBase {

  public BlockCrafter(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
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
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
          }
        });
        tileCrafter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.OUTPUT).ifPresent(h -> {
          for (int i = 0; i < h.getSlots(); i++) {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
          }
        });
      }
    }
    worldIn.updateComparatorOutputLevel(pos, this);
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
    super.onPlayerDestroy(worldIn, pos, state);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCrafter();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.crafter, ScreenCrafter::new);
  }
}
