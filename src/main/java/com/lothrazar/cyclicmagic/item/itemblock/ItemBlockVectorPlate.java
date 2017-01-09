package com.lothrazar.cyclicmagic.item.itemblock;
import java.util.List;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockVectorPlate extends ItemBlock {
  // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1432714-forge-using-addinformation-on-a-block
  public ItemBlockVectorPlate(Block block) {
    super(block);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    stack.getItem().updateItemStackNBT(stack.getTagCompound());
    String d = UtilNBT.getItemStackDisplayInteger(stack, TileVector.NBT_ANGLE);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.angle") + d);
    d = UtilNBT.getItemStackDisplayInteger(stack, TileVector.NBT_POWER);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.power") + d);
    d = UtilNBT.getItemStackDisplayInteger(stack, TileVector.NBT_YAW);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.yaw") + d);
  }
  /**
   * set default dag data so its nonempty. just like ItemSkull
   */
  @Override
  public boolean updateItemStackNBT(NBTTagCompound nbt) {
    boolean altered = false;
    if (nbt == null) {
      nbt = new NBTTagCompound();
    }
    if (!nbt.hasKey(TileVector.NBT_ANGLE)) {
      nbt.setInteger(TileVector.NBT_ANGLE, TileVector.DEFAULT_ANGLE);
      altered = true;
    }
    if (!nbt.hasKey(TileVector.NBT_POWER)) {
      nbt.setInteger(TileVector.NBT_POWER, TileVector.DEFAULT_POWER);
      altered = true;
    }
    if (!nbt.hasKey(TileVector.NBT_YAW)) {
      nbt.setInteger(TileVector.NBT_YAW, TileVector.DEFAULT_YAW);
      altered = true;
    }
    if (!nbt.hasKey(TileVector.NBT_SOUND)) {
      nbt.setInteger(TileVector.NBT_SOUND, 1);
      altered = true;
    }
    return altered;
  }
}
