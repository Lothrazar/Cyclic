package com.lothrazar.cyclic.block.harvester;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilShape;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileHarvester extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final INamedTag<Block> HARVEST_BREAK = BlockTags.makeWrapperTag(new ResourceLocation(ModCyclic.MODID, "harvester_break").toString());
  private static final int RADIUS = 9;
  private static final int ATTEMPTS_PERTICK = 16;
  static final int MAX = 640000;

  public static enum Fields {
    REDSTONE;
  }

  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  BlockPos laserTarget;
  int laserTimer;

  public TileHarvester() {
    super(TileRegistry.harvesterTile);
  }
  //  @Override
  //  public boolean hasFastRenderer() {
  //    return true;
  //  }

  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(this.getPos(), RADIUS);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (this.laserTimer > 0) {
      laserTimer--;
    }
    IEnergyStorage cap = this.energy.orElse(null);
    if (cap == null) {
      return;
    }
    for (int i = 0; i < ATTEMPTS_PERTICK; i++) {
      BlockPos target = UtilWorld.getRandomPos(world.rand, getPos(), RADIUS);
      if (cap.getEnergyStored() < ConfigManager.HARVESTERPOWER.get()) {
        break;//too broke
      }
      if (this.tryHarvestSingle(target)) {
        cap.extractEnergy(ConfigManager.HARVESTERPOWER.get(), false);
        break;
      }
    }
  }

  private boolean tryHarvestSingle(BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    if (TileHarvester.simpleBreakDrop(blockState)) {
      UtilItemStack.drop(world, posCurrent, blockState.getBlock());
      world.destroyBlock(posCurrent, false);
      return true;
    }
    IntegerProperty propInt = TileHarvester.getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerWorld)) {
      return false;
    }
    int currentAge = blockState.get(propInt);
    int minAge = Collections.min(propInt.getAllowedValues());
    int maxAge = Collections.max(propInt.getAllowedValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return false;
    }
    //what does the non-grown state drop
    List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, posCurrent, (TileEntity) null);
    List<ItemStack> seeds = Block.getDrops(blockState.getBlock().getDefaultState(),
        (ServerWorld) world, posCurrent, (TileEntity) null);
    boolean deleteSeed = drops.size() > 0;
    ItemStack seed = ItemStack.EMPTY;
    if (seeds != null && seeds.size() > 0) {
      seed = seeds.get(0);
    }
    //  if it dropped more than one ( seed and a thing)
    for (Iterator<ItemStack> iterator = drops.iterator(); iterator.hasNext();) {
      final ItemStack drop = iterator.next();
      if (deleteSeed && drop.getItem() == seed.getItem()
          && drops.size() > 1) {
        // Remove exactly one seed (consume for replanting)
        drop.shrink(1);
        deleteSeed = false;
      } //else dont remove a seed if theres only 1 to start with
      if (drop.getCount() > 0) {
        UtilWorld.dropItemStackInWorld(world, posCurrent, drop);
      }
    }
    world.setBlockState(posCurrent, blockState.with(propInt, minAge));
    laserTarget = posCurrent;
    laserTimer = 15;
    world.notifyBlockUpdate(posCurrent, getBlockState(), getBlockState(), 3);
    UtilWorld.flagUpdate(world, pos, this.getBlockState(), this.getBlockState());
    this.markDirty();
    return true;
  }

  private static boolean simpleBreakDrop(BlockState blockState) {
    boolean breakit = blockState.getBlock().isIn(HARVEST_BREAK);
    // the list tells all
    return breakit;
  }

  public static IntegerProperty getAgeProp(BlockState blockState) {
    for (Property<?> p : blockState.getProperties()) {
      if (p != null && p.getName() != null
          && p instanceof IntegerProperty &&
          p.getName().equalsIgnoreCase("age")) {
        return (IntegerProperty) p;
      }
    }
    return null;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        setNeedsRedstone(value);
      break;
    }
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX / 4);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    this.laserTarget = UtilNBT.getBlockPos(tag);
    laserTimer = tag.getInt("lt");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    if (laserTarget == null) {
      laserTarget = BlockPos.ZERO;
    }
    UtilNBT.putBlockPos(tag, laserTarget);
    tag.putInt("lt", laserTimer);
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerHarvester(i, world, pos, playerInventory, playerEntity);
  }
}
