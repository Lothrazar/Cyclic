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
package com.lothrazar.cyclicmagic.util;

import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class UtilFakePlayer {

  public static WeakReference<FakePlayer> initFakePlayer(WorldServer ws, UUID uname, String blockName) {
    GameProfile breakerProfile = new GameProfile(uname, Const.MODID + ".fake_player." + blockName);
    WeakReference<FakePlayer> fakePlayer;
    try {
      fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get(ws, breakerProfile));
    }
    catch (Exception e) {
      ModCyclic.logger.error("Exception thrown trying to create fake player : " + e.getMessage());
      fakePlayer = null;
    }
    if (fakePlayer == null || fakePlayer.get() == null) {
      fakePlayer = null;
      return null; // trying to get around https://github.com/PrinceOfAmber/Cyclic/issues/113
    }
    fakePlayer.get().onGround = true;
    fakePlayer.get().connection = new NetHandlerPlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.SERVERBOUND), fakePlayer.get()) {

      @SuppressWarnings("rawtypes")
      @Override
      public void sendPacket(Packet packetIn) {}
    };
    fakePlayer.get().setSilent(true);
    return fakePlayer;
  }
}
