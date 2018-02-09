package com.lothrazar.cyclicmagic.component.cable.energy;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.component.cable.BlockCableBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerCable extends BlockCableBase implements IHasRecipe {
  public BlockPowerCable() {
    //    super(Material.CLAY);
    this.setPowerTransport();
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityCablePower();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 8),
        "sis",
        "i i",
        "sis",
        's', Blocks.BRICK_STAIRS,
        'i', "dustRedstone");
  }
}
