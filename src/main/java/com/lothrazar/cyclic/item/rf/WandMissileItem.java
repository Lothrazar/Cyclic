package com.lothrazar.cyclic.item.rf;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.slingshot.MagicMissileEntity;
import com.lothrazar.cyclic.util.UtilEntity;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class WandMissileItem extends ItemBaseCyclic {

  public static IntValue COST;
  public static IntValue RANGE;

  public WandMissileItem(Properties properties) {
    super(properties.stacksTo(1));
    this.setUsesEnergy();
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.SPEAR;
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return 0xBA0909; // TODO: cyclic-client.toml ?
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    this.doAction(itemstack, worldIn, playerIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
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
    if (!world.isClientSide) {
      IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      final int cost = COST.get();
      if (storage != null && storage.extractEnergy(cost, true) == cost) {
        //we can afford it
        storage.extractEnergy(cost, false);
        MagicMissileEntity projectile = new MagicMissileEntity(player, world);
        projectile.setTarget(UtilEntity.getClosestEntity(world, player, trimmedTargets));
        shootMe(world, player, projectile, 0, ItemBaseCyclic.VELOCITY_MAX);
      }
    }
  }
}
