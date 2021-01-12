package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.RelativeShape;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileShapedata extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int SLOT_A = 0;
  private static final int SLOT_B = 1;
  private static final int SLOT_CARD = 2;
  ItemStackHandler inventory = new ItemStackHandler(3) {

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
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private RelativeShape copiedShape;
  private int hasStashIfOne;

  static enum Fields {
    RENDER, COMMAND, STASH;
  }

  static enum StructCommands {
    READ, COPY, MERGE, PASTE;
  }

  /**
   * packet to trigger this from custom btn
   * 
   * @param cmd
   */
  public void execute(StructCommands cmd) {
    ItemStack shapeCard = inventory.getStackInSlot(SLOT_CARD);
    if (!(shapeCard.getItem() instanceof ShapeCard)) {
      return;
    }
    //  ModCyclic.LOGGER.info("apply " + cmd + " to " + shapeCard.getTag());
    RelativeShape cardShape = RelativeShape.read(shapeCard);
    switch (cmd) {
      case READ:
        BlockPos invA = getTarget(SLOT_A);
        BlockPos invB = getTarget(SLOT_B);
        if (invA != null && invB != null) {
          List<BlockPos> shape = UtilShape.rect(invA, invB);
          RelativeShape worldShape = new RelativeShape(world, shape, this.pos);
          //read from WORLD to CARD
          //only works if all three cards set
          worldShape.write(shapeCard);
          ModCyclic.LOGGER.info(cmd + " success");
          /// shape set
        }
      break;
      case COPY:
        //copy shape from CARD to BUFFER
        //only works
        this.copiedShape = new RelativeShape(cardShape);
        ModCyclic.LOGGER.info(cmd + " success");
      break;
      case PASTE:
        //from BUFFER to CARD
        //only works on EMPTY CARDS
        if (this.copiedShape != null && shapeCard.getTag() != null) {
          //
          shapeCard.setTag(null);//paste and not merge so overwrite
          this.copiedShape.write(shapeCard);
          ModCyclic.LOGGER.info(cmd + " success");
          //          this.copiedShape = null;
        }
      break;
      case MERGE:
        //from BUFFER to CARD
        //only works on NOT EMPTY cards
        if (this.copiedShape != null && cardShape.getShape().size() > 0) {
          //  
          cardShape.merge(this.copiedShape);
          cardShape.write(shapeCard);
          ModCyclic.LOGGER.info(cmd + " success");
          //          this.copiedShape = null;
        }
      break;
    }
  }

  public TileShapedata() {
    super(TileRegistry.computer_shape);
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
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("copiedShape")) {
      CompoundNBT cs = (CompoundNBT) tag.get("copiedShape");
      this.copiedShape = RelativeShape.read(cs);
    }
    hasStashIfOne = tag.getInt("stashToggle");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("stashToggle", hasStashIfOne);
    if (this.copiedShape != null) {
      CompoundNBT copiedShapeTags = this.copiedShape.write(new CompoundNBT());
      tag.put("copiedShape", copiedShapeTags);
    }
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world.isRemote == false) {
      hasStashIfOne = (this.copiedShape == null) ? 0 : 1;
    }
  }

  /**
   * is command available for use
   * 
   * @param shape
   * @return
   */
  public boolean isAvailable(StructCommands shape) {
    ItemStack stack = inventory.getStackInSlot(SLOT_CARD);
    if (stack.isEmpty()) {
      return false;
    }
    boolean cardEmpty = stack.getTag() == null
        || !stack.getTag().getBoolean(ShapeCard.VALID_SHAPE);
    BlockPos invA = getTarget(SLOT_A);
    BlockPos invB = getTarget(SLOT_B);
    boolean hasTargets = invA != null && invB != null;
    switch (shape) {
      case COPY:
        //dont check stash, copy always overwrites. just need a red card
        return !cardEmpty;
      case MERGE:
        //both card and stash have valid shapes to merge
        return !cardEmpty && this.hasStashIfOne == 1;
      case PASTE:
        //pasting my shape into an empty card
        return cardEmpty && this.hasStashIfOne == 1;
      case READ:
        return cardEmpty && hasTargets;
    }
    return true;
  }

  public BlockPos getTarget(int s) {
    ItemStack stackA = inventory.getStackInSlot(s);
    BlockPosDim loc = LocationGpsCard.getPosition(stackA);
    return loc == null ? null : loc.getPos();
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case COMMAND:
        return 0;//unused 
      case RENDER:
        return this.render;
      case STASH:
        return hasStashIfOne;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case STASH:
        hasStashIfOne = value;
      break;
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
