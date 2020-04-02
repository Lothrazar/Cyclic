package com.lothrazar.cyclic.item.random;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RandomizerItem extends ItemBase {

  private static final int COOLDOWN = 15;

  public RandomizerItem(Properties properties) {
    super(properties.maxStackSize(1).maxDamage(4096));
  }

  public static ItemStack getIfHeld(PlayerEntity player) {
    ItemStack heldItem = player.getHeldItemMainhand();
    if (heldItem.getItem() instanceof RandomizerItem) {
      return heldItem;
    }
    heldItem = player.getHeldItemOffhand();
    if (heldItem.getItem() instanceof RandomizerItem) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    ItemStack stack = context.getItem();
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return super.onItemUse(context);
    }
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    if (player.world.isRemote) {
      PacketRegistry.INSTANCE.sendToServer(new PacketRandomize(pos, side, context.getHand()));
    }
    UtilEntity.setCooldownItem(player, this, COOLDOWN);
    return super.onItemUse(context);
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

  public static boolean canMove(BlockState stateHere, World world, BlockPos p) {
    if (stateHere.getBlock().getBlockHardness(stateHere, world, p) < 0) {
      return false;//unbreakable
    }
    if (world.getTileEntity(p) == null
        && world.isAirBlock(p) == false
        && stateHere.getMaterial().isLiquid() == false) {
      return true;
    }
    return false;
  }
}
