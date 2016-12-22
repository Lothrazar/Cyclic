package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFan;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFan extends BlockBaseFacing implements IHasRecipe {
  //block rotation in json http://www.minecraftforge.net/forum/index.php?topic=32753.0
  public BlockFan() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(true);
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFan();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("tile.fan.tooltip"));
  }
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " i ",
        "iri",
        "sis",
        'i', Items.IRON_INGOT,
        'r', Items.REPEATER,
        's', Blocks.STONE);
  }
}
