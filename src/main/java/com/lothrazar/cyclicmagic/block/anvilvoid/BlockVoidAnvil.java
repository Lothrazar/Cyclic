package com.lothrazar.cyclicmagic.block.anvilvoid;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockVoidAnvil extends BlockBaseHasTile implements IHasRecipe, IContent {

  public static int FUEL_COST = 0;

  public BlockVoidAnvil() {
    super(Material.ANVIL);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_VOID);
    this.setTranslucent();
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
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 2000, 0, 500000, Const.ConfigText.fuelCost);
    enabled = config.getBoolean("void_anvil", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, "void_anvil", GuideCategory.BLOCK);
    GameRegistry.registerTileEntity(TileEntityVoidAnvil.class, "void_anvil_te");
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
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
