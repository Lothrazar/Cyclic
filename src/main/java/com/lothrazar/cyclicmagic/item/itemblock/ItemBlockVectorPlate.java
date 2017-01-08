package com.lothrazar.cyclicmagic.item.itemblock;
import java.util.List;
import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockVectorPlate extends ItemBlock {
  // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1432714-forge-using-addinformation-on-a-block
  public ItemBlockVectorPlate(Block block) {
    super(block);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean advanced) {
    String d = UtilNBT.getItemStackDisplayInteger(item, TileVector.NBT_ANGLE);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.angle") + d);
    d = UtilNBT.getItemStackDisplayInteger(item, TileVector.NBT_POWER);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.power") + d);
    d = UtilNBT.getItemStackDisplayInteger(item, TileVector.NBT_YAW);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.yaw") + d);
  }
}
