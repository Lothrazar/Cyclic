package com.lothrazar.cyclicmagic.block.voidshelf;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockVoidAnvil extends BlockBaseFacingInventory implements IHasRecipe {

  public BlockVoidAnvil() {
    super(Material.ROCK, ForgeGuiHandler.GUI_INDEX_VOID);

  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityVoidAnvil();
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
        'o', "cobblestone",
        'g', "nuggetIron",
        's', Blocks.ANVIL,
        'r', "blockRedstone",
        'b', Items.FLINT);
  }
}
