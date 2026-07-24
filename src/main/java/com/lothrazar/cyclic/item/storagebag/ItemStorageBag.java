package com.lothrazar.cyclic.item.storagebag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;

public class ItemStorageBag extends ItemBaseCyclic {
  public static CompoundTag getCustomData(ItemStack stack) {
    return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
  }
  public static void setCustomData(ItemStack stack, CompoundTag tag) {
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  private static final String NBT_COLOUR = "COLOUR";
  public static final int REFILL_TICKS = 4;
  public static final int SLOTS = 81;
  public int timer = 0;

  public ItemStorageBag(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide() && !playerIn.isCrouching()) {
      int slot = handIn == InteractionHand.MAIN_HAND ? playerIn.getInventory().selected : 40;
      ((ServerPlayer) playerIn).openMenu(new StorageBagContainerProvider(slot), buf -> buf.writeInt(slot));
    }
    return super.use(worldIn, playerIn, handIn);
  }

  public static void setColour(ItemStack stack, DyeColor col) {
    CompoundTag tags = getCustomData(stack);
    tags.putInt(NBT_COLOUR, col.getTextColor()); // getColorValue
  }

  public static int getColour(ItemStack stack) {
    CompoundTag tags = getCustomData(stack);
    if (tags.contains(NBT_COLOUR) == false) {
      return DyeColor.BROWN.getTextColor(); //BROWN as default for normal look
    }
    return tags.getIntOr(NBT_COLOUR, 0);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos pos = context.getClickedPos();
    Direction face = context.getClickedFace();
    Level world = context.getLevel();
    BlockEntity te = world.getBlockEntity(pos);
    ItemStack bag = context.getItemInHand();
    DepositMode mode = getDepositMode(bag);
    if (mode == DepositMode.NOTHING) {
      return InteractionResult.PASS;
    }
    ItemStackHandler handler = getInventory(bag);
    if (handler != null) {
      IItemHandler teHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, face);
      Set<Item> itemsInTargetInventory = new HashSet<>();
      if (teHandler != null) {
        for (int j = 0; j < teHandler.getSlots(); j++) {
          itemsInTargetInventory.add(teHandler.getStackInSlot(j).getItem());
        }
      }
      for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack stack = handler.getStackInSlot(i);
        ItemStack remaining = stack.copy();
        if (!stack.isEmpty()) {
          if (mode == DepositMode.DUMP || (mode == DepositMode.MERGE && itemsInTargetInventory.contains(stack.getItem()))) {
            remaining = ItemHandlerHelper.insertItem(teHandler, stack, false);
            handler.setStackInSlot(i, remaining);
          }
        }
      }
      SoundUtil.playSound(context.getPlayer(), SoundRegistry.BASEY.get());
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    CompoundTag nbt = getCustomData(stack);
    String pickupMode = nbt.getStringOr(PickupMode.NBT, "");
    String depositMode = nbt.getStringOr("deposit_mode", "");
    String refillMode = nbt.getStringOr("refill_mode", "");
    if (!pickupMode.equals("")) {
      tooltip.accept(Component.translatable("item.cyclic.storage_bag.tooltip.pickup",
          Component.translatable(String.format(
              pickupMode.equals("nothing") ? "item.cyclic.storage_bag.disabled" : "item.cyclic.storage_bag.pickup.%s", pickupMode)))
          .withStyle(ChatFormatting.GREEN));
    }
    if (!depositMode.equals("")) {
      tooltip.accept(Component.translatable("item.cyclic.storage_bag.tooltip.deposit",
          Component.translatable(String.format(
              depositMode.equals("nothing") ? "item.cyclic.storage_bag.disabled" : "item.cyclic.storage_bag.deposit.%s", depositMode)))
          .withStyle(ChatFormatting.BLUE));
    }
    if (!refillMode.equals("")) {
      tooltip.accept(Component.translatable("item.cyclic.storage_bag.tooltip.refill",
          Component.translatable(String.format(
              refillMode.equals("nothing") ? "item.cyclic.storage_bag.disabled" : "item.cyclic.storage_bag.refill.%s", refillMode)))
          .withStyle(ChatFormatting.RED));
    }
  }

  @Override
  public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
    timer++;
    if (timer < REFILL_TICKS) {
      return;
    }
    timer = 0;
    if (!world.isClientSide() && entity instanceof Player) {
      if (getRefillMode(stack) == RefillMode.HOTBAR) {
        tryRefillHotbar(stack, (Player) entity);
      }
    }
  }

  private void tryRefillHotbar(ItemStack bag, Player player) {
    ItemStackHandler handler = getInventory(bag);
    if (handler == null) {
      return;
    }
    for (int i = 0; i < Math.min(9, player.getInventory().getContainerSize()); i++) { //hotbar is 0-8, but do a Math.min in case some mod reduces inventory size? Idk, why not.
      ItemStack stack = player.getInventory().getItem(i);
      if (!stack.isEmpty()) {
        //we found a non
        int slot = getLastSlotWithStack(bag, stack);
        if (slot != -1) {
          boolean success = refillHotbar(bag, player, slot, i);
          if (success) {
            handler.extractItem(slot, 1, false);
            return; //Only fill up 1 stack at a time.
          }
        }
      }
    }
  }

  private boolean refillHotbar(ItemStack bag, Player player, int bagSlot, int invSlot) {
    ItemStackHandler handler = getInventory(bag);
    boolean success = false;
    if (handler != null) {
      ItemStack extracted = handler.extractItem(bagSlot, 1, true);
      success = player.getInventory().add(invSlot, extracted);
    }
    return success;
  }

  private static ItemStackHandler getInventory(ItemStack bag) {
    var handler = bag.getCapability(Capabilities.ItemHandler.ITEM);
    return handler instanceof ItemStackHandler ish ? ish : null;
  }

  public static ItemStack tryInsert(ItemStack bag, ItemStack stack) {
    AtomicReference<ItemStack> returnStack = new AtomicReference<>(stack.copy());
    IItemHandler h = bag.getCapability(Capabilities.ItemHandler.ITEM); if (h != null) {
      returnStack.set(ItemHandlerHelper.insertItem(h, stack, false));
    }
    return returnStack.get();
  }

  public static ItemStack tryFilteredInsert(ItemStack bag, ItemStack stack) {
    if (bag.getCapability(Capabilities.ItemHandler.ITEM) != null && bagHasItem(bag, stack)) {
      return tryInsert(bag, stack);
    }
    return stack;
  }

  private static boolean bagHasItem(ItemStack bag, ItemStack stack) {
    AtomicBoolean hasItem = new AtomicBoolean(false);
    IItemHandler h = bag.getCapability(Capabilities.ItemHandler.ITEM); if (h != null) {
      for (int i = 0; i < h.getSlots(); i++) {
        if (h.getStackInSlot(i).getItem() == stack.getItem()) {
          hasItem.set(true);
        }
      }
    }
    return hasItem.get();
  }

  //unused but possibly useful
  public static int getFirstSlotWithStack(ItemStack bag, ItemStack stack) {
    AtomicInteger slot = new AtomicInteger(-1);
    IItemHandler h = bag.getCapability(Capabilities.ItemHandler.ITEM); if (h != null) {
      for (int i = 0; i < h.getSlots(); i++) {
        if (h.getStackInSlot(i).getItem() == stack.getItem()) {
          slot.set(i);
        }
      }
    }
    return slot.get();
  }

  private static int getLastSlotWithStack(ItemStack bag, ItemStack stack) {
    AtomicInteger slot = new AtomicInteger(-1);
    IItemHandler h = bag.getCapability(Capabilities.ItemHandler.ITEM); if (h != null) {
      for (int i = h.getSlots() - 1; i >= 0; i--) {
        if (h.getStackInSlot(i).getItem() == stack.getItem()) {
          slot.set(i);
        }
      }
    }
    return slot.get();
  }

  public static PickupMode getPickupMode(ItemStack stack) {
    String mode = getCustomData(stack).getStringOr(PickupMode.NBT, "");
    for (int i = 0; i < PickupMode.values().length; i++) {
      if (mode.equals(PickupMode.values()[i].getSerializedName())) {
        return PickupMode.values()[i];
      }
    }
    return PickupMode.NOTHING;
  }

  private static DepositMode getDepositMode(ItemStack stack) {
    String mode = getCustomData(stack).getStringOr(DepositMode.NBT, "");
    for (int i = 0; i < DepositMode.values().length; i++) {
      if (mode.equals(DepositMode.values()[i].getSerializedName())) {
        return DepositMode.values()[i];
      }
    }
    return DepositMode.NOTHING;
  }

  private static RefillMode getRefillMode(ItemStack stack) {
    String mode = getCustomData(stack).getAsString();
    for (int i = 0; i < RefillMode.values().length; i++) {
      if (mode.equals(RefillMode.values()[i].getSerializedName())) {
        return RefillMode.values()[i];
      }
    }
    return RefillMode.NOTHING;
  }

  public static List<Integer> getAllBagSlots(Player player) {
    List<Integer> slots = new ArrayList<>();
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      if (isBag(player.getInventory().getItem(i))) {
        slots.add(i);
      }
    }
    return slots;
  }

  //unused but possibly useful
  public static int getFirstBagSlot(Player player) {
    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
      if (isBag(player.getInventory().getItem(i))) {
        return i;
      }
    }
    return -1;
  }

  private static boolean isBag(ItemStack stack) {
    return stack.getItem() instanceof ItemStorageBag;
  }

  public static void setTimestamp(ItemStack myBag) {
    if (myBag.isEmpty()) {
      return;
    }
    CustomData.update(DataComponents.CUSTOM_DATA, myBag, tag -> tag.putLong("ts", System.currentTimeMillis()));
  }
}
