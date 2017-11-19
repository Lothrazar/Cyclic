package com.lothrazar.cyclicmagic.component.wireless;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockWireless extends ItemBlock {
  public ItemBlockWireless(Block block) {
    super(block);

  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    NBTTagCompound nbt = stack.getTagCompound();
    if (nbt != null) {
      
      BlockPos pos = UtilNBT.getItemStackBlockPos(stack);
      
      if(pos != null){
        
        
        tooltip.add(pos.toString());
      }
      
      
      
      
    }
  }
}
