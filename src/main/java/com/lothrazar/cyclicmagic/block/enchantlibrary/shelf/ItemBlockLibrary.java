package com.lothrazar.cyclicmagic.block.enchantlibrary.shelf;

import java.util.List;
import com.lothrazar.cyclicmagic.data.EnchantStack;
import com.lothrazar.cyclicmagic.data.QuadrantEnum;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockLibrary extends ItemBlock {

  public ItemBlockLibrary(Block block) {
    super(block);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    if (stack.getTagCompound() == null) {
      super.addInformation(stack, worldIn, tooltip, flagIn);
      return;
    }
    //    EnchantStack[] storage = new EnchantStack[QuadrantEnum.values().length];
    for (QuadrantEnum q : QuadrantEnum.values()) {
      EnchantStack s = new EnchantStack();
      s.readFromNBT(stack.getTagCompound(), q.name());
      if (s.getCount() > 0) {
        tooltip.add(s.toString());
      }
    }
  }
}
