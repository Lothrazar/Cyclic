package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.facade.ITileFacade;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;

public class TileLightFacade extends TileBlockEntityCyclic implements ITileFacade {

  public TileLightFacade(BlockPos pos, BlockState state) {
    super(TileRegistry.LIGHT_CAMO.get(), pos, state);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag syncData = super.getUpdateTag(registries);
    return syncData;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
    if (pkt.getTag().contains(NBT_FACADE)) {
      this.loadAdditional(pkt.getTag(), registries);
      super.onDataPacket(net, pkt, registries);
    }
  }

  @Override
  public void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
    this.loadFacade(tag);
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
    this.saveFacade(tag);
    super.saveAdditional(tag, registries);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  private CompoundTag facadeState = null;

  @Override
  public CompoundTag getFacade() {
    return facadeState;
  }

  @Override
  public void setFacade(CompoundTag facadeStateIn) {
    this.facadeState = facadeStateIn;
  }
}
