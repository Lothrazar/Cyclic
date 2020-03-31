package com.lothrazar.cyclic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketRandomize;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class RandomizerItem extends ItemBase {

  private static final int COOLDOWN = 15;

  public RandomizerItem(Properties properties) {
    super(properties.maxStackSize(1));
    CatEntity x;
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
      PacketRegistry.INSTANCE.sendToServer(new PacketRandomize(pos, side));
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
}
