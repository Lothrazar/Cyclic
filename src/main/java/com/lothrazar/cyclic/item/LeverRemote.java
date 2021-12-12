package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LeverRemote extends ItemBase {

  public LeverRemote(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    BlockPos pointer = UtilNBT.getItemStackBlockPos(stack);
    if (pointer != null) {
      int dimensionTarget = stack.getOrCreateTag().getInt("LeverDim");
      tooltip.add(
          new TranslationTextComponent(
              TextFormatting.RED + UtilChat.blockPosToString(pointer) + " [" + dimensionTarget + "]"));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
    ItemStack stack = playerIn.getHeldItem(hand);
    boolean success = false;
    success = trigger(stack, worldIn, playerIn);
    if (success) {
      playerIn.swingArm(hand);
      return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
    else {
      return new ActionResult<>(ActionResultType.FAIL, stack);
    }
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    World world = context.getWorld();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUse(context);
    }
    ItemStack stack = player.getHeldItem(context.getHand());
    BlockPos pos = context.getPos();
    if (world.getBlockState(pos).getBlock() instanceof LeverBlock) {
      UtilNBT.setItemStackBlockPos(stack, pos);
      //and save dimension
      stack.getOrCreateTag().putString("LeverDim", UtilWorld.dimensionToString(player.world));
      //      UtilNBT.setItemStackNBTVal(stack, "LeverDim", player.dimension.getId());
      if (world.isRemote) {
        UtilChat.sendStatusMessage(player, this.getTranslationKey() + ".saved");
      }
      //      UtilSound.playSound(player, SoundEvents.BLOCK_LEVER_CLICK);
      return ActionResultType.SUCCESS;
    }
    else {
      boolean success = false;
      success = trigger(stack, world, player);
      if (success) {
        return ActionResultType.SUCCESS;
      }
      else {
        return ActionResultType.FAIL;
      }
    }
    //    return super.onItemUse(context);
  }

  private boolean trigger(ItemStack stack, World world, PlayerEntity player) {
    BlockPos blockPos = UtilNBT.getItemStackBlockPos(stack);
    //default is zero which is ok
    if (blockPos == null) {
      if (world.isRemote) {
        UtilChat.sendStatusMessage(player, this.getTranslationKey() + ".invalid");
      }
      return false;
    }
    String dimensionTarget = stack.getOrCreateTag().getString("LeverDim");
    //check if we can avoid crossing dimensions
    String currentDim = UtilWorld.dimensionToString(player.world);
    if (dimensionTarget.equalsIgnoreCase(currentDim)) { //same dim eh
      BlockState blockState = world.getBlockState(blockPos);
      if (blockState == null || blockState.getBlock() != Blocks.LEVER) {
        if (world.isRemote) {
          UtilChat.sendStatusMessage(player, this.getTranslationKey() + ".invalid");
        }
        return false;
      }
      blockState = world.getBlockState(blockPos);
      boolean hasPowerHere = blockState.get(LeverBlock.POWERED);
      UtilWorld.toggleLeverPowerState(world, blockPos, blockState);
      UtilChat.sendStatusMessage(player, this.getTranslationKey() + ".powered." + (!hasPowerHere));
      //      UtilSound.playSound(player, SoundEvents.BLOCK_LEVER_CLICK);
      //      UtilEntity.setCooldownItem(player, this, COOLDOWN);
      return true;
    }
    return false;
  }
}
