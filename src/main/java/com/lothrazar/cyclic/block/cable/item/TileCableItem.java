package com.lothrazar.cyclic.block.cable.item;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.Map;
import com.lothrazar.cyclic.util.UtilDirection;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableItem extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final int FLOW_QTY = 64; // fixed, for non-extract motion
  private int extractQty = 64; // default
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  private Map<Direction, LazyOptional<IItemHandler>> flow = Maps.newHashMap();

  public TileCableItem() {
    super(TileRegistry.item_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(TileCableItem::createHandler));
    }
  }

  private static ItemStackHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public void tick() {
    for (final Direction extractSide : Direction.values()) {
      final EnumProperty<EnumConnectType> extractFace = CableBase.FACING_TO_PROPERTY_MAP.get(extractSide);
      final EnumConnectType connection = this.getBlockState().get(extractFace);
      if (connection.isExtraction()) {
        final IItemHandler sideHandler = flow.get(extractSide).orElse(null);
        tryExtract(sideHandler, extractSide, extractQty, filter);
      }
    }
    normalFlow();
  }

  private void normalFlow() {
    incomingSideLoop: for (final Direction incomingSide : Direction.values()) {
      //in all cases sideHandler is required
      final IItemHandler sideHandler = flow.get(incomingSide).orElse(null);

      for (final Direction outgoingSide : UtilDirection.inDifferingOrder.next()) {
        if (outgoingSide == incomingSide)
          continue;

        final EnumProperty<EnumConnectType> outgoingFace = CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide);
        final EnumConnectType outgoingConnection = this.getBlockState().get(outgoingFace);
        if (outgoingConnection.isExtraction() || outgoingConnection.isBlocked())
          continue;

        if (this.moveItems(outgoingSide, FLOW_QTY, sideHandler))
          continue incomingSideLoop; //if items have been moved then change side
      }

      //if no items have been moved then move items in from adjacent
      this.moveItems(incomingSide, FLOW_QTY, sideHandler);
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
        return flow.get(side).cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    extractQty = tag.getInt("extractCount");
    for (final Direction direction : Direction.values()) {
      flow.get(direction).ifPresent(itemHandler -> {
        final String key = UtilDirection.itemNBTKeyMapping.get(direction);
        final CompoundNBT itemTag = tag.getCompound(key);
        ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(itemTag);
      });
    }
    filter.deserializeNBT(tag.getCompound("filter"));
    super.read(bs, tag);
  }

  @SuppressWarnings("unchecked")
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("extractCount", extractQty);
    for (final Direction direction : Direction.values()) {
      flow.get(direction).ifPresent(itemHandler -> {
        final CompoundNBT itemTag = ((INBTSerializable<CompoundNBT>) itemHandler).serializeNBT();
        final String key = UtilDirection.itemNBTKeyMapping.get(direction);
        tag.put(key, itemTag);
      });
    }
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {
    this.extractQty = value;
  }

  @Override
  public int getField(int field) {
    return this.extractQty;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerCableItem(i, world, pos, playerInventory, playerEntity);
  }
}
