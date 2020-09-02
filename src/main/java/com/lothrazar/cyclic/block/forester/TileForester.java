package com.lothrazar.cyclic.block.forester;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileForester extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int MAX_HEIGHT = 32;
  private int height = MAX_HEIGHT;
  private static final int MAX_SIZE = 9;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int size = MAX_SIZE;
  IOptionalNamedTag<Block> forge_sapling = BlockTags.createOptional(new ResourceLocation("forge", "saplings"));
  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private WeakReference<FakePlayer> fakePlayer;
  private int shapeIndex = 0;
  //  public enum PlantingMode {
  //    //full is every square
  //    //spread is grid with 2 between so every three 
  //    //twos is 2x2 trees with 2 between
  //    FULL, GRID1, GRID2, LARGE;
  //  }

  //
  //  private PlantingMode mode;
  //harvest mode: do we shear or break leaves
  public enum Fields {
    REDSTONE, RENDER;
  }

  public TileForester() {
    super(TileRegistry.forester);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerForester(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    timer--;
    IEnergyStorage en = this.energy.orElse(null);
    IItemHandler inv = this.inventory.orElse(null);
    if (en == null || inv == null
        || en.getEnergyStored() < ConfigManager.FORESTERPOWER.get()) {
      return;
    }
    if (timer > 0) {
      return;
    }
    //
    List<BlockPos> shape = this.getShape();
    if (shape.size() == 0) {
      return;
    }
    updateTargetPos(shape);
    ItemStack dropMe = inv.getStackInSlot(0).copy();
    //only saplings at my level, the rest is harvesting
    if (this.isSapling(dropMe) && targetPos.getY() == this.pos.getY()) {
      try {
        if (fakePlayer == null && world instanceof ServerWorld) {
          fakePlayer = setupBeforeTrigger((ServerWorld) world, "forester");
        }
        TileEntityBase.tryEquipItem(inventory, fakePlayer, 0);
        //plant me baby 
        //        targetPos = this.pos.offset(this.getCurrentFacing());
        //loop on positions
        ActionResultType result = TileEntityBase.rightClickBlock(fakePlayer, world, targetPos);
        if (result == ActionResultType.SUCCESS) {
          //ok then DRAIN POWER
          ModCyclic.LOGGER.info("planted forester");
          en.extractEnergy(ConfigManager.FORESTERPOWER.get(), false);
        }
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("User action item error", e);
      }
    }
    //
    skipSomeAirBlocks(shape);
    if (this.isTree(dropMe)) {
      if (TileEntityBase.mineClickBlock(fakePlayer, world, targetPos)) {
        //ok then DRAIN POWER
        ModCyclic.LOGGER.info("harvest forester");
        en.extractEnergy(ConfigManager.FORESTERPOWER.get(), false);
      }
    }
  }

  private void updateTargetPos(List<BlockPos> shape) {
    shapeIndex++;
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    targetPos = shape.get(shapeIndex);
  }

  BlockPos targetPos = null;

  private void skipSomeAirBlocks(List<BlockPos> shape) {
    int skipping = MAX_HEIGHT - 2;
    int i = 0;
    while (world.isAirBlock(targetPos) && i < skipping
        && targetPos.getY() > pos.getY()) {
      updateTargetPos(shape);
      i++;
    }
  }

  //for harvest
  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = UtilShape.squareHorizontalFull(this.getCurrentFacingPos(size), size);
    shape = UtilShape.repeatShapeByHeight(shape, height);
    return shape;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(size), this.size);
    return shape;
  }

  private boolean isSapling(ItemStack dropMe) {
    //    if(dropMe.getItem().isIn(Tags.Blocks.SAND))
    //sapling tag SHOULD exist. it doesnt. idk WHY
    Block block = Block.getBlockFromItem(dropMe.getItem());
    return block.isIn(forge_sapling) ||
        block instanceof SaplingBlock;
  }

  private boolean isTree(ItemStack dropMe) {
    Block block = Block.getBlockFromItem(dropMe.getItem());
    return block.isIn(BlockTags.LOGS) ||
        block.isIn(BlockTags.LEAVES);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }
}
