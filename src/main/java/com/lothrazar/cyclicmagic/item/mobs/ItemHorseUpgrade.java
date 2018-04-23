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
package com.lothrazar.cyclicmagic.item.mobs;

import java.lang.reflect.Field;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilReflection;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.data.Const.HorseMeta;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHorseUpgrade extends BaseItem implements IHasRecipe {

  public static int HEARTS_MAX;
  public static int SPEED_MAX;
  public static int JUMP_MAX;
  private static final double SPEED_AMT = 0.004;
  private static final double JUMP_AMT = 0.008;
  private ItemStack recipeItem;
  private HorseUpgradeType upgradeType;

  public ItemHorseUpgrade(HorseUpgradeType type, ItemStack rec) {
    super();
    recipeItem = rec;
    upgradeType = type;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (stack == null || stack.getItem() == null) {
      return;
    } // just being safe
    Item carrot = stack.getItem();
    tooltip.add(UtilChat.lang(carrot.getUnlocalizedName(stack) + ".effect"));
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this), Items.CARROT, recipeItem);
  }

  public static void onHorseInteract(AbstractHorse ahorse, EntityPlayer player, ItemStack held, ItemHorseUpgrade heldItem) {
    if (ahorse.isDead) {
      return;
    }
    if (player.getCooldownTracker().hasCooldown(held.getItem())) {
      return;
    }
    World world = player.getEntityWorld();
    boolean success = false;
    switch (heldItem.upgradeType) {
      case HEALTH:
        float mh = (float) ahorse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
        if (mh < 2 * HEARTS_MAX) { // 20 hearts == 40 health points
          ahorse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(mh + 2);
          success = true;
        }
      break;
      case JUMP:
        if (ReflectionRegistry.horseJumpStrength != null) {
          double jump = ahorse.getEntityAttribute(ReflectionRegistry.horseJumpStrength).getAttributeValue();// horse.getHorseJumpStrength()
          double newjump = jump + JUMP_AMT;
          if (UtilEntity.getJumpTranslated(newjump) < JUMP_MAX) {
            ahorse.getEntityAttribute(ReflectionRegistry.horseJumpStrength).setBaseValue(newjump);
            success = true;
          }
        }
        else {
          ModCyclic.logger.error("Failed to set horse jump strength, reflection failed on JUMP_STRENGTH");
        }
      break;
      case SPEED:
        double speed = ahorse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
        double newSpeed = speed + SPEED_AMT;
        if (UtilEntity.getSpeedTranslated(newSpeed) < SPEED_MAX) {
          ahorse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
          success = true;
        }
      break;
      case TYPE:
        //they are not subtypes (data properties) in 1.11 they are different classes horse.setType(HorseType.ZOMBIE);
        //import net.minecraft.entity.passive.HorseType; was removed
        AbstractHorse toSpawn;
        if (ahorse instanceof EntityHorse) {
          EntityHorse horse = (EntityHorse) ahorse;
          toSpawn = new EntityZombieHorse(world);
          toSpawn.setPosition(ahorse.posX, ahorse.posY, ahorse.posZ);
          toSpawn.forceSpawn = true;//ignore natural rules/limits/etc
          //copy the owner to the new horse
          if (ahorse.isTame() && ahorse.getOwnerUniqueId() != null) {
            toSpawn.setHorseTamed(true);
            toSpawn.setOwnerUniqueId(ahorse.getOwnerUniqueId());
          }
          if (world.isRemote == false) {
            if (ahorse.isHorseSaddled()) {
              Field f = UtilReflection.getPrivateField("horseChest", "field_110296_bGs", AbstractHorse.class);
              try {
                ContainerHorseChest horseChest = (ContainerHorseChest) f.get(ahorse);
                ItemStack oldSaddle = horseChest.getStackInSlot(0).copy();
                horseChest.setInventorySlotContents(0, ItemStack.EMPTY);
                horse.setHorseSaddled(false);
                ContainerHorseChest toSpawn_horseChest = (ContainerHorseChest) f.get(toSpawn);
                toSpawn_horseChest.setInventorySlotContents(0, oldSaddle);
                toSpawn.setHorseSaddled(true);
              }
              catch (Exception e) {
                ModCyclic.logger.error("Failed fix horse saddle, reflection failed on horseChest");
                e.printStackTrace();
              }
            }
            world.spawnEntity(toSpawn);
          }
          ahorse.attackEntityFrom(DamageSource.MAGIC, 5000);
          //remove entity DOES WORK but we lose armor and saddle
          world.removeEntity(ahorse);
          success = true;
        }
      break;
      case VARIANT:
        if (ahorse instanceof EntityHorse) {
          EntityHorse horse = (EntityHorse) ahorse;
          int var = horse.getHorseVariant();
          int varReduced = 0;
          int varNew = 0;
          while (var - 256 > 0) {
            varReduced += 256;// this could be done with modulo % arithmetic too,
            // but meh
            // doesnt matter either way
            var -= 256;
          } // invalid numbers make horse invisible, but this is somehow safe. and
          // easier than
          // doing bitwise ops
          switch (var) {
            case HorseMeta.variant_black:
              varNew = HorseMeta.variant_brown;
            break;
            case HorseMeta.variant_brown:
              varNew = HorseMeta.variant_brown_dark;
            break;
            case HorseMeta.variant_brown_dark:
              varNew = HorseMeta.variant_chestnut;
            break;
            case HorseMeta.variant_chestnut:
              varNew = HorseMeta.variant_creamy;
            break;
            case HorseMeta.variant_creamy:
              varNew = HorseMeta.variant_gray;
            break;
            case HorseMeta.variant_gray:
              varNew = HorseMeta.variant_white;
            break;
            case HorseMeta.variant_white:
              varNew = HorseMeta.variant_black;
            break;
          }
          varNew += varReduced;
          horse.setHorseVariant(varNew);
          success = true;
        }
      break;
    }
    if (success) {
      if (player.capabilities.isCreativeMode == false) {
        held.shrink(1);
      }
      player.getCooldownTracker().setCooldown(heldItem, 5);
      UtilParticle.spawnParticle(ahorse.getEntityWorld(), EnumParticleTypes.SMOKE_LARGE, ahorse.getPosition());
      UtilSound.playSound(player, ahorse.getPosition(), SoundEvents.ENTITY_HORSE_EAT, SoundCategory.NEUTRAL);
      ahorse.setEatingHaystack(true); // makes horse animate and bend down to eat
    }
  }

  public static enum HorseUpgradeType {
    HEALTH, JUMP, SPEED, TYPE, VARIANT
  }
}
