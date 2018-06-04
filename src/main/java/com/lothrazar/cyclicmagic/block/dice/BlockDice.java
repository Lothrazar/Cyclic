package com.lothrazar.cyclicmagic.block.dice;

import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
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

public class BlockDice extends BlockBaseFacingOmni implements IHasRecipe {

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
