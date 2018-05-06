package com.lothrazar.cyclicmagic.energy.battery;

import java.util.List;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBattery extends ItemBlock {
  
  
  public ItemBlockBattery(Block block) {
    super(block);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (item.getTagCompound() != null && item.getTagCompound() .hasKey("energy")) {
      
      
      int energy = item.getTagCompound().getInteger("energy");
      int energyMAX = item.getTagCompound().getInteger("energyMAX");
      //      ModCyclic.logger.log(energy.toString());
      tooltip.add(energy + "/" + energyMAX + " FE");
      //      IEnergyStorage energy = CapabilityEnergy.ENERGY.getDefaultInstance();
      //      CapabilityEnergy.ENERGY.readNBT(energy, null, item.getTagCompound());
      //      tooltip.add(energy.getEnergyStored() + "/" + energy.getMaxEnergyStored());

    }
    else
      tooltip.add(UtilChat.lang("tile.battery.tooltip"));
  }
  //ICapabilityProvider doesnt exist/notprovidded
  //oh well http://www.minecraftforge.net/forum/topic/54711-1102-forge-energy-capability-in-item-class/
  //  @Override
  //  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
  //
  //    ICapabilityProvider superProvider = super.initCapabilities(stack, nbt);
  //    return new ICapabilityProvider() {
  //
  //      @Override
  //      public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
  //        return capability == CapabilityEnergy.ENERGY ? true : superProvider.hasCapability(capability, facing);
  //      }
  //
  //      @Override
  //      public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
  //        //NULL POINTER here so idk
  //        return capability == CapabilityEnergy.ENERGY ? (T) stack.getCapability(CapabilityEnergy.ENERGY, facing) : superProvider.getCapability(capability, facing);
  //      }
  //    };
  //  }
  //
  //  
}
