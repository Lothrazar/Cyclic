package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class WandHypnoItem extends ItemHasEnergy {

  public static ModConfigSpec.IntValue COST;
  public static ModConfigSpec.IntValue RANGE;
  public static ModConfigSpec.IntValue MAX;

  public WandHypnoItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack stack) {
    return ItemUseAnimation.SPEAR;
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    this.doAction(itemstack, worldIn, playerIn);
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  private void doAction(ItemStack stack, Level world, Player player) {
    if (!world.isClientSide()) {
      IEnergyStorage storage = CapabilityUtil.energy(stack);//stack.getCapability(Capabilities.ENERGY, null).orElse(null);
      final int cost = COST.get();
      if (storage != null && storage.extractEnergy(cost, true) == cost) {
        storage.extractEnergy(cost, false);
        fireHypnoAggression(world, player, stack);
      }
    }
  }

  private void fireHypnoAggression(Level world, Player player, ItemStack stack) {
    BlockPos p = player.blockPosition();
    final int r = RANGE.get();
    List<Mob> all = world.getEntitiesOfClass(Mob.class, new AABB(p.getX() - r, p.getY() - r, p.getZ() - r, p.getX() + r, p.getY() + r, p.getZ() + r));
    List<Mob> trimmedTargets = new ArrayList<Mob>();
    for (Mob target : all) {
      MobCategory type = target.getClassification(false);
      if (target.getUUID().compareTo(player.getUUID()) != 0
          && !type.isFriendly()) {
        //not friendly and not me
        trimmedTargets.add(target);
      }
    }
    Mob cur;
    Mob curTarget;
    int targeted = 0;
    for (int i = 0; i < trimmedTargets.size(); i++) {
      cur = trimmedTargets.get(i);
      cur.setTarget(null);
      int j = world.getRandom().nextInt(trimmedTargets.size());
      if (j != i) { // not self
        curTarget = trimmedTargets.get(j);

        cur.setLastHurtMob(curTarget);
        cur.setTarget(curTarget); // this leads to forge hook onLivingSetAttackTarget
        // 26.1: DRAGON_BREATH is now a ParticleType<PowerParticleOption>, not directly usable as ParticleOptions
        ParticleUtil.spawnParticle(world, PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F), cur.blockPosition(), 15);
        targeted++;
      }
    }
    if (targeted == 0) {
      ChatUtil.sendStatusMessage(player, "wand.result.notargets");
    }
    else {
      player.getCooldowns().addCooldown(stack, 60);
    }
  }
}
