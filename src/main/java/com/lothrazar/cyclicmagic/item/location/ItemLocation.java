package com.lothrazar.cyclicmagic.item.location;

import java.util.List;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLocation extends BaseItem implements IHasRecipe {

  public ItemLocation() {
    this.setMaxStackSize(1);
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    this.savePosition(player, pos, hand);
    return EnumActionResult.SUCCESS;
  }

  private void savePosition(EntityPlayer player, BlockPos pos, EnumHand hand) {
    ItemStack held = player.getHeldItem(hand);
    player.swingArm(hand);
    UtilNBT.setItemStackBlockPos(held, pos);
    UtilChat.sendStatusMessage(player, UtilChat.lang("item.location.saved")
        + UtilChat.blockPosToString(pos));
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (stack.hasTagCompound()) {
      tooltip.add(UtilChat.blockPosToString(getPosition(stack)));
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
  public static BlockPos getPosition(ItemStack item) {
    return UtilNBT.getItemStackBlockPos(item);
  }
  @Override
  public IRecipe addRecipe() {
    // TODO Auto-generated method stub
    return null;
  }
}
