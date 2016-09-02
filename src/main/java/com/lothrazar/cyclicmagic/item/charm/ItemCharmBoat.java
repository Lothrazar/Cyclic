package com.lothrazar.cyclicmagic.item.charm;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCharmBoat extends BaseCharm implements IHasRecipe{
  private static final int durability = 2000;
  
  public ItemCharmBoat() {
    super(durability);
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
      
//      if(living.getAir() < 2)
      
      
      if(entityIn.getRidingEntity() instanceof EntityBoat){

        EntityBoat boat = (EntityBoat)entityIn.getRidingEntity();

        if (living.moveForward > 0) {
          float reduce = 0.08F;
          //pulled from private EntityBoat.controlBoat() fn
          boat.motionX += net.minecraft.util.math.MathHelper.sin(-boat.rotationYaw * 0.017453292F)*reduce;
          boat.motionZ += net.minecraft.util.math.MathHelper.cos(boat.rotationYaw * 0.017453292F)*reduce;
          if(worldIn.rand.nextDouble() < 0.1){
            super.damageCharm(living, stack, itemSlot);
          }
        }
      }
    }
  }

  @Override
  public void addRecipe() {
//    GameRegistry.addShapelessRecipe(new ItemStack(this), 
//        Items.BOAT,Items.FISH,Items.NETHER_WART,Items.REDSTONE,Items.GLOWSTONE_DUST,Items.GUNPOWDER);
  }
}
