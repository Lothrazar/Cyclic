package com.lothrazar.cyclicmagic.block.buttondoorbell;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.block.BlockButton;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoorbell extends BlockButton implements IHasRecipe {

  public BlockDoorbell() {
    super(false);
  }

  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return 0;
  }

  @Override
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return 0;
  }

  @Override
  public boolean canProvidePower(IBlockState state) {
    return false;
  }

  @Override
  protected void playClickSound(EntityPlayer player, World worldIn, BlockPos pos) {
    UtilSound.playSound(player, SoundRegistry.heart_container);
  }

  @Override
  protected void playReleaseSound(World worldIn, BlockPos pos) {}

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this), Blocks.STONE_BUTTON, "nuggetIron");
  }
}
