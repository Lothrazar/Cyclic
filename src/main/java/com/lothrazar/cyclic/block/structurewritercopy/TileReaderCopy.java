package com.lothrazar.cyclic.block.structurewritercopy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.LocationGpsItem;
import com.lothrazar.cyclic.item.StructureDiskItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
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
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileReaderCopy extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int SLOT_GPSSTART = 0;
  private static final int SLOT_GPSEND = 1;
  private static final int SLOT_RESULT = 2;
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private String name = "testschematic";

  public TileReaderCopy() {
    super(BlockRegistry.Tiles.structure_copy);
  }

  private IItemHandler createHandler() {
    //3 for the 2 gps and the 1 OUTPUT
    //then the rest for INVOOOO
    return new ItemStackHandler(3 + 18) {//WTF +1 hack

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == SLOT_GPSSTART || slot == SLOT_GPSEND)
          return stack.getItem() instanceof LocationGpsItem;
        return true;
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
    return new ContainerReaderCopy(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(tag);
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
          ModCyclic.LOGGER.info("about to save , FIRST gather and delete all materials?");
          ResourceLocation saved = this.saveCopy(targetPos.getPos(), endPos.getPos());
          ModCyclic.LOGGER.info("saved? = " + saved);
          //TOOD: the namecard 
          if (saved != null) {
            ItemStack newDisk = new ItemStack(ItemRegistry.structure_disk);
            StructureDiskItem.saveDisk(newDisk, saved);
            //ok go
            inv.insertItem(SLOT_RESULT, newDisk, false);
          }
        }
      });
    }
  }

  private ResourceLocation saveCopy(final BlockPos targetPos, final BlockPos endPos) {
    ServerWorld serverworld = (ServerWorld) this.world;
    TemplateManager templatemanager = serverworld.getStructureTemplateManager();
    Template template;
    try {
      ResourceLocation nameResource = ResourceLocation.tryCreate(this.name);
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
      ModCyclic.LOGGER.info("SIZE = " + size);
      ModCyclic.LOGGER.info("targetPos = " + targetPos);
      template.takeBlocksFromWorld(this.world,
          start, size, false, Blocks.STRUCTURE_VOID);
      if (this.detectEnoughMaterials(start, end, template) == false) {
        ModCyclic.LOGGER.info("NOT ENOUGH STUFF = ");
        return null;
      }
      template.setAuthor(ModCyclic.MODID);
      //and go 
      if (templatemanager.writeToFile(nameResource)) {
        return nameResource;//return if it saved
      }
    }
    catch (ResourceLocationException var8) {
      ModCyclic.LOGGER.error("schematic", var8);
    }
    return null;
  }

  private boolean detectEnoughMaterials(BlockPos start, BlockPos end, Template template) {
    Map<Block, Integer> blRequired = new HashMap<>();
    Map<Fluid, Integer> fluidsNeeded = new HashMap<>();
    for (List<BlockInfo> b : template.blocks) {
      for (BlockInfo bl : b) {
        if (bl.state.getFluidState() != null
            && bl.state.getFluidState().isSource()) {
          IFluidState fluidHere = bl.state.getFluidState();
          Fluid fluidUnit = fluidHere.getFluid();
          if (fluidsNeeded.containsKey(fluidUnit)) {
            fluidsNeeded.put(fluidUnit, fluidsNeeded.get(fluidUnit) + 1);
          }
          else {//first time
            fluidsNeeded.put(fluidUnit, 1);
          }
        }
        Block blockHere = bl.state.getBlock();
        if (blRequired.containsKey(blockHere)) {
          blRequired.put(blockHere, blRequired.get(blockHere) + 1);
        }
        else {//first time
          blRequired.put(blockHere, 1);
        }
      }
    }
    ModCyclic.LOGGER.info("TEMPLATE VERSION unique blocks required " + blRequired.size());
    ModCyclic.LOGGER.info("TEMPLATE VERSION fluidsNeeded required " + fluidsNeeded.size());
    //loop
    blRequired = new HashMap<>();
    fluidsNeeded = new HashMap<>();
    BlockState current;
    for (int x = start.getX(); x <= end.getX(); x++)
      for (int y = start.getX(); y <= end.getX(); y++)
        for (int z = start.getX(); z <= end.getX(); z++) {
          BlockPos p = new BlockPos(x, y, z);
          current = world.getBlockState(p);
          Block blockHere = current.getBlock();
          //is it fluid?
          IFluidState fluidHere = current.getFluidState();
          if (fluidHere != null && fluidHere.isSource()) {
            ModCyclic.LOGGER.info("fluid check" + fluidHere);
            Fluid fluidUnit = fluidHere.getFluid();
            if (fluidsNeeded.containsKey(fluidUnit)) {
              fluidsNeeded.put(fluidUnit, fluidsNeeded.get(fluidUnit) + 1);
            }
            else {//first time
              fluidsNeeded.put(fluidUnit, 1);
            }
          }
          else if (blockHere != Blocks.AIR) {
            //solid block
            if (blRequired.containsKey(blockHere)) {
              blRequired.put(blockHere, blRequired.get(blockHere) + 1);
            }
            else {//first time
              blRequired.put(blockHere, 1);
            }
          }
        }
    ModCyclic.LOGGER.info("unique blocks required " + blRequired.size());
    ModCyclic.LOGGER.info("fluidsNeeded required " + fluidsNeeded.size());
    return false;
  }

  @Override
  public void setField(int field, int value) {}
}
