package com.lothrazar.cyclicmagic.util;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilEntity {
  public static void teleportWallSafe(EntityLivingBase player, World world, BlockPos coords) {
    player.setPositionAndUpdate(coords.getX(), coords.getY(), coords.getZ());
    moveEntityWallSafe(player, world);
  }
  public static void moveEntityWallSafe(EntityLivingBase entity, World world) {
    while (world.collidesWithAnyBlock(entity.getEntityBoundingBox())) {
      entity.setPositionAndUpdate(entity.posX, entity.posY + 1.0D, entity.posZ);
    }
  }
  public static void setMaxHealth(EntityLivingBase living, double max) {
    living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(max);
  }
  public static double getMaxHealth(EntityLivingBase living) {
    return living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
  }
  public static int incrementMaxHealth(EntityLivingBase living, int by) {
    int newVal = (int) getMaxHealth(living) + by;
    setMaxHealth(living, newVal);
    return newVal;
  }
  public static EnumFacing getPlayerFacing(EntityLivingBase entity) {
    int yaw = (int) entity.rotationYaw;
    if (yaw < 0) // due to the yaw running a -360 to positive 360
      yaw += 360; // not sure why it's that way
    yaw += 22; // centers coordinates you may want to drop this line
    yaw %= 360; // and this one if you want a strict interpretation of the
    // zones
    int facing = yaw / 45; // 360degrees divided by 45 == 8 zones
    return EnumFacing.getHorizontal(facing / 2);
  }
  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Block block) {
    return dropItemStackInWorld(worldObj, pos, new ItemStack(block));
  }
  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Item item) {
    return dropItemStackInWorld(worldObj, pos, new ItemStack(item));
  }
  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack) {
    EntityItem entityItem = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
    if (worldObj.isRemote == false) {
      // do not spawn a second 'ghost' one onclient side
      worldObj.spawnEntityInWorld(entityItem);
    }
    return entityItem;
  }
  public static double getSpeedTranslated(double speed) {
    return speed * 100;
  }
  public static double getJumpTranslated(double jump) {
    // double jump = horse.getHorseJumpStrength();
    // convert from scale factor to blocks
    double jumpHeight = 0;
    double gravity = 0.98;
    while (jump > 0) {
      jumpHeight += jump;
      jump -= 0.08;
      jump *= gravity;
    }
    return jumpHeight;
  }
  /**
   * Launch entity in the fixed facing direction given
   * 
   * @param entity
   * @param rotationPitch
   * @param power
   * @param facing
   */
  public static void launchDirection(Entity entity, float rotationPitch, float power, EnumFacing facing) {
    double velX = 0;
    double velZ = 0;
    double velY = 0;
    switch (facing) {
    case EAST:
      velX = Math.abs(power);
      velZ = 0;
      break;
    case WEST:
      velX = -1 * Math.abs(power);
      velZ = 0;
      break;
    case NORTH:
      velX = 0;
      velZ = -1 * Math.abs(power);
      break;
    case SOUTH:
      velX = 0;
      velZ = Math.abs(power);
      break;
    case UP:
    case DOWN:
    default:
      break;
    }
    Entity ridingEntity = entity.getRidingEntity();
    if (ridingEntity != null) {
      // boost power a bit, horses are heavy as F
      ridingEntity.motionY = 0;
      ridingEntity.fallDistance = 0;
      ridingEntity.addVelocity(velX, velY, velZ);
    }
    else {
      entity.motionY = 0;
      entity.fallDistance = 0;
      entity.addVelocity(velX, velY, velZ);
    }
  }
  /**
   * Launch entity in the direction it is already facing
   * 
   * @param entity
   * @param rotationPitch
   * @param power
   */
  public static void launch(Entity entity, float rotationPitch, float power) {
    float rotationYaw = entity.rotationYaw;
    float mountPower = (float) (power - 0.5);
    double velX = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power);
    double velZ = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power);
    double velY = (double) (-MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power);
    // launch the player up and forward at minimum angle
    // regardless of look vector
    if (velY < 0) {
      velY *= -1;// make it always up never down
    }
    //		if (velY < 0.4) {
    //			System.out.println("A");
    //			velY = 0.4 + player.jumpMovementFactor;
    //		}
    //		boolean isLookingDown = (player.getLookVec().yCoord < -20);
    //		if(isLookingDown){
    //			System.out.println("B");
    //			velY += 2.5;
    //		}
    Entity ridingEntity = entity.getRidingEntity();
    if (ridingEntity != null) {
      // boost power a bit, horses are heavy as F
      ridingEntity.motionY = 0;
      ridingEntity.fallDistance = 0;
      ridingEntity.addVelocity(velX * mountPower, velY * mountPower, velZ * mountPower);
    }
    else {
      entity.motionY = 0;
      entity.fallDistance = 0;
      entity.addVelocity(velX, velY, velZ);
    }
  }
  public static int pullEntityItemsTowards(World world, BlockPos pos, float ITEMSPEED, int ITEM_HRADIUS, int ITEM_VRADIUS) {
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    return pullEntityItemsTowards(world,x,y,z,ITEMSPEED,ITEM_HRADIUS,ITEM_VRADIUS);
  }
  public static int pullEntityItemsTowards(World world, double x, double y, double z, float ITEMSPEED, int ITEM_HRADIUS, int ITEM_VRADIUS) {

    List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, 
        new AxisAlignedBB(
            x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, 
            x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));
    int moved = 0;
    for (EntityItem eitem : found) {
      Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
      moved++;
    }
    List<EntityXPOrb> foundExp = world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));
    for (EntityXPOrb eitem : foundExp) {
      Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
      moved++;
    }
    return moved;
  }

  public static void addOrMergePotionEffect(EntityLivingBase player, PotionEffect newp) {
    // this could be in a utilPotion class i guess...
    if (player.isPotionActive(newp.getPotion())) {
      // do not use built in 'combine' function, just add up duration
      PotionEffect p = player.getActivePotionEffect(newp.getPotion());
      int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
      int dur = newp.getDuration() + p.getDuration();
      player.addPotionEffect(new PotionEffect(newp.getPotion(), dur, ampMax));
    }
    else {
      player.addPotionEffect(newp);
    }
  }
}
