package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;

public class ItemObsShears extends ItemShears implements IHasRecipe, IContent {

  public ItemObsShears() {
    super();
    int ironDur = 238;
    this.setMaxDamage(ironDur * 10);
  }

  @Override
  public void register() {
    ItemRegistry.register(this, "shears_obsidian", GuideCategory.ITEM);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("shears_obsidian", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
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
