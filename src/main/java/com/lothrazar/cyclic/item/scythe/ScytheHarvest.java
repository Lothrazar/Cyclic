package com.lothrazar.cyclic.item.scythe;

import java.util.List;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;

public class ScytheHarvest extends ItemBase {

  public ScytheHarvest(Properties properties) {
    super(properties);
  }

  private static final int RADIUS = 6; //13x13
  private static final int RADIUS_SNEAKING = 2; //2x2

  public List<BlockPos> getShape(BlockPos pos, int radius) {
    return UtilShape.squareHorizontalFull(pos, radius);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    if (side != null) {
      pos = pos.relative(side);
    }
    Player player = context.getPlayer();
    int radius = (player.isCrouching()) ? RADIUS_SNEAKING : RADIUS;
    for (BlockPos p : getShape(pos, radius)) {
      TileHarvester.tryHarvestSingle(context.getLevel(), p);
    }
    context.getPlayer().swing(context.getHand());
    UtilItemStack.damageItem(context.getPlayer(), context.getItemInHand());
    return super.useOn(context);
  }
}
