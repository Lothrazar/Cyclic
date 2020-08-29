package com.lothrazar.cyclic.block.breaker;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TileBreaker extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static enum Fields {
    REDSTONE;
  }

  public TileBreaker() {
    super(BlockRegistry.TileRegistry.breakerTile);
  }

  private void setAnimation(boolean lit) {
    BlockState st = this.world.getBlockState(pos);
    boolean previous = st.get(BlockBreaker.IS_LIT);
    if (previous != lit)
      this.world.setBlockState(pos, st.with(BlockBreaker.IS_LIT, lit));
  }

  private Direction getCurrentFacing() {
    return this.getBlockState().get(BlockStateProperties.FACING);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerBreaker(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setAnimation(false);
      return;
    }
    setAnimation(true);
    if (world.rand.nextDouble() < 0.3) {
      BlockPos target = pos.offset(this.getCurrentFacing());
      BlockState state = world.getBlockState(target);
      if (state.getBlockHardness(world, target) >= 0) {
        this.world.destroyBlock(target, true);
      }
      //else unbreakable
    }
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.getNeedsRedstone();
    }
    return 0;
  }
}
