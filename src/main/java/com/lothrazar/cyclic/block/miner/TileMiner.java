package com.lothrazar.cyclic.block.miner;

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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileMiner extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static IntValue POWERCONF;
  private int shapeIndex = 0;
  static final int MAX_HEIGHT = 64;
  public static final int MAX_SIZE = 12;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int height = MAX_HEIGHT / 2;
  private int size = 5;
  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private WeakReference<FakePlayer> fakePlayer;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private BlockPos targetPos = BlockPos.ZERO;
  private boolean directionIsUp = false;

  enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public TileMiner() {
    super(TileRegistry.miner);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
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
    return new ContainerMiner(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    size = tag.getInt("size");
    height = tag.getInt("height");
    isCurrentlyMining = tag.getBoolean("isCurrentlyMining");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("size", size);
    tag.putInt("height", height);
    isCurrentlyMining = tag.getBoolean("isCurrentlyMining");
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
    if ((world instanceof ServerWorld) && fakePlayer == null)
      fakePlayer = setupBeforeTrigger((ServerWorld) world, "miner");
    try {
      TileEntityBase.tryEquipItem(inventory, fakePlayer, 0, Hand.MAIN_HAND);
      //TODO: does this target block match filter
      List<BlockPos> shape = getShape();
      //        resetProgress(); 
      //        shapeIndex = 0;
      //        targetPos = null;
      //      }
      if (shape.size() == 0) {
        return;
      }
      setLitProperty(true);
      updateMiningProgress(shape);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Miner action error", e);
    }
  }

  private boolean updateMiningProgress(List<BlockPos> shape) {
    if (fakePlayer == null) {
      return false;
    }
    //    if (isCurrentlyMining == false) { //we can mine but are not currently. so try moving to a new position
    ////  This is making me skip block zero and other stuff randomly? i think this is bad
    //      updateTargetPos(shape);
    //    }
    if (isTargetValid()) { //if target is valid, allow mining (no air, no blacklist, etc)
      //      ModCyclic.LOGGER.info(" target valid, ismining true ? " + targetPos);
      isCurrentlyMining = true;
      //then keep current target
    }
    else { // no valid target, back out 
      updateTargetPos(shape);
      resetProgress();
    }
    Integer cost = POWERCONF.get();
    IEnergyStorage cap = this.energy.orElse(null);
    if (cap.getEnergyStored() < cost && cost > 0) {
      return false;
    }
    //currentlyMining may have changed, and we are still turned on:
    if (isCurrentlyMining) {
      BlockState targetState = world.getBlockState(targetPos);
      float relative = targetState.getPlayerRelativeBlockHardness(fakePlayer.get(), world, targetPos);
      //state.getPlayerRelativeBlockHardness(player, worldIn, pos);UtilItemStack.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), world, targetPos);
      curBlockDamage += relative;
      //
      //      ModCyclic.LOGGER.info(targetState + " progress ? " + targetPos + "  relative = " + relative + "    curBlockDamage=" + curBlockDamage);
      //if hardness is relative, jus fekin break it like air eh
      if (curBlockDamage >= 1.0f || relative == 0) {
        //        ModCyclic.LOGGER.info("TRY t" + targetPos);
        boolean harvested = fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
        if (!harvested) {
          //            world.destroyBlock(targetPos, true, fakePlayer.get()); 
          harvested = world.getBlockState(targetPos)
              .removedByPlayer(world, pos, fakePlayer.get(), true, world.getFluidState(pos));
          //          ModCyclic.LOGGER.info(" hacked ? " + targetPos + " removed");
        }
        if (harvested) {
          // success 
          cap.extractEnergy(cost, false);
          resetProgress();
        }
        else {
          //          ModCyclic.LOGGER.info(" FAILED ? " + targetPos + " removed");
          world.sendBlockBreakProgress(fakePlayer.get().getUniqueID().hashCode(),
              targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
    else {//is mining is false
      //      ModCyclic.LOGGER.info(" why not mining ? " + targetPos);
      world.sendBlockBreakProgress(fakePlayer.get().getUniqueID().hashCode(),
          targetPos, (int) (curBlockDamage * 10.0F) - 1);
    }
    return false;
  }

  private boolean isTargetValid() {
    if (targetPos == null || world.isAirBlock(targetPos)) {
      return false;//dont mine air or liquid. 
    }
    //is this valid
    BlockState blockSt = world.getBlockState(targetPos);
    if (blockSt.hardness < 0) {
      return false;//unbreakable 
    }
    if (fakePlayer != null) {
      //water logged is 
      if (blockSt.getFluidState() != null) {
        //am i PURE liquid? or just a WATERLOGGED block
        if (blockSt.hasProperty(BlockStateProperties.WATERLOGGED) == false) {
          //pure liquid. but this will make canHarvestBlock go true , which is a lie actually so, no. dont get stuck here
          return false;
        }
      }
      //its a solid non-air, non-fluid block (but might be like waterlogged stairs or something)
      return blockSt.canHarvestBlock(world, targetPos, fakePlayer.get());
    }
    //TODO: Fix this once forge is fixed
    //but its busted
    //  Set<ToolType> tools = tool.getItem().getToolTypes(tool);
    //    ItemStack tool = fakePlayer.get().getHeldItem(Hand.MAIN_HAND);
    //aww this isnt working, many things like WOOD PLANKS have harvestTool = null, meaning "axe" valid tools still have not effective come back
    //    ModCyclic.LOGGER.info(shapeIndex + ":" + tool + " not effective on " + block + "??" + block.getBlock().getHarvestTool(block));
    //  boolean isToolEffective = false;
    //    if (tools.size() == 0)
    //      isToolEffective = true;//if item is not a tool, treat like empty hand = "badly effective everywhere"
    //    else
    //      for (ToolType t : tools) {
    //        if (block.isToolEffective(t)) {
    //          isToolEffective = true;
    //          break;
    //        }
    //      }
    //    if (!isToolEffective) {
    //      ModCyclic.LOGGER.info(shapeIndex + ":" + tool + " not effective on " + block + "??" + block.getBlock().getHarvestTool(block));
    //    }
    //i guess any non fluid
    return world.getFluidState(targetPos) != null;
  }

  private void updateTargetPos(List<BlockPos> shape) {
    shapeIndex++;
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    targetPos = shape.get(shapeIndex);
  }

  private void resetProgress() {
    isCurrentlyMining = false;
    curBlockDamage = 0;
    if (fakePlayer != null && targetPos != null) {
      //BlockPos targetPos = pos.offset(state.getValue(BlockMiner.PROPERTYFACING));
      getWorld().sendBlockBreakProgress(fakePlayer.get().getUniqueID().hashCode(), targetPos, -1);
    }
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = UtilShape.squareHorizontalFull(this.getCurrentFacingPos(size + 1), size);
    int diff = directionIsUp ? 1 : -1;
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  public List<BlockPos> getShapeHollow() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(size + 1), size);
    int diff = directionIsUp ? 1 : -1;
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case DIRECTION:
        return (directionIsUp) ? 1 : 0;
      case HEIGHT:
        return height;
      case SIZE:
        return size;
      default:
      break;
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
      case DIRECTION:
        this.directionIsUp = value == 1;
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
      case SIZE:
        size = Math.min(value, MAX_SIZE);
      break;
      default:
      break;
    }
  }
}
