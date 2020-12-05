package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.net.PacketTileInventory;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class EnderShelfItemHandler extends ItemStackHandler {

  public TileEnderShelf shelf;
  private boolean fake;
  private static final int MAX_MULTIBLOCK_DISTANCE = 8;

  public EnderShelfItemHandler(TileEnderShelf shelf) {
    super(6);
    this.shelf = shelf;
    this.fake = false;
  }

  public EnderShelfItemHandler(TileEnderShelf shelf, boolean fake) {
    super(6);
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
    if (!fake)
      return super.extractItem(slot, amount, simulate);

    long rand = (long) (Math.random() * ForgeRegistries.ENCHANTMENTS.getValues().size());
    Enchantment randomEnchant = ForgeRegistries.ENCHANTMENTS.getValues().stream().skip(rand).findAny().orElse(Enchantments.AQUA_AFFINITY);
    int randLevel = Math.max(randomEnchant.getMinLevel(), (int) (Math.random() * randomEnchant.getMaxLevel()));
    EnchantmentData ench = new EnchantmentData(randomEnchant, randLevel);
    ItemStack book = EnchantedBookItem.getEnchantedItemStack(ench);
    return book;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
    return stack.getItem() == Items.ENCHANTED_BOOK &&
            EnchantedBookItem.getEnchantments(stack).size() == 1 &&
            (this.getStackInSlot(slot).isEmpty() ||
                    UtilEnchant.doBookEnchantmentsMatch(stack, this.getStackInSlot(slot)));
  }

  @Override
  protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
    return isFakeSlot(slot) ? 0 : 16;
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    //System.out.printf("Simulate: %s. Insert: %d of %s into slot %d. Currently has %d of %s in slot.%n", simulate,
    //        stack.getCount(), stack.getOrCreateTag().getString(), slot,
    //        this.getStackInSlot(slot).getCount(), this.getStackInSlot(slot).getOrCreateTag().getString());
    AtomicReference<ItemStack> remaining = new AtomicReference<>(ItemStack.EMPTY);
    if (isFakeSlot(slot)) { //The last slot is a "fake slot" for handling multiblock insert
      BlockPos newPos = findShelfForInsert(stack);
      int newSlot = findMatchingSlot((TileEnderShelf) this.shelf.getWorld().getTileEntity(newPos), stack);
      if (newPos != this.shelf.getPos()) {
        //System.out.println("Found connected shelf at " + newPos.toString() + " in slot " + newSlot);
        this.shelf.getWorld().getTileEntity(newPos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
          ItemStack remaining2 = h.insertItem(newSlot, stack, simulate);
          //System.out.println("Of the " + stack.getCount() + " items we had, there are " + remaining2.getCount() + " left");
          if (!this.shelf.getWorld().isRemote && !simulate) {
            PacketRegistry.sendToAllClients(this.shelf.getWorld(), new PacketTileInventory(newPos, newSlot, h.getStackInSlot(newSlot), PacketTileInventory.TYPE.SET));
          }
          remaining.set(remaining2);
        });
      }
      else
        return stack;
    }
    //if (isFakeSlot(slot) && !remaining.get().isEmpty())
      //remaining.set(super.insertItem(slot, remaining.get(), simulate));
    if (!isFakeSlot(slot))
      remaining.set(super.insertItem(slot, stack, simulate));
    if (!this.shelf.getWorld().isRemote && !simulate) {
      PacketRegistry.sendToAllClients(this.shelf.getWorld(), new PacketTileInventory(this.shelf.getPos(), slot, this.getStackInSlot(slot), PacketTileInventory.TYPE.SET));
    }
    return remaining.get();
  }

  private boolean isFakeSlot(int slot) {
    return slot == getFakeSlot();
  }

  private int getFakeSlot() {
    return this.getSlots() - 1;
  }

  public BlockPos findShelfForInsert(ItemStack stack) {
    //Set<BlockPos> connectedShelves = findConnectedShelves(new HashSet<>(), this.shelf.getWorld(), this.shelf.getPos(), 0);
    Set<BlockPos> connectedShelves = findConnectedShelves(this.shelf.getWorld(), this.shelf.getPos());
    Set<BlockPos> shelvesWithFreeSlots = new HashSet<>();
    BlockPos originalPos = this.shelf.getPos();

    Optional<BlockPos> possibleOtherDestination = connectedShelves.stream().filter(pos -> {
      TileEntity otherTile = this.shelf.getWorld().getTileEntity(pos);
      if (otherTile instanceof TileEnderShelf) {
        if (otherTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
          EnderShelfItemHandler handler = (EnderShelfItemHandler) otherTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
          for (int i = 0; i < handler.getSlots() - 1; i++) {
            ItemStack otherStack = handler.getStackInSlot(i);
            //System.out.println("Other stack is " + otherStack.toString());
            if (otherStack.isEmpty()) {
              if (!shelvesWithFreeSlots.contains(pos))
                //System.out.println("Added " + pos.toString() + " to free slot list");
              shelvesWithFreeSlots.add(pos);
            }
            if (pos != originalPos && UtilEnchant.doBookEnchantmentsMatch(stack, otherStack) && otherStack.getCount() < handler.getStackLimit(i, stack)) {
              //System.out.println("Candidate at " + pos.toString());
              return true;
            }
            //System.out.println("Ruled out " + pos.toString());
          }
        }
      }
      return false;
    }).findFirst();
    BlockPos finals = possibleOtherDestination.orElse(shelvesWithFreeSlots.stream().findFirst().orElse(originalPos));
    //System.out.println("Going to try " + finals.toString());
    return finals;
  }

  private Set<BlockPos> findConnectedShelves(Set<BlockPos> connectedShelves, World world, BlockPos currentPos, int distanceTravelled) {
    if (distanceTravelled > MAX_MULTIBLOCK_DISTANCE)
      return connectedShelves;
    if (!(world.getTileEntity(currentPos) instanceof TileEnderShelf))
      return connectedShelves;
    connectedShelves.add(currentPos);
    //System.out.println("Added " + currentPos.toString());
    distanceTravelled++;
    connectedShelves.addAll(findConnectedShelves(connectedShelves, world, currentPos.offset(Direction.UP), distanceTravelled));
    connectedShelves.addAll(findConnectedShelves(connectedShelves, world, currentPos.offset(Direction.DOWN), distanceTravelled));
    connectedShelves.addAll(findConnectedShelves(connectedShelves, world, currentPos.offset(Direction.EAST), distanceTravelled));
    connectedShelves.addAll(findConnectedShelves(connectedShelves, world, currentPos.offset(Direction.WEST), distanceTravelled));
    connectedShelves.addAll(findConnectedShelves(connectedShelves, world, currentPos.offset(Direction.NORTH), distanceTravelled));
    connectedShelves.addAll(findConnectedShelves(connectedShelves, world, currentPos.offset(Direction.SOUTH), distanceTravelled));

    return connectedShelves;
  }

  private Set<BlockPos> findConnectedShelves(World world, BlockPos currentPos) {
    List<BlockPos> blocks = UtilShape.cubeSquareBase(currentPos, MAX_MULTIBLOCK_DISTANCE, MAX_MULTIBLOCK_DISTANCE);
    return new HashSet<BlockPos>(blocks.stream().filter(pos -> world.getTileEntity(pos) instanceof TileEnderShelf).collect(Collectors.toSet()));
  }

  private int findMatchingSlot(TileEnderShelf shelf, ItemStack book) {
    AtomicInteger slot = new AtomicInteger(getFakeSlot());
    AtomicInteger firstFreeSlot = new AtomicInteger(getFakeSlot());
    shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots() - 1; i++) {
        if (firstFreeSlot.get() == 5 && h.getStackInSlot(i).isEmpty())
          firstFreeSlot.set(i);
        if (UtilEnchant.doBookEnchantmentsMatch(book, h.getStackInSlot(i)))
          slot.set(i);
      }
    });
    return slot.get() != getFakeSlot() ? slot.get() : firstFreeSlot.get();
  }
}
