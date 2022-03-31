package com.lothrazar.cyclic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilParticle;
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

public class WandHypnoItem extends ItemBaseCyclic {

  private static final double RANGE = 16.0;

  public WandHypnoItem(Properties properties) {
    super(properties.defaultDurability(1024 * 4));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.NONE;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
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
    System.out.println("Item that takes RF?");
    UtilItemStack.damageItem(player, itemstack);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }
}
