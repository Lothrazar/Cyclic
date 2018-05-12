package com.lothrazar.cyclicmagic.potion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionDropItems extends PotionBase {

  public PotionDropItems() {
    super("butter", false, 0xFFFF66);
  }

  @Override
  public void tick(EntityLivingBase entity) {
    World world = entity.getEntityWorld();
    BlockPos here = entity.getPosition();
    if (entity instanceof EntityPlayer && world.isRemote) {


      ModCyclic.logger.log("butter on player ! clientside!"
          + "?entity.isMoving(entity)  " + isMoving(entity));
    }
    if (this.isMoving(entity) && world.rand.nextDouble() < 0.1) {
      //NET PACKET
      ItemStack stack;
      List<EntityEquipmentSlot> slots = Arrays.asList(EntityEquipmentSlot.values());
      Collections.shuffle(slots);
      for (EntityEquipmentSlot slot : slots) {
        stack = entity.getItemStackFromSlot(slot);
        if (stack.isEmpty() == false) {
          ModCyclic.logger.log("DROP SLOT " + slot + " on world isREmote==" + world.isRemote);
          //if (world.isRemote == false) {
          entity.entityDropItem(stack.copy(), 0.9F);
          //}
          entity.setItemStackToSlot(slot, ItemStack.EMPTY);
          break;
        }
      }
    }
  }

  private boolean isMoving(EntityLivingBase entity) {

    // return entity.prevPosX != entity.posX || entity.prevPosZ != entity.posZ;
    return (Math.abs(entity.motionX) > 0) || (Math.abs(entity.motionZ) > 0);
  }
}
