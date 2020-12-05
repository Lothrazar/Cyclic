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
package com.lothrazar.cyclic.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Vector3;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class UtilEntity {

  public static void setPlayerReach(PlayerEntity player, int currentReach) {
    //thank you ForgeMod for adding this when mojang removed
    player.getAttribute(ForgeMod.REACH_DISTANCE.get()).setBaseValue(currentReach);
  }

  public static double getExpTotal(PlayerEntity player) {
    //  validateExpPositive(player);
    int level = player.experienceLevel;
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    double totalExp = getXpForLevel(level);
    double progress = Math.round(player.xpBarCap() * player.experience);
    totalExp += (int) progress;
    return totalExp;
  }

  public static int getXpForLevel(int level) {
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    int totalExp = 0;
    if (level <= 15)
      totalExp = level * level + 6 * level;
    else if (level <= 30)
      totalExp = (int) (2.5 * level * level - 40.5 * level + 360);
    else
      // level >= 31
      totalExp = (int) (4.5 * level * level - 162.5 * level + 2220);
    return totalExp;
  }

  private static final double ENTITY_PULL_DIST = 0.4;//closer than this and nothing happens
  private static final double ENTITY_PULL_SPEED_CUTOFF = 3;//closer than this and it slows down
  public static final UUID HEALTH_MODIFIER_ID = UUID.fromString("60b1b9b5-dc5d-43a2-aa4e-655353070dbe");
  public static final String HEALTH_MODIFIER_NAME = "Cyclic Health Modifier";
  private final static float ITEMSPEEDFAR = 0.9F;
  private final static float ITEMSPEEDCLOSE = 0.2F;

  public static void teleportWallSafe(Entity player, World world, double x, double y, double z) {
    BlockPos coords = new BlockPos(x, y, z);
    //    world.markBlockRangeForRenderUpdate(coords, coords);
    //    world.notifyBlockUpdate(pos, oldState, newState, flags);
    world.getChunk(coords).setModified(true);
    player.setPositionAndUpdate(x, y, z);
    moveEntityWallSafe(player, world);
  }

  /**
   *
   * @return true if teleport was a success
   */
  public static boolean enderTeleportEvent(LivingEntity player, World world, double x, double y, double z) {
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
  public static boolean enderTeleportEvent(LivingEntity player, World world, BlockPos target) {
    return enderTeleportEvent(player, world, target.getX(), target.getY(), target.getZ());
  }

  public static void teleportWallSafe(LivingEntity player, World world, double x, double y, double z) {
    BlockPos coords = new BlockPos(x, y, z);
    //    world.update
    //    world.markBlockRangeForRenderUpdate(coords, coords);
    world.getChunk(coords).setModified(true);
    player.setPositionAndUpdate(x, y, z);
    moveEntityWallSafe(player, world);
  }

  public static void teleportWallSafe(Entity entityIn, World world, BlockPos coords) {
    teleportWallSafe(entityIn, world, coords.getX(), coords.getY(), coords.getZ());
  }

  public static void moveEntityWallSafe(Entity entity, World world) {
    //    world.checkBlockCollision(bb)
    //    world.hasNoCollisions(p_226669_1_)
    while (world.hasNoCollisions(entity) == false) {
      entity.setPositionAndUpdate(entity.prevPosX, entity.prevPosY + 1.0D, entity.prevPosZ);
    }
  }

  public static Direction getFacing(LivingEntity entity) {
    int yaw = (int) entity.rotationYaw;
    if (yaw < 0) // due to the yaw running a -360 to positive 360
      yaw += 360; // not sure why it's that way
    yaw += 22; // centers coordinates you may want to drop this line
    yaw %= 360; // and this one if you want a strict interpretation of the
    // zones
    int facing = yaw / 45; // 360degrees divided by 45 == 8 zones
    return Direction.byHorizontalIndex(facing / 2);
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
  public static void launchDirection(Entity entity, float power, Direction facing) {
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
      entity.setMotion(entity.getMotion().x, 0, entity.getMotion().z);
      ridingEntity.fallDistance = 0;
      ridingEntity.addVelocity(velX, velY, velZ);
    }
    else {
      entity.setMotion(entity.getMotion().x, 0, entity.getMotion().z);
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
    entity.setMotion(0, 0, 0);
    double velX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velY = MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
    //    if (velY < 0) {
    //      velY *= -1;// make it always up never down
    //    }
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
      entity.setMotion(entity.getMotion().x, 0, entity.getMotion().z);
      ridingEntity.fallDistance = 0;
      ridingEntity.addVelocity(velX * mountPower, velY * mountPower, velZ * mountPower);
    }
    else {
      entity.setMotion(entity.getMotion().x, 0, entity.getMotion().z);
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
    all.addAll(world.getEntitiesWithinAABB(ItemEntity.class, range));
    all.addAll(world.getEntitiesWithinAABB(ExperienceOrbEntity.class, range));
    return all;
  }

  public static boolean speedupEntityIfMoving(LivingEntity entity, float factor) {
    if (entity.moveForward > 0) {
      if (entity.getRidingEntity() != null && entity.getRidingEntity() instanceof LivingEntity) {
        speedupEntity((LivingEntity) entity.getRidingEntity(), factor);
        return true;
      }
      else {
        speedupEntity(entity, factor);
        return true;
      }
    }
    return false;
  }

  public static void speedupEntity(LivingEntity entity, float factor) {
    float x = MathHelper.sin(-entity.rotationYaw * 0.017453292F) * factor;
    float z = MathHelper.cos(entity.rotationYaw * 0.017453292F) * factor;
    entity.setMotion(x, entity.getMotion().y, z);
  }

  public static int moveEntityLivingNonplayers(World world, double x, double y, double z, int ITEM_HRADIUS, int ITEM_VRADIUS, boolean towardsPos, float speed) {
    AxisAlignedBB range = UtilEntity.makeBoundingBox(x, y, z, ITEM_HRADIUS, ITEM_VRADIUS);
    List<LivingEntity> nonPlayer = getLivingHostile(world, range);
    return pullEntityList(x, y, z, towardsPos, nonPlayer, speed, speed);
  }

  public static List<LivingEntity> getLivingHostile(World world, AxisAlignedBB range) {
    List<LivingEntity> all = world.getEntitiesWithinAABB(LivingEntity.class, range);
    List<LivingEntity> nonPlayer = new ArrayList<LivingEntity>();
    for (LivingEntity ent : all) {
      if (ent instanceof PlayerEntity == false) {
        //&& ent.isCreatureType(EnumCreatureType.MONSTER, false)) {//players are not monsters so, redundant?
        nonPlayer.add(ent);
      }
    }
    return nonPlayer;
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<? extends Entity> all) {
    return pullEntityList(x, y, z, towardsPos, all, ITEMSPEEDCLOSE, ITEMSPEEDFAR);
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<? extends Entity> all, float speedClose, float speedFar) {
    //    ModCyclic.LOGGER.info("Found for magnet " + all.size());
    int moved = 0;
    double hdist, xDist, zDist;
    float speed;
    int direction = (towardsPos) ? 1 : -1;//negative to flip the vector and push it away
    for (Entity entity : all) {
      if (entity == null) {
        continue;
      } //being paranoid
      if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCrouching()) {
        continue;//sneak avoid feature
      }
      BlockPos p = entity.getPosition();
      xDist = Math.abs(x - p.getX());
      zDist = Math.abs(z - p.getZ());
      hdist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (hdist > ENTITY_PULL_DIST) {
        speed = (hdist > ENTITY_PULL_SPEED_CUTOFF) ? speedFar : speedClose;
        setEntityMotionFromVector(entity, x, y, z, direction * speed);
        moved++;
      } //else its basically on it, no point
    }
    return moved;
  }

  public static void setEntityMotionFromVector(Entity entity, double x, double y, double z, float modifier) {
    Vector3 originalPosVector = new Vector3(x, y, z);
    Vector3 entityVector = new Vector3(entity);
    Vector3 finalVector = originalPosVector.copy().subtract(entityVector);
    if (finalVector.mag() > 1)
      finalVector.normalize();
    double motionX = finalVector.x * modifier;
    double motionY = finalVector.y * modifier;
    double motionZ = finalVector.z * modifier;
    entity.setMotion(motionX, motionY, motionZ);
  }

  public static void addOrMergePotionEffect(LivingEntity player, EffectInstance newp) {
    // this could be in a utilPotion class i guess...
    if (player.isPotionActive(newp.getPotion())) {
      // do not use built in 'combine' function, just add up duration
      EffectInstance p = player.getActivePotionEffect(newp.getPotion());
      int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
      int dur = newp.getDuration() + p.getDuration();
      player.addPotionEffect(new EffectInstance(newp.getPotion(), dur, ampMax));
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
    entity.setPosition(fixedX, entity.getPosition().getY(), fixedZ);
  }

  private static final int TICKS_FALLDIST_SYNC = 16;//tick every so often

  public static void tryMakeEntityClimb(World worldIn, LivingEntity entity, double climbSpeed) {
    Vector3d motion = entity.getMotion();
    if (entity.isCrouching()) {
      entity.setMotion(motion.x, 0, motion.z);
    }
    else if (entity.moveForward > 0.0F && motion.y < climbSpeed) {
      entity.setMotion(motion.x, climbSpeed, motion.z);
    }
    if (worldIn.isRemote && //setting fall distance on clientside wont work
        entity instanceof PlayerEntity && entity.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
      PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
    }
  }

  public static List<VillagerEntity> getVillagers(World world, BlockPos p, int r) {
    BlockPos start = p.add(-r, -r, -r);
    BlockPos end = p.add(r, r, r);
    return world.getEntitiesWithinAABB(VillagerEntity.class, new AxisAlignedBB(start, end));
  }

  public static LivingEntity getClosestEntity(World world, PlayerEntity player, List<? extends LivingEntity> list) {
    LivingEntity closest = null;
    double minDist = 999999;
    double dist, xDistance, zDistance;
    for (LivingEntity ent : list) {
      xDistance = Math.abs(player.prevPosX - ent.prevPosX);
      zDistance = Math.abs(player.prevPosZ - ent.prevPosZ);
      dist = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
      if (dist < minDist) {
        minDist = dist;
        closest = ent;
      }
    }
    return closest;
  }

  public static VillagerEntity getVillager(World world, int x, int y, int z) {
    List<VillagerEntity> all = world.getEntitiesWithinAABB(VillagerEntity.class, new AxisAlignedBB(new BlockPos(x, y, z)));
    if (all.size() == 0)
      return null;
    else
      return all.get(0);
  }

  //  public static int getVillagerCareer(VillagerEntity merchant) {
  //    return ObfuscationReflectionHelper.getPrivateValue(VillagerEntity.class, merchant, "careerId", "field_175563_bv");
  //  }
  //
  //  public static void setVillagerCareer(VillagerEntity merchant, int c) {
  //    ObfuscationReflectionHelper.setPrivateValue(VillagerEntity.class, merchant, c, "careerId", "field_175563_bv");
  //  }
  public static String getCareerName(VillagerEntity merchant) {
    return merchant.getDisplayName().getString(); //.getFormattedText();//getProfessionForge().getCareer(maybeC).getName();
  }

  public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    float f = (float) ((180.0f * Math.atan2(posX2 - posX, posZ2 - posZ)) / (float) Math.PI);
    return f;
  }

  public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    return (float) Math.toDegrees(Math.atan2(posY2 - posY, Math.sqrt((posX2 - posX) * (posX2 - posX) + (posZ2 - posZ) * (posZ2 - posZ))));
  }

  public static Vector3d lookVector(float rotYaw, float rotPitch) {
    return new Vector3d(
        Math.sin(rotYaw) * Math.cos(rotPitch),
        Math.sin(rotPitch),
        Math.cos(rotYaw) * Math.cos(rotPitch));
  }

  public static float getYawFromFacing(Direction currentFacing) {
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

  public static void setEntityFacing(LivingEntity entity, Direction currentFacing) {
    float yaw = 0;
    switch (currentFacing) {
      case EAST:
        yaw = 270F;
      break;
      case NORTH:
        yaw = 180F;
      break;
      case WEST:
        yaw = 90F;
      break;
      case DOWN:
      case UP:
      case SOUTH:
      default:
        yaw = 0;
    }
    entity.rotationYaw = yaw;
  }

  /**
   * used by bounce potion and vector plate
   *
   * @param entity
   * @param verticalMomentumFactor
   */
  public static void dragEntityMomentum(LivingEntity entity, double verticalMomentumFactor) {
    double x = entity.getMotion().x / verticalMomentumFactor;
    double z = entity.getMotion().z / verticalMomentumFactor;
    entity.setMotion(x, entity.getMotion().y, z);
  }

  public static void setCooldownItem(PlayerEntity player, Item item, int cooldown) {
    player.getCooldownTracker().setCooldown(item, cooldown);
  }

  public static Attribute getAttributeJump(HorseEntity ahorse) {
    return Attributes.HORSE_JUMP_STRENGTH;//was reflection lol
  }

  public static void eatingHorse(HorseEntity ahorse) {
    try {
      Method m = ObfuscationReflectionHelper.findMethod(AbstractHorseEntity.class, "func_110266_cB");// "eatingHorse");
      //      Method m = AbstractHorseEntity.class.getDeclaredMethod("eatingHorse");
      m.setAccessible(true);
      m.invoke(ahorse);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Horse eating animation error", e);
    }
  }
}
