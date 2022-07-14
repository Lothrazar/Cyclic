package com.lothrazar.cyclic.block.forester;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileForester extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE;
  }

  static final int MAX = 64000;
  static final int MAX_HEIGHT = 32;
  private static final int MAX_SIZE = 12; //radius 7 translates to 15x15 area (center block + 7 each side)
  public static IntValue POWERCONF;
  private int height = MAX_HEIGHT;
  private int radius = MAX_SIZE;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return isSapling(stack);
    }
  };
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
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
    BlockPos targetPos = getShapeTarget(shape);
    skipSomeAirBlocks(shape);
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
        if (targetPos.getY() == this.worldPosition.getY()) {
          InteractionResult result = TileBlockEntityCyclic.interactUseOnBlock(fakePlayer, level, targetPos, InteractionHand.OFF_HAND, Direction.DOWN);
          if (result == InteractionResult.CONSUME) {
            //ok then DRAIN POWER
            energy.extractEnergy(cost, false);
          }
        }
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Forester action item error", e);
    }
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerForester(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    shapeIndex = tag.getInt("shapeIndex");
    radius = tag.getInt("radius");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("shapeIndex", shapeIndex);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.putInt("radius", radius);
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  /**
   * off-hand for saplings; main-hand for mining tool
   */
  private void equipTool() {
    if (fakePlayer == null) {
      return;
    }
    TileBlockEntityCyclic.tryEquipItem(inventoryCap, fakePlayer, 0, InteractionHand.OFF_HAND);
    if (fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
      ItemStack tool = new ItemStack(Items.DIAMOND_AXE);
      tool.enchant(Enchantments.BLOCK_FORTUNE, 3);
      TileBlockEntityCyclic.tryEquipItem(tool, fakePlayer, InteractionHand.MAIN_HAND);
    }
  }

  private void skipSomeAirBlocks(List<BlockPos> shape) {
    //    int skipping = MAX_HEIGHT - 2;
    //    int i = 0;
    //    while (world.isAirBlock(targetPos) && i < skipping
    //        && targetPos.getY() > pos.getY()) {
    //      updateTargetPos(shape);
    //      i++;
    //    }
  }

  private BlockPos getShapeTarget(List<BlockPos> shape) {
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    return shape.get(shapeIndex);
  }

  //for harvest
  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = ShapeUtil.cubeSquareBase(this.getCurrentFacingPos(radius + 1), radius, height);
    return shape;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    List<BlockPos> shape = ShapeUtil.squareHorizontalHollow(this.getCurrentFacingPos(radius + 1), this.radius);
    BlockPos targetPos = getShapeTarget(shape);
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

  private boolean isSapling(ItemStack dropMe) {
    //    if(dropMe.getItem().isIn(Tags.Blocks.SAND))
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
        radius = value % MAX_SIZE;
      break;
    }
  }
}
