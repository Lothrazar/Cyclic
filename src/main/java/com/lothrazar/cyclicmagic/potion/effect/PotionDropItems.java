package com.lothrazar.cyclicmagic.potion.effect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketEntityDropRandom;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PotionDropItems extends PotionBase {

  private static final double DROP_CHANCE = 0.06;

  public PotionDropItems() {
    super("butter", false, 0xe5e500);
  }

  @Override
  public void tick(EntityLivingBase entity) {
    PotionEffect pot = entity.getActivePotionEffect(this);
    World world = entity.getEntityWorld();
    List<EntityEquipmentSlot> slots = null;
    if (pot != null && this.isMoving(entity)) {
      if (pot.getAmplifier() == Const.Potions.I) {
        slots = Arrays.asList(EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND);
      }
      else {
        slots = Arrays.asList(EntityEquipmentSlot.values());
      }
      Collections.shuffle(slots);
      ItemStack stack = ItemStack.EMPTY;
      for (EntityEquipmentSlot slot : slots) {
        stack = entity.getItemStackFromSlot(slot);
        if (stack.isEmpty() == false && world.rand.nextDouble() < DROP_CHANCE) {
          if (world.isRemote) {
            ModCyclic.network.sendToServer(new PacketEntityDropRandom(entity.getEntityId(), slot.ordinal()));
          }
          //          entity.setItemStackToSlot(slot, ItemStack.EMPTY);
          break;
        }
      }
    }
  }

  /**
   * Only makes sense client side.
   * 
   * @param entity
   * @return isMoving
   */
  private boolean isMoving(EntityLivingBase entity) {
    return (Math.abs(entity.motionX) > 0) || (Math.abs(entity.motionZ) > 0);
  }
}
