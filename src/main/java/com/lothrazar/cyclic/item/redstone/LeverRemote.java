package com.lothrazar.cyclic.item.redstone;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.TagDataUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class LeverRemote extends ItemBaseCyclic {

  private static final String LEVER_DIM = "LeverDim";

  private static String getDim(ItemStack stack) {
    return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(LEVER_DIM, "");
  }

  private static void setDim(ItemStack stack, String dim) {
    CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putString(LEVER_DIM, dim);
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public LeverRemote(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    BlockPos pointer = TagDataUtil.getItemStackBlockPos(stack);
    if (pointer != null) {
      String dimensionTarget = getDim(stack);
      tooltip.add(Component.translatable(ChatFormatting.RED + ChatUtil.blockPosToString(pointer) + " [" + dimensionTarget + "]"));
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand hand) {
    ItemStack stack = playerIn.getItemInHand(hand);
    boolean success = trigger(stack, worldIn, playerIn);
    if (success) {
      playerIn.swing(hand);
      return new InteractionResult(InteractionResult.SUCCESS, stack);
    }
    else {
      return new InteractionResult(InteractionResult.FAIL, stack);
    }
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    Level world = context.getLevel();
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.useOn(context);
    }
    ItemStack stack = player.getItemInHand(context.getHand());
    BlockPos pos = context.getClickedPos();
    if (world.getBlockState(pos).getBlock() instanceof LeverBlock) {
      TagDataUtil.setItemStackBlockPos(stack, pos);
      //and save dimension
      setDim(stack, LevelWorldUtil.dimensionToString(player.level()));
      //      UtilNBT.setItemStackNBTVal(stack, "LeverDim", player.dimension.getId());
      if (world.isClientSide()) {
        ChatUtil.sendStatusMessage(player, this.getDescriptionId() + ".saved");
      }
      //      UtilSound.playSound(player, SoundEvents.BLOCK_LEVER_CLICK);
      return InteractionResult.SUCCESS;
    }
    else {
      boolean success = false;
      success = trigger(stack, world, player);
      if (success) {
        return InteractionResult.SUCCESS;
      }
      else {
        return InteractionResult.FAIL;
      }
    }
    //    return super.onItemUse(context);
  }

  private boolean trigger(ItemStack stack, Level world, Player player) {
    BlockPos blockPos = TagDataUtil.getItemStackBlockPos(stack);
    //default is zero which is ok
    if (blockPos == null) {
      if (world.isClientSide()) {
        ChatUtil.sendStatusMessage(player, this.getDescriptionId() + ".invalid");
      }
      return false;
    }
    String dimensionTarget = getDim(stack);
    //check if we can avoid crossing dimensions
    String currentDim = LevelWorldUtil.dimensionToString(player.level());
    if (dimensionTarget.equalsIgnoreCase(currentDim)) { //same dim eh
      BlockState blockState = world.getBlockState(blockPos);
      if (blockState == null || blockState.getBlock() != Blocks.LEVER) {
        if (world.isClientSide()) {
          ChatUtil.sendStatusMessage(player, this.getDescriptionId() + ".invalid");
        }
        return false;
      }
      blockState = world.getBlockState(blockPos);
      boolean hasPowerHere = blockState.getValue(LeverBlock.POWERED).booleanValue();
      LevelWorldUtil.toggleLeverPowerState(world, blockPos, blockState);
      ChatUtil.sendStatusMessage(player, this.getDescriptionId() + ".powered." + (!hasPowerHere));

      return true;
    }
    return false;
  }
}
