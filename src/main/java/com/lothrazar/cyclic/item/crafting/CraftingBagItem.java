package com.lothrazar.cyclic.item.crafting;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class CraftingBagItem extends ItemBase {

  private int slots;

  public CraftingBagItem(Properties properties) {
    super(properties);
  }

  public CraftingBagItem(Properties properties, int slots) {
    this(properties);
    this.slots = slots;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new CraftingBagContainerProvider(), playerIn.getPosition());
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
  // 
  //  @Override
  //  public ActionResultType onItemUse(ItemUseContext context) {
  //    BlockPos pos = context.getPos();
  //    Direction face = context.getFace();
  //    World world = context.getWorld();
  //    TileEntity te = world.getTileEntity(pos);
  //    ItemStack bag = context.getItem();
  //    DepositMode mode = getDepositMode(bag);
  //    ItemStackHandler handler = getInventory(bag);
  //    if (handler != null && te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent()) {
  //      IItemHandler teHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).orElse(null);
  //      Set<Item> itemsInTargetInventory = new HashSet<>();
  //      if (teHandler != null) {
  //        for (int j = 0; j < teHandler.getSlots(); j++) {
  //          itemsInTargetInventory.add(teHandler.getStackInSlot(j).getItem());
  //        }
  //      }
  //      for (int i = 0; i < handler.getSlots(); i++) {
  //        ItemStack stack = handler.getStackInSlot(i);
  //        ItemStack remaining = ItemHandlerHelper.copyStackWithSize(stack, stack.getCount());
  //        if (!stack.isEmpty()) {
  //          if (mode == DepositMode.DUMP || (mode == DepositMode.MERGE && itemsInTargetInventory.contains(stack.getItem()))) {
  //            remaining = ItemHandlerHelper.insertItem(teHandler, stack, false);
  //            handler.setStackInSlot(i, remaining);
  //          }
  //        }
  //      }
  //      return ActionResultType.SUCCESS;
  //    }
  //    return ActionResultType.PASS;
  //  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.crafting_bag, CraftingBagScreen::new);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new CraftingBagCapabilityProvider(stack, slots);
  }

  @Nullable
  private static ItemStackHandler getInventory(ItemStack bag) {
    if (bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
      return (ItemStackHandler) bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
    return null;
  }

  public static ItemStack tryInsert(ItemStack bag, ItemStack stack) {
    AtomicReference<ItemStack> returnStack = new AtomicReference<>(ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()));
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      returnStack.set(ItemHandlerHelper.insertItem(h, stack, false));
    });
    return returnStack.get();
  }

  private static boolean bagHasItem(ItemStack bag, ItemStack stack) {
    AtomicBoolean hasItem = new AtomicBoolean(false);
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        if (h.getStackInSlot(i).getItem() == stack.getItem())
          hasItem.set(true);
      }
    });
    return hasItem.get();
  }
  //unused but possibly useful
  //  private static int getFirstSlotWithStack(ItemStack bag, ItemStack stack) {
  //    AtomicInteger slot = new AtomicInteger(-1);
  //    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
  //      for (int i = 0; i < h.getSlots(); i++) {
  //        if (h.getStackInSlot(i).getItem() == stack.getItem())
  //          slot.set(i);
  //      }
  //    });
  //    return slot.get();
  //  }

  public static Set<Integer> getAllBagSlots(PlayerEntity player) {
    Set<Integer> slots = new HashSet<>();
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      if (isBag(player.inventory.getStackInSlot(i)))
        slots.add(i);
    }
    return slots;
  }
  //unused but possibly useful
  //  private static int getFirstBagSlot(PlayerEntity player) {
  //    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
  //      if (isBag(player.inventory.getStackInSlot(i)))
  //        return i;
  //    }
  //    return -1;
  //  }

  private static boolean isBag(ItemStack stack) {
    return stack.getItem() instanceof CraftingBagItem;
  }
}
