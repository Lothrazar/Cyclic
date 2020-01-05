package com.lothrazar.cyclic.item.tool;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketMoveBlock;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class WrenchItem extends ItemBase {

  public WrenchItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    //    ItemStack stack = player.getHeldItem(context.getHand());
    //
    if (context.getWorld().isRemote) {
      PacketRegistry.INSTANCE.sendToServer(new PacketMoveBlock(context.getPos(), context.getFace()));
    }
    //hack the sound back in
    //    IBlockState placeState = con.getBlockState(pos);
    //    if (placeState != null && placeState.getBlock() != null) {
    //      UtilSound.playSoundPlaceBlock(player, pos, placeState.getBlock());
    //    }
    //    onUse(stack, player, worldObj, hand);
    return super.onItemUse(context);// ActionResultType.PASS;
  }
}
