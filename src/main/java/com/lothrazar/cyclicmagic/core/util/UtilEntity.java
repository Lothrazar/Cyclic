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
package com.lothrazar.cyclicmagic.core.util;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.data.Vector3;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class UtilEntity {

  private static final double ENTITY_PULL_DIST = 0.4;//closer than this and nothing happens
  private static final double ENTITY_PULL_SPEED_CUTOFF = 3;//closer than this and it slows down
  private final static float ITEMSPEEDFAR = 0.9F;
  private final static float ITEMSPEEDCLOSE = 0.2F;

  /**
   * 
   * @return true if teleport was a success
   */
  public static boolean enderTeleportEvent(EntityLivingBase player, World world, double x, double y, double z) {
    EnderTeleportEvent event = new EnderTeleportEvent(player, x, y, z, 0);
    boolean wasCancelled = MinecraftForge.EVENT_BUS.post(event);
    if (wasCancelled == false) {
      //new target? maybe, maybe not. https://github.com/PrinceOfAmber/Cyclic/issues/438
      UtilEntity.teleportWallSafe(player, world, event.getTargetX(), event.getTargetY(), event.getTargetZ());
    }
    return !wasCancelled;
  }

  /**
   * 
   * @return true if teleport was a success
   */
  public static boolean enderTeleportEvent(EntityLivingBase player, World world, BlockPos target) {
    return enderTeleportEvent(player, world, target.getX(), target.getY(), target.getZ());
  }

  public static void teleportWallSafe(EntityLivingBase player, World world, double x, double y, double z) {
    BlockPos coords = new BlockPos(x, y, z);
    world.markBlockRangeForRenderUpdate(coords, coords);
    //CommandTP ?
    //       ((EntityPlayerMP)p_189863_0_).connection.setPlayerLocation(p_189863_1_.getAmount(), p_189863_2_.getAmount(), p_189863_3_.getAmount(), f, f1, set);
    player.setPositionAndUpdate(x, y, z);
    moveEntityWallSafe(player, world);
  }

  public static void teleportWallSafe(EntityLivingBase player, World world, BlockPos coords) {
    teleportWallSafe(player, world, coords.getX(), coords.getY(), coords.getZ());
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

  public static EnumFacing getFacing(EntityLivingBase entity) {
    int yaw = (int) entity.rotationYaw;
    if (yaw < 0) // due to the yaw running a -360 to positive 360
      yaw += 360; // not sure why it's that way
    yaw += 22; // centers coordinates you may want to drop this line
    yaw %= 360; // and this one if you want a strict interpretation of the
    // zones
    int facing = yaw / 45; // 360degrees divided by 45 == 8 zones
    return EnumFacing.getHorizontal(facing / 2);
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
  public static void launchDirection(Entity entity, float power, EnumFacing facing) {
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
    launch(entity, rotationPitch, rotationYaw, power);
  }

  static final float lowEnough = 0.001F;

  //      float LIMIT = 180F;
  public static void setVelocity(Entity entity, float rotationPitch, float rotationYaw, float power) {
    entity.motionX = 0;
    entity.motionY = 0;
    entity.motionZ = 0;
    double velX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velY = -MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
    if (velY < 0) {
      velY *= -1;// make it always up never down
    }
    if (Math.abs(velX) < lowEnough) velX = 0;
    if (Math.abs(velY) < lowEnough) velY = 0;
    if (Math.abs(velZ) < lowEnough) velZ = 0;
    //    if(entity.getEntityWorld().isRemote){
    //    ModCyclic.logger.info("(angle,yaw,power) = " + rotationPitch + "," + rotationYaw + "," + power);
    //    ModCyclic.logger.info("!setvelocity " + velX + "," + velY + "," + velZ);
    ////    ModCyclic.logger.info("!onground " + entity.onGround);
    //    ModCyclic.logger.info("!posY " + entity.posY);
    //    }
    //setting to zero first then using add, pretty much the same as set
    entity.addVelocity(velX, velY, velZ);
  }

  public static void launch(Entity entity, float rotationPitch, float rotationYaw, float power) {
    float mountPower = (float) (power + 0.5);
    double velX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velY = -MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
    // launch the player up and forward at minimum angle
    // regardless of look vector
    if (velY < 0) {
      velY *= -1;// make it always up never down
    }
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

  public static AxisAlignedBB makeBoundingBox(BlockPos center, int hRadius, int vRadius) {
    //so if radius is 1, it goes 1 in each direction, and boom, 3x3 selected
    return new AxisAlignedBB(center).expand(hRadius, vRadius, hRadius);
  }

  public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AxisAlignedBB(
        x - hRadius, y - vRadius, z - hRadius,
        x + hRadius, y + vRadius, z + hRadius);
  }

  public static int moveEntityItemsInRegion(World world, BlockPos pos, int hRadius, int vRadius) {
    return moveEntityItemsInRegion(world, pos.getX(), pos.getY(), pos.getZ(), hRadius, vRadius, true);
  }

  public static int moveEntityItemsInRegion(World world, double x, double y, double z, int hRadius, int vRadius, boolean towardsPos) {
    AxisAlignedBB range = makeBoundingBox(x, y, z, hRadius, vRadius);
    List<Entity> all = getItemExp(world, range);
    return pullEntityList(x, y, z, towardsPos, all);
  }

  public static List<Entity> getItemExp(World world, AxisAlignedBB range) {
    List<Entity> all = new ArrayList<Entity>();
    all.addAll(world.getEntitiesWithinAABB(EntityItem.class, range));
    all.addAll(world.getEntitiesWithinAABB(EntityXPOrb.class, range));
    return all;
  }

  public static void speedupEntityIfMoving(EntityLivingBase entity, float factor) {
    if (entity.moveForward > 0) {
      if (entity.getRidingEntity() != null && entity.getRidingEntity() instanceof EntityLivingBase) {
        speedupEntity((EntityLivingBase) entity.getRidingEntity(), factor);
      }
      else {
        speedupEntity(entity, factor);
      }
    }
  }

  public static void speedupEntity(EntityLivingBase entity, float factor) {
    entity.motionX += net.minecraft.util.math.MathHelper.sin(-entity.rotationYaw * 0.017453292F) * factor;
    entity.motionZ += net.minecraft.util.math.MathHelper.cos(entity.rotationYaw * 0.017453292F) * factor;
  }

  public static int moveEntityLivingNonplayers(World world, double x, double y, double z, int ITEM_HRADIUS, int ITEM_VRADIUS, boolean towardsPos, float speed) {
    AxisAlignedBB range = UtilEntity.makeBoundingBox(x, y, z, ITEM_HRADIUS, ITEM_VRADIUS);
    List<EntityLivingBase> nonPlayer = getLivingHostile(world, range);
    return pullEntityList(x, y, z, towardsPos, nonPlayer, speed, speed);
  }

  public static List<EntityLivingBase> getLivingHostile(World world, AxisAlignedBB range) {
    List<EntityLivingBase> all = world.getEntitiesWithinAABB(EntityLivingBase.class, range);
    List<EntityLivingBase> nonPlayer = new ArrayList<EntityLivingBase>();
    for (EntityLivingBase ent : all) {
      if (ent instanceof EntityPlayer == false && ent.isCreatureType(EnumCreatureType.MONSTER, false)) {//players are not monsters so, redundant?
        nonPlayer.add(ent);
      }
    }
    return nonPlayer;
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<? extends Entity> all) {
    return pullEntityList(x, y, z, towardsPos, all, ITEMSPEEDCLOSE, ITEMSPEEDFAR);
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<? extends Entity> all, float speedClose, float speedFar) {
    int moved = 0;
    double hdist, xDist, zDist;
    float speed;
    int direction = (towardsPos) ? 1 : -1;//negative to flip the vector and push it away
    for (Entity entity : all) {
      if (entity == null) {
        continue;
      } //being paranoid
      if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
        continue;//sneak avoid feature
      }
      xDist = Math.abs(x - entity.getPosition().getX());
      zDist = Math.abs(z - entity.getPosition().getZ());
      hdist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (hdist > ENTITY_PULL_DIST) {
        speed = (hdist > ENTITY_PULL_SPEED_CUTOFF) ? speedFar : speedClose;
        //        if (lockHorizontal) {
        //          entity.motionY = -1 * direction * speed;
        //        }
        //        else {
        Vector3.setEntityMotionFromVector(entity, x, y, z, direction * speed);
        //        }
        moved++;
      } //else its basically on it, no point
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

  /**
   * Force horizontal centering, so move from 2.9, 6.2 => 2.5,6.5
   * 
   * @param entity
   * @param pos
   */
  public static void centerEntityHoriz(Entity entity, BlockPos pos) {
    float fixedX = pos.getX() + 0.5F;//((float) (MathHelper.floor_double(entity.posX) + MathHelper.ceiling_double_int(entity.posX))  )/ 2;
    float fixedZ = pos.getZ() + 0.5F;//((float) (MathHelper.floor_double(entity.posX) + MathHelper.ceiling_double_int(entity.posX))  )/ 2;
    entity.setPosition(fixedX, entity.posY, fixedZ);
  }

  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often

  public static void tryMakeEntityClimb(World worldIn, EntityLivingBase entity, double climbSpeed) {
    if (entity.isSneaking()) {
      entity.motionY = 0.0D;
    }
    else if (entity.moveForward > 0.0F && entity.motionY < climbSpeed) {
      entity.motionY = climbSpeed;
    }
    if (worldIn.isRemote && //setting fall distance on clientside wont work
        entity instanceof EntityPlayer && entity.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
      ModCyclic.network.sendToServer(new PacketPlayerFalldamage());
    }
  }

  public static List<EntityVillager> getVillagers(World world, BlockPos p, int r) {
    BlockPos start = p.add(-r, -r, -r);
    BlockPos end = p.add(r, r, r);
    return world.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(start, end));
  }

  public static EntityLivingBase getClosestEntity(World world, EntityPlayer player, List<? extends EntityLivingBase> list) {
    EntityLivingBase closest = null;
    double minDist = 999999;
    double dist, xDistance, zDistance;
    for (EntityLivingBase ent : list) {
      xDistance = Math.abs(player.posX - ent.posX);
      zDistance = Math.abs(player.posZ - ent.posZ);
      dist = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
      if (dist < minDist) {
        minDist = dist;
        closest = ent;
      }
    }
    return closest;
  }

  public static EntityVillager getVillager(World world, int x, int y, int z) {
    List<EntityVillager> all = world.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(new BlockPos(x, y, z)));
    if (all.size() == 0)
      return null;
    else
      return all.get(0);
  }

  public static int getVillagerCareer(EntityVillager merchant) {
    return ObfuscationReflectionHelper.getPrivateValue(EntityVillager.class, merchant, "careerId", "field_175563_bv");
  }

  public static void setVillagerCareer(EntityVillager merchant, int c) {
    ObfuscationReflectionHelper.setPrivateValue(EntityVillager.class, merchant, c, "careerId", "field_175563_bv");
  }

  public static String getCareerName(EntityVillager merchant) {
    return merchant.getDisplayName().getFormattedText();//getProfessionForge().getCareer(maybeC).getName();
  }

  public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    float f = (float) ((180.0f * Math.atan2(posX2 - posX, posZ2 - posZ)) / (float) Math.PI);
    return f;
  }

  public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    return (float) Math.toDegrees(Math.atan2(posY2 - posY, Math.sqrt((posX2 - posX) * (posX2 - posX) + (posZ2 - posZ) * (posZ2 - posZ))));
  }

  public static Vec3d lookVector(float rotYaw, float rotPitch) {
    return new Vec3d(
        Math.sin(rotYaw) * Math.cos(rotPitch),
        Math.sin(rotPitch),
        Math.cos(rotYaw) * Math.cos(rotPitch));
  }

  public static float getYawFromFacing(EnumFacing currentFacing) {
    switch (currentFacing) {
      case DOWN:
      case UP:
      case SOUTH:
      default:
        return 0;
      case EAST:
        return 270F;
      case NORTH:
        return 180F;
      case WEST:
        return 90F;
    }
  }

  /**
   * used by bounce potion and vector plate
   * 
   * @param entity
   * @param verticalMomentumFactor
   */
  public static void dragEntityMomentum(EntityLivingBase entity, double verticalMomentumFactor) {
    entity.motionX = entity.motionX / verticalMomentumFactor;
    entity.motionZ = entity.motionZ / verticalMomentumFactor;
  }

  public static ResourceLocation getResourceLocation(Entity entityHit) {
    try {
      return ForgeRegistries.ENTITIES.getKey(EntityRegistry.getEntry(entityHit.getClass()));
    }
    catch (Exception e) {
      return null;
    }
  }
}
