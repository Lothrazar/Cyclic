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
package com.lothrazar.cyclic.util;

import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class UtilFakePlayer {

  public static boolean isFakePlayer(Entity attacker) {
    return attacker instanceof FakePlayer;
  }

  public static WeakReference<FakePlayer> initFakePlayer(ServerLevel ws, UUID uname, String blockName) {
    GameProfile breakerProfile = new GameProfile(uname, ModCyclic.MODID + ".fake_player." + blockName);
    WeakReference<FakePlayer> fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get(ws, breakerProfile));
    if (fakePlayer == null || fakePlayer.get() == null) {
      fakePlayer = null;
      return null; // trying to get around https://github.com/PrinceOfAmber/Cyclic/issues/113
    }
    fakePlayer.get().setOnGround(true);
    //    fakePlayer.get().onGround = true;
    fakePlayer.get().connection = new ServerGamePacketListenerImpl(ws.getServer(), new Connection(PacketFlow.SERVERBOUND), fakePlayer.get()) {

      @Override
      public void send(Packet<?> packetIn) {}
    };
    fakePlayer.get().setSilent(true);
    return fakePlayer;
  }
}
