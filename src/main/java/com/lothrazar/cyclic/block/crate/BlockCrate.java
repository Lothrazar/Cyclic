package com.lothrazar.cyclic.block.crate;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class BlockCrate extends BlockCyclic {

  private static final String NBTCRATE = "crate";

  public BlockCrate(Properties properties) {
    super(properties.strength(1.1F, 3600000.0F).sound(SoundType.STONE));
    this.setHasGui();
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileCrate(pos, state);
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState bs) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState st, Level level, BlockPos pos, Direction direction) {
    return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
  }

  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel worldIn, BlockPos pos, boolean movedByPiston) {
    if (true) {
      worldIn.removeBlockEntity(pos);
    }
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    // because harvestBlock manually forces a drop, we must do this to dodge that
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    BlockEntity tileentity = worldIn.getBlockEntity(pos);
    if (!(tileentity instanceof TileCrate crate)) {
      return;
    }
    //pull inventory from the item's capability (which loads from CUSTOM_DATA) into the tile
    IItemHandler stackInv = CapabilityUtil.item(stack);
    if (stackInv != null) {
      int slots = Math.min(stackInv.getSlots(), crate.inventory.getSlots());
      for (int i = 0; i < slots; i++) {
        crate.inventory.setStackInSlot(i, stackInv.getStackInSlot(i).copy());
      }
    }
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity tileentity, ItemStack stackToolUsed) {
    super.playerDestroy(world, player, pos, state, tileentity, stackToolUsed);
    ItemStack newStack = new ItemStack(this);
    if (tileentity instanceof TileCrate crate) {
      //write the tile's inventory into the dropped item's capability (which persists into CUSTOM_DATA)
      IItemHandler stackInv = CapabilityUtil.item(newStack);
      if (stackInv != null) {
        int slots = Math.min(stackInv.getSlots(), crate.inventory.getSlots());
        for (int i = 0; i < slots; i++) {
          ItemStack contents = crate.inventory.getStackInSlot(i);
          if (!contents.isEmpty() && stackInv instanceof net.neoforged.neoforge.items.ItemStackHandler sh) {
            sh.setStackInSlot(i, contents.copy());
          }
        }
      }
      CompoundTag nbt = new CompoundTag();
      // crate save stub
    }
    ItemStackUtil.dropItemStackMotionless(world, pos, newStack);
  }
}
