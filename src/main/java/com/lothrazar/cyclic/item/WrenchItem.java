package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketRotateBlock;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class WrenchItem extends ItemBase {

  public WrenchItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    if (context.getWorld().isRemote) {
      PacketRegistry.INSTANCE.sendToServer(new PacketRotateBlock(context.getPos(), context.getFace(), context.getHand()));
    }
    //hack the sound back in
    //    IBlockState placeState = con.getBlockState(pos);
    //    if (placeState != null && placeState.getBlock() != null) {
    //    }
    //    onUse(stack, player, worldObj, hand);
    return super.onItemUse(context);// ActionResultType.PASS;
  }
}
