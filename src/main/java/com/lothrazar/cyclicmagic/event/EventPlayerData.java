package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
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
  public void onSpawn(PlayerLoggedInEvent event) {
    if (event.player instanceof EntityPlayerMP && event.player.getEntityWorld().isRemote == false) {
      EntityPlayerMP p = (EntityPlayerMP) event.player;
      if (p != null) {
        CapabilityRegistry.syncServerDataToClient(p);
      }
    }
  }
  @SubscribeEvent
  public void onJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof EntityPlayerMP && event.getEntity().getEntityWorld().isRemote == false) {
      EntityPlayerMP p = (EntityPlayerMP) event.getEntity();
      if (p != null) {
        CapabilityRegistry.syncServerDataToClient(p);
      }
    }
  }
  /**
   * 
   * TODO
   * 
   * SHOULD BE AttachCapabilitiesEvent<EntityPlayer> ..BUT that NEVER EVER
   * fires, so data never gets attached to player soo NPEs all over crash the
   * game SO IM forced to do it this way, fire it on GLOBAL object and check
   * instanceof at runtime NO IDEA if its a bug in forge or if there is a right
   * way / wrong way. but of course forge has no docs and nobody to ask
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
      if (ret instanceof NBTTagCompound) { return (NBTTagCompound) ret; }
      return null;
    }
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
      ModCyclic.CAPABILITYSTORAGE.getStorage().readNBT(ModCyclic.CAPABILITYSTORAGE, inst, null, nbt);
    }
  }
}
