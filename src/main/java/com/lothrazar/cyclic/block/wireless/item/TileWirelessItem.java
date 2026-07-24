package com.lothrazar.cyclic.block.wireless.item;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;

public class TileWirelessItem extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    RENDER, TRANSFER_RATE, REDSTONE;
  }

  public TileWirelessItem(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_ITEM.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessItem e) {
    e.tick();
  }

  //TWO INVENTORIES: ONE FOR GETCAP IO AND ONE FOR HIDDEN CARD
  private int transferRate = 1;
  ItemStackHandler inventory = new ItemStackHandler(1);
  ItemStackHandler gpsSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  @Override
  public Component getDisplayName() {
    return BlockRegistry.WIRELESS_ITEM.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerWirelessItem(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    gpsSlots.deserialize(input.childOrEmpty(NBTINV + "gps"));
    this.transferRate = input.getIntOr("transferRate", 0);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    output.putInt("transferRate", transferRate);
    inventory.serialize(output.child(NBTINV));
    gpsSlots.serialize(output.child(NBTINV + "gps"));
    super.saveAdditional(output);
  }

  //  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (level.isClientSide()) {
      return;
    }
    boolean moved = false;
    //run the transfer. one slot only
    BlockPosDim loc = getTargetInSlot();
    if (loc != null) {
      if (LevelWorldUtil.dimensionIsEqual(loc, level)) {
        moved = moveItems(loc.getSide(), loc.getPos(), this.transferRate, this.inventory, 0);
      }
      else if (ConfigRegistry.TRANSFER_NODES_DIMENSIONAL.get()) {
        moved = moveItemsDimensional(loc, this.transferRate, this.inventory, 0);
      }
    }
    this.setLitProperty(moved);
  }

  BlockPosDim getTargetInSlot() {
    return LocationGpsCard.getPosition(this.gpsSlots.getStackInSlot(0));
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % PreviewOutlineType.values().length;
      break;
      case TRANSFER_RATE:
        transferRate = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case TRANSFER_RATE:
        return this.transferRate;
    }
    return 0;
  }

  public float getRed() {
    return 0.89F;
  }

  public float getBlue() {
    return 0;
  }

  public float getGreen() {
    return 0.12F;
  }

  public float getAlpha() {
    return 0.9F;
  }

  public float getThick() {
    return 0.065F;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
