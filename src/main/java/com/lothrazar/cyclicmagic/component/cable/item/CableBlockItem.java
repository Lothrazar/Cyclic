package com.lothrazar.cyclicmagic.component.cable.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.component.cable.CableBlockPrimary;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CableBlockItem extends CableBlockPrimary implements IHasRecipe {
  public CableBlockItem() {
    this.setItemTransport();
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityItemCable();
  }
  
  
  
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 32),
        "sis",
        " x ",
        "sis",
        's', Blocks.SANDSTONE_STAIRS, 'i', "ingotIron", 'x', "string");
  }
}
