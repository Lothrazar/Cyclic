package com.lothrazar.cyclicmagic.component.itemsort;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockItemCableSort extends BlockBaseHasTile implements IHasRecipe {
  public BlockItemCableSort() {
    super(Material.CLAY);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_SORT);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityItemCableSort();
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityBaseMachineInvo) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBaseMachineInvo) tileentity);
    }
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 4),
        "sis",
        "fxf",
        "sis",
        'i', Blocks.STONE_PRESSURE_PLATE,
        's', "ingotIron",
        'x', "dustRedstone",
        'f', Blocks.DROPPER);
   
  }
}
