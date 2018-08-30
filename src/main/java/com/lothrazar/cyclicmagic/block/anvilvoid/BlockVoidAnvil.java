package com.lothrazar.cyclicmagic.block.anvilvoid;

import com.lothrazar.cyclicmagic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockVoidAnvil extends BlockBaseHasTile implements IHasConfig, IHasRecipe {

  public static int FUEL_COST = 0;

  public BlockVoidAnvil() {
    super(Material.ANVIL);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_VOID);
    this.setTranslucent();
  }

  @Override
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 2000, 0, 500000, Const.ConfigText.fuelCost);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityVoidAnvil();
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return BlockAnvilAuto.Z_AXIS_AABB;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ror",
        "gbg",
        "gsg",
        'o', "obsidian",
        'g', "nuggetGold",
        's', Blocks.ANVIL,
        'r', "dustRedstone",
        'b', Items.FLINT);
  }
}
