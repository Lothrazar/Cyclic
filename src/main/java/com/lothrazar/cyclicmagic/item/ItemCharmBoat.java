package com.lothrazar.cyclicmagic.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCharmBoat extends BaseCharm {
  

  public ItemCharmBoat() {
    super(64);
  }

  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
   * update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
  {
    //TODO 1: stop drowning
    //TODO 2: boat speed if possible

    if (entityIn instanceof EntityPlayer) {
      EntityPlayer living = (EntityPlayer) entityIn;
      
      if(entityIn.getRidingEntity() instanceof EntityBoat){

        EntityBoat boat = (EntityBoat)entityIn.getRidingEntity();

        if (living.moveForward > 0.0F) {
          System.out.println("Boat Power!!!");
          //pulled from private EntityBoat.controlBoat() fn
          boat.motionX += net.minecraft.util.math.MathHelper.sin(-boat.rotationYaw * 0.017453292F);
          boat.motionZ += net.minecraft.util.math.MathHelper.cos(boat.rotationYaw * 0.017453292F);
        }
      }
    }
  }
}
