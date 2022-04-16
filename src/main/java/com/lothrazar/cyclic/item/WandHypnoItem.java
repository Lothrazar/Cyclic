package com.lothrazar.cyclic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.capabilities.CapabilityProviderEnergyStack;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilParticle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class WandHypnoItem extends ItemBaseCyclic {

  public static final int COST = 50;
  private static final int MAX_ENERGY = 16000;
  private static final double MIN_CHARGE = 0.6;
  private static final double RANGE = 16.0;

  public WandHypnoItem(Properties properties) {
    super(properties.defaultDurability(1024 * 4));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.SPEAR;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000;
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
    return 0xBA0909; // TODO: cyclic-client.toml ?
  }
  //** energy

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    if (percentageCharged < MIN_CHARGE) { // MINIMUM_CHARGE
      return; //not enough force to go with any realistic path 
    }
    if (player.level.isClientSide) {
      return;
    }
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null && storage.extractEnergy(COST, true) == COST) {
      storage.extractEnergy(COST, false);
      fireHypnoAggression(world, player);
    }
    //    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  private void fireHypnoAggression(Level world, Player player) {
    BlockPos p = player.blockPosition();
    List<Mob> all = world.getEntitiesOfClass(Mob.class, new AABB(p.getX() - RANGE, p.getY() - RANGE, p.getZ() - RANGE, p.getX() + RANGE, p.getY() + RANGE, p.getZ() + RANGE));
    List<Mob> trimmedTargets = new ArrayList<Mob>();
    for (Mob target : all) {
      MobCategory type = target.getClassification(false);
      if (target.getUUID().compareTo(player.getUUID()) != 0
          && !type.isFriendly()) {
        //not friendly and not me
        trimmedTargets.add(target);
      }
    }
    //
    Mob cur;
    Mob curTarget;
    int targeted = 0;
    for (int i = 0; i < trimmedTargets.size(); i++) {
      cur = trimmedTargets.get(i);
      cur.setTarget(null);
      int j = world.random.nextInt(trimmedTargets.size());
      if (j != i) { // not self
        curTarget = trimmedTargets.get(j);
        //        cur.setRevengeTarget(curTarget);
        //        cur.setLastAttackedEntity(curTarget);
        cur.setLastHurtMob(curTarget);
        cur.setTarget(curTarget); // this leads to forge hook onLivingSetAttackTarget
        UtilParticle.spawnParticle(world, ParticleTypes.DRAGON_BREATH, cur.blockPosition(), 15);
        targeted++;
      }
    }
    if (targeted == 0) {
      UtilChat.sendStatusMessage(player, "wand.result.notargets");
    }
  }
}
