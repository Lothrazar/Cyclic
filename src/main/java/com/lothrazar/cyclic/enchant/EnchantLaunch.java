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

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Deprecated
public class EnchantLaunch extends EnchantBase {

  public EnchantLaunch(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
  }

  private static final float LAUNCH_POWER = 1.05F;
  private static final int ROTATIONPITCH = 70;
  private static final int COOLDOWN = 3 * 20;
  private static final String NBT_USES = "launchuses";

  @Override
  public int getMaxLevel() {
    return 5;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        stack.getItem() instanceof ElytraItem ||
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
      //      if ((p.isAirBorne == false || p.onGround) &&
      //          UtilNBT.getItemStackNBTVal(armorStack, NBT_USES) > 0) {
      //        //you have landed on the ground, dont count previous jumps
      //        UtilNBT.setItemStackNBTVal(armorStack, NBT_USES, 0);
      //      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {
    PlayerEntity player = ModCyclic.proxy.getClientPlayer();
    //    if (player.isRiding() && player.getRidingEntity() instanceof EntityBoat) {
    //      return;
    //    }
    //    ItemStack feet = getFirstArmorStackWithEnchant(player);
    //    if (feet == null || feet.isEmpty() || player.isSneaking()) {
    //      return;
    //    } //sneak to not double jump
    //    if (EnchantmentHelper.getEnchantments(feet).containsKey(this) == false) {
    //      return;
    //    }
    //    if (player.getCooldownTracker().hasCooldown(feet.getItem())) {
    //      return;
    //    }
    //    if (FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isKeyDown()
    //        && player.posY < player.lastTickPosY && player.isAirBorne && player.isInWater() == false) {
    //      //JUMP IS pressed and you are moving down
    //      int level = EnchantmentHelper.getEnchantments(feet).get(this);
    //      int uses = UtilNBT.getItemStackNBTVal(feet, NBT_USES);
    //      player.fallDistance = 0;
    //      float angle = (player.motionX == 0 && player.motionZ == 0) ? 90 : ROTATIONPITCH;
    //      UtilEntity.launch(player, angle, LAUNCH_POWER);
    //      UtilParticle.spawnParticle(player.getEntityWorld(), EnumParticleTypes.CRIT_MAGIC, player.getPosition());
    //      UtilSound.playSound(player, player.getPosition(), SoundRegistry.enchant_launch, SoundCategory.PLAYERS, 0.04F);
    //      UtilItemStack.damageItem(player, feet);
    //      uses++;
    //      if (uses >= level) { // level is maxuses
    //        //now block useage for a while
    //        if (!feet.isEmpty()) {
    //          UtilEntity.setCooldownItem(player, feet.getItem(), COOLDOWN);
    //        }
    //        uses = 0;
    //      }
    //      UtilNBT.setItemStackNBTVal(feet, NBT_USES, uses);
    //      player.fallDistance = 0;
    //      ModCyclic.network.sendToServer(new PacketPlayerFalldamage());//reset at bottom of jump
    //    }
  }
}
