package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPasswordRemote extends BaseItem implements IHasRecipe {
  public ItemPasswordRemote() {
    this.setMaxStackSize(1);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    BlockPos pointer = UtilNBT.getItemStackBlockPos(stack);
    if (pointer != null) {
      tooltip.add(TextFormatting.RED + UtilChat.blockPosToString(pointer));
    }
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (worldIn.getBlockState(pos).getBlock() instanceof BlockLever) {
      UtilNBT.setItemStackBlockPos(stack, pos);
      if (worldIn.isRemote) {
        UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".saved");
      }
      UtilSound.playSound(playerIn, SoundEvents.BLOCK_LEVER_CLICK);
      return EnumActionResult.SUCCESS;
    }
    else {
      boolean success = false;
      success = trigger(stack, worldIn, playerIn);
      if (success)
        return EnumActionResult.SUCCESS;
      else
        return EnumActionResult.FAIL;
    }
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    boolean success = false;
    success = trigger(stack, worldIn, playerIn);
    if (success)
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    else
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }
  private boolean trigger(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    BlockPos pointer = UtilNBT.getItemStackBlockPos(stack);
    if (pointer == null) {
      if (worldIn.isRemote) {
        UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".invalid");
      }
      return false;
    }
    else {
      IBlockState blockState = worldIn.getBlockState(pointer);
      if (blockState == null || blockState.getBlock() != Blocks.LEVER) {
        UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".invalid");
        return false;
      }
      else {
        boolean hasPowerHere = blockState.getValue(BlockLever.POWERED);//this.block.getStrongPower(blockState, worldIn, pointer, EnumFacing.UP) > 0;
        worldIn.setBlockState(pointer, blockState.withProperty(BlockLever.POWERED, !hasPowerHere));
        UtilSound.playSound(playerIn, SoundEvents.BLOCK_LEVER_CLICK);
        return true;
      }
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this),
        new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.STONE.getMetadata()),
        Blocks.STONE_BUTTON,
        Blocks.LEVER);
  }
}
