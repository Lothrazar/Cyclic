package com.lothrazar.cyclic.block.enderctrl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEnderCtrl extends TileBlockEntityCyclic {

  private static final String NBT_SHELVES = "shelves";
  EnderControllerItemHandler controllerInv = new EnderControllerItemHandler(this);
  private final LazyOptional<EnderControllerItemHandler> controllerInventoryCap = LazyOptional.of(() -> controllerInv);
  private List<BlockPos> connectedShelves = new ArrayList<>();
  RenderTextType renderStyle = RenderTextType.TEXT;

  public TileEnderCtrl(BlockPos pos, BlockState state) {
    super(TileRegistry.ENDER_CONTROLLER.get(), pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  /**
   * Converts to sorted list
   *
   * @param shelvesIn
   */
  public void setAndSort(Set<BlockPos> shelvesIn) {
    this.connectedShelves = shelvesIn.stream().sorted(Comparator.comparing(o -> o.distSqr(this.worldPosition))).collect(Collectors.toList());
  }

  public List<BlockPos> getShelves() {
    if (connectedShelves == null) {
      connectedShelves = new ArrayList<>();
    }
    return connectedShelves;
  }

  @Override
  public void invalidateCaps() {
    controllerInventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return controllerInventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    if (tag.contains("RenderTextType")) {
      int rt = tag.getInt("RenderTextType");
      this.renderStyle = RenderTextType.values()[rt];
    }
    if (tag.contains(NBT_SHELVES)) {
      ListTag shelves = tag.getList(NBT_SHELVES, Tag.TAG_COMPOUND);
      for (int i = 0; i < shelves.size(); i++) {
        BlockPos pos = NbtUtils.readBlockPos(shelves.getCompound(i));
        this.connectedShelves.add(pos);
      }
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("RenderTextType", this.renderStyle.ordinal());
    ListTag shelves = new ListTag();
    for (BlockPos pos : this.connectedShelves) {
      shelves.add(NbtUtils.writeBlockPos(pos));
    }
    tag.put(NBT_SHELVES, shelves);
    super.saveAdditional(tag);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }
}
