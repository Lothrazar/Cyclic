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
package com.lothrazar.cyclicmagic.component.merchant;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketVillagerTrade implements IMessage, IMessageHandler<PacketVillagerTrade, IMessage> {

  private static final String NBT_DUPE_BLOCKER = Const.MODID + "_iscurrentlytrading";
  private int selectedMerchantRecipe;

  public PacketVillagerTrade() {}

  public PacketVillagerTrade(int s) {
    selectedMerchantRecipe = s;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    selectedMerchantRecipe = tags.getInteger("selectedMerchantRecipe");
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("selectedMerchantRecipe", selectedMerchantRecipe);
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketVillagerTrade message, MessageContext ctx) {
    if (ctx.side == Side.SERVER) {
      EntityPlayer player = ctx.getServerHandler().player;
      if (UtilNBT.getEntityBoolean(player, NBT_DUPE_BLOCKER)) {
        //once i got ConcurrentModificationException , this hopefully should fix it in theory. if its happneing where i think it is
        return null;
      } //dedupe fix: if player spams button, dont start second trade WHILE first is still processing
      UtilNBT.setEntityBoolean(player, NBT_DUPE_BLOCKER);
      try {
        if (player != null && player.openContainer instanceof ContainerMerchantBetter) {
          ContainerMerchantBetter c = (ContainerMerchantBetter) player.openContainer;
          c.setCurrentRecipeIndex(message.selectedMerchantRecipe); //TODO: well this duplicates packetsyncvilltoserv..so..
          c.doTrade(player, message.selectedMerchantRecipe);
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Error trying to perform villager trade from Almanac: " + e.getMessage());
        e.printStackTrace();
      }
      finally {
        UtilNBT.setEntityBoolean(player, NBT_DUPE_BLOCKER, false);
      }
    }
    return null;
  }
}
