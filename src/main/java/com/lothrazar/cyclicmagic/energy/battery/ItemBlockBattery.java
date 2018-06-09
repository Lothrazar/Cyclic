package com.lothrazar.cyclicmagic.energy.battery;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBattery extends ItemBlock {

  public static final String ENERGY = "energy";

  public ItemBlockBattery(Block block) {
    super(block);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    return true;
  }

  /**
   * Queries the percentage of the 'Durability' bar that should be drawn.
   *
   * @param stack
   *          The current ItemStack
   * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
   */
  @Override
  public double getDurabilityForDisplay(ItemStack item) {
    IEnergyStorage storage = item.getCapability(CapabilityEnergy.ENERGY, null);
    double energy = storage.getEnergyStored();
    return 1 - energy / TileEntityBattery.CAPACITY;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    IEnergyStorage storage = item.getCapability(CapabilityEnergy.ENERGY, null);
    tooltip.add(storage.getEnergyStored() + "/" + TileEntityBattery.CAPACITY);
    tooltip.add(UtilChat.lang("tile.battery.tooltip"));
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
    return new EnergyCapabilityProvider(stack);
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
  private static class EnergyCapabilityProvider implements ICapabilityProvider {

    //reuse same energy as TileEntity
    public final EnergyStore storage;
    private int maxPower = 999999;

    public EnergyCapabilityProvider(final ItemStack stack) {
      this.storage = new EnergyStore(maxPower) {

        @Override
        public int getEnergyStored() {
          if (stack.hasTagCompound()) {
            return stack.getTagCompound().getInteger(ENERGY);
          }
          else {
            return 0;
          }
        }

        @Override
        public void setEnergyStored(int energy) {
          if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
          }
          stack.getTagCompound().setInteger(ENERGY, energy);
        }
      };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
      return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
      if (capability == CapabilityEnergy.ENERGY) {
        return CapabilityEnergy.ENERGY.cast(this.storage);
      }
      return null;
    }
  }
}
