package com.lothrazar.cyclic.block.battery;

<<<<<<< HEAD
=======
import com.lothrazar.cyclic.capability.CapabilityProviderEnergyStack;
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
import java.util.List;
import com.lothrazar.cyclic.capability.EnergyCapabilityItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBlockBattery extends BlockItem {

  public static final String ENERGYTTMAX = "energyttmax";
  public static final String ENERGYTT = "energytt";

  public ItemBlockBattery(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    return stack.hasTag() && stack.getTag().contains(ENERGYTT);
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xBC000C;
  }

  @Override
<<<<<<< HEAD
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      TranslatableComponent t = new TranslatableComponent(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored());
      t.withStyle(ChatFormatting.RED);
      tooltip.add(t);
=======
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      int current = stack.getTag().getInt(ENERGYTT);
      int energyttmax = stack.getTag().getInt(ENERGYTTMAX);
      tooltip.add(new TranslationTextComponent(current + "/" + energyttmax).mergeStyle(TextFormatting.RED));
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  /**
   * Queries the percentage of the 'Durability' bar that should be drawn.
   *
   * @param stack
   *          The current ItemStack
   * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
   */
  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    double current = 0;
    if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      current = stack.getTag().getInt(ENERGYTT);
    }
    double max = Math.max(1D, stack.getTag().getInt(ENERGYTTMAX));
    return 1D - current / max;
  }

  @Override
<<<<<<< HEAD
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new EnergyCapabilityItemStack(stack, TileBattery.MAX);
=======
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
    return new CapabilityProviderEnergyStack(TileBattery.MAX);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = stack.getOrCreateTag();
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (storage != null) {
      nbt.putInt(ENERGYTT, storage.getEnergyStored());
      nbt.putInt(ENERGYTTMAX, storage.getMaxEnergyStored());
    }
    return nbt;
  }

  //clientside read tt
  @Override
  public void readShareTag(ItemStack stack, CompoundNBT nbt) {
    if (nbt != null) {
      CompoundNBT stackTag = stack.getOrCreateTag();
      stackTag.putInt(ENERGYTT, nbt.getInt(ENERGYTT));
      stackTag.putInt(ENERGYTTMAX, nbt.getInt(ENERGYTTMAX));
    }
    super.readShareTag(stack, nbt);
>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
  }
}
