package com.lothrazar.cyclic.block.structurereadercreative;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.LocationGpsItem;
import com.lothrazar.cyclic.item.StructureDiskItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileReader extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int SLOT_GPSSTART = 0;
  private static final int SLOT_GPSEND = 1;
  private static final int SLOT_RESULT = 2;
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private String schematicName = "creativeschematic";

  public TileReader() {
    super(BlockRegistry.Tiles.structure_reader);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(3) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == SLOT_GPSSTART || slot == SLOT_GPSEND)
          return stack.getItem() instanceof LocationGpsItem;
        return true;
      }
    };
  }

  @Override
  public void setFieldString(int field, String value) {
    ModCyclic.LOGGER.info("TE save " + value);
    this.schematicName = value;// only field
  }

  @Override
  public String getFieldString(int field) {
    return this.schematicName;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerReader(i, world, pos, playerInventory, playerEntity);
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
    tag.putString("schematicName", schematicName);
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    this.schematicName = tag.getString("schematicName");
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (!this.world.isRemote) {
      inventory.ifPresent(inv -> {
        ItemStack stack = inv.getStackInSlot(SLOT_GPSSTART);
        BlockPosDim targetPos = LocationGpsItem.getPosition(stack);
        stack = inv.getStackInSlot(SLOT_GPSEND);
        BlockPosDim endPos = LocationGpsItem.getPosition(stack);
        ItemStack resultStack = inv.getStackInSlot(SLOT_RESULT);
        //        ModCyclic.LOGGER.info("check result before go" + resultStack.isEmpty());
        if (targetPos != null && endPos != null &&
            resultStack.isEmpty()) {
          ResourceLocation saved = this.saveCut(targetPos.getPos(), endPos.getPos());
          ModCyclic.LOGGER.info("copy saved? = " + saved);
          //TOOD: the namecard 
          ItemStack newDisk = new ItemStack(ItemRegistry.structure_disk);
          StructureDiskItem.saveDisk(newDisk, saved);
          //ok go
          inv.insertItem(SLOT_RESULT, newDisk, false);
        }
      });
    }
  }

  private ResourceLocation saveCut(final BlockPos targetPos, final BlockPos endPos) {
    ServerWorld serverworld = (ServerWorld) this.world;
    TemplateManager templatemanager = serverworld.getStructureTemplateManager();
    Template template;
    try {
      ResourceLocation nameResource = ResourceLocation.tryCreate(this.schematicName);
      template = templatemanager.getTemplateDefaulted(nameResource);
      int xSize = Math.abs(targetPos.getX() - endPos.getX());
      int ySize = Math.abs(targetPos.getY() - endPos.getY());
      int zSize = Math.abs(targetPos.getZ() - endPos.getZ());
      //
      BlockPos size = new BlockPos(xSize, ySize, zSize);
      BlockPos start = new BlockPos(
          Math.min(targetPos.getX(), endPos.getX()),
          Math.min(targetPos.getY(), endPos.getY()),
          Math.min(targetPos.getZ(), endPos.getZ()));
      BlockPos end = new BlockPos(
          Math.max(targetPos.getX(), endPos.getX()),
          Math.max(targetPos.getY(), endPos.getY()),
          Math.max(targetPos.getZ(), endPos.getZ()));
      template.takeBlocksFromWorld(this.world,
          start, size, false, Blocks.STRUCTURE_VOID);
      template.setAuthor(ModCyclic.MODID);
      //and go 
      if (templatemanager.writeToFile(nameResource)) {
        ModCyclic.LOGGER.info("copied schematic for FREEE");
        //        this.deleteAll(start, end);
        return nameResource;//return if it saved
      }
    }
    catch (ResourceLocationException var8) {
      ModCyclic.LOGGER.error("schematic", var8);
    }
    return null;
  }
  //
  //  private void deleteAll(BlockPos start, BlockPos end) {
  //    for (int x = start.getX(); x <= end.getX(); x++)
  //      for (int y = start.getY(); y <= end.getY(); y++)
  //        for (int z = start.getZ(); z <= end.getZ(); z++)
  //          this.setToAir(x, y, z);
  //  }
  //
  //  private void setToAir(int x, int y, int z) {
  //    world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
  //  }

  @Override
  public void setField(int field, int value) {}
}
