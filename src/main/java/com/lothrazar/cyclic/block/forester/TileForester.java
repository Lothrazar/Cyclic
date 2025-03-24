package com.lothrazar.cyclic.block.forester;

import java.lang.ref.WeakReference;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.CustomEnergyStorage;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileForester extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT;
  }

  static final int MAX = 64000;
  static final int MAX_HEIGHT = 32;
  static final int MAX_SIZE = 12; //radius 7 translates to 15x15 area (center block + 7 each side)
  public static ModConfigSpec.IntValue POWERCONF;
  private int height = MAX_HEIGHT;
  private int radius = MAX_SIZE;
  private BlockPos targetPos = null;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return isSapling(stack);
    }
  };
  private WeakReference<FakePlayer> fakePlayer;
  private int shapeIndex = 0;

  public TileForester(BlockPos pos, BlockState state) {
    super(TileRegistry.FORESTER.get(), pos, state);
    this.needsRedstone = 1;
    this.render = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileForester e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileForester e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (this.level.isClientSide) {
      return;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost) {
      if (cost > 0) {
        return;
      }
    }
    //
    List<BlockPos> shape = this.getShape();
    if (shape.size() == 0) {
      return;
    }
    //update target
    shapeIndex++;
    targetPos = getShapeTarget(shape);
    //only saplings at my level, the rest is harvesting
    try {
      if (fakePlayer == null && level instanceof ServerLevel) {
        fakePlayer = setupBeforeTrigger((ServerLevel) level, "forester");
      }
      this.equipTool();
      ItemStack dropMe = inventory.getStackInSlot(0).copy();
      if (this.isTree(targetPos)) {
        if (TileBlockEntityCyclic.tryHarvestBlock(fakePlayer, level, targetPos)) {
          //ok then DRAIN POWER
          energy.extractEnergy(cost, false);
        }
      }
      else if (this.isSapling(dropMe)) {
        //plant me  . if im on the lowest level  
        InteractionResult result = TileBlockEntityCyclic.interactUseOnBlock(fakePlayer, level, targetPos, InteractionHand.OFF_HAND, Direction.DOWN);
        if (result == InteractionResult.CONSUME) {
          updateComparatorOutputLevel();
        }
        //ok then DRAIN POWER
        energy.extractEnergy(cost, false);
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Forester action item error", e);
    }
  }

//  @Override
//  public AABB getRenderBoundingBox() {
//    return BlockEntity.INFINITE_EXTENT_AABB;
//  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.FORESTER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {

    return new ContainerForester(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    height = tag.getInt("height");
    shapeIndex = tag.getInt("shapeIndex");
    radius = tag.getInt("radius");
    energy.deserializeNBT(registries,tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("height", height);
    tag.putInt("shapeIndex", shapeIndex);
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.putInt("radius", radius);
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag);
  }

  /**
   * off-hand for saplings; main-hand for mining tool
   */
  private void equipTool() {
    if (fakePlayer == null) {
      return;
    }
    TileBlockEntityCyclic.tryEquipItem(inventory, fakePlayer, 0, InteractionHand.OFF_HAND);
    if (fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
      ItemStack tool = new ItemStack(Items.DIAMOND_AXE);
      tool.enchant(Enchantments.FORTUNE, 3);
      TileBlockEntityCyclic.tryEquipItem(tool, fakePlayer, InteractionHand.MAIN_HAND);
    }
  }

  private BlockPos getShapeTarget(List<BlockPos> shape) {
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    return shape.get(shapeIndex);
  }

  private int heightWithDirection() {
    Direction blockFacing = this.getBlockState().getValue(BlockStateProperties.FACING);
    int diff = 1;//directionIsUp ? 1 : -1;
    if (blockFacing.getAxis().isVertical()) {
      diff = (blockFacing == Direction.UP) ? 1 : -1;
    }
    return diff * height;
  }

  //for harvest
  public List<BlockPos> getShape() {
    //    List<BlockPos>    shape = ShapeUtil.cubeSquareBase(this.getCurrentFacingPos(radius + 1), radius, height);
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

  private boolean isSapling(ItemStack dropMe) {
    //sapling tag SHOULD exist. it doesnt. idk WHY
    BlockState block = Block.byItem(dropMe.getItem()).defaultBlockState();
    return block.is(BlockTags.SAPLINGS) || block.getBlock() instanceof SaplingBlock;
  }

  private boolean isTree(BlockPos targetPos) {
    if (targetPos == null) {
      return false;
    }
    BlockState block = level.getBlockState(targetPos);
    return block.is(BlockTags.LOGS) ||
        block.is(BlockTags.LEAVES);
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
      case HEIGHT:
        this.height = Math.min(value, MAX_HEIGHT);
      break;
    }
  }

  public boolean hasSapling() {
    return !this.inventory.getStackInSlot(0).isEmpty();
  }
}
