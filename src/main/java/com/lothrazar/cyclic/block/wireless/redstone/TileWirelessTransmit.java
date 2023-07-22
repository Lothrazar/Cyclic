package com.lothrazar.cyclic.block.wireless.redstone;

import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;

public class TileWirelessTransmit extends TileBlockEntityCyclic implements MenuProvider {

  private static final String REDSTONE_ID = "redstone_id";

  static enum Fields {
    RENDER;
  }

  ItemStackHandler inventory = new ItemStackHandler(9) {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }
  };
  private UUID id;

  public TileWirelessTransmit(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_TRANSMITTER.get(), pos, state);
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerTransmit(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.hasUUID(REDSTONE_ID)) {
      this.id = tag.getUUID(REDSTONE_ID);
    }
    else {
      this.id = UUID.randomUUID();
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
    tag.putUUID(REDSTONE_ID, id);
    super.saveAdditional(tag);
  }

  private void toggleTarget(BlockPosDim dimPos) {
    BlockPos targetPos = dimPos.getPos();
    ServerLevel serverLevel = dimPos.getTargetLevel(level);
    if (serverLevel == null) {
      ModCyclic.LOGGER.info("Dimension not found " + dimPos.getDimension());
      return;
    }
    if (!serverLevel.isLoaded(targetPos)) {
      ModCyclic.LOGGER.info("DimPos is unloaded" + dimPos);
      return;
    }
    boolean isPowered = level.hasNeighborSignal(worldPosition);
    //    BlockState target = serverLevel.getBlockState(targetPos);
    if (serverLevel.getBlockEntity(targetPos) instanceof TileWirelessRec receiver) {
      //am I powered?
      if (isPowered) {
        receiver.putPowerSender(this.id);
      }
      else {
        receiver.removePowerSender(this.id);
      }
    }
    if (level.isLoaded(worldPosition) && level.getBlockState(worldPosition).getBlock() == this.getBlockState().getBlock()) {
      level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, isPowered));
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessTransmit e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessTransmit e) {
    //    e.tick();
  }

  public void tick() {
    for (int s = 0; s < inventory.getSlots(); s++) {
      BlockPosDim targetPos = getTargetInSlot(s);
      if (targetPos != null) {
        toggleTarget(targetPos);
      }
    }
  }

  BlockPosDim getTargetInSlot(int s) {
    ItemStack stack = inventory.getStackInSlot(s);
    return LocationGpsCard.getPosition(stack);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case RENDER:
        this.render = value % 2;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case RENDER:
        return render;
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
}
