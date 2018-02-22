package com.lothrazar.cyclicmagic.component.pump.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItemPump extends BlockBaseFacingOmni implements ITileEntityProvider, IHasRecipe {
  public BlockItemPump() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(3F);
    this.setHarvestLevel("pickaxe", 1);
    this.setTranslucent();
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_ITEMPUMP);
    this.placeType = PlacementType.SIDE_BLOCK;
  }
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if (side == EnumFacing.UP || side == EnumFacing.DOWN)
      return false;//allows, for example, you to open chest that is directly below
    return true;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityItemPump();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "i i",
        " r ",
        "ibi",
        'b', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
        'i', "nuggetIron",
        'r', Blocks.DROPPER);
  }
}
