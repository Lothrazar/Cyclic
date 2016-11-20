package com.lothrazar.cyclicmagic.block;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityMagnetAnti;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMagnetAnti extends Block implements IHasRecipe, IHasConfig {
  protected static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);
  public BlockMagnetAnti() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityMagnetAnti();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return BOUNDS;
  }
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  /**
   * Used to determine ambient occlusion and culling when rebuilding chunks for
   * render
   */
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  //for transparency
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  /**
   * Return true if an entity can be spawned inside the block (used to get the
   * player's bed spawn location)
   */
  public boolean canSpawnInBlock() {
    return true;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 1),
        "sbs",
        "bxb",
        "sbs",
        's', new ItemStack(Items.DIAMOND),
        'b', new ItemStack(Blocks.field_189878_dg),//netherwart block
        'x', new ItemStack(Blocks.field_189880_di));//bone block
  }
  @Override
  public void syncConfig(Configuration config) {
//    //old default was 140
//    TileEntityMagnetAnti.TIMER_FULL = 10;//config.getInt("MagnetBlockTimer", Const.ConfigCategory.modpackMisc, 100, 5, 5000, "How fast it pulses.  Smaller numbers are faster");
//    TileEntityMagnetAnti.ITEM_HRADIUS = 32;//config.getInt("MagnetBlockDistance", Const.ConfigCategory.modpackMisc, 16, 2, 128, "Distance it pulls items from.");
//    TileEntityMagnetAnti.ITEM_VRADIUS = 2;//config.getInt("MagnetBlockHeight", Const.ConfigCategory.modpackMisc, 2, 1, 128, "Height it pulls at items");
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    String s = UtilChat.lang("tile.magnet_anti_block.tooltip").replace("$t$", TileEntityMagnetAnti.TIMER_FULL + "");
    tooltip.add(s);
  }
}
