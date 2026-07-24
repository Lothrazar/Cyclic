package com.lothrazar.cyclic.item.enderbook;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class EnderBookItem extends ItemBaseCyclic {

  private static final String ITEMCOUNT = "itemCount";
  private static final int TP_COUNTDOWN = 60;
  private static final String ENDERSLOT = "enderslot";
  private static final String TELEPORT_COUNTDOWN = "TeleportCountdown";

  public EnderBookItem(Properties properties) {
    super(properties);
  }

  private static CompoundTag getData(ItemStack stack) {
    return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
  }

  private static void setData(ItemStack stack, CompoundTag tag) {
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    CompoundTag tag = getData(stack);
    if (tag.contains(ITEMCOUNT)) {
      int itemCount = tag.getIntOr(ITEMCOUNT, 0);
      MutableComponent t = Component.translatable("cyclic.screen.filter.item.count");
      t.append("" + itemCount);
      t.withStyle(ChatFormatting.GRAY);
      tooltip.add(t);
    }
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    if (getData(stack).contains(TELEPORT_COUNTDOWN)) {
      return true;
    }
    return super.isFoil(stack);
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack stack = playerIn.getItemInHand(handIn);
    if (!worldIn.isClientSide() && !playerIn.isCrouching()) {
      ((ServerPlayer) playerIn).openMenu(new EnderBookContainerProvider());
    }
    if (!worldIn.isClientSide() && playerIn.isCrouching()) {
      if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
        CompoundTag tag = getData(stack);
        int enderslot = tag.getIntOr(ENDERSLOT, 0);
        BlockPosDim loc = getLocation(stack, enderslot);
        if (loc != null) {
          ChatUtil.addServerChatMessage(playerIn, Component.translatable("item.cyclic.ender_book.start").append(loc.toString()));
          tag.putInt(TELEPORT_COUNTDOWN, TP_COUNTDOWN);
          setData(stack, tag);
        }
      }
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    CompoundTag tag = getData(stack);
    if (!tag.contains(TELEPORT_COUNTDOWN) || !(entityIn instanceof LivingEntity)) {
      return;
    }
    if (entityIn instanceof Player player && !player.onGround()) {
      cancelTeleport(stack);
      ChatUtil.sendStatusMessage(player, Component.translatable("item.cyclic.ender_book.cancel"));
      return;
    }
    int ct = tag.getIntOr(TELEPORT_COUNTDOWN, 0);
    if (ct < 0) {
      cancelTeleport(stack);
      return;
    }
    if (ct == 0 && entityIn instanceof Player p) {
      cancelTeleport(stack);
      int enderslot = tag.getIntOr(ENDERSLOT, 0);
      BlockPosDim loc = getLocation(stack, enderslot);
      if (loc != null && loc.getPos() != null) {
        if (loc.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(worldIn))) {
          EntityUtil.enderTeleportEvent(p, worldIn, loc.getPos());
        }
        else {
          EntityUtil.dimensionTeleport((ServerPlayer) p, (ServerLevel) worldIn, loc);
        }
        ItemStackUtil.damageItem(p, stack);
        return;
      }
    }
    else if (ct % Const.TICKS_PER_SEC == 0 && entityIn instanceof Player player) {
      ChatUtil.sendStatusMessage(player, Component.translatable("item.cyclic.ender_book.countdown").append("" + (ct / 20)));
    }
    ct--;
    tag.putInt(TELEPORT_COUNTDOWN, ct);
    setData(stack, tag);
  }

  @Override
  public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
    return repair.getItem() == Items.ENDER_PEARL;
  }

  public static void cancelTeleport(ItemStack stack) {
    CompoundTag tag = getData(stack);
    tag.remove(TELEPORT_COUNTDOWN);
    setData(stack, tag);
  }

  private static BlockPosDim getLocation(ItemStack stack, int enderSlot) {
    IItemHandler cap = stack.getCapability(Capabilities.ItemHandler.ITEM);
    if (cap != null) {
      return LocationGpsCard.getPosition(cap.getStackInSlot(enderSlot));
    }
    return null;
  }

  public static void scroll(ServerPlayer player, int slot, boolean isDown) {
    ItemStack book = player.getInventory().getItem(slot);
    if (!(book.getItem() instanceof EnderBookItem)) {
      return;
    }
    CompoundTag tag = getData(book);
    int enderslot = tag.getIntOr(ENDERSLOT, 0);
    enderslot = scrollSlot(isDown, enderslot);
    enderslot = enderslot % EnderBookCapability.SLOTS;
    tag.putInt(ENDERSLOT, enderslot);
    setData(book, tag);
    BlockPosDim loc = getLocation(book, enderslot);
    String msg = "---";
    if (loc != null) {
      msg = loc.getDisplayString();
    }
    ChatUtil.addServerChatMessage(player, Component.translatable(enderslot + " : ").append(msg));
  }

  private static int scrollSlot(final boolean isDown, int enderslot) {
    enderslot += isDown ? -1 : 1;
    if (enderslot < 0) {
      enderslot = EnderBookCapability.SLOTS - 1;
    }
    else if (enderslot >= EnderBookCapability.SLOTS) {
      enderslot = 0;
    }
    return enderslot;
  }
}
