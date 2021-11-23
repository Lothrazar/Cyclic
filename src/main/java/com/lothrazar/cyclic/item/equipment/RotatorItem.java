package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.net.PacketRotateBlock;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class RotatorItem extends ItemBaseCyclic {

  public RotatorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    if (context.getLevel().isClientSide) {
      PacketRegistry.INSTANCE.sendToServer(new PacketRotateBlock(context.getClickedPos(), context.getClickedFace(), context.getHand()));
      context.getPlayer().swing(context.getHand());
    }
    return super.useOn(context);
  }
}
