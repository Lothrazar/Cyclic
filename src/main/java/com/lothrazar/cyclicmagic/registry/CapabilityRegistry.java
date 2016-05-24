package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityRegistry {

	public static void register(){

		CapabilityManager.INSTANCE.register(IPlayerExtendedProperties.class, new Storage(),
				InstancePlayerExtendedProperties.class);
	}
	
	
	
	

	public static IPlayerExtendedProperties getPlayerProperties(EntityPlayer player){
		if(player == null){
			ModMain.logger.error("Null player, cannot get properties");
			return null;
		}
		return player.getCapability(ModMain.CAPABILITYSTORAGE, null);
	}

	public interface IPlayerExtendedProperties {
		boolean isSleeping();

		void setSleeping(boolean value);

		boolean hasInventoryCrafting();

		void setInventoryCrafting(boolean value);

		boolean hasInventoryExtended();

		void setInventoryExtended(boolean value);
		
		NBTTagCompound getDataAsNBT();
		void setDataFromNBT(NBTTagCompound nbt);
	}

	public static class InstancePlayerExtendedProperties implements IPlayerExtendedProperties {
		private boolean isSleeping = false;
		private boolean hasInventoryCrafting = false;
		private boolean hasInventoryExtended = false;

		@Override
		public boolean isSleeping() {
			return isSleeping;
		}

		@Override
		public void setSleeping(boolean value) {
			this.isSleeping = value;
		}

		@Override
		public boolean hasInventoryCrafting() {
			return hasInventoryCrafting;
		}

		@Override
		public void setInventoryCrafting(boolean value) {
			hasInventoryCrafting = value;
		}

		@Override
		public boolean hasInventoryExtended() {
			return hasInventoryExtended;
		}

		@Override
		public void setInventoryExtended(boolean value) {
			hasInventoryExtended = value;
		}

		@Override
		public NBTTagCompound getDataAsNBT() {
			NBTTagCompound tags = new NBTTagCompound();
			tags.setByte("isSleeping", (byte) (this.isSleeping() ? 1 : 0));
			tags.setByte("hasInventoryCrafting", (byte) (this.hasInventoryCrafting() ? 1 : 0));
			tags.setByte("hasInventoryExtended", (byte) (this.hasInventoryExtended() ? 1 : 0));

			return tags;
		}

		@Override
		public void setDataFromNBT(NBTTagCompound nbt) {
			NBTTagCompound tags;
			if (nbt instanceof NBTTagCompound == false) {
				tags = new NBTTagCompound();
			} else {
				tags = (NBTTagCompound) nbt;
			}
			this.setSleeping(tags.getByte("isSleeping") == 1);
			this.setInventoryCrafting(tags.getByte("hasInventoryCrafting") == 1);
			this.setInventoryExtended(tags.getByte("hasInventoryExtended") == 1);
		}
	}

	public static class Storage implements IStorage<IPlayerExtendedProperties> {
		@Override
		public NBTTagCompound writeNBT(Capability<IPlayerExtendedProperties> capability, IPlayerExtendedProperties instance, EnumFacing side) {

			return instance.getDataAsNBT();
		}

		@Override
		public void readNBT(Capability<IPlayerExtendedProperties> capability, IPlayerExtendedProperties instance, EnumFacing side, NBTBase nbt) {
			try{
				instance.setDataFromNBT((NBTTagCompound)nbt);
			}
			catch(Exception e){
				ModMain.logger.error("Invalid NBT compound: "+e.getMessage());
				ModMain.logger.error(e.getStackTrace().toString());
			}
		}
	}

}
