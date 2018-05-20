package com.lothrazar.cyclicmagic.block.firestarter;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockFireStarter extends BlockBaseFacingInventory implements IHasConfig, IHasRecipe {


  public static int FUEL_COST = 0;
  public BlockFireStarter() {
    super(Material.ROCK, ForgeGuiHandler.GUI_INDEX_FIREST);

  }

  @Override
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 250, 0, 500000, Const.ConfigText.fuelCost);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFireStarter();
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "bsb",
        "gbg",
        "ooo",
        'o', "cobblestone",
        'g', "nuggetIron",
        's', Blocks.DISPENSER,
        'b', Items.FLINT);
  }
}
