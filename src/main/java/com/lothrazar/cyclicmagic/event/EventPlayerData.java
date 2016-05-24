package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.ModMain.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerData;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class EventPlayerData implements IHasConfig{
	
	@SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent event){
		if(event.getEntity() instanceof EntityPlayer && event.getEntity().worldObj.isRemote == false
				&& event.getEntity() instanceof EntityPlayerMP){
			System.out.println("Player join world");
			EntityPlayer p = (EntityPlayer)event.getEntity();
			 
			ModMain.network.sendTo(new PacketSyncPlayerData(ModMain.getPlayerProperties(p).getDataAsNBT()), (EntityPlayerMP)p);
		}
    }

    @SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event){
		System.out.println("CLONE");

		IPlayerExtendedProperties src = ModMain.getPlayerProperties(event.getOriginal());

		System.out.println("original has crafting="+src.hasInventoryCrafting());
		
		IPlayerExtendedProperties dest = ModMain.getPlayerProperties(event.getEntityPlayer());
		System.out.println("PLAYER has crafting="+dest.hasInventoryCrafting());
//original has true, player has false
		dest.setInventoryCrafting(src.hasInventoryCrafting());
		dest.setInventoryExtended(src.hasInventoryExtended());
		
		

		System.out.println("AFTER COPY has crafting="+dest.hasInventoryCrafting());
		
	}
	
    @SubscribeEvent
    public void onEntityConstruct(AttachCapabilitiesEvent evt)
    {
        evt.addCapability(new ResourceLocation(Const.MODID, "IModdedSleeping"), new ICapabilitySerializable<NBTTagCompound>()
        {
            IPlayerExtendedProperties inst = ModMain.CAPABILITYSTORAGE.getDefaultInstance();
            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return capability == ModMain.CAPABILITYSTORAGE;
            }

            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                return capability == ModMain.CAPABILITYSTORAGE ? ModMain.CAPABILITYSTORAGE.<T>cast(inst) : null;
            }

            @Override
            public NBTTagCompound serializeNBT() {
            	try{
                return (NBTTagCompound)ModMain.CAPABILITYSTORAGE.getStorage().writeNBT(ModMain.CAPABILITYSTORAGE, inst, null);
            	}catch(java.lang.ClassCastException e){
            		return new NBTTagCompound();
            	}
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt) {
            	ModMain.CAPABILITYSTORAGE.getStorage().readNBT(ModMain.CAPABILITYSTORAGE, inst, null, nbt);
            }
        });
    }

    @SubscribeEvent
    public void onBedCheck(SleepingLocationCheckEvent evt)
    {
        final IPlayerExtendedProperties sleep = evt.getEntityPlayer().getCapability(ModMain.CAPABILITYSTORAGE, null);
    	System.out.println("onBedCheck isnull "+ (sleep==null));
    	if(sleep!=null)System.out.println("onWakeUp.isSleeping "+sleep.isSleeping());
    	
        if (sleep != null && sleep.isSleeping())
            evt.setResult(Result.ALLOW);
    }

    @SubscribeEvent
    public void onWakeUp(PlayerWakeUpEvent evt)
    {

        final IPlayerExtendedProperties sleep = evt.getEntityPlayer().getCapability(ModMain.CAPABILITYSTORAGE, null);
    	System.out.println("onWakeUp isnull "+ (sleep==null));
    	if(sleep!=null)System.out.println("onWakeUp.isSleeping "+sleep.isSleeping());
    	
        if (sleep != null)
            sleep.setSleeping(false);
    }

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
