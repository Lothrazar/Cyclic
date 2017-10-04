package com.lothrazar.cyclicmagic.component.miner;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMiner extends BlockBaseFacingOmni implements IHasRecipe {
  public BlockMiner() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_BLOCKMINER);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityBlockMiner();
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    ((TileEntityBlockMiner) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        " g ",
        "ooo",
        'o', Blocks.MOSSY_COBBLESTONE,
        'g', Items.IRON_PICKAXE,
        's', Blocks.DISPENSER,
        'r', "bone");
  }
}
