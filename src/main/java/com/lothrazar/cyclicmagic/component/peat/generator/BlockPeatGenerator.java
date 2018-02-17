package com.lothrazar.cyclicmagic.component.peat.generator;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPeatGenerator extends BlockBaseHasTile implements IHasRecipe {
  private Item peat_fuel;
  public BlockPeatGenerator(Item peat_fuel) {
    super(Material.ROCK);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_PEATGEN);
    this.peat_fuel=peat_fuel;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPeatGenerator();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pip",
        "cbc",
        "pip",
        'p', peat_fuel,//Item.getByNameOrId(Const.MODRES + "peat_fuel"),
        'i', new ItemStack(Blocks.PISTON),
        'b', "blockIron",
        'c', "blockCoal");
  }
}
