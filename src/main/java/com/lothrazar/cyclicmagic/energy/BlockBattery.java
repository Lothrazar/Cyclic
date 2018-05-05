package com.lothrazar.cyclicmagic.energy;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBattery extends BlockBaseHasTile implements IHasRecipe {

  private boolean isCreative;

  public BlockBattery(boolean creat) {
    super(Material.ROCK);
    this.isCreative = creat;
  }

  @Override
  public IRecipe addRecipe() {
    if (isCreative)
      return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this),
          new ItemStack(Blocks.COMMAND_BLOCK), new ItemStack(Blocks.FIRE));
    
    
    return RecipeRegistry.addShapedOreRecipe(new ItemStack (this),
        "aaa",
        "aaa",
        "aaa",
        'a', "blockRedstone");
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    if (isCreative)
      return new TileEntityBatteryInfinite();
    else
    return new TileEntityBattery();
  }
}
