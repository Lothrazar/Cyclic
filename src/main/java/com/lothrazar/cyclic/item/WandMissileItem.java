package com.lothrazar.cyclic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.item.slingshot.MagicMissileEntity;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class WandMissileItem extends ItemBaseCyclic {

  private static final double MIN_CHARGE = 0.6;
  private static final double RANGE = 16.0; // TOOD config

  public WandMissileItem(Properties properties) {
    super(properties.defaultDurability(1024 * 4));
    this.setUsesEnergy();
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.SPEAR;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000;
  }

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
    System.out.println("TODO: consume RF and new MAIGC MISSILE  entity");
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
    if (!world.isClientSide) {
      MagicMissileEntity projectile = new MagicMissileEntity(player, world);
      projectile.setTarget(UtilEntity.getClosestEntity(world, player, trimmedTargets));
      shootMe(world, player, projectile, 0, ItemBaseCyclic.VELOCITY_MAX * percentageCharged);
    }
    // TODO: RF POWER NOT DURAB
    UtilItemStack.damageItem(player, stack);
  }
}
