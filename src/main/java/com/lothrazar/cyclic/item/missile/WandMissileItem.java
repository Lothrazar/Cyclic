package com.lothrazar.cyclic.item.missile;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.ItemHasEnergy;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.core.BlockPos;
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

public class WandMissileItem extends ItemHasEnergy {

  public static ModConfigSpec.IntValue COST;
  public static ModConfigSpec.IntValue RANGE;
  public static ModConfigSpec.IntValue MAX;
  public static ModConfigSpec.IntValue DAMAGE_MIN;
  public static ModConfigSpec.IntValue DAMAGE_MAX;

  public WandMissileItem(Properties properties) {
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
    if (!world.isClientSide()) {
      IEnergyStorage storage = CapabilityUtil.energy(stack);//stack.getCapability(Capabilities.ENERGY, null).orElse(null);
      final int cost = COST.get();
      if (storage != null && storage.extractEnergy(cost, true) == cost) {
        //we can afford it
        storage.extractEnergy(cost, false);
        MagicMissileEntity projectile = new MagicMissileEntity(player, world);
        projectile.setTarget(EntityUtil.getClosestEntity(world, player, trimmedTargets));
        shootMe(world, player, projectile, 0, ItemBaseCyclic.VELOCITY_MAX);
      }
    }
  }
}
