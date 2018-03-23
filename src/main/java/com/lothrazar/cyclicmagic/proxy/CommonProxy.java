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
package com.lothrazar.cyclicmagic.proxy;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
  //https://github.com/coolAlias/Tutorial-Demo/blob/e8fa9c94949e0b1659dc0a711674074f8752d80e/src/main/java/tutorial/CommonProxy.java
  public void init() {}
  public void preInit() {}
  public World getClientWorld() {
    return null;
  }
  public EntityPlayer getClientPlayer() {
    return null;
  }
  public BlockPos getBlockMouseoverExact(int max) {
    return null;
  }
  public BlockPos getBlockMouseoverOffset(int max) {
    return null;
  }
  public EnumFacing getSideMouseover(int max) {
    return null;
  }
  public void setClientPlayerData(MessageContext ctx, NBTTagCompound tags) {}
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return ctx.getServerHandler().player.getServer();
  }
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return ctx.getServerHandler().player;
  }
  public BlockPos getBlockMouseoverSingle() {
    return null;
  }
  public void renderItemOnScreen(ItemStack current, int x, int y) {}
  public void renderItemOnGui(ItemStack secondItemToBuy, RenderItem itemRender, FontRenderer fontRendererObj, int x, int y) {}
  public void setPlayerReach(EntityPlayer player, int currentReach) {
    player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(currentReach);
  }
  public void closeSpectatorGui() {}
}
