package com.lothrazar.cyclicmagic.component.workbench;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockWorkbench extends BlockBaseHasTile implements IHasRecipe {
  public BlockWorkbench() {
    super(Material.IRON);
    this.setTranslucent();
    this.setGuiId(ModGuiHandler.GUI_INDEX_WORKBENCH); 
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityWorkbench();
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        " t ",
        "s s",
        "   ",
        't', Blocks.CRAFTING_TABLE,
        's', Blocks.COBBLESTONE);
  }
}
