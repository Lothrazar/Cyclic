package com.lothrazar.cyclic.item.builder;

import java.util.List;
import com.lothrazar.cyclic.item.ItemHasEnergy;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;


public class BuilderItem extends ItemHasEnergy {

  public BuildStyle style;
  public static ModConfigSpec.IntValue COST;
  public static ModConfigSpec.IntValue MAX;

  public BuilderItem(Properties properties, BuildStyle t) {
    super(properties.stacksTo(1));
    style = t;
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    String msg = ChatFormatting.GREEN + ChatUtil.lang(BuilderActionType.getName(stack));
    tooltip.add(Component.translatable(msg));
    BlockState target = BuilderActionType.getBlockState(null, stack);
    String block = "scepter.cyclic.nothing";
    if (target != null) {
      block = target.getBlock().getDescriptionId();
    }
    tooltip.add(Component.translatable(ChatFormatting.AQUA + ChatUtil.lang(block)));
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    BuilderActionType.tickTimeout(stack);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    ItemStack stack = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    BuildStyle buildStyle = ((BuilderItem) stack.getItem()).style;
    // on top of selected = do offset
    if (side != null && buildStyle.isOffset()) {
      pos = pos.relative(side);
    }
    if (context.getLevel().isClientSide) {
      BuilderActionType type = getActionType(stack);
      PacketSwapBlock message = new PacketSwapBlock(pos, type, side, context.getHand());
      PacketDistributor.sendToServer(message);
      return InteractionResult.SUCCESS;
    }
    return super.useOn(context);
  }

  public static BuilderActionType getActionType(ItemStack stack) {
    BuilderActionType type = BuilderActionType.values()[BuilderActionType.get(stack)];
    return type;
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof BuilderItem) {
      return heldItem;
    }
    heldItem = player.getOffhandItem();
    if (heldItem.getItem() instanceof BuilderItem) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }
}
