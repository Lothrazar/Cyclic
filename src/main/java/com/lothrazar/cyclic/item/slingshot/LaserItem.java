package com.lothrazar.cyclic.item.slingshot;

import java.util.List;
import com.lothrazar.cyclic.capabilities.CapabilityProviderEnergyStack;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class LaserItem extends ItemBaseCyclic {

  public static final int DELAYDAMAGETICKS = 5;
  public static final int DMG_FAR = 4;
  public static final int COST = 50;
  public static final int DMG_CLOSE = 6;
  public static final double RANGE_FACTOR = 8;
  public static final double RANGE_MAX = 6000;
  private static final int MAX_ENERGY = 16000;

  public LaserItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  //** energy
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new CapabilityProviderEnergyStack(MAX_ENERGY);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    int current = 0;
    int energyttmax = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      current = storage.getEnergyStored();
      energyttmax = storage.getMaxEnergyStored();
      tooltip.add(new TranslatableComponent(current + "/" + energyttmax).withStyle(ChatFormatting.RED));
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    return storage != null && storage.getEnergyStored() > 0;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    float current = 0;
    float max = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      current = storage.getEnergyStored();
      max = storage.getMaxEnergyStored();
    }
    return (max == 0) ? 0 : Math.round(13.0F * current / max);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return TextureRegistry.COLOUR_RF_BAR;
  }
  //** energy

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.NONE;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000 * 2;
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof LaserItem) {
      return heldItem;
    }
    //MAIN HAND ONLY for this case 
    return ItemStack.EMPTY;
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {}

  public static void resetStackDamageCool(ItemStack lasercannon, long gametime) {
    lasercannon.getOrCreateTag().putLong("damagecooldown", gametime);
  }

  public static int getDamageCooldown(ItemStack lasercannon) {
    int thisOne = lasercannon.getOrCreateTag().getInt("damagecooldown");
    return thisOne;
  }
}
