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
package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.core.BaseCommand;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class CommandWorldHome extends BaseCommand implements ICommand {

  public static final String name = "worldhome";

  public CommandWorldHome(boolean op) {
    super(name, op);
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
    if (ic instanceof EntityPlayer == false) {
      return;
    }
    World world = ic.getCommandSenderEntity().getEntityWorld();
    EntityPlayer player = (EntityPlayer) ic;
    if (player.dimension != 0) {
      // :"Can only teleport to worldhome in the overworld"
      UtilChat.addChatMessage(ic, "command.worldhome.dim");
      return;
    }
    // this tends to always get something at y=64, regardless if there is AIR or
    // not
    // so we need to safely push the player up out of any blocks they are in
    UtilSound.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, player.getSoundCategory());
    UtilEntity.teleportWallSafe(player, world, world.getSpawnPoint());
    UtilSound.playSound(player, world.getSpawnPoint(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, player.getSoundCategory());
  }
}
