package com.lothrazar.cyclic.item.crafting;

import java.util.HashSet;
import java.util.Set;
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

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.crafting_bag, CraftingBagScreen::new);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new CraftingBagCapabilityProvider(stack, slots);
  }
  //  @Nullable
  //  private static ItemStackHandler getInventory(ItemStack bag) {
  //    if (bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
  //      return (ItemStackHandler) bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
  //    return null;
  //  }

  public static ItemStack tryInsert(ItemStack bag, ItemStack stack) {
    AtomicReference<ItemStack> returnStack = new AtomicReference<>(ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()));
    bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      returnStack.set(ItemHandlerHelper.insertItem(h, stack, false));
    });
    return returnStack.get();
  }

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
