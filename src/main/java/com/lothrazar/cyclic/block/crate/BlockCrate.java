package com.lothrazar.cyclic.block.crate;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCrate extends BlockBase {

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
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    //    if (state.hasTileEntity() && (!state.is(newState.getBlock()) || !newState.hasTileEntity())) {
    if (!state.is(newState.getBlock())) {
      worldIn.removeBlockEntity(pos);
    }
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.CRATE, ScreenCrate::new);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootContext.Builder builder) {
    // because harvestBlock manually forces a drop, we must do this to dodge that
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    BlockEntity tileentity = worldIn.getBlockEntity(pos);
    if (stack.getTag() != null && tileentity instanceof TileCrate && stack.getTag().contains(NBTCRATE + "0")) {
      //to tile from tag
      TileCrate crate = (TileCrate) tileentity;
      for (int i = 0; i < crate.inventory.getSlots(); i++) {
        //
        ItemStack crateStack = ItemStack.of(stack.getTag().getCompound(NBTCRATE + i));
        crate.inventory.setStackInSlot(i, crateStack);
      }
    }
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity tileentity, ItemStack stackToolUsed) {
    super.playerDestroy(world, player, pos, state, tileentity, stackToolUsed);
    ItemStack newStack = new ItemStack(this);
    if (tileentity instanceof TileCrate) {
      TileCrate crate = (TileCrate) tileentity;
      //read from tile, write to itemstack
      for (int i = 0; i < crate.inventory.getSlots(); i++) {
        CompoundTag nbt = new CompoundTag();
        crate.inventory.getStackInSlot(i).save(nbt);
        newStack.getOrCreateTag().put(NBTCRATE + i, nbt);
      }
    }
    UtilItemStack.drop(world, pos, newStack);
  }
}
