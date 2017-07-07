package com.lothrazar.cyclicmagic.component.placer;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPlacer extends BlockBaseFacingOmni implements IHasRecipe {
  public BlockPlacer() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_PLACER);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPlacer();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        "gbg",
        "ooo",
        'o', "cobblestone",
        'g', "ingotIron",
        's', Blocks.DISPENSER,
        'r', "stone",
        'b', "dustRedstone");
  }
}
