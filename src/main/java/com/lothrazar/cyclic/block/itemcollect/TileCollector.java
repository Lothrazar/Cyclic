package com.lothrazar.cyclic.block.itemcollect;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.CyclicRegistry;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCollector extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

  private static final int RADIUS = 8;
  private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

  public TileCollector() {
    super(CyclicRegistry.collectortile);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(36);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerCollector(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return handler.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    CompoundNBT invTag = tag.getCompound("inv");
    handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    handler.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world.isRemote) {
      return;
    }
    List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(
        pos.getX() - RADIUS, pos.getY(), pos.getZ() - RADIUS,
        pos.getX() + RADIUS, pos.getY() + 2, pos.getZ() + RADIUS), (entity) -> {
          return entity.isAlive();//  && entity.getXpValue() > 0;//entity != null && entity.getHorizontalFacing() == facing;
        });
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(world.rand.nextInt(list.size()));
      IItemHandler h = handler.orElse(null);
      ItemStack remainder = stackEntity.getItem();
      for (int i = 0; i < h.getSlots(); i++) {
        if (remainder.isEmpty()) {
          break;
        }
        remainder = h.insertItem(i, remainder, false);
      }
      stackEntity.setItem(remainder);
      if (remainder.isEmpty()) {
        stackEntity.remove();//kill it
      }
    }
  }
}
