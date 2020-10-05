package com.lothrazar.cyclic.block.disenchant;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileDisenchant extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static IntValue POWERCONF;
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  static final int MAX = 640000;
  private static final int SLOT_INPUT = 0;
  private static final int SLOT_BOOK = 1;
  private static final int SLOT_OUT = 2;

  static enum Fields {
    REDSTONE;
  }

  public TileDisenchant() {
    super(TileRegistry.disenchanter);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    IEnergyStorage cap = this.energy.orElse(null);
    if (cap == null) {
      return;
    }
    Integer cost = POWERCONF.get();
    if (cap.getEnergyStored() < cost && (cost > 0)) {
      return;//broke
    }
    inventory.ifPresent(inv -> {
      ItemStack input = inv.getStackInSlot(SLOT_INPUT);
      ItemStack book = inv.getStackInSlot(SLOT_BOOK);
      if (book.getItem() != Items.BOOK
          || inv.getStackInSlot(SLOT_OUT).isEmpty() == false
          || input.getCount() != 1) {
        return;
      }
      //input is size 1, at least one book exists, and output IS empty
      Map<Enchantment, Integer> outEnchants = Maps.<Enchantment, Integer> newLinkedHashMap();
      Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(input);
      Enchantment keyMoved = null;
      for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
        keyMoved = entry.getKey();
        outEnchants.put(keyMoved, entry.getValue());
        break;
      }
      if (outEnchants.size() == 0 || keyMoved == null) {
        return;
      }
      //and input has at least one enchantment 
      //success happening
      UtilSound.playSound(world, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);
      cap.extractEnergy(cost, true);
      //do all the moving over
      enchants.remove(keyMoved);
      ItemStack eBook = new ItemStack(Items.ENCHANTED_BOOK);
      EnchantmentHelper.setEnchantments(outEnchants, eBook);//add to book
      //replace book with enchanted
      inv.extractItem(SLOT_BOOK, 1, false);
      inv.insertItem(SLOT_OUT, eBook, false);
      //do i replace input with a book?
      if (input.getItem() == Items.ENCHANTED_BOOK && enchants.size() == 0) {
        inv.extractItem(SLOT_INPUT, 64, false);//delete input
        inv.insertItem(SLOT_INPUT, new ItemStack(Items.BOOK), false);
      }
      else {
        //was a normal item, so ok to set its ench list to empty
        if (input.getItem() == Items.ENCHANTED_BOOK) {//hotfix workaround for book: so it dont try to merge eh
          ItemStack inputCopy = new ItemStack(Items.ENCHANTED_BOOK);
          EnchantmentHelper.setEnchantments(enchants, inputCopy);//set as remove
          inv.extractItem(SLOT_INPUT, 64, false);//delete input
          inv.insertItem(SLOT_INPUT, inputCopy, false);
        }
        else {
          EnchantmentHelper.setEnchantments(enchants, input);//set as removed
          //          inv.extractItem(SLOT_INPUT, 64, false);//delete input
          //          inv.insertItem(SLOT_INPUT, input, false);
        }
        //        }
      }
    });
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(3) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == SLOT_BOOK) {
          return stack.getItem() == Items.BOOK;
        }
        else if (slot == SLOT_INPUT) {
          Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
          return enchants != null && enchants.size() > 0;
        }
        return stack.getItem() == Items.ENCHANTED_BOOK;
      }
    };
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX / 4);
  }

  @Override
  public int getEnergy() {
    return this.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerDisenchant(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }
}
