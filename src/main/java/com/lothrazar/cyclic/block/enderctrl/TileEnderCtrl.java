package com.lothrazar.cyclic.block.enderctrl;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEnderCtrl extends TileEntityBase {

  private static final String NBT_SHELVES = "shelves";
  EnderControllerItemHandler controllerInv = new EnderControllerItemHandler(this);
  private final LazyOptional<EnderControllerItemHandler> controllerInventoryCap = LazyOptional.of(() -> controllerInv);
  private List<BlockPos> connectedShelves = new ArrayList<>();
  RenderTextType renderStyle = RenderTextType.TEXT;

  public TileEnderCtrl(BlockPos pos, BlockState state) {
    super(TileRegistry.ender_controller,pos,state );
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
  public void setShelves(Set<BlockPos> shelvesIn) {
    ModCyclic.LOGGER.info("resetting and sorting the shelves " + shelvesIn.size());
    this.connectedShelves = shelvesIn.stream().sorted(Comparator.comparing(o -> o.distSqr(this.worldPosition))).collect(Collectors.toList());
  }

  public List<BlockPos> getShelves() {
    if (connectedShelves == null) {
      connectedShelves = new ArrayList<>();
    }
    return connectedShelves;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return controllerInventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load( CompoundTag tag) {
    if (tag.contains("RenderTextType")) {
      int rt = tag.getInt("RenderTextType");
      this.renderStyle = RenderTextType.values()[rt];
    }
    if (tag.contains(NBT_SHELVES)) {
      ListTag shelves = tag.getList(NBT_SHELVES, Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < shelves.size(); i++) {
        BlockPos pos = NbtUtils.readBlockPos(shelves.getCompound(i));
        this.connectedShelves.add(pos);
      }
    }
    super.load( tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.putInt("RenderTextType", this.renderStyle.ordinal());
    ListTag shelves = new ListTag();
    for (BlockPos pos : this.connectedShelves) {
      shelves.add(NbtUtils.writeBlockPos(pos));
    }
    tag.put(NBT_SHELVES, shelves);
    return super.save(tag);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }
}
