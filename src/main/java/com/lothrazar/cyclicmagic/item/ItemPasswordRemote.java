package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPasswordRemote extends BaseItem implements IHasRecipe {
  public ItemPasswordRemote() {
    this.setMaxStackSize(1);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip,net.minecraft.client.util.ITooltipFlag advanced) {
    BlockPos pointer = UtilNBT.getItemStackBlockPos(stack);
    if (pointer != null) {
      tooltip.add(TextFormatting.RED + UtilChat.blockPosToString(pointer));
    }
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack stack = playerIn.getHeldItem(hand);
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
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack stack = playerIn.getHeldItem(hand);
    boolean success = false;
    success = trigger(stack, worldIn, playerIn);
    if (success)
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    else
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }
  private boolean trigger(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    BlockPos blockPos = UtilNBT.getItemStackBlockPos(stack);
    if (blockPos == null) {
      if (worldIn.isRemote) {
        UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".invalid");
      }
      return false;
    }
    else {
      IBlockState blockState = worldIn.getBlockState(blockPos);
      if (blockState == null || blockState.getBlock() != Blocks.LEVER) {
        if (worldIn.isRemote) {
          UtilChat.addChatMessage(playerIn, this.getUnlocalizedName() + ".invalid");
        }
        return false;
      }
      else {
        boolean hasPowerHere = blockState.getValue(BlockLever.POWERED);//this.block.getStrongPower(blockState, worldIn, pointer, EnumFacing.UP) > 0;
        setLeverPowerState(worldIn, blockPos, blockState, hasPowerHere);
        UtilSound.playSound(playerIn, SoundEvents.BLOCK_LEVER_CLICK);
        return true;
      }
    }
  }
  private void setLeverPowerState(World worldIn, BlockPos blockPos, IBlockState blockState, boolean hasPowerHere) {
    IBlockState stateNew = blockState.withProperty(BlockLever.POWERED, !hasPowerHere);
    boolean success = worldIn.setBlockState(blockPos, stateNew);
    if (success) {
      flagUpdate(worldIn, blockPos, blockState, stateNew);
      flagUpdate(worldIn, blockPos.down(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.up(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.west(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.east(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.north(), blockState, stateNew);
      flagUpdate(worldIn, blockPos.south(), blockState, stateNew);
    }
  }
  private void flagUpdate(World worldIn, BlockPos blockPos, IBlockState blockState, IBlockState stateNew) {
    //    worldIn.notifyBlockUpdate(blockPos,blockState,stateNew,3);
    worldIn.notifyNeighborsOfStateChange(blockPos, blockState.getBlock(), true);//THIS one works only with true
    //    worldIn.scheduleBlockUpdate(blockPos, stateNew.getBlock(), 3, 3);
    //    worldIn.scheduleUpdate(blockPos, stateNew.getBlock(), 3);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this),
        new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.STONE.getMetadata()),
        Blocks.STONE_BUTTON,
        Blocks.LEVER);
  }
}
