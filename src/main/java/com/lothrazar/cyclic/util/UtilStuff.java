package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class UtilStuff {

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
