package com.lothrazar.cyclicmagic.block.cablewireless;

import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCableWireless extends BlockBaseHasTile implements IHasRecipe {

  public BlockCableWireless() {
    super(Material.IRON);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_BATTERYWIRELESS);
    this.setTranslucent();
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileCableWireless();
  }

  @Override
  public IRecipe addRecipe() {

    return null;
  }
}
