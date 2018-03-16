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
package com.lothrazar.cyclicmagic.entity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityStoneMinecart extends EntityMinecartFurnace {
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  public EntityStoneMinecart(World worldIn) {
    super(worldIn);
    this.setCartBlock(Blocks.AIR.getDefaultState());
  }
  public EntityStoneMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  private void setCartBlock(IBlockState b) {
    dropCartBlock();
    this.setDisplayTile(b);
  }
  public void dropCartBlock() {
    IBlockState current = this.getDisplayTile();
    if (current.getBlock() != Blocks.AIR) {
      UtilItemStack.dropBlockState(this.world, this.getPosition(), current);
    }
  }
  @Override
  public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, player, hand))) return true;
    ItemStack held = player.getHeldItem(hand);
    IBlockState heldBlock = UtilItemStack.getStateFromStack(held);
    this.setCartBlock(heldBlock);
    if (heldBlock.getBlock() != Blocks.AIR) {
      held.shrink(1);
    }
    return true;
  }
  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      this.setCartBlock(Blocks.AIR.getDefaultState());
    }
  }
  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      dropCartBlock();
      ItemStack itemstack = getCartItem();
      if (this.hasCustomName()) {
        itemstack.setStackDisplayName(this.getCustomNameTag());
      }
      this.entityDropItem(itemstack, 0.0F);
    }
  }
  @Override
  public ItemStack getCartItem() {
    return new ItemStack(dropItem);
  }
}
