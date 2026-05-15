package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.packet.PacketRotateBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.network.PacketDistributor;

public class RotatorItem extends ItemBaseCyclic {

  public RotatorItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    if (context.getLevel().isClientSide) {
      PacketDistributor.sendToServer(new PacketRotateBlock(context.getClickedPos(), context.getClickedFace(), context.getHand()));
      context.getPlayer().swing(context.getHand());
    }
    return super.useOn(context);
  }
}
