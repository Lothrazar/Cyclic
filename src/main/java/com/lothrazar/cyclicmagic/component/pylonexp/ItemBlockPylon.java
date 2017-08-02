package com.lothrazar.cyclicmagic.component.pylonexp;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockPylon extends ItemBlock {
  // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1432714-forge-using-addinformation-on-a-block
  public ItemBlockPylon(Block block) {
    super(block);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang("tile.exp_pylon.tooltip"));
    if (item.getTagCompound() != null) {
      int amt = item.getTagCompound().getInteger(BlockBase.NBT_FLUIDSIZE);
      String rsc = item.getTagCompound().getString(BlockBase.NBT_FLUIDTYPE);
      tooltip.add(amt + " " + rsc);
    }
  }
}
