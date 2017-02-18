package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTrader extends BaseItem {
  public static final int radius = 1;
  public ItemTrader() {
    super();
    this.setMaxStackSize(1);
  }
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player, EnumHand hand) {
    BlockPos p = player.getPosition();
    if (world.isRemote == false) {
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_VILLAGER, world, p.getX(), p.getY(), p.getZ());
    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
  }
}
