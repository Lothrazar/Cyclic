package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class UtilStuff {

  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often

  public static void tryMakeEntityClimb(World worldIn, LivingEntity entity, double climbSpeed) {
    if (entity.isSneaking()) {
      entity.setMotion(entity.getMotion().x, 0.0, entity.getMotion().z);
    }
    else if (entity.moveForward > 0.0F && entity.getMotion().y < climbSpeed) {
      entity.setMotion(entity.getMotion().x, climbSpeed, entity.getMotion().z);
      entity.fallDistance = 0.0F;
      //TODO: packet for falldamgage
    } //setting fall distance on clientside wont work
    if (worldIn.isRemote && entity.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
      PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
    }
  }

  public static void playSound(PlayerEntity player, SoundEvent thunk) {
    playSound(player, player.getPosition(), thunk);
  }

  public static void playSound(PlayerEntity player, BlockPos pos, SoundEvent thunk) {
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    playSound(player, here, thunk, player.getSoundCategory());
  }

  public static final float VOLUME = 1.0F;
  public static final float PITCH = 1.0F;

  public static void playSound(PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory cat) {
    if (player == null) {
      return;
    }
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    player.getEntityWorld().playSound(player, here, soundIn, cat, VOLUME, PITCH);
  }

  public static String lang(String message) {
    TranslationTextComponent t = new TranslationTextComponent(message);
    return t.getFormattedText();
  }

  public static void messageChat(PlayerEntity player, String message) {
    player.sendMessage(new TranslationTextComponent(message));
  }

  public static void messageStatus(PlayerEntity player, String message) {
    player.sendStatusMessage(new TranslationTextComponent(message), true);
  }

  public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
  }

  public static Direction getFacingFromEntityHorizontal(BlockPos clickedBlock, LivingEntity entity) {
    Direction d = getFacingFromEntity(clickedBlock, entity);
    //if only horizontal is allowed
    if (d == Direction.UP || d == Direction.DOWN) {
      return entity.getHorizontalFacing().getOpposite();
    }
    return d;
  }

  public static ArrayList<BlockPos> findBlocks(World world, BlockPos start, Block blockHunt, int RADIUS) {
    ArrayList<BlockPos> found = new ArrayList<BlockPos>();
    int xMin = start.getX() - RADIUS;
    int xMax = start.getX() + RADIUS;
    int yMin = start.getY() - RADIUS;
    int yMax = start.getY() + RADIUS;
    int zMin = start.getZ() - RADIUS;
    int zMax = start.getZ() + RADIUS;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (world.getBlockState(posCurrent).getBlock().equals(blockHunt)) {
            found.add(posCurrent);
          }
        }
      }
    }
    return found;
  }
}
