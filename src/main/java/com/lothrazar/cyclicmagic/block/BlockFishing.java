package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFishing extends Block {
  public BlockFishing() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(5F);
//    this.setStepSound(soundTypeWood);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFishing();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISHING_ROD, 1, 0),
        'p', Blocks.PLANKS);
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
