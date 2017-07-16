package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
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

public class BlockHydrator extends BlockBaseHasTile implements IHasRecipe {
  public BlockHydrator() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_HYDRATOR);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityHydrator();
  }
 
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        " g ",
        "ooo",
        'o', Blocks.MOSSY_COBBLESTONE,
        'g', Items.BUCKET, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
        's', Blocks.DISPENSER,
        'r', "bone"
 
    );
  }
}
