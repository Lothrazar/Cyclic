package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.net.PacketEntityLaser;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.SoundUtil;
import com.lothrazar.library.util.TagDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;

public class LaserItem extends ItemHasEnergy {

  public static final int DELAYDAMAGETICKS = 5;

  private static final String NBT_TAG = "damage_cooldown";
  private static long previousMessageTime = 0;

  public LaserItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.NONE;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public void onUseTick(Level level, LivingEntity entityLiving, ItemStack stack, int count) {
    if (level.isClientSide() && entityLiving instanceof Player player) {
      IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
      if (storage == null || storage.getEnergyStored() < ConfigRegistry.LaserItemEnergy.get()) {
        return;
      }

      long gameTime = level.getGameTime();
      boolean crosshair = false;

      if (gameTime - getDamageCooldown(stack) >= DELAYDAMAGETICKS) {
        Minecraft mc = Minecraft.getInstance();
        Entity target = null;

        // Close-range target (handled by Minecraft)
        if (mc.crosshairPickEntity != null && mc.crosshairPickEntity.isAlive()) {
          crosshair = true;
          target = mc.crosshairPickEntity;
        }
        // Long-range target
        else {
          double laserRange = ConfigRegistry.LaserItemRange.get();
          Vec3 eyePos = player.getEyePosition(1.0F);
          Vec3 view = player.getViewVector(1.0F);
          Vec3 end = eyePos.add(view.scale(laserRange));
          AABB aabb = player.getBoundingBox().expandTowards(view.scale(laserRange)).inflate(1.0D);

          EntityHitResult ehr = ProjectileUtil.getEntityHitResult(player, eyePos, end, aabb, ent -> ent.isAttackable() && ent.isAlive(), 0);

          if (ehr != null) {
            Vec3 hitLoc = ehr.getLocation();
            // First vector eyePos = FROM, second vector hitLoc = TO
            BlockHitResult miss = level.clip(new ClipContext(eyePos, hitLoc, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));

            if (miss.getType() != HitResult.Type.BLOCK) {
              // Don't shoot through walls
              target = ehr.getEntity();
            }
          }
        }

        // Handling the shot
        if (target != null) {
          resetStackDamageCool(stack, gameTime);
          PacketDistributor.sendToServer(new PacketEntityLaser(target.getId(), crosshair)); // Envoi NeoForge 1.21.1
          SoundUtil.playSound(player, SoundRegistry.LASERBEANPEW.get(), 0.2F);
        } else {
          if (!ConfigRegistry.LaserRenderMisses.get()) {
            if (previousMessageTime == 0) {
              previousMessageTime = gameTime;
            } else {
              previousMessageTime = 0;
            }
            player.displayClientMessage(Component.translatable("item.cyclic.laser_cannon.notarget"), true);
          }
        }
      }
    }
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 72000 * 2;
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof LaserItem) {
      return heldItem;
    }
    //MAIN HAND ONLY for this case
    return ItemStack.EMPTY;
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {}

  public static void resetStackDamageCool(ItemStack stack, long gameTime) {
    TagDataUtil.setItemStackNBTVal(stack, NBT_TAG, gameTime);
  }

  public static int getDamageCooldown(ItemStack stack) {
    return TagDataUtil.getItemStackNBT(stack).getInt(NBT_TAG);
  }
}
