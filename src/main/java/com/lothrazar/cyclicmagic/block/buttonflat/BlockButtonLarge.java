package com.lothrazar.cyclicmagic.block.buttonflat;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.BlockButton;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockButtonLarge extends BlockButton implements IHasRecipe {

  private static final double SMALL = 0.0925D;
  private static final double LARGE = 1 - SMALL;// 0.9375D;//0.875D;
  protected static final AxisAlignedBB AABB_DOWN_OFF = new AxisAlignedBB(SMALL, LARGE, SMALL, LARGE, 1.0D, LARGE);
  protected static final AxisAlignedBB AABB_UP_OFF = new AxisAlignedBB(SMALL, 0.0D, SMALL, LARGE, SMALL, LARGE);
  protected static final AxisAlignedBB AABB_NORTH_OFF = new AxisAlignedBB(SMALL, SMALL, LARGE, LARGE, LARGE, 1.0D);
  protected static final AxisAlignedBB AABB_SOUTH_OFF = new AxisAlignedBB(SMALL, SMALL, 0.0D, LARGE, LARGE, SMALL);
  protected static final AxisAlignedBB AABB_WEST_OFF = new AxisAlignedBB(LARGE, SMALL, SMALL, 1.0D, LARGE, LARGE);
  protected static final AxisAlignedBB AABB_EAST_OFF = new AxisAlignedBB(0.0D, SMALL, SMALL, SMALL, LARGE, LARGE);
  protected static final AxisAlignedBB AABB_DOWN_ON = new AxisAlignedBB(SMALL, LARGE, SMALL, LARGE, 1.0D, LARGE);
  protected static final AxisAlignedBB AABB_UP_ON = new AxisAlignedBB(SMALL, 0.0D, SMALL, LARGE, SMALL, LARGE);
  protected static final AxisAlignedBB AABB_NORTH_ON = new AxisAlignedBB(SMALL, SMALL, LARGE, LARGE, LARGE, 1.0D);
  protected static final AxisAlignedBB AABB_SOUTH_ON = new AxisAlignedBB(SMALL, SMALL, 0.0D, LARGE, LARGE, SMALL);
  protected static final AxisAlignedBB AABB_WEST_ON = new AxisAlignedBB(LARGE, SMALL, SMALL, 1.0D, LARGE, LARGE);
  protected static final AxisAlignedBB AABB_EAST_ON = new AxisAlignedBB(0.0D, SMALL, SMALL, SMALL, LARGE, LARGE);

  public BlockButtonLarge() {
    super(false);
  }

  @Override
  protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos) {
    worldIn.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
  }

  @Override
  protected void playReleaseSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this), Blocks.WOODEN_BUTTON, Blocks.STONE_PRESSURE_PLATE, "nuggetIron");
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    EnumFacing enumfacing = state.getValue(FACING);
    boolean flag = state.getValue(POWERED).booleanValue();
    switch (enumfacing) {
      case EAST:
        return flag ? AABB_EAST_ON : AABB_EAST_OFF;
      case WEST:
        return flag ? AABB_WEST_ON : AABB_WEST_OFF;
      case SOUTH:
        return flag ? AABB_SOUTH_ON : AABB_SOUTH_OFF;
      case NORTH:
      default:
        return flag ? AABB_NORTH_ON : AABB_NORTH_OFF;
      case UP:
        return flag ? AABB_UP_ON : AABB_UP_OFF;
      case DOWN:
        return flag ? AABB_DOWN_ON : AABB_DOWN_OFF;
    }
  }
}
