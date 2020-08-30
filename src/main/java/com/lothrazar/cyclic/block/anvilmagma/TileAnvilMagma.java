package com.lothrazar.cyclic.block.anvilmagma;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileAnvilMagma extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static final INamedTag<Item> IMMUNE = ItemTags.makeWrapperTag(new ResourceLocation(ModCyclic.MODID, "anvil_immune").toString());
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  public FluidTankBase tank;

  public static enum Fields {
    TIMER, REDSTONE;
  }

  private static final int OUT = 1;
  private static final int IN = 0;

  public TileAnvilMagma() {
    super(TileRegistry.anvil_magma);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> p.getFluid() == FluidMagmaHolder.STILL.get();
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(2) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == IN)
          return stack.isRepairable() &&
              stack.getDamage() > 0;
        if (slot == OUT)
          return stack.isRepairable() &&
              stack.getDamage() == 0;
        return true;
      }
    };
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerAnvilMagma(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    tank.readFromNBT(tag.getCompound("fluid"));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    inventory.ifPresent(inv -> {
      ItemStack stack = inv.getStackInSlot(0);
      if (stack.isEmpty() || stack.getItem().isIn(IMMUNE)) {
        return;
      }
      final int repair = ConfigManager.ANVILPOWER.get();
      if (tank != null &&
          tank.getFluidAmount() >= repair &&
          stack.isRepairable() &&
          stack.getDamage() > 0) {
        //we can repair so steal some power 
        //ok drain power  
        UtilItemStack.repairItem(stack);
        tank.drain(repair, FluidAction.EXECUTE);
      }
      //shift to other slot
      if ((stack.getDamage() == 0 || !stack.isRepairable())
          && inv.getStackInSlot(1).isEmpty()) {
        //
        inv.insertItem(1, stack.copy(), false);
        inv.extractItem(0, stack.getCount(), false);
      }
    });
  }

  @Override
  public void setField(int field, int value) {}

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }
}
