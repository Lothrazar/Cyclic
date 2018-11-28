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
package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.capability.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.capability.InstancePlayerExtendedProperties;
import com.lothrazar.cyclicmagic.capability.PlayerStorage;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityRegistry {

  @SuppressWarnings("deprecation")
  public static void register() {
    CapabilityManager.INSTANCE.register(IPlayerExtendedProperties.class, new PlayerStorage(),
        InstancePlayerExtendedProperties.class);
  }

  public static IPlayerExtendedProperties getPlayerProperties(EntityPlayer player) {
    IPlayerExtendedProperties c = player.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (c == null) {
      ModCyclic.logger.error("Null IPlayerExtendedProperties error, cannot get properties");
    }
    return c;
  }

  public static void syncServerDataToClient(EntityPlayerMP p) {
    if (p == null) {
      return;
    }
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(p);
    if (props != null) {
      ModCyclic.network.sendTo(new PacketSyncPlayerData(props.getDataAsNBT()), p);
    }
  }
}
