package com.lothrazar.cyclic.block.forester;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileForester extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int MAX_HEIGHT = 32;
  public static IntValue POWERCONF;
  private int height = MAX_HEIGHT;
  private static final int MAX_SIZE = 11;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int radius = MAX_SIZE;
  static final int MAX = 64000;
  //
  //
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return isSapling(stack);
    }
  };
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
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
  static enum Fields {
    REDSTONE, RENDER, SIZE;
  }

  public TileForester() {
    super(TileRegistry.forester);
    this.needsRedstone = 1;
    this.render = 0;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (this.world.isRemote) {
      return;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost) {
      if (cost > 0)
        return;
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
      if (fakePlayer == null && world instanceof ServerWorld) {
        fakePlayer = setupBeforeTrigger((ServerWorld) world, "forester");
      }
      this.equipTool();
      ItemStack dropMe = inventory.getStackInSlot(0).copy();
      if (this.isTree(targetPos)) {
        if (TileEntityBase.tryHarvestBlock(fakePlayer, world, targetPos)) {
          //ok then DRAIN POWER
          energy.extractEnergy(cost, false);
          //          ModCyclic.LOGGER.info("drain " + cost + "current" + en.getEnergyStored());
        }
      }
      else if (this.isSapling(dropMe)) {
        //plant me  . if im on the lowest level
        if (targetPos.getY() == this.pos.getY()) {
          ActionResultType result = TileEntityBase.rightClickBlock(fakePlayer, world, targetPos, Hand.OFF_HAND);
          if (result == ActionResultType.SUCCESS) {
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
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
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
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    shapeIndex = tag.getInt("shapeIndex");
    radius = tag.getInt("radius");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("shapeIndex", shapeIndex);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.putInt("radius", radius);
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  /**
   * off-hand for saplings; main-hand for mining tool
   */
  private void equipTool() {
    if (fakePlayer == null) {
      return;
    }
    TileEntityBase.tryEquipItem(inventoryCap, fakePlayer, 0, Hand.OFF_HAND);
    if (fakePlayer.get().getHeldItem(Hand.MAIN_HAND).isEmpty()) {
      ItemStack tool = new ItemStack(Items.DIAMOND_AXE);
      //TODO:we can do both silk/fortune with toggle
      tool.addEnchantment(Enchantments.FORTUNE, 3);
      TileEntityBase.tryEquipItem(tool, fakePlayer, Hand.MAIN_HAND);
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
    shape = UtilShape.cubeSquareBase(this.getCurrentFacingPos(radius + 1), radius, height);
    return shape;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    List<BlockPos> shape = UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(radius + 1), this.radius);
    BlockPos targetPos = getShapeTarget(shape);
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

  private boolean isSapling(ItemStack dropMe) {
    //    if(dropMe.getItem().isIn(Tags.Blocks.SAND))
    //sapling tag SHOULD exist. it doesnt. idk WHY
    Block block = Block.getBlockFromItem(dropMe.getItem());
    return block.isIn(BlockTags.SAPLINGS) ||
        block instanceof SaplingBlock;
  }

  private boolean isTree(BlockPos targetPos) {
    if (targetPos == null) {
      return false;
    }
    Block block = world.getBlockState(targetPos).getBlock();
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
