package com.lothrazar.cyclicmagic.component.workbench;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
  public void addRecipe() {
//    GameRegistry.addRecipe(new ItemStack(this),
//        "pcp",
//        "y x",
//        "pkp",
//        'k', new ItemStack(Blocks.BONE_BLOCK),
//        'x', new ItemStack(Blocks.OBSERVER),
//        'y', new ItemStack(Blocks.PISTON),
//        'c', new ItemStack(Blocks.CRAFTING_TABLE),
//        'p', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
  }
}
