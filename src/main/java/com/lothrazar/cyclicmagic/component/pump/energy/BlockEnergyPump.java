package com.lothrazar.cyclicmagic.component.pump.energy;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnergyPump extends BlockBaseFacingOmni implements ITileEntityProvider, IHasRecipe {
  public BlockEnergyPump() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(3F);
    this.setHarvestLevel("pickaxe", 1);
    this.setTranslucent();
    this.placeType = PlacementType.SIDE_BLOCK;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityEnergyPump();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "i i",
        " r ",
        "ibi",
        'b', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
        'i', "dustRedstone",
        'r', Blocks.DROPPER);
  }
}
