package com.lothrazar.cyclic.enchant;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

public class MultiBowEnchant {

  public static final String ID = "multishot";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  public static void spawnArrow(Level worldIn, Player player, ItemStack stackBow, int charge, Vec3 offsetVector) {
    ArrowItem arrowitem = (ArrowItem) Items.ARROW;
    AbstractArrow abstractarrowentity = arrowitem.createArrow(worldIn, stackBow, player, stackBow);
    abstractarrowentity.pickup = AbstractArrow.Pickup.DISALLOWED;
    abstractarrowentity.setPos(abstractarrowentity.getX() + offsetVector.x(), abstractarrowentity.getY(), abstractarrowentity.getZ() + offsetVector.z());
    float f = BowItem.getPowerForTime(charge);
    abstractarrowentity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
    if (f == 1.0F) {
      abstractarrowentity.setCritArrow(true);
    }
    var lookup = worldIn.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
    int j = EnchantmentHelper.getTagEnchantmentLevel(lookup.getOrThrow(Enchantments.POWER), stackBow);
    if (j > 0) {
      abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + j * 0.5D + 0.5D);
    }

    worldIn.addFreshEntity(abstractarrowentity);
  }
}
