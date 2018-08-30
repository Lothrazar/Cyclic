package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemObsShears extends ItemShears implements IHasRecipe {

  public ItemObsShears() {
    super();
    int ironDur = 238;
    this.setMaxDamage(ironDur * 10);
  }

  @Override
  public IRecipe addRecipe() {
    ItemStack self = new ItemStack(this);
    //no this is too OP
    //  self.addEnchantment(Enchantments.SILK_TOUCH, Enchantments.SILK_TOUCH.getMaxLevel());
    return RecipeRegistry.addShapedRecipe(self,
        " l ",
        "lsl",
        " l ",
        'l', "obsidian",
        's', new ItemStack(Items.SHEARS, 1, 0));
  }

  @Override
  public boolean canHarvestBlock(IBlockState blockIn) {
    Block block = blockIn.getBlock();
    return block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || super.canHarvestBlock(blockIn);
  }

  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {
    Block block = state.getBlock();
    if (block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN) {
      return 15F;//
    }
    return super.getStrVsBlock(stack, state);
  }
}
