package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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

  static enum Fields {
    RENDER;
  }

  public TileWirelessTransmit(BlockPos pos, BlockState state) {
    super(TileRegistry.wireless_transmitter, pos, state);
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
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.save(tag);
  }

  private void toggleTarget(BlockPos targetPos) {
    BlockState target = level.getBlockState(targetPos);
    if (target.hasProperty(BlockStateProperties.POWERED)) {
      boolean targetPowered = target.getValue(BlockStateProperties.POWERED);
      //update target based on my state
      boolean isPowered = level.hasNeighborSignal(worldPosition);
      if (targetPowered != isPowered) {
        level.setBlockAndUpdate(targetPos, target.setValue(BlockStateProperties.POWERED, isPowered));
        //and update myself too   
        level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, isPowered));
      }
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessTransmit e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessTransmit e) {
    e.tick();
  }

  public void tick() {
    for (int s = 0; s < inventory.getSlots(); s++) {
      BlockPosDim targetPos = getTargetInSlot(s);
      if (targetPos == null ||
          UtilWorld.dimensionIsEqual(targetPos, level) == false) {
        continue;
      }
      toggleTarget(targetPos.getPos());
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
