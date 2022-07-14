package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PearlEnchant extends EnchantmentCyclic {

  public static final int COOLDOWN = 6 * 20;
  private static final float VELOCITY = 1.5F; //Same as EnderPearlItem
  private static final float INNACCURACY = 1F; //Same as EnderPearlItem
  public static final String ID = "ender";
  public static BooleanValue CFG;

  public PearlEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    if (isEnabled()) MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public boolean isTradeable() {
    return isEnabled() && super.isTradeable();
  }

  @Override
  public boolean isDiscoverable() {
    return isEnabled() && super.isDiscoverable();
  }

  @Override
  public boolean isAllowedOnBooks() {
    return isEnabled() && super.isAllowedOnBooks();
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && stack.getItem() instanceof SwordItem;
  }

  @SubscribeEvent
  public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
    if (!isEnabled()) {
      return;
    }
    Level world = event.getWorld();
    if (!world.isClientSide && event.getResult() != Result.DENY) {
      int level = EnchantmentHelper.getItemEnchantmentLevel(this, event.getItemStack());
      if (level > 0) {
        int adjustedCooldown = COOLDOWN / level;
        Player player = event.getPlayer();
        if (player.getCooldowns().isOnCooldown(event.getItemStack().getItem())) {
          return;
        }
        ThrownEnderpearl pearl = new ThrownEnderpearl(world, player);
        Vec3 lookVector = player.getLookAngle();
        pearl.shoot(lookVector.x(), lookVector.y(), lookVector.z(), VELOCITY, INNACCURACY);
        EntityUtil.setCooldownItem(player, event.getItemStack().getItem(), adjustedCooldown);
        SoundUtil.playSound(player, SoundEvents.ENDER_PEARL_THROW, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        world.addFreshEntity(pearl);
        //block propogation of event 
        event.setResult(Result.DENY);
        event.setCanceled(true);
      }
    }
  }
}
