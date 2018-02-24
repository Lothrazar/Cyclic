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
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncVillagerToClient implements IMessage, IMessageHandler<PacketSyncVillagerToClient, IMessage> {
  private int career;
  private MerchantRecipeList trades;
  public PacketSyncVillagerToClient() {}
  public PacketSyncVillagerToClient(int h, MerchantRecipeList merchantrecipelist) {
    career = h;
    trades = merchantrecipelist;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    career = tags.getInteger("h");
    NBTTagCompound tradeTag = (NBTTagCompound) tags.getTag("trades");
    trades = new MerchantRecipeList();
    trades.readRecipiesFromTags(tradeTag);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("h", career);
    NBTTagCompound tradeTag = trades.getRecipiesAsTags();
    tags.setTag("trades", tradeTag);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSyncVillagerToClient message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      EntityPlayer player = ModCyclic.proxy.getPlayerEntity(ctx);
      //      player.getEntityData().setInteger(Const.MODID + "_VILLAGERHACK", message.career);//TODO: validate/delete
      if (player != null && player.openContainer instanceof ContainerMerchantBetter) {
        //TODO: this spams every second, not sure why
        ContainerMerchantBetter c = (ContainerMerchantBetter) player.openContainer;
        c.setCareer(message.career);
        if (message.trades != null) {
          c.setTrades(message.trades);
        }
      }
    }
    return null;
  }
}
