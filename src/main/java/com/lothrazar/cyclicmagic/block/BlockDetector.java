package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDetector extends BlockBaseHasTile implements IHasRecipe {
  public BlockDetector() {
    super(Material.IRON);
    this.setTooltip("tile.entity_detector.tooltip");
    this.setGuiId(ModGuiHandler.GUI_INDEX_DETECTOR);
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
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "dcd",
        " q ",
        "r r",
        'r', Items.REDSTONE,
        'q', Blocks.QUARTZ_BLOCK,
        'c', Blocks.COAL_BLOCK,
        'd', Items.DIAMOND);
  }
}
