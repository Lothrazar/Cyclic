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

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.Vector3;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.world.DimensionTransit;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@SuppressWarnings("deprecation")
public class UtilEntity {

  private static final double ENTITY_PULL_DIST = 0.4; //closer than this and nothing happens
  private static final double ENTITY_PULL_SPEED_CUTOFF = 3; //closer than this and it slows down
  private static final float ITEMSPEEDFAR = 0.9F;
  private static final float ITEMSPEEDCLOSE = 0.2F;
  private static final int TICKS_FALLDIST_SYNC = 22; //tick every so often

  /**
   *
   * @return true if teleport was a success
   */
  private static boolean enderTeleportEvent(LivingEntity player, Level world, double x, double y, double z) {
    if (player.getRootVehicle() != player) {
      return false;
    }
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
  public static boolean enderTeleportEvent(LivingEntity player, Level world, BlockPos target) {
    return enderTeleportEvent(player, world, target.getX() + .5F, target.getY() + .5F, target.getZ() + .5F);
  }

  private static void teleportWallSafe(LivingEntity player, Level world, double x, double y, double z) {
    BlockPos coords = new BlockPos(x, y, z);
    //    world.update
    //    world.markBlockRangeForRenderUpdate(coords, coords);
    world.getChunk(coords).setUnsaved(true);
    player.teleportTo(x, y, z);
    moveEntityWallSafe(player, world);
  }

  public static void moveEntityWallSafe(Entity entity, Level world) {
    //    world.checkBlockCollision(bb)
    //    world.hasNoCollisions(p_226669_1_)
    while (world.noCollision(entity) == false) {
      entity.teleportTo(entity.xo, entity.yo + 1.0D, entity.zo);
    }
  }

  public static Direction getFacing(LivingEntity entity) {
    int yaw = (int) entity.yRot;
    if (yaw < 0) { // due to the yaw running a -360 to positive 360
      yaw += 360; // not sure why it's that way
    }
    yaw += 22; // centers coordinates you may want to drop this line
    yaw %= 360; // and this one if you want a strict interpretation of the
    // zones
    int facing = yaw / 45; // 360degrees divided by 45 == 8 zones
    return Direction.from2DDataValue(facing / 2);
  }

  public static double getSpeedTranslated(double speed) {
    return speed * 100;
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
    Entity ridingEntity = entity.getVehicle();
    if (ridingEntity != null) {
      // boost power a bit, horses are heavy as F
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      ridingEntity.fallDistance = 0;
      ridingEntity.push(velX, velY, velZ);
    }
    else {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      entity.fallDistance = 0;
      entity.push(velX, velY, velZ);
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
    float rotationYaw = entity.yRot;
    launch(entity, rotationPitch, rotationYaw, power);
  }

  public static void launch(Entity entity, float rotationPitch, float rotationYaw, float power) {
    float mountPower = (float) (power + 0.5);
    double velX = -Mth.sin(rotationYaw / 180.0F * (float) Math.PI) * Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velZ = Mth.cos(rotationYaw / 180.0F * (float) Math.PI) * Mth.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
    double velY = -Mth.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
    // launch the player up and forward at minimum angle
    // regardless of look vector
    if (velY < 0) {
      velY *= -1; // make it always up never down
    }
    Entity ridingEntity = entity.getVehicle();
    if (ridingEntity != null) {
      // boost power a bit, horses are heavy as F
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      ridingEntity.fallDistance = 0;
      ridingEntity.push(velX * mountPower, velY * mountPower, velZ * mountPower);
    }
    else {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
      entity.fallDistance = 0;
      entity.push(velX, velY, velZ);
    }
  }

  public static AABB makeBoundingBox(BlockPos center, int hRadius, int vRadius) {
    //so if radius is 1, it goes 1 in each direction, and boom, 3x3 selected
    return new AABB(center).expandTowards(hRadius, vRadius, hRadius);
  }

  public static AABB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AABB(
        x - hRadius, y - vRadius, z - hRadius,
        x + hRadius, y + vRadius, z + hRadius);
  }

  public static int moveEntityItemsInRegion(Level world, BlockPos pos, int hRadius, int vRadius) {
    return moveEntityItemsInRegion(world, pos.getX(), pos.getY(), pos.getZ(), hRadius, vRadius, true);
  }

  public static int moveEntityItemsInRegion(Level world, double x, double y, double z, int hRadius, int vRadius, boolean towardsPos) {
    AABB range = makeBoundingBox(x, y, z, hRadius, vRadius);
    List<Entity> all = getItemExp(world, range);
    return pullEntityList(x, y, z, towardsPos, all);
  }

  public static List<Entity> getItemExp(Level world, AABB range) {
    List<Entity> all = new ArrayList<Entity>();
    all.addAll(world.getEntitiesOfClass(ItemEntity.class, range));
    all.addAll(world.getEntitiesOfClass(ExperienceOrb.class, range));
    return all;
  }

  public static boolean speedupEntityIfMoving(LivingEntity entity, float factor) {
    if (entity.zza > 0) {
      if (entity.getVehicle() != null && entity.getVehicle() instanceof LivingEntity) {
        speedupEntity((LivingEntity) entity.getVehicle(), factor);
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
    float x = Mth.sin(-entity.yRot * 0.017453292F) * factor;
    float z = Mth.cos(entity.yRot * 0.017453292F) * factor;
    entity.setDeltaMovement(x, entity.getDeltaMovement().y, z);
  }

  public static int moveEntityLivingNonplayers(Level world, double x, double y, double z, int horizRadius, int height, boolean towardsPos, float speed) {
    AABB range = UtilEntity.makeBoundingBox(x, y, z, horizRadius, height);
    List<LivingEntity> nonPlayer = getLivingHostile(world, range);
    return pullEntityList(x, y, z, towardsPos, nonPlayer, speed, speed);
  }

  public static List<LivingEntity> getLivingHostile(Level world, AABB range) {
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, range);
    List<LivingEntity> nonPlayer = new ArrayList<LivingEntity>();
    for (LivingEntity ent : all) {
      if (ent instanceof Player == false) {
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
    int moved = 0;
    double hdist, xDist, zDist;
    float speed;
    int direction = (towardsPos) ? 1 : -1; //negative to flip the vector and push it away
    for (Entity entity : all) {
      if (entity == null) {
        continue;
      } //being paranoid
      if (entity instanceof Player && ((Player) entity).isCrouching()) {
        continue; //sneak avoid feature
      }
      BlockPos p = entity.blockPosition();
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
    if (finalVector.mag() > 1) {
      finalVector.normalize();
    }
    double motionX = finalVector.x * modifier;
    double motionY = finalVector.y * modifier;
    double motionZ = finalVector.z * modifier;
    entity.setDeltaMovement(motionX, motionY, motionZ);
  }

  public static void addOrMergePotionEffect(LivingEntity player, MobEffectInstance newp) {
    // this could be in a utilPotion class i guess...
    if (player.hasEffect(newp.getEffect())) {
      // do not use built in 'combine' function, just add up duration
      MobEffectInstance p = player.getEffect(newp.getEffect());
      int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
      int dur = newp.getDuration() + p.getDuration();
      player.addEffect(new MobEffectInstance(newp.getEffect(), dur, ampMax));
    }
    else {
      player.addEffect(newp);
    }
  }

  /**
   * Force horizontal centering, so move from 2.9, 6.2 => 2.5,6.5
   *
   * @param entity
   * @param pos
   */
  public static void centerEntityHoriz(Entity entity, BlockPos pos) {
    float fixedX = pos.getX() + 0.5F; //((float) (MathHelper.floor_double(entity.posX) + MathHelper.ceiling_double_int(entity.posX))  )/ 2;
    float fixedZ = pos.getZ() + 0.5F; //((float) (MathHelper.floor_double(entity.posX) + MathHelper.ceiling_double_int(entity.posX))  )/ 2;
    entity.setPos(fixedX, entity.blockPosition().getY(), fixedZ);
  }

  public static List<Villager> getVillagers(Level world, BlockPos p, int r) {
    BlockPos start = p.offset(-r, -r, -r);
    BlockPos end = p.offset(r, r, r);
    return world.getEntitiesOfClass(Villager.class, new AABB(start, end));
  }

  public static LivingEntity getClosestEntity(Level world, Player player, List<? extends LivingEntity> list) {
    LivingEntity closest = null;
    double minDist = 999999;
    double dist, xDistance, zDistance;
    for (LivingEntity ent : list) {
      xDistance = Math.abs(player.xo - ent.xo);
      zDistance = Math.abs(player.zo - ent.zo);
      dist = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
      if (dist < minDist) {
        minDist = dist;
        closest = ent;
      }
    }
    return closest;
  }

  public static Villager getVillager(Level world, int x, int y, int z) {
    List<Villager> all = world.getEntitiesOfClass(Villager.class, new AABB(new BlockPos(x, y, z)));
    if (all.size() == 0) {
      return null;
    }
    else {
      return all.get(0);
    }
  }
  //  public static int getVillagerCareer(VillagerEntity merchant) {
  //    return ObfuscationReflectionHelper.getPrivateValue(VillagerEntity.class, merchant, "careerId", "field_175563_bv");
  //  }
  //
  //  public static void setVillagerCareer(VillagerEntity merchant, int c) {
  //    ObfuscationReflectionHelper.setPrivateValue(VillagerEntity.class, merchant, c, "careerId", "field_175563_bv");
  //  }
  //  public static String getCareerName(VillagerEntity merchant) {
  //    return merchant.getDisplayName().getString(); //.getFormattedText();//getProfessionForge().getCareer(maybeC).getName();
  //  }

  public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    float f = (float) ((180.0f * Math.atan2(posX2 - posX, posZ2 - posZ)) / (float) Math.PI);
    return f;
  }

  public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    return (float) Math.toDegrees(Math.atan2(posY2 - posY, Math.sqrt((posX2 - posX) * (posX2 - posX) + (posZ2 - posZ) * (posZ2 - posZ))));
  }

  public static Vec3 lookVector(float rotYaw, float rotPitch) {
    return new Vec3(
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
    entity.yRot = yaw;
  }

  /**
   * used by bounce potion and vector plate
   *
   * @param entity
   * @param verticalMomentumFactor
   */
  public static void dragEntityMomentum(LivingEntity entity, double verticalMomentumFactor) {
    double x = entity.getDeltaMovement().x / verticalMomentumFactor;
    double z = entity.getDeltaMovement().z / verticalMomentumFactor;
    entity.setDeltaMovement(x, entity.getDeltaMovement().y, z);
  }

  public static void setCooldownItem(Player player, Item item, int cooldown) {
    player.getCooldowns().addCooldown(item, cooldown);
  }

  public static Attribute getAttributeJump(Horse ahorse) {
    return Attributes.JUMP_STRENGTH; //was reflection lol
  }

  public static void eatingHorse(Horse ahorse) {
    try {
      Method m = ObfuscationReflectionHelper.findMethod(AbstractHorse.class, "eating"); // "eatingHorse");
      //      Method m = AbstractHorseEntity.class.getDeclaredMethod("eatingHorse");
      m.setAccessible(true);
      m.invoke(ahorse);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Horse eating animation error", e);
    }
  }

  public static void tryMakeEntityClimb(Level worldIn, LivingEntity entity, double climbSpeed) {
    if (entity.isCrouching()) {
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0.0, entity.getDeltaMovement().z);
    }
    else if (entity.zza > 0.0F && entity.getDeltaMovement().y < climbSpeed) {
      entity.setDeltaMovement(entity.getDeltaMovement().x, climbSpeed, entity.getDeltaMovement().z);
      entity.fallDistance = 0.0F;
    } //setting fall distance on clientside wont work
    if (worldIn.isClientSide && entity.tickCount % TICKS_FALLDIST_SYNC == 0) {
      PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
    }
  }

  public static void dimensionTeleport(Player player, Level world, BlockPosDim loc) {
    if (world instanceof ServerLevel) {
      DimensionTransit transit = new DimensionTransit((ServerLevel) world, loc);
      transit.teleport(player);
    }
  }
}
