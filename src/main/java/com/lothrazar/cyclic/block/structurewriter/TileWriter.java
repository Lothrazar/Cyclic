package com.lothrazar.cyclic.block.structurewriter;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.LocationGpsItem;
import com.lothrazar.cyclic.item.StructureDiskItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWriter extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int SLOT_DISK = 0;
  private static final int SLOT_GPS = 1;
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  public static enum Fields {
    REDSTONE, STATUS, MODE;
  }

  /**
   * NONE : no item disk
   * 
   * INVALID : disk item but schematic not found
   * 
   * VALID : found and can build (show ingredients)
   */
  enum StructureStatus {
    NONE, INVALID, VALID;
  }

  enum StructureMode {
    PREVIEW, BUILD;
  }

  private int shapeIndex = 0;// current index of shape array
  StructureStatus structStatus = StructureStatus.NONE;
  StructureMode mode = StructureMode.PREVIEW;
  private Mirror mirror = Mirror.NONE;
  private Rotation rotation = Rotation.NONE;

  public TileWriter() {
    super(BlockRegistry.Tiles.structure_writer);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(2) {//1 gps and one disk

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == SLOT_DISK)
          return stack.getItem() instanceof StructureDiskItem;
        return stack.getItem() instanceof LocationGpsItem;
      }

      @Override
      public int getSlotLimit(int slot) {
        return 1;
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
    return new ContainerWriter(i, world, pos, playerInventory, playerEntity);
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
    shapeIndex = tag.getInt("shapeIndex");
    structStatus = StructureStatus.values()[tag.getInt("struct_status")];
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("shapeIndex", shapeIndex);
    tag.putInt("struct_status", structStatus.ordinal());
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    inventory.ifPresent(inv -> {
      ItemStack disk = inv.getStackInSlot(SLOT_DISK);
      ItemStack gps = inv.getStackInSlot(SLOT_GPS);
      BlockPosDim posTarget = LocationGpsItem.getPosition(gps);
      if (!disk.isEmpty() && disk.getItem() instanceof StructureDiskItem
          && posTarget != null) {
        this.structStatus = StructureStatus.INVALID;
        ResourceLocation structureLoc = StructureDiskItem.readDisk(disk);
        if (structureLoc != null) {
          this.structStatus = StructureStatus.VALID;
          ModCyclic.LOGGER.info("found " + structureLoc);
          if (!this.world.isRemote
              && this.mode == StructureMode.BUILD
              && this.build(structureLoc, disk, posTarget.getPos())) {
            //clear disk NBT 
            StructureDiskItem.deleteDisk(disk);
            // and refresh it
            inv.extractItem(SLOT_DISK, 64, false);
            inv.insertItem(SLOT_DISK, disk, false);
          }
          //else either build failed or its in preview mode
        }
      }
      else {
        this.structStatus = StructureStatus.NONE;
      }
    });
    //
  }

  private boolean build(ResourceLocation location, ItemStack disk, BlockPos blockPos) {
    ServerWorld serverworld = (ServerWorld) this.world;
    TemplateManager templatemanager = serverworld.getStructureTemplateManager();
    Template template = templatemanager.getTemplate(location);
    PlacementSettings placementsettings = (new PlacementSettings())
        .setMirror(this.mirror)
        .setRotation(this.rotation)
        .setIgnoreEntities(true)
        .setChunk((ChunkPos) null);
    boolean DOBUILD = true;
    if (DOBUILD) {//if true this does do the build
      ModCyclic.LOGGER.info("Build starting at " + blockPos);
      //addBlocksToWorldChunk 
      //      template.func_237149_a_(this.world, blockPos, placementsettings, world.rand);
      return true;
    }
    return false;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
      case STATUS:
        structStatus = StructureStatus.values()[value];
      break;
      case MODE:
        mode = StructureMode.values()[value];
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.getNeedsRedstone();
      case STATUS:
        return this.structStatus.ordinal();
      case MODE:
        return this.mode.ordinal();
    }
    return 0;
  }

  public List<BlockPos> getShape() {
    return null;
  }
}
