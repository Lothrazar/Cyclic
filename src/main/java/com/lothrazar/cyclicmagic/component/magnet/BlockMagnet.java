package com.lothrazar.cyclicmagic.component.magnet;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMagnet extends BlockBaseHasTile implements IHasRecipe, IHasConfig {
  protected static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);
  public BlockMagnet() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityMagnet();
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return BOUNDS;
  }
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  /**
   * Return true if an entity can be spawned inside the block (used to get the
   * player's bed spawn location)
   */
  public boolean canSpawnInBlock() {
    return true;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 4),
        "sbs",
        "bxb",
        "sbs",
        's', "ingotIron",
        'b', new ItemStack(Items.COAL),
        'x', "dyePurple");
  }
  @Override
  public void syncConfig(Configuration config) {
    //old default was 140
    TileEntityMagnet.TIMER_FULL = config.getInt("MagnetBlockTimer", Const.ConfigCategory.modpackMisc, 100, 5, 5000, "How fast it pulses.  Smaller numbers are faster");
    TileEntityMagnet.ITEM_HRADIUS = config.getInt("MagnetBlockDistance", Const.ConfigCategory.modpackMisc, 16, 2, 128, "Distance it pulls items from.");
    TileEntityMagnet.ITEM_VRADIUS = config.getInt("MagnetBlockHeight", Const.ConfigCategory.modpackMisc, 2, 1, 128, "Height it pulls at items");
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    String s = UtilChat.lang("tile.magnet_block.tooltip").replace("$t$", TileEntityMagnet.TIMER_FULL + "");
    tooltip.add(s);
  }
  /**
   * adding this stops fences from connecting
   */
  public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}
