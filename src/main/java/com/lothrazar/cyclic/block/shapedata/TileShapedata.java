package com.lothrazar.cyclic.block.shapedata;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.BuildShape;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileShapedata extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int SLOT_A = 0;
  private static final int SLOT_B = 1;
  private static final int SLOT_CARD = 2;
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  static enum Fields {
    RENDER, COMMAND;
  }

  static enum StructCommands {
    //TODO: need new packet
    READ, CLEAR, FILL, COPY, PASTE;
  }

  /**
   * packet to trigger this from custom btn
   * 
   * @param cmd
   */
  public void execute(StructCommands cmd) {
    IItemHandler inv = this.inventory.orElse(null);
    if (inv == null) {
      return;
    }
    ModCyclic.LOGGER.info("apply " + cmd);
    ItemStack shapeCard = inv.getStackInSlot(SLOT_CARD);
    if (!(shapeCard.getItem() instanceof ShapeCard)) {
      return;
    }
    ModCyclic.LOGGER.info("apply " + cmd + " to " + shapeCard.getTag());
    BlockPos invA = getTarget(SLOT_A);
    BlockPos invB = getTarget(SLOT_B);
    List<BlockPos> shape = UtilShape.rect(invA, invB);
    switch (cmd) {
      case CLEAR:
        //delete data in slotcard
        shapeCard.setTag(null);
      break;
      case READ:
        //        shape = UtilShape.filterAir(world, shape);
        BuildShape build = new BuildShape(world, shape);
        build.write(shapeCard);
      /// shape set
      break;
      case COPY:
      break;
      case PASTE:
      break;
      //      case FLAT:
      //      break;
      //      case MERGE:
      //      break;
    }
  }

  public TileShapedata() {
    super(TileRegistry.computer_shape);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(3) {

      @Override
      public int getSlotLimit(int slot) {
        return 1;
      }

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == SLOT_A || slot == SLOT_B)
          return stack.getItem() instanceof LocationGpsCard;
        else
          return stack.getItem() instanceof ShapeCard;
      }
    };
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerShapedata(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    BlockPos invA = getTarget(SLOT_A);
    BlockPos invB = getTarget(SLOT_B);
    //
    //
    //    BlockPos targetPos = new BlockPos(65, 68, -130);
    //    if (this.requiresRedstone() && !this.isPowered()) {
    //      return;
    //    }
    //    inventory.ifPresent(inv -> {
    //      ItemStack stack = inv.getStackInSlot(0);
    //      if (stack.isEmpty() || Block.getBlockFromItem(stack.getItem()) == Blocks.AIR) {
    //        return;
    //      }
    //      Direction dir = this.getBlockState().get(BlockStateProperties.FACING);
    //      BlockPos offset = pos.offset(dir);
    //      BlockState state = Block.getBlockFromItem(stack.getItem()).getDefaultState();
    //      if (world.isAirBlock(offset) &&
    //          world.setBlockState(offset, state)) {
    //        stack.shrink(1);
    //      }
    //    });
  }

  public boolean isAvailable(StructCommands shape) {
    IItemHandler inv = this.inventory.orElse(null);
    if (inv == null) {
      return false;
    }
    ItemStack stack = inv.getStackInSlot(SLOT_CARD);
    if (stack.isEmpty()) {
      return false;
    }
    if (stack.getTag() == null) {
      //cannot clear if already clear
      return shape != StructCommands.CLEAR;
    }
    return true;
  }

  public BlockPos getTarget(int s) {
    IItemHandler inv = this.inventory.orElse(null);
    if (inv == null) {
      return null;
    }
    ItemStack stackA = inv.getStackInSlot(s);
    BlockPosDim loc = LocationGpsCard.getPosition(stackA);
    return loc == null ? null : loc.getPos();
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case COMMAND:
        return 0;
      case RENDER:
        return this.render;
    }
    return super.getField(field);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case COMMAND:
        if (value >= StructCommands.values().length) {
          value = 0;
        }
        StructCommands cmd = StructCommands.values()[value];
        this.execute(cmd);
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }
}
