package com.lothrazar.cyclicmagic.component.hydrator;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

public class BlockHydrator extends BlockBaseHasTile implements IHasRecipe {
  public static ArrayList<IRecipe> recipeList = new ArrayList<IRecipe>();
  public BlockHydrator() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setTickRandomly(true);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_HYDRATOR);
    this.setTranslucent();
    this.addAllRecipes();
  }
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }
  private void addAllRecipes() {
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.FARMLAND)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.GRASS), new ItemStack(Blocks.GRASS_PATH)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.STONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.HARDENED_CLAY), new ItemStack(Blocks.CLAY)));
    for (EnumDyeColor col : EnumDyeColor.values()) {
      recipeList.add(new RecipeHydrate(new ItemStack(Blocks.CONCRETE_POWDER, 1, col.getDyeDamage()), new ItemStack(Blocks.CONCRETE, 1, col.getDyeDamage())));
    }
    for (IRecipe r : recipeList) {
      RecipeRegistry.register(r);
    }
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityHydrator();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        "ogo",
        " o ",
        'o', Blocks.MOSSY_COBBLESTONE,
        'g', Blocks.IRON_BLOCK,
        's', Blocks.DROPPER,
        'r', Items.WATER_BUCKET);
  }
}
