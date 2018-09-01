package com.lothrazar.cyclicmagic.block.dice;

import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.core.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDice extends BlockBaseFacingOmni implements IHasRecipe, IContent {

  public BlockDice() {
    super(Material.ROCK);
    this.setTranslucent();
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityDice();
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, @Nullable EntityLivingBase placer) {
    EnumFacing fac = getRandom(world.rand);
    return this.getDefaultState().withProperty(PROPERTYFACING, fac);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //set random blockstate 
    TileEntity tile = world.getTileEntity(pos);
    if (hand == EnumHand.MAIN_HAND && tile instanceof TileEntityDice) {
      ((TileEntityDice) tile).startSpinning();
      UtilSound.playSound(player, SoundRegistry.dice_mikekoenig);
      return true;
    }
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
    TileEntity tile = world.getTileEntity(pos);
    if (tile instanceof TileEntityDice) {
      if (((TileEntityDice) tile).isSpinning()) {
        return 0;
      }
    }
    EnumFacing fac = blockState.getValue(PROPERTYFACING);
    int factor = 2;
    switch (fac) {
      case NORTH:
        return 1 * factor;
      case SOUTH:
        return 2 * factor;
      case UP:
        return 3 * factor;
      case DOWN:
        return 4 * factor;
      case WEST:
        return 5 * factor;
      case EAST:
        return 6 * factor;
      default:
        return super.getComparatorInputOverride(blockState, world, pos);
    }
  }

  /**
   * TODO: util
   * 
   * @param rand
   * @return
   */
  public static EnumFacing getRandom(Random rand) {
    int index = MathHelper.getInt(rand, 0, EnumFacing.values().length - 1);
    return EnumFacing.values()[index];
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("dice", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(new BlockDice(), "dice", GuideCategory.BLOCK);
    GameRegistry.registerTileEntity(TileEntityDice.class, "dice_te");
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "sts",
        "tit",
        "sts",
        't', "cobblestone",
        'i', "dyeBlack",
        's', "bone");
  }
}
