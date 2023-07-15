package com.lothrazar.cyclic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class WandHypnoItem extends ItemBaseCyclic {

  public static IntValue COST;
  public static IntValue RANGE;

  public WandHypnoItem(Properties properties) {
    super(properties.stacksTo(1));
    this.setUsesEnergy();
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.SPEAR;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    this.doAction(itemstack, worldIn, playerIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  private void doAction(ItemStack stack, Level world, Player player) {
    if (!world.isClientSide) {
      IEnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
      final int cost = COST.get();
      if (storage != null && storage.extractEnergy(cost, true) == cost) {
        storage.extractEnergy(cost, false);
        fireHypnoAggression(world, player);
      }
    }
  }

  private void fireHypnoAggression(Level world, Player player) {
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
      int j = world.random.nextInt(trimmedTargets.size());
      if (j != i) { // not self
        curTarget = trimmedTargets.get(j);
        //        cur.setRevengeTarget(curTarget);
        //        cur.setLastAttackedEntity(curTarget);
        cur.setLastHurtMob(curTarget);
        cur.setTarget(curTarget); // this leads to forge hook onLivingSetAttackTarget
        ParticleUtil.spawnParticle(world, ParticleTypes.DRAGON_BREATH, cur.blockPosition(), 15);
        targeted++;
      }
    }
    if (targeted == 0) {
      ChatUtil.sendStatusMessage(player, "wand.result.notargets");
    }
    else {
      player.getCooldowns().addCooldown(this, 60);
    }
  }
}
