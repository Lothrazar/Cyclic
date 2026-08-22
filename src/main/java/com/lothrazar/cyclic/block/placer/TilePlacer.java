package com.lothrazar.cyclic.block.placer;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TilePlacer extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER;
  }

  static final int MAX = 64000;
  public static final int TIMER_FULL = 500;
  ItemStackHandler inventory = new ItemStackHandler(1);

  public TilePlacer(BlockPos pos, BlockState state) {
    super(TileRegistry.PLACER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePlacer e) {
    e.tick();
  }

  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.isEmpty() || Block.byItem(stack.getItem()) == Blocks.AIR) {
      return;
    }
    Direction dir = this.getBlockState().getValue(BlockStateProperties.FACING);
    BlockPos offset = worldPosition.relative(dir);
    BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
    if (level.isEmptyBlock(offset) && level.setBlockAndUpdate(offset, state)) {
      stack.shrink(1);
      this.updateComparatorOutputLevel();
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.PLACER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPlacer(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
