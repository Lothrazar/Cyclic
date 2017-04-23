package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.component.vector.TileEntityVector;
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
    String d = UtilNBT.getItemStackDisplayInteger(stack, TileEntityVector.NBT_ANGLE);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.angle") + d);
    d = UtilNBT.getItemStackDisplayInteger(stack, TileEntityVector.NBT_POWER);
    if (d.length() > 0)
      tooltip.add(UtilChat.lang("tile.plate_vector.tooltip.power") + d);
    d = UtilNBT.getItemStackDisplayInteger(stack, TileEntityVector.NBT_YAW);
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
    if (!nbt.hasKey(TileEntityVector.NBT_ANGLE)) {
      nbt.setInteger(TileEntityVector.NBT_ANGLE, TileEntityVector.DEFAULT_ANGLE);
      altered = true;
    }
    if (!nbt.hasKey(TileEntityVector.NBT_POWER)) {
      nbt.setInteger(TileEntityVector.NBT_POWER, TileEntityVector.DEFAULT_POWER);
      altered = true;
    }
    if (!nbt.hasKey(TileEntityVector.NBT_YAW)) {
      nbt.setInteger(TileEntityVector.NBT_YAW, TileEntityVector.DEFAULT_YAW);
      altered = true;
    }
    if (!nbt.hasKey(TileEntityVector.NBT_SOUND)) {
      nbt.setInteger(TileEntityVector.NBT_SOUND, 1);
      altered = true;
    }
    return altered;
  }
}
