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
package com.lothrazar.cyclicmagic.block.scaffolding;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemBlockScaffolding extends ItemBlock {

  public ItemBlockScaffolding(Block block) {
    super(block);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (player.isSneaking()) {
      return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    } //skip if sneaking
    BlockPos pos = player.getPosition().up();// at eye level
    int direction = MathHelper.floor((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
    //imported from my scaffolding spell https://github.com/PrinceOfAmber/CyclicMagic/blob/37ebb722378cbf940aa9cfb4fa99ce0e80127533/src/main/java/com/lothrazar/cyclicmagic/spell/SpellScaffolding.java
    // -45 is up
    // +45 is pitch down
    // first; is it up or down?
    boolean doHoriz = true;
    EnumFacing facing = EnumFacing.UP;
    if (player.rotationPitch < -82) {
      // really really up
      doHoriz = false;
      pos = pos.up().up();
      facing = EnumFacing.UP;
    }
    else if (player.rotationPitch > 82) {
      // really really down
      doHoriz = false;
      pos = pos.down();
      facing = EnumFacing.DOWN;
    }
    else if (player.rotationPitch < -45) {
      // angle is pretty high up. so offset up again
      pos = pos.up();
      facing = EnumFacing.UP;
      doHoriz = true;
    }
    else if (player.rotationPitch > 45) {
      // you are angled down, so bring down from eye level
      pos = pos.down();
      facing = EnumFacing.DOWN;
      doHoriz = true;
    }
    // else doHoriz = true; stays
    // if not, go by dir
    if (doHoriz) {
      switch (direction) {
        case Const.DIR_EAST:
          pos = pos.east();
          facing = EnumFacing.EAST;
        break;
        case Const.DIR_WEST:
          pos = pos.west();// .offset(EnumFacing.WEST);
          facing = EnumFacing.WEST;
        break;
        case Const.DIR_SOUTH:
          pos = pos.south();
          facing = EnumFacing.SOUTH;
        break;
        case Const.DIR_NORTH:
          pos = pos.north();
          facing = EnumFacing.NORTH;
        break;
      }
    }
    if (worldIn.isRemote == false && worldIn.isAirBlock(pos)) {
      return new ActionResult<ItemStack>(this.onItemUse(player, worldIn, pos, hand, facing, 0, 0, 0), stack);
    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
  }

  /**
   * This event is fired on both sides whenever the player right clicks while targeting a block. This event controls which of {@link net.minecraft.block.Block#onBlockActivated} and/or
   * {@link net.minecraft.item.Item#onItemUse} will be called after {@link net.minecraft.item.Item#onItemUseFirst} is called. Canceling the event will cause none of the above three to be called. There
   * are various results to this event, see the getters below. Note that handling things differently on the client vs server may cause desynchronizations!
   */
  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    //instanceof ItemBlockScaffolding 
    if (event.getItemStack() != null && event.getItemStack().getItem() == this && event.getEntityPlayer().isSneaking()) {
      EnumFacing opp = event.getFace().getOpposite();
      BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, this.getBlock());
      this.onItemUse(event.getEntityPlayer(), event.getWorld(), dest, event.getHand(), opp, 0, 0, 0);
      event.setCanceled(true);
    }
  }
}
