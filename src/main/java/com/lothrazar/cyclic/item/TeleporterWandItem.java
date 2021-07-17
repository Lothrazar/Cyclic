package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TeleporterWandItem extends ItemBase {

  private static final int TICK_REPAIR = 4;
  private static final int TICKS_USING = 93000;
  public static IntValue RANGE;

  public TeleporterWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING; //bow has 72000
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof PlayerEntity && stack.isDamaged() && stack.getDamage() >= TICK_REPAIR) {
      tryRepairWith(stack, (PlayerEntity) entityIn, Items.ENDER_PEARL);
    }
  }

  @Override
  public void tryRepairWith(ItemStack stackToRepair, PlayerEntity player, Item target) {
    if (stackToRepair.isDamaged()) {
      ItemStack pearls = this.findAmmo(player, target);
      if (!pearls.isEmpty()) {
        pearls.shrink(1);
        UtilItemStack.repairItem(stackToRepair, 4);
      }
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int chargeTimer) {
    if (!(entity instanceof PlayerEntity)) {
      return;
    }
    float percentageCharged = getChargedPercent(stack, chargeTimer);
    if (percentageCharged >= 0.98) { //full charge with a bit of buffer room
      PlayerEntity player = (PlayerEntity) entity;
      RayTraceResult trace = player.pick(RANGE.get(), 0, true);
      if (trace.getType() == RayTraceResult.Type.BLOCK) {
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) trace;
        Direction face = blockRayTraceResult.getFace();
        BlockPos newPos = blockRayTraceResult.getPos().offset(face);
        BlockPos oldPos = player.getPosition();
        if (UtilEntity.enderTeleportEvent(player, world, newPos)) { // && player.getPosition() != currentPlayerPos    
          UtilItemStack.damageItem(player, stack);
          if (world.isRemote) {
            UtilParticle.spawnParticleBeam(world, ParticleTypes.PORTAL, oldPos, newPos, RANGE.get());
            UtilSound.playSound(player, newPos, SoundRegistry.WARP_ECHO);
          }
        }
      }
    }
  }
}
