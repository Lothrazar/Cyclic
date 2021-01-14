package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.net.PacketTileInventory;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import javax.annotation.Nonnull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class EnderShelfItemHandler extends ItemStackHandler {

  public TileEnderShelf shelf;
  private final boolean fake;

  public EnderShelfItemHandler(TileEnderShelf shelf) {
    super(5);
    this.shelf = shelf;
    this.fake = false;
  }

  public EnderShelfItemHandler(TileEnderShelf shelf, boolean fake) {
    super(5);
    this.shelf = shelf;
    this.fake = fake;
  }

  public ItemStack emptySlot(int slot) {
    ItemStack returnStack = this.getStackInSlot(slot);
    this.stacks.set(slot, ItemStack.EMPTY);
    return returnStack;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (this.shelf.getWorld() == null) {
      return ItemStack.EMPTY;
    }
    if (!fake) {
      ItemStack extracted = super.extractItem(slot, amount, simulate);
      if (!this.shelf.getWorld().isRemote && !simulate) {
        PacketRegistry.sendToAllClients(this.shelf.getWorld(), new PacketTileInventory(this.shelf.getPos(), slot, this.getStackInSlot(slot), PacketTileInventory.TYPE.SET));
      }
      return extracted;
    }
    long rand = (long) (Math.random() * ForgeRegistries.ENCHANTMENTS.getValues().size());
    Enchantment randomEnchant = ForgeRegistries.ENCHANTMENTS.getValues().stream().skip(rand).findAny().orElse(Enchantments.AQUA_AFFINITY);
    int randLevel = Math.max(randomEnchant.getMinLevel(), (int) (Math.random() * randomEnchant.getMaxLevel()));
    EnchantmentData ench = new EnchantmentData(randomEnchant, randLevel);
    return EnchantedBookItem.getEnchantedItemStack(ench);
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return stack.getItem() == Items.ENCHANTED_BOOK &&
        EnchantedBookItem.getEnchantments(stack).size() == 1 &&
        (this.getStackInSlot(slot).isEmpty() || UtilEnchant.doBookEnchantmentsMatch(stack, this.getStackInSlot(slot)));
  }

  @Override
  protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
    return 64;
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (shelf == null || shelf.getWorld() == null) {
      return stack;
    }
    ItemStack remaining = super.insertItem(slot, stack, simulate);
    if (!this.shelf.getWorld().isRemote && !simulate) {
      PacketRegistry.sendToAllClients(this.shelf.getWorld(), new PacketTileInventory(this.shelf.getPos(), slot, this.getStackInSlot(slot), PacketTileInventory.TYPE.SET));
    }
    return remaining;
  }

  public int firstSlotWithItem() {
    for (int i = 0; i < this.getSlots(); i++) {
      if (!this.getStackInSlot(i).isEmpty()) {
        return i;
      }
    }
    return -1;
  }
}
