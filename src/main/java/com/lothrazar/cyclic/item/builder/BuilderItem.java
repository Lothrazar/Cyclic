package com.lothrazar.cyclic.item.builder;

import java.util.List;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BuilderItem extends ItemBase {

  public BuildStyle style;

  public BuilderItem(Properties properties, BuildStyle t) {
    super(properties.stacksTo(1).durability(4096));
    style = t;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    String msg = ChatFormatting.GREEN + UtilChat.lang(BuilderActionType.getName(stack));
    tooltip.add(new TranslatableComponent(msg));
    BlockState target = BuilderActionType.getBlockState(stack);
    String block = "scepter.cyclic.nothing";
    if (target != null) {
      block = target.getBlock().getDescriptionId();
    }
    tooltip.add(new TranslatableComponent(ChatFormatting.AQUA + UtilChat.lang(block)));
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
    //TODO: ??INSIDE building no offset
    // on top of selected = do offset
    if (side != null && buildStyle.isOffset()) {
      pos = pos.relative(side);
    }
    if (context.getLevel().isClientSide) {
      BuilderActionType type = getActionType(stack);
      PacketSwapBlock message = new PacketSwapBlock(pos, type, side, context.getHand());
      PacketRegistry.INSTANCE.sendToServer(message);
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
