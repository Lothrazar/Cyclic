package com.lothrazar.cyclic.block.wirelessredstone;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWirelessTransmit extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public TileWirelessTransmit() {
    super(TileRegistry.wireless_transmitter);
  }

  ItemStackHandler inventory = new ItemStackHandler(9) {

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerTransmit(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  private void toggleTarget(BlockPos targetPos) {
    BlockState target = world.getBlockState(targetPos);
    if (target.hasProperty(BlockStateProperties.POWERED)) {
      boolean targetPowered = target.get(BlockStateProperties.POWERED);
      //update target based on my state
      boolean isPowered = world.isBlockPowered(pos);
      if (targetPowered != isPowered) {
        world.setBlockState(targetPos, target.with(BlockStateProperties.POWERED, isPowered));
        //and update myself too   
        world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.POWERED, isPowered));
        //TODO: send exact 1-16 power level
        //        world.getTileEntity(targetPos) instanceof TileWirelessRec
        //        && target.getBlock() instanceof BlockWirelessRec
      }
    }
  }

  @Override
  public void tick() {
    for (int s = 0; s < inventory.getSlots(); s++) {
      ItemStack stack = inventory.getStackInSlot(s);
      BlockPosDim targetPos = LocationGpsCard.getPosition(stack);
      if (targetPos == null) {
        // || targetPos.getDimension() != world.dimension.getType().getId()) {
        return;
      }
      toggleTarget(targetPos.getPos());
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
