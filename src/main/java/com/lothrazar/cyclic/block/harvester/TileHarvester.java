package com.lothrazar.cyclic.block.harvester;

import com.google.common.collect.Sets;
import com.lothrazar.cyclic.api.IHarvesterOverride;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileHarvester extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public static final Set<IHarvesterOverride> HARVEST_OVERRIDES = Sets.newIdentityHashSet();
  public static final int MAX_SIZE = 12;
  static final int MAX_ENERGY = 640000;
  static final int MAX_HEIGHT = 16;
  public static IntValue POWERCONF;
  private int radius = MAX_SIZE;
  private int shapeIndex = 0;
  private int height = 1;
  private boolean directionIsUp = false;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX_ENERGY, MAX_ENERGY / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  public TileHarvester() {
    super(TileRegistry.HARVESTER);
    timer = 1;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    //get and update target
    BlockPos targetPos = getShapeTarget();
    shapeIndex++;
    //does it exist
    if (targetPos != null && tryHarvestSingle(this.world, targetPos)) {
      //energy is per action
      energy.extractEnergy(cost, false);
    }
  }

  private BlockPos getShapeTarget() {
    List<BlockPos> shape = this.getShape();
    if (shape.size() == 0) {
      return null;
    }
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    return shape.get(shapeIndex);
  }

  //for harvest
  public List<BlockPos> getShape() {
    List<BlockPos> shape = UtilShape.cubeSquareBase(this.getCurrentFacingPos(radius + 1), radius, 0);
    int diff = directionIsUp ? 1 : -1;
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    List<BlockPos> shape = UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(radius + 1), radius);
    int diff = directionIsUp ? 1 : -1;
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  public static boolean tryHarvestSingle(World world, BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    // Try running override logic
    IHarvesterOverride applicable = null;
    for (IHarvesterOverride override : HARVEST_OVERRIDES) {
      if (override.appliesTo(blockState, world, posCurrent)) {
        applicable = override;
        break;
      }
    }
    if (applicable != null) {
      return applicable.attemptHarvest(blockState, world, posCurrent, stack -> UtilItemStack.drop(world, posCurrent, blockState.getBlock()));
    }
    // Fall back to default logic
    if (TileHarvester.simpleBreakDrop(blockState)) {
      UtilItemStack.drop(world, posCurrent, blockState.getBlock());
      world.destroyBlock(posCurrent, false);
      return true;
    }
    //don't break stems see Issue #1601
    if (world.getBlockState(posCurrent).getBlock() instanceof StemBlock) {
      return false;
    }
    //growable crops have an age block property
    IntegerProperty propInt = TileHarvester.getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerWorld)) {
      return false;
    }
    final int currentAge = blockState.get(propInt);
    final int minAge = Collections.min(propInt.getAllowedValues());
    final int maxAge = Collections.max(propInt.getAllowedValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return false;
    }
    //we have an age property, so harvest now and run drops
    //update behavior to address Issue #1600
    //
    Item seed = null;
    if (blockState.getBlock() instanceof CropsBlock) {
      CropsBlock crop = (CropsBlock) blockState.getBlock();
      ItemStack defaultSeedDrop = crop.getItem(world, posCurrent, blockState);
      if (!defaultSeedDrop.isEmpty()) {
        seed = defaultSeedDrop.getItem();
      }
    }
    List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, posCurrent, null);
    for (ItemStack dropStack : drops) {
      if (seed != null && dropStack.getItem() == seed) {
        //we found the seed. steal one for replant
        dropStack.shrink(1);
        seed = null;
      }
      //drop the rest
      UtilWorld.dropItemStackInWorld(world, posCurrent, dropStack);
    }
    if (world instanceof ServerWorld) {
      blockState.spawnAdditionalDrops((ServerWorld) world, posCurrent, ItemStack.EMPTY);
    }
    //now update age to zero after harvest
    BlockState newState = blockState.with(propInt, minAge);
    boolean updated = world.setBlockState(posCurrent, newState);
    return updated || drops.size() > 0;
  }

  private static boolean simpleBreakDrop(BlockState blockState) {
    // the list tells all
    return blockState.isIn(DataTags.VINES) || blockState.isIn(DataTags.CROP_BLOCKS);
  }

  public static IntegerProperty getAgeProp(BlockState blockState) {
    if (blockState.getBlock() instanceof CropsBlock) {
      CropsBlock crops = (CropsBlock) blockState.getBlock();
      //better mod compatibility if they dont use 'age'
      return crops.getAgeProperty();
    }
    String age = CropsBlock.AGE.getName();
    ResourceLocation bid = blockState.getBlock().getRegistryName();
    if (CompatConstants.RESYNTH.equalsIgnoreCase(bid.getNamespace())) {
      //some silly old mods dont use age for compatibility
      // https://github.com/Resynth-Minecraft-Mod/Resynth-Mod/blob/a9f47439d103c1c17ca7a4ffd05c2dc0397e5e5f/src/main/java/com/ki11erwolf/resynth/plant/block/BlockBiochemicalPlant.java#L59
      //so we hack it
      age = CompatConstants.RESYNTH_GROWTH_STAGE;
    }
    for (Property<?> p : blockState.getProperties()) {
      if (p != null && p.getName() != null
          && p instanceof IntegerProperty &&
          p.getName().equalsIgnoreCase(age)) {
        return (IntegerProperty) p;
      }
    }
    //IGrowable is useless here, i tried. no way to tell if its fully grown, or what age/stage its in
    return null;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case SIZE:
        return radius;
      case HEIGHT:
        return height;
      case DIRECTION:
        return directionIsUp ? 1 : 0;
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
      case SIZE:
        radius = Math.min(value, MAX_SIZE);
      break;
      case DIRECTION:
        this.directionIsUp = value == 1;
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    radius = tag.getInt("radius");
    height = tag.getInt("height");
    directionIsUp = tag.getBoolean("directionIsUp");
    shapeIndex = tag.getInt("shapeIndex");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("radius", radius);
    tag.putInt("shapeIndex", shapeIndex);
    tag.putInt("height", height);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTENERGY, energy.serializeNBT());
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerHarvester(i, world, pos, playerInventory, playerEntity);
  }
}
