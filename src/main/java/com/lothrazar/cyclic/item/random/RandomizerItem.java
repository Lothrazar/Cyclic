package com.lothrazar.cyclic.item.random;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RandomizerItem extends ItemBaseCyclic {

  private static final int COOLDOWN = 15;

  public RandomizerItem(Properties properties) {
    super(properties.stacksTo(1).durability(4096));
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof RandomizerItem) {
      return heldItem;
    }
    heldItem = player.getOffhandItem();
    if (heldItem.getItem() instanceof RandomizerItem) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    if (player.getCooldowns().isOnCooldown(stack.getItem())) {
      return super.useOn(context);
    }
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    if (player.level.isClientSide) {
      PacketRegistry.INSTANCE.sendToServer(new PacketRandomize(pos, side, context.getHand()));
    }
    UtilEntity.setCooldownItem(player, this, COOLDOWN);
    return super.useOn(context);
  }

  public static List<BlockPos> getPlaces(final BlockPos pos, final Direction side) {
    List<BlockPos> places = new ArrayList<BlockPos>();
    int xMin = pos.getX();
    int yMin = pos.getY();
    int zMin = pos.getZ();
    int xMax = pos.getX();
    int yMax = pos.getY();
    int zMax = pos.getZ();
    boolean isVertical = (side == Direction.UP || side == Direction.DOWN);
    int offsetRadius = 0;
    //    case X5:
    offsetRadius = 2;
    if (offsetRadius > 0) {
      if (isVertical) {
        //then we just go in all horizontal directions
        xMin -= offsetRadius;
        xMax += offsetRadius;
        zMin -= offsetRadius;
        zMax += offsetRadius;
      }
      //we hit a horizontal side
      else if (side == Direction.EAST || side == Direction.WEST) {
        //now we go in a vertical plane
        zMin -= offsetRadius;
        zMax += offsetRadius;
        yMin -= offsetRadius;
        yMax += offsetRadius;
      }
      else {
        //axis hit was north/south, so we go in YZ
        xMin -= offsetRadius;
        xMax += offsetRadius;
        yMin -= offsetRadius;
        yMax += offsetRadius;
      }
      places = UtilWorld.getPositionsInRange(pos, xMin, xMax, yMin, yMax, zMin, zMax);
    }
    return places;
  }

  public static boolean canMove(BlockState stateHere, Level world, BlockPos p) {
    if (stateHere.getDestroySpeed(world, p) < 0) {
      return false; //unbreakable
    }
    if (world.getBlockEntity(p) == null
        && world.isEmptyBlock(p) == false
        && stateHere.getMaterial().isLiquid() == false) {
      return true;
    }
    return false;
  }
}
