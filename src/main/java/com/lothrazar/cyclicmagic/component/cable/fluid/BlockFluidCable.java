package com.lothrazar.cyclicmagic.component.cable.fluid;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFluidCable extends BlockBaseCable implements IHasRecipe {
  public BlockFluidCable() {
    super(Material.CLAY);
    this.setFluidTransport();
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityFluidCable();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 32),
        "sis",
        " x ",
        "sis",
        's', Blocks.STONE_STAIRS, 'i', "ingotIron", 'x', "string");
  }
}
