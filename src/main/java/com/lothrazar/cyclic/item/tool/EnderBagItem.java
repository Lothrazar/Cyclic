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
package com.lothrazar.cyclic.item.tool;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EnderBagItem extends ItemBase {

  public EnderBagItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    EnderChestInventory enderchestinventory = player.getInventoryEnderChest();
    enderchestinventory.setChestTileEntity(null);
    player.openContainer(new SimpleNamedContainerProvider((p_220114_1_, p_220114_2_, p_220114_3_) -> {
      return ChestContainer.createGeneric9X3(p_220114_1_, p_220114_2_, enderchestinventory);
    }, EnderChestBlock.field_220115_d));
    player.addStat(Stats.OPEN_ENDERCHEST);
    world.playSound(player, player.getPosition(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.3F, 1);
    //    if (world.rand.nextDouble() > 0.5)
    //      UtilSound.playSound(player, SoundEvents.BLOCK_ENDERCHEST_OPEN);
    //    else
    //      UtilSound.playSound(player, SoundEvents.BLOCK_ENDERCHEST_CLOSE);
    return super.onItemRightClick(world, player, hand);
  }
}
