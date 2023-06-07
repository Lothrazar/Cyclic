package com.lothrazar.cyclic.block.cable.item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableItem extends TileBlockEntityCyclic implements MenuProvider {

  private static final int FLOW_QTY = 64; // fixed, for non-extract motion
  private int extractQty = 64; // default
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }
  };
  private Map<Direction, LazyOptional<IItemHandler>> flow = Maps.newHashMap();

  public TileCableItem(BlockPos pos, BlockState state) {
    super(TileRegistry.ITEM_PIPE.get(), pos, state);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(TileCableItem::createHandler));
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableItem e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileCableItem e) {
    e.tick();
  }

  private static ItemStackHandler createHandler() {
    return new ItemStackHandler(1);
  }

  List<Integer> rawList = IntStream.rangeClosed(
      0,
      5).boxed().collect(Collectors.toList());

  //  @Override
  public void tick() {
    for (Direction extractSide : Direction.values()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(extractSide));
      if (connection.isExtraction()) {
        IItemHandler sideHandler = flow.get(extractSide).orElse(null);
        tryExtract(sideHandler, extractSide, extractQty, filter);
      }
    }
    normalFlow();
  }

  private void normalFlow() {
    IItemHandler sideHandler;
    Direction outgoingSide;
    for (Direction incomingSide : Direction.values()) {
      sideHandler = flow.get(incomingSide).orElse(null);
      //thise items came from that
      Collections.shuffle(rawList);
      boolean validAdjacent = false;
      for (Integer i : rawList) {
        outgoingSide = Direction.values()[i];
        if (outgoingSide == incomingSide) {
          continue;
        }
        EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
        if (connection.isExtraction() || connection.isBlocked()) {
          continue;
        }
        validAdjacent = validAdjacent || this.moveItems(outgoingSide, 64, sideHandler);
      }
      if (!validAdjacent) {
        this.moveItems(incomingSide, FLOW_QTY, sideHandler);
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
        return flow.get(side).cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void load(CompoundTag tag) {
    extractQty = tag.getInt("extractCount");
    LazyOptional<IItemHandler> item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      item.ifPresent(h -> {
        CompoundTag itemTag = tag.getCompound("item" + f.toString());
        ((INBTSerializable<CompoundTag>) h).deserializeNBT(itemTag);
      });
    }
    filter.deserializeNBT(tag.getCompound("filter"));
    super.load(tag);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("extractCount", extractQty);
    LazyOptional<IItemHandler> item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      item.ifPresent(h -> {
        CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
        tag.put("item" + f.toString(), compound);
      });
    }
    super.saveAdditional(tag);
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
  public Component getDisplayName() {
    return BlockRegistry.ITEM_PIPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCableItem(i, level, worldPosition, playerInventory, playerEntity);
  }
}
