package com.lothrazar.cyclicmagic.block.anvilvoid;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclicmagic.block.core.BlockBaseFacing;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockVoidAnvil extends BlockBaseFacing implements IHasRecipe, IContent {

  public static int FUEL_COST = 0;

  public BlockVoidAnvil() {
    super(Material.ANVIL);
    this.setSoundType(SoundType.ANVIL);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_VOID);
    this.setTranslucent();
  }

  @Override
  public String getContentName() {
    return "void_anvil";
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityVoidAnvil();
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    EnumFacing enumfacing = state.getValue(BlockHorizontal.FACING);
    return enumfacing.getAxis() == EnumFacing.Axis.X ? BlockAnvilAuto.X_AXIS_AABB : BlockAnvilAuto.Z_AXIS_AABB;
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().rotateY());
  }

  @Override
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(getContentName(), Const.ConfigCategory.fuelCost, 2000, 0, 500000, Const.ConfigText.fuelCost);
    enabled = config.getBoolean(getContentName(), Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, getContentName(), GuideCategory.BLOCK);
    BlockRegistry.registerTileEntity(TileEntityVoidAnvil.class, getContentName() + "_te");
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
