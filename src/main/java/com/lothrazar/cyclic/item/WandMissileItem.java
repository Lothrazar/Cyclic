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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class WandMissileItem extends ItemBaseCyclic {

  private static final double RANGE = 16.0;

  public WandMissileItem(Properties properties) {
    super(properties.defaultDurability(1024 * 4));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.NONE;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    //    playerIn.startUsingItem(handIn);
    System.out.println("TODO: consume RF and new MAIGC MISSILE  entity");
    BlockPos p = player.blockPosition();
    List<Mob> all = world.getEntitiesOfClass(Mob.class, new AABB(p.getX() - RANGE, p.getY() - 1, p.getZ() - RANGE, p.getX() + RANGE, p.getY() + 1, p.getZ() + RANGE));
    List<Mob> trimmedTargets = new ArrayList<Mob>();
    for (Mob target : all) {
      MobCategory type = target.getClassification(false);
      if (target.getUUID().compareTo(player.getUUID()) != 0
          && !type.isFriendly()) {
        //not friendly and not me
        trimmedTargets.add(target);
      }
    }
    MagicMissileEntity projectile = new MagicMissileEntity(player, world);
    projectile.setTarget(UtilEntity.getClosestEntity(world, player, trimmedTargets));
    shootMe(world, player, projectile, 0, ItemBaseCyclic.VELOCITY_MAX * 0.3F);
    // TODO: RF POWER NOT DURAB
    UtilItemStack.damageItem(player, itemstack);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }
}
