package com.lothrazar.cyclicmagic.component.entitydetector;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDetector extends BlockBaseHasTile implements IHasRecipe {
  public BlockDetector() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_DETECTOR);
    this.setTranslucent();
  }
  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    TileEntityDetector te = (TileEntityDetector) blockAccess.getTileEntity(pos);
    if (te == null) { return 0; }
    return te.isPowered() ? 15 : 0;
  }
  @Override
  public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return false;
  }
  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityDetector();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "dcd",
        " q ",
        "r r",
        'r', "dustRedstone",
        'q', "gemQuartz",
        'c', "blockCoal",
        'd', "gemDiamond");
  }
}
