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
package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLeverRemote extends BaseItem implements IHasRecipe {

  public ItemLeverRemote() {
    this.setMaxStackSize(1);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    BlockPos pointer = UtilNBT.getItemStackBlockPos(stack);
    if (pointer != null) {
      tooltip.add(TextFormatting.RED + UtilChat.blockPosToString(pointer));
    }
    super.addInformation(stack, playerIn, tooltip, advanced);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack stack = playerIn.getHeldItem(hand);
    if (worldIn.getBlockState(pos).getBlock() instanceof BlockLever) {
      UtilNBT.setItemStackBlockPos(stack, pos);
      if (worldIn.isRemote) {
        UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".saved");
      }
      UtilSound.playSound(playerIn, SoundEvents.BLOCK_LEVER_CLICK);
      return EnumActionResult.SUCCESS;
    }
    else {
      boolean success = false;
      success = trigger(stack, worldIn, playerIn);
      if (success)
        return EnumActionResult.SUCCESS;
      else
        return EnumActionResult.FAIL;
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack stack = playerIn.getHeldItem(hand);
    boolean success = false;
    success = trigger(stack, worldIn, playerIn);
    if (success) {
      playerIn.swingArm(hand);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    else
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }

  private boolean trigger(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    BlockPos blockPos = UtilNBT.getItemStackBlockPos(stack);
    if (blockPos == null) {
      if (worldIn.isRemote) {
        UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".invalid");
      }
      return false;
    }
    else {
      IBlockState blockState = worldIn.getBlockState(blockPos);
      if (blockState == null || blockState.getBlock() != Blocks.LEVER) {
        if (worldIn.isRemote) {
          UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".invalid");
        }
        return false;
      }
      else {
        boolean hasPowerHere = blockState.getValue(BlockLever.POWERED);//this.block.getStrongPower(blockState, worldIn, pointer, EnumFacing.UP) > 0;
        setLeverPowerState(worldIn, blockPos, blockState, hasPowerHere);
        UtilSound.playSound(playerIn, SoundEvents.BLOCK_LEVER_CLICK);
        return true;
      }
    }
  }

  private void setLeverPowerState(World worldIn, BlockPos blockPos, IBlockState blockState, boolean hasPowerHere) {
    IBlockState stateNew = blockState.withProperty(BlockLever.POWERED, !hasPowerHere);
    boolean success = worldIn.setBlockState(blockPos, stateNew);
    if (success) {
      flagUpdate(worldIn, blockPos, blockState, stateNew);
      flagUpdate(worldIn, blockPos.down(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.up(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.west(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.east(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.north(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.south(), blockState, stateNew);
    }
  }

  private void flagUpdate(World worldIn, BlockPos blockPos, IBlockState blockState, IBlockState stateNew) {
    //    worldIn.notifyBlockUpdate(blockPos,blockState,stateNew,3);
    worldIn.notifyNeighborsOfStateChange(blockPos, blockState.getBlock(), true);//THIS one works only with true
    //    worldIn.scheduleBlockUpdate(blockPos, stateNew.getBlock(), 3, 3);
    //    worldIn.scheduleUpdate(blockPos, stateNew.getBlock(), 3);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        " t ",
        " l ",
        'l', new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.STONE.getMetadata()),
        's', Blocks.STONE_BUTTON,
        't', Blocks.LEVER);
  }
}
