package com.lothrazar.cyclicmagic.block.batterycheat;

import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBatteryInfinite extends BlockBaseHasTile implements IHasRecipe {

  public BlockBatteryInfinite() {
    super(Material.ROCK);
  }

  @Override
  public IRecipe addRecipe() {
      return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this),
          new ItemStack(Blocks.COMMAND_BLOCK), new ItemStack(Blocks.BARRIER));

  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityBatteryInfinite();
  }

}
