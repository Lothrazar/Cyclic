package com.lothrazar.cyclicmagic.component.bucketstorage;
import java.util.List;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBucket extends ItemBlock {
  // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1432714-forge-using-addinformation-on-a-block
  public ItemBlockBucket(Block block) {
    super(block);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, World player, List<String> tooltip,net.minecraft.client.util.ITooltipFlag advanced) {
    if (item.getItem() != Item.getItemFromBlock(BlockRegistry.block_storeempty))
      tooltip.add(UtilChat.lang("tile.block_storeany.tooltip") + BlockBucketStorage.getBucketsStored(item));
    else
      tooltip.add(UtilChat.lang("tile.block_storeempty.tooltip"));
  }
}
