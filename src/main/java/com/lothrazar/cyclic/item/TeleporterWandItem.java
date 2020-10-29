package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilParticle;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class TeleporterWandItem extends ItemBase {

  private static final int TICKS_USING = 93000;

  public TeleporterWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING;//bow has 72000
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof PlayerEntity && stack.isDamaged() && stack.getDamage() >= 4)
      tryRepairWith(stack, (PlayerEntity) entityIn, Items.ENDER_PEARL);
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
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int chargeTimer) {
    if (!(entity instanceof PlayerEntity))
      return;
    float percentageCharged = getChargedPercent(stack, chargeTimer);
    if (percentageCharged == 1) { //full charge
      PlayerEntity player = (PlayerEntity) entity;
      RayTraceResult rt0 = player.pick(50D, 0, true);
      if (rt0.getType() == RayTraceResult.Type.BLOCK) {
        Direction face = ((BlockRayTraceResult) rt0).getFace();
        BlockPos pos = ((BlockRayTraceResult) rt0).getPos().offset(face);
        BlockPos currentPlayerPos = player.getPosition();
        UtilEntity.teleportWallSafe(player, player.world, pos);
        if (player.getPosition() != currentPlayerPos) {
          UtilParticle.spawnParticleBeam(worldIn, ParticleTypes.PORTAL, currentPlayerPos, player.getPosition(), 50);
          worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
          UtilItemStack.damageItem(player, stack);
        }
      }
    }
  }
}
