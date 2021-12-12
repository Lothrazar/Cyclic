package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStorageBag extends ItemBase {

  private static final String NBT_COLOUR = "COLOUR";
  public static final int REFILL_TICKS = 4;
  public static final int SLOTS = 81;
  public int timer = 0;

  public ItemStorageBag(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new StorageBagContainerProvider(), playerIn.getPosition());
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  public static void setColour(ItemStack stack, DyeColor col) {
    CompoundNBT tags = stack.getOrCreateTag();
    tags.putInt(NBT_COLOUR, col.getColorValue());
  }

  public static int getColour(ItemStack stack) {
    CompoundNBT tags = stack.getOrCreateTag();
    if (!tags.contains(NBT_COLOUR)) {
      return DyeColor.BROWN.getColorValue(); //BROWN as default for normal look
    }
    return tags.getInt(NBT_COLOUR);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos pos = context.getPos();
    Direction face = context.getFace();
    World world = context.getWorld();
    TileEntity te = world.getTileEntity(pos);
    ItemStack bag = context.getItem();
    DepositMode mode = getDepositMode(bag);
    if (mode == DepositMode.NOTHING) {
      return ActionResultType.PASS;
    }
    ItemStackHandler handler = getInventory(bag);
    if (handler != null && te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent()) {
      IItemHandler teHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).orElse(null);
      Set<Item> itemsInTargetInventory = new HashSet<>();
      if (teHandler != null) {
        for (int j = 0; j < teHandler.getSlots(); j++) {
          itemsInTargetInventory.add(teHandler.getStackInSlot(j).getItem());
        }
      }
      for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack stack = handler.getStackInSlot(i);
        ItemStack remaining = ItemHandlerHelper.copyStackWithSize(stack, stack.getCount());
        if (!stack.isEmpty()) {
          if (mode == DepositMode.DUMP || (mode == DepositMode.MERGE && itemsInTargetInventory.contains(stack.getItem()))) {
            remaining = ItemHandlerHelper.insertItem(teHandler, stack, false);
            handler.setStackInSlot(i, remaining);
          }
        }
      }
      UtilSound.playSound(context.getPlayer(), SoundRegistry.BASEY);
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.PASS;
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    CompoundNBT nbt = stack.getOrCreateTag();
    String pickupMode = nbt.getString(PickupMode.NBT);
    String depositMode = nbt.getString("deposit_mode");
    String refillMode = nbt.getString("refill_mode");
    if (!pickupMode.equals("")) {
      tooltip.add(new TranslationTextComponent("item.cyclic.storage_bag.tooltip.pickup",
          new TranslationTextComponent(String.format(
              pickupMode.equals("nothing") ? "item.cyclic.storage_bag.disabled" : "item.cyclic.storage_bag.pickup.%s", pickupMode)))
                  .mergeStyle(TextFormatting.GREEN));
    }
    if (!depositMode.equals("")) {
      tooltip.add(new TranslationTextComponent("item.cyclic.storage_bag.tooltip.deposit",
          new TranslationTextComponent(String.format(
              depositMode.equals("nothing") ? "item.cyclic.storage_bag.disabled" : "item.cyclic.storage_bag.deposit.%s", depositMode)))
                  .mergeStyle(TextFormatting.BLUE));
    }
    if (!refillMode.equals("")) {
      tooltip.add(new TranslationTextComponent("item.cyclic.storage_bag.tooltip.refill",
          new TranslationTextComponent(String.format(
              refillMode.equals("nothing") ? "item.cyclic.storage_bag.disabled" : "item.cyclic.storage_bag.refill.%s", refillMode)))
                  .mergeStyle(TextFormatting.RED));
    }
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.STORAGE_BAG, ScreenStorageBag::new);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
    return new StorageBagCapability(stack, nbt);
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    timer++;
    if (timer < REFILL_TICKS) {
      return;
    }
    timer = 0;
    if (!world.isRemote && entity instanceof PlayerEntity) {
      if (getRefillMode(stack) == RefillMode.HOTBAR) {
        tryRefillHotbar(stack, (PlayerEntity) entity);
      }
    }
  }

  private void tryRefillHotbar(ItemStack bag, PlayerEntity player) {
    ItemStackHandler handler = getInventory(bag);
    if (handler == null) {
      return;
    }
    for (int i = 0; i < Math.min(9, player.inventory.getSizeInventory()); i++) { //hotbar is 0-8, but do a Math.min in case some mod reduces inventory size? Idk, why not.
      ItemStack stack = player.inventory.getStackInSlot(i);
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

  private boolean refillHotbar(ItemStack bag, PlayerEntity player, int bagSlot, int invSlot) {
    ItemStackHandler handler = getInventory(bag);
    boolean success = false;
    if (handler != null) {
      ItemStack extracted = handler.extractItem(bagSlot, 1, true);
      success = player.inventory.add(invSlot, extracted);
    }
    return success;
  }

  private static ItemStackHandler getInventory(ItemStack bag) {
    if (bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
      return (ItemStackHandler) bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
    }
    return null;
  }

  public static ItemStack tryInsert(ItemStack bag, ItemStack stack) {
    AtomicReference<ItemStack> returnStack = new AtomicReference<>(ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()));
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      returnStack.set(ItemHandlerHelper.insertItem(h, stack, false));
    });
    return returnStack.get();
  }

  public static ItemStack tryFilteredInsert(ItemStack bag, ItemStack stack) {
    if (bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() && bagHasItem(bag, stack)) {
      return tryInsert(bag, stack);
    }
    return stack;
  }

  private static boolean bagHasItem(ItemStack bag, ItemStack stack) {
    AtomicBoolean hasItem = new AtomicBoolean(false);
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        if (h.getStackInSlot(i).getItem() == stack.getItem()) {
          hasItem.set(true);
        }
      }
    });
    return hasItem.get();
  }

  @SuppressWarnings("unused")
  public static int getFirstSlotWithStack(ItemStack bag, ItemStack stack) {
    AtomicInteger slot = new AtomicInteger(-1);
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        if (h.getStackInSlot(i).getItem() == stack.getItem()) {
          slot.set(i);
        }
      }
    });
    return slot.get();
  }

  private static int getLastSlotWithStack(ItemStack bag, ItemStack stack) {
    AtomicInteger slot = new AtomicInteger(-1);
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = h.getSlots() - 1; i >= 0; i--) {
        if (h.getStackInSlot(i).getItem() == stack.getItem()) {
          slot.set(i);
        }
      }
    });
    return slot.get();
  }

  public static PickupMode getPickupMode(ItemStack stack) {
    String mode = stack.getOrCreateTag().getString(PickupMode.NBT);
    for (int i = 0; i < PickupMode.values().length; i++) {
      if (mode.equals(PickupMode.values()[i].getString())) {
        return PickupMode.values()[i];
      }
    }
    return PickupMode.NOTHING;
  }

  private static DepositMode getDepositMode(ItemStack stack) {
    String mode = stack.getOrCreateTag().getString(DepositMode.NBT);
    for (int i = 0; i < DepositMode.values().length; i++) {
      if (mode.equals(DepositMode.values()[i].getString())) {
        return DepositMode.values()[i];
      }
    }
    return DepositMode.NOTHING;
  }

  private static RefillMode getRefillMode(ItemStack stack) {
    String mode = stack.getOrCreateTag().getString();
    for (int i = 0; i < RefillMode.values().length; i++) {
      if (mode.equals(RefillMode.values()[i].getString())) {
        return RefillMode.values()[i];
      }
    }
    return RefillMode.NOTHING;
  }

  public static List<Integer> getAllBagSlots(PlayerEntity player) {
    List<Integer> slots = new ArrayList<>();
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      if (isBag(player.inventory.getStackInSlot(i))) {
        slots.add(i);
      }
    }
    return slots;
  }

  @SuppressWarnings("unused")
  public static int getFirstBagSlot(PlayerEntity player) {
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      if (isBag(player.inventory.getStackInSlot(i))) {
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
    myBag.getOrCreateTag().putLong("ts", System.currentTimeMillis());
  }
}
