package com.lothrazar.cyclic.block.harvester;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.HarvestUtil;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TileHarvester extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public static final int MAX_SIZE = 12;
  static final int MAX_ENERGY = 640000;
  public static final int MAX_HEIGHT = 16;
  public static ModConfigSpec.IntValue POWERCONF;
  private int radius = MAX_SIZE / 2;
  private int shapeIndex = 0;
  BlockPos targetPos = null;
  private int height = 1;
  private boolean directionIsUp = false;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX_ENERGY, MAX_ENERGY / 4);

  public TileHarvester(BlockPos pos, BlockState state) {
    super(TileRegistry.HARVESTER.get(), pos, state);
    timer = 1;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileHarvester e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileHarvester e) {
    e.tick();
  }

  public void tick() {
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
    if (this.level.isClientSide) {
      return;
    }
    //get and update target
    List<BlockPos> shape = this.getShape();
    targetPos = getShapeTarget(shape);
    shapeIndex++;
    //does it exist
    if (targetPos != null && HarvestUtil.tryHarvestSingle(this.level, targetPos)) {
      //energy is per action
      energy.extractEnergy(cost, false);
    }
  }

  private BlockPos getShapeTarget(List<BlockPos> shape) {
    if (shape.size() == 0) {
      return null;
    }
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    return shape.get(shapeIndex);
  }

  private int heightWithDirection() {
    Direction blockFacing = this.getBlockState().getValue(BlockStateProperties.FACING);
    int diff = directionIsUp ? 1 : -1;
    if (blockFacing.getAxis().isVertical()) {
      diff = (blockFacing == Direction.UP) ? 1 : -1;
    }
    return diff * height;
  }

  //for harvest
  public List<BlockPos> getShape() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = ShapeUtil.cubeSquareBase(center, radius, 0);
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = ShapeUtil.repeatShapeByHeight(shape, heightWithDirection);
    }
    return shape;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = ShapeUtil.squareHorizontalHollow(center, radius);
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = ShapeUtil.repeatShapeByHeight(shape, heightWithDirection);
    }
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

//  @Override
//  public AABB getRenderBoundingBox() {
//    return BlockEntity.INFINITE_EXTENT_AABB;
//  }

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
        this.render = value % PreviewOutlineType.values().length;
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
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    radius = tag.getInt("radius");
    height = tag.getInt("height");
    directionIsUp = tag.getBoolean("directionIsUp");
    shapeIndex = tag.getInt("shapeIndex");
    energy.deserializeNBT(registries,tag.getCompound(NBTENERGY));
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("radius", radius);
    tag.putInt("shapeIndex", shapeIndex);
    tag.putInt("height", height);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.HARVESTER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerHarvester(i, level, worldPosition, playerInventory, playerEntity);
  }
}
