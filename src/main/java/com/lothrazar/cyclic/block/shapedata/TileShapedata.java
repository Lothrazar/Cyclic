package com.lothrazar.cyclic.block.shapedata;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.library.data.RelativeShape;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileShapedata extends TileBlockEntityCyclic implements MenuProvider {

  private static final int SLOT_A = 0;
  private static final int SLOT_B = 1;
  private static final int SLOT_CARD = 2;
  ItemStackHandler inventory = new ItemStackHandler(3) {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == SLOT_A || slot == SLOT_B) {
        return stack.getItem() instanceof LocationGpsCard;
      }
      else {
        return stack.getItem() instanceof ShapeCard;
      }
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
    //ModCyclic.LOGGER.info("apply " + cmd + " to " + shapeCard.getTag());
    RelativeShape cardShape = RelativeShape.read(shapeCard);
    switch (cmd) {
      case READ:
        BlockPos invA = getTarget(SLOT_A);
        BlockPos invB = getTarget(SLOT_B);
        if (invA != null && invB != null) {
          List<BlockPos> shape = ShapeUtil.rect(invA, invB);
          RelativeShape worldShape = new RelativeShape(level, shape, this.worldPosition);
          //read from WORLD to CARD
          //only works if all three cards set
          worldShape.write(shapeCard);
          /// shape set
        }
      break;
      case COPY:
        //copy shape from CARD to BUFFER
        //only works
        this.copiedShape = new RelativeShape(cardShape);
      break;
      case PASTE:
        //from BUFFER to CARD
        //only works on EMPTY CARDS
        if (this.copiedShape != null && shapeCard.getTag() != null) {
          //
          shapeCard.setTag(null); //paste and not merge so overwrite
          this.copiedShape.write(shapeCard);
          ModCyclic.LOGGER.info(cmd + " success");
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

  public TileShapedata(BlockPos pos, BlockState state) {
    super(TileRegistry.COMPUTER_SHAPE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileShapedata e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileShapedata e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.COMPUTER_SHAPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerShapedata(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("copiedShape")) {
      CompoundTag cs = (CompoundTag) tag.get("copiedShape");
      this.copiedShape = RelativeShape.read(cs);
    }
    hasStashIfOne = tag.getInt("stashToggle");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("stashToggle", hasStashIfOne);
    if (this.copiedShape != null) {
      CompoundTag copiedShapeTags = this.copiedShape.write(new CompoundTag());
      tag.put("copiedShape", copiedShapeTags);
    }
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  //  @Override
  public void tick() {
    if (level.isClientSide == false) {
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
        || !stack.getTag().getBoolean(RelativeShape.VALID_SHAPE);
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
        return 0; //unused 
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
        this.render = value % PreviewOutlineType.values().length;
      break;
    }
  }
}
