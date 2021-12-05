package com.lothrazar.cyclic.item.scythe;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.net.PacketHarvesting;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;

public class ScytheHarvest extends ItemBaseCyclic {

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
    PacketRegistry.INSTANCE.sendToServer(new PacketHarvesting(pos, radius));
    //    HarvestUtil.harvestShape(context.getLevel(), pos, radius);
    context.getPlayer().swing(context.getHand());
    UtilItemStack.damageItem(context.getPlayer(), context.getItemInHand());
    return super.useOn(context);
  }
}
