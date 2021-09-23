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

import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantLaunch extends EnchantBase {

  public EnchantLaunch(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "launch";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  private static final float LAUNCH_POWER = 1.05F;
  private static final int ROTATIONPITCH = 70;
  private static final int COOLDOWN = 3 * 20;
  private static final String NBT_USES = "launchuses";

  @Override
  public int getMaxLevel() {
    return 10;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet 
    boolean yes = stack.getItem() instanceof ElytraItem ||
        (stack.getItem() instanceof ArmorItem)
            && ((ArmorItem) stack.getItem()).getEquipmentSlot() == EquipmentSlotType.FEET;
    return yes;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) event.getEntity();
      ItemStack armorStack = getFirstArmorStackWithEnchant(p);
      if (armorStack.isEmpty()) {
        return;
      }
      //if you are on the ground (or not airborne, should be same thing
      if ((p.isAirBorne == false || p.isOnGround()) && //onGround
          armorStack.getOrCreateTag().getInt(NBT_USES) > 0) {
        //you have landed on the ground, dont count previous jumps
        UtilNBT.setItemStackNBTVal(armorStack, NBT_USES, 0);
      }
    }
  }

  public void onKeyInput(PlayerEntity player) {
    if (player == null || player.getRidingEntity() instanceof BoatEntity) {
      return;
    }
    ItemStack feet = getFirstArmorStackWithEnchant(player);
    if (feet.isEmpty() || player.isCrouching()) {
      return;
    } //sneak to not double jump
    if (EnchantmentHelper.getEnchantments(feet).containsKey(this) == false) {
      return;
    }
    if (player.getCooldownTracker().hasCooldown(feet.getItem())) {
      return;
    }
    if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()
        && player.getPosY() < player.lastTickPosY && player.isAirBorne && player.isInWater() == false) {
      //JUMP IS pressed and you are moving down
      int level = EnchantmentHelper.getEnchantments(feet).get(this);
      int uses = feet.getOrCreateTag().getInt(NBT_USES);
      player.fallDistance = 0;
      float angle = (player.getMotion().x == 0 && player.getMotion().z == 0) ? 90 : ROTATIONPITCH;
      UtilEntity.launch(player, angle, LAUNCH_POWER);
      UtilParticle.spawnParticle(player.getEntityWorld(), ParticleTypes.CRIT, player.getPosition(), 7);
      //      UtilSound.playSound(player, player.getPosition(), SoundRegistry.enchant_launch, SoundCategory.PLAYERS, 0.04F);
      //      UtilItemStack.damageItem(player, feet);
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
      //
      PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage()); //reset at bottom of jump
    }
  }
}
