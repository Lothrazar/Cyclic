package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockCrafter extends BlockBaseFacingInventory implements IHasRecipe, IHasConfig {
  public static int FUEL_COST = 0;
  public BlockCrafter() {
    super(Material.ROCK, ForgeGuiHandler.GUI_INDEX_CRAFTER);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityCrafter();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pcp",
        "y x",
        "pkp",
        'k', new ItemStack(Blocks.BONE_BLOCK),
        'x', new ItemStack(Blocks.OBSERVER),
        'y', new ItemStack(Blocks.PISTON),
        'c', "workbench",
        'p', "dyePurple");
  }
  @Override
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 25, 0, 500000, Const.ConfigText.fuelCost);
  }
}
