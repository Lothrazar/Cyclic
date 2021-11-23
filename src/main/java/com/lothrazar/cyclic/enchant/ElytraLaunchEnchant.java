/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ElytraLaunchEnchant extends EnchantmentCyclic {

  public ElytraLaunchEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "launch";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  public static final int COOLDOWN = 7 * Const.TPS;
  private static final float POWER = 1.07F;
  private static final int ROTATIONPITCH = 68;
  private static final String NBT_USES = "launchuses";

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return stack.getItem() instanceof ElytraItem;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canEnchant(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof Player) {
      Player p = (Player) event.getEntity();
      ItemStack armorStack = getFirstArmorStackWithEnchant(p);
      if (armorStack.isEmpty()) {
        return;
      }
      //if you are on the ground or not airborne, should be same thing
      if ((p.hasImpulse == false || p.isOnGround()) &&
          armorStack.getOrCreateTag().getInt(NBT_USES) > 0) {
        //you have landed on the ground, dont count previous jumps
        UtilNBT.setItemStackNBTVal(armorStack, NBT_USES, 0);
      }
    }
  }

  public void onKeyInput(Player player) {
    if (player == null || player.getVehicle() instanceof Boat) {
      return;
    }
    ItemStack feet = getFirstArmorStackWithEnchant(player);
    if (feet.isEmpty() || player.isCrouching()) {
      return;
    } //sneak to not double jump
    if (EnchantmentHelper.getEnchantments(feet).containsKey(this) == false) {
      return;
    }
    if (player.getCooldowns().isOnCooldown(feet.getItem())) {
      return;
    }
    if (Minecraft.getInstance().options.keyJump.isDown()
        && player.getY() < player.yOld && player.hasImpulse && player.isInWater() == false) {
      //JUMP IS pressed and you are moving down
      int level = EnchantmentHelper.getEnchantments(feet).get(this);
      int uses = feet.getOrCreateTag().getInt(NBT_USES);
      player.fallDistance = 0;
      float angle = (player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0) ? 90 : ROTATIONPITCH;
      UtilEntity.launch(player, angle, POWER);
      UtilParticle.spawnParticle(player.getCommandSenderWorld(), ParticleTypes.CRIT, player.blockPosition(), 7);
      uses++;
      if (uses >= level) { // level is maxuses
        //now block useage for a while
        if (!feet.isEmpty()) {
          UtilEntity.setCooldownItem(player, feet.getItem(), COOLDOWN);
        }
        uses = 0;
      }
      UtilNBT.setItemStackNBTVal(feet, NBT_USES, uses);
      player.fallDistance = 0;
      PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage()); //reset at bottom of jump
    }
  }
}
