package com.lothrazar.cyclicmagic.block.cable;

import com.lothrazar.cyclicmagic.core.item.BaseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CableBlocker extends BaseItem {

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    //  UtilChat.addChatMessage(player, "TEST" + side + " " + hitX + " " + hitY + " " + hitZ);
    if (world.getTileEntity(pos) instanceof TileEntityCableBase) {
      //      TileEntityCableBase te = (TileEntityCableBase) world.getTileEntity(pos);
      //      te.toggleBlacklist(side);
      //      boolean theNew = te.getBlacklist(side);
      //      UtilChat.sendStatusMessage(player, "TOGGLED" + theNew + " at side " + side);
      //      world.setBlockState(pos, world.getBlockState(pos)
      //          .withProperty(BlockCableBase.PROPERTIES.get(side),
      //              (theNew) ? EnumConnectType.BLOCKED : EnumConnectType.NONE));
      //      //TODO : stacksize
    }
    return EnumActionResult.FAIL;
  }

  //  @SubscribeEvent
  //  public void onHit(PlayerInteractEvent.RightClickBlock event) {
  //    if (event.getItemStack().getItem() == this) {
  //      UtilChat.addChatMessage(event.getEntityPlayer(), "RightClickBlock  " + event.getSide());
  //    }
  //  }
}
