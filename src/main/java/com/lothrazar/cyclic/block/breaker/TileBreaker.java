package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class TileBreaker extends TileEntityBase implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER;
  }

  static final int MAX = 64000;
  public static final int TIMER_FULL = 500;
  //  public static IntValue POWERCONF;
  //  private CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  //  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  public TileBreaker() {
    super(TileRegistry.breakerTile);
  }

//  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (level.isClientSide) {
      return;
    }
    BlockPos target = worldPosition.relative(this.getCurrentFacing());
    BlockState state = level.getBlockState(target);
    if (state.getBlock() != Blocks.AIR &&
        state.getDestroySpeed(level, target) >= 0) {
      this.level.destroyBlock(target, true);
      //      int cost = POWERCONF.get();
      //      ModCyclic.LOGGER.info("cost" + cost + " have " + energy.getEnergyStored());
      //      if (cost > 0) {
      //        energy.extractEnergy(cost, false);
      //      }
    }
    //else unbreakable
  }
  //  @Override
  //  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
  //    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
  //      return energyCap.cast();
  //    }
  //    return super.getCapability(cap, side);
  //  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerBreaker(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    //    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.load(bs, tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    //    tag.put(NBTENERGY, energy.serializeNBT());
    return super.save(tag);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        timer = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return timer;
    }
    return 0;
  }

  public int getEnergyMax() {
    return MAX;
  }
}
