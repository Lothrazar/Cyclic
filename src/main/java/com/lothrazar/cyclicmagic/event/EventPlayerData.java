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
package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventPlayerData {

  // send from both events to avoid NULL player; known issue due to threading race conditions
  // https://github.com/MinecraftForge/MinecraftForge/issues/1583
  // player data storage based on API source code example:
  // https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
  @SubscribeEvent
  //  @SideOnly(Side.SERVER) // no dont do this. breaks hearts in SSP
  public void onSpawn(PlayerLoggedInEvent event) {
    if (event.player instanceof EntityPlayerMP &&
        event.player != null &&
        event.player.isDead == false) {
      EntityPlayerMP p = (EntityPlayerMP) event.player;
      if (p != null) {
        CapabilityRegistry.syncServerDataToClient(p);
        setDefaultHealth(p);
      }
    }
  }

  @SubscribeEvent
  //  @SideOnly(Side.SERVER)// no dont do this. breaks hearts in SSP
  public void onJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof EntityPlayerMP &&
        event.getEntity() != null &&
        event.getEntity().isDead == false) {
      EntityPlayerMP p = (EntityPlayerMP) event.getEntity();
      if (p != null) {
        CapabilityRegistry.syncServerDataToClient(p);
        setDefaultHealth(p);
      }
    }
  }

  private void setDefaultHealth(EntityPlayerMP p) {
    IPlayerExtendedProperties src = CapabilityRegistry.getPlayerProperties(p);
    //    UtilChat.sendStatusMessage(p,"Setting your maximum health to "+src.getMaxHealth());
    if (src.getMaxHealth() > 0) {
      UtilEntity.setMaxHealth(p, src.getMaxHealth());
    }
  }

  /**
   * 
   * TODO
   * 
   * SHOULD BE AttachCapabilitiesEvent<EntityPlayer> ..BUT that NEVER EVER fires, so data never gets attached to player soo NPEs all over crash the game SO IM forced to do it this way, fire it on
   * GLOBAL object and check instanceof at runtime NO IDEA if its a bug in forge or if there is a right way / wrong way. but of course forge has no docs and nobody to ask
   * 
   * @param event
   */
  @SuppressWarnings("rawtypes")
  @SubscribeEvent
  public void onEntityConstruct(AttachCapabilitiesEvent event) {//was AttachCapabilitiesEvent.Entity in 1.11 and previous
    if (event.getObject() instanceof EntityPlayer) {
      event.addCapability(new ResourceLocation(Const.MODID, "IModdedSleeping"), new PlayerCapInstance());
    }
  }

  class PlayerCapInstance implements ICapabilitySerializable<NBTTagCompound> {

    IPlayerExtendedProperties inst = ModCyclic.CAPABILITYSTORAGE.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
      return capability == ModCyclic.CAPABILITYSTORAGE;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
      return capability == ModCyclic.CAPABILITYSTORAGE ? ModCyclic.CAPABILITYSTORAGE.<T> cast(inst) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
      NBTBase ret = ModCyclic.CAPABILITYSTORAGE.getStorage().writeNBT(ModCyclic.CAPABILITYSTORAGE, inst, null);
      if (ret instanceof NBTTagCompound) {
        return (NBTTagCompound) ret;
      }
      return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
      ModCyclic.CAPABILITYSTORAGE.getStorage().readNBT(ModCyclic.CAPABILITYSTORAGE, inst, null, nbt);
    }
  }
}
