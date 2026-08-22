package com.lothrazar.cyclic.item.builder;

import com.lothrazar.cyclic.item.ItemHasEnergy;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Consumer;


public class BuilderItem extends ItemHasEnergy {

  public BuildStyle style;
  public static ModConfigSpec.IntValue COST;
  public static ModConfigSpec.IntValue MAX;

  public BuilderItem(Properties properties, BuildStyle t) {
    super(properties.stacksTo(1));
    style = t;
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    String msg = ChatFormatting.GREEN + ChatUtil.lang(BuilderActionType.getName(stack));
    tooltip.accept(Component.translatable(msg));
    BlockState target = BuilderActionType.getBlockState(null, stack);
    String block = "scepter.cyclic.nothing";
    if (target != null) {
      block = target.getBlock().getDescriptionId();
    }
    tooltip.accept(Component.translatable(ChatFormatting.AQUA + ChatUtil.lang(block)));
  }

  @Override
  public void inventoryTick(ItemStack stack, ServerLevel worldIn, Entity entityIn, EquipmentSlot slot) {
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
    if (context.getLevel().isClientSide()) {
      BuilderActionType type = getActionType(stack);
      PacketSwapBlock message = new PacketSwapBlock(pos, type, side, context.getHand());
      ClientPacketDistributor.sendToServer(message);
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
