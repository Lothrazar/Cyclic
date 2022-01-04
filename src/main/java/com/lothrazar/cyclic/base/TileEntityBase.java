package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import com.lothrazar.cyclic.util.UtilEnergyStorage;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilFakePlayer;
import com.lothrazar.cyclic.util.UtilFluidHandler;
import com.lothrazar.cyclic.util.UtilItemHandler;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.client.CPlayerDiggingPacket.Action;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityBase extends TileEntity implements IInventory {

  public static final String NBTINV = "inv";
  public static final String NBTFLUID = "fluid";
  public static final String NBTENERGY = "energy";
  public static final String NBTFILTER = "filter";
  public static final int MENERGY = 64 * 1000;
  protected int flowing = 1;
  protected int needsRedstone = 1;
  protected int render = 0; // default to do not render
  protected int timer = 0;

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public static void tryEquipItem(final ItemStack item, final WeakReference<FakePlayer> fakePlayerWeakReference, final Hand hand) {
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return;
    }
    fakePlayer.setHeldItem(hand, item);
  }

  public static void syncEquippedItem(final LazyOptional<IItemHandler> i, final WeakReference<FakePlayer> fakePlayerWeakReference, final int slot, final Hand hand) {
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return;
    }

    
    final IItemHandler itemHandler = i.orElse(null);
    
    if (itemHandler == null) {
      return;
    }

    itemHandler.extractItem(slot, itemHandler.getStackInSlot(slot).getMaxStackSize(), false);
    itemHandler.insertItem(slot, fakePlayer.getHeldItem(hand), false);
  }

  @SuppressWarnings("UnusedParameters")
  public static void tryEquipItem(final LazyOptional<IItemHandler> i, final WeakReference<FakePlayer> fakePlayerWeakReference, final int slot, final Hand hand) {
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return;
    }

    
    final IItemHandler itemHandler = i.resolve().orElse(null);
    
    if (itemHandler == null) {
      return;
    }

    ItemStack maybeTool = itemHandler.getStackInSlot(0);
    if (maybeTool.getCount() <= 0) {
      maybeTool = ItemStack.EMPTY;
    }

    if (!maybeTool.equals(fakePlayer.getHeldItem(hand))) {
      fakePlayer.setHeldItem(hand, maybeTool);
    }
  }

  public static ActionResultType leftClickBlock(final WeakReference<FakePlayer> fakePlayerWeakReference,
                                                final BlockPos targetPos, final Direction facing) {
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return ActionResultType.FAIL;
    }
    final Direction placementOn = (facing == null) ? fakePlayer.getAdjustedHorizontalFacing() : facing;
    final BlockRayTraceResult blockRayTraceResult = new BlockRayTraceResult(fakePlayer.getLookVec(), placementOn, targetPos, true);
    try {
      fakePlayer.interactionManager.func_225416_a(blockRayTraceResult.getPos(), Action.START_DESTROY_BLOCK, facing, 0);
      return ActionResultType.SUCCESS;
    }
    catch (Exception e) {
      return ActionResultType.FAIL;
    }
  }

  public static ActionResultType rightClickBlock(final WeakReference<FakePlayer> fakePlayerWeakReference,
                                   final World world, final BlockPos targetPos, final Hand hand, final Direction facing) {
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return ActionResultType.FAIL;
    }
    final Direction placementOn = (facing == null) ? fakePlayer.getAdjustedHorizontalFacing() : facing;
    final BlockRayTraceResult blockRayTraceResult = new BlockRayTraceResult(fakePlayer.getLookVec(), placementOn, targetPos, true);
    //processRightClick
    //it becomes CONSUME result 1 bucket. then later i guess it doesnt save, and then its water_bucket again
    return fakePlayer.interactionManager.func_219441_a(fakePlayer, world, fakePlayer.getHeldItem(hand), hand, blockRayTraceResult);
  }

  @SuppressWarnings("UnusedParameters")
  public static boolean tryHarvestBlock(final WeakReference<FakePlayer> fakePlayerWeakReference, final World world, final BlockPos targetPos) {
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return false;
    }
    return fakePlayer.interactionManager.tryHarvestBlock(targetPos);
  }

  public int getTimer() {
    return timer;
  }

  protected PlayerEntity getLookingPlayer(final int maxRange, final boolean mustCrouch) {
    final AxisAlignedBB boundingBox = new AxisAlignedBB(pos.getX() - maxRange, pos.getY() - maxRange, pos.getZ() - maxRange,
        pos.getX() + maxRange, pos.getY() + maxRange, pos.getZ() + maxRange);

    final List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, boundingBox);
    for (PlayerEntity player : players) {
      if (mustCrouch && !player.isCrouching()) {
        continue; //check the next one
      }
      //am i looking
      Vector3d positionEyes = player.getEyePosition(1F);
      Vector3d look = player.getLook(1F);
      //take the player eye position. draw a vector from the eyes, in the direction they are looking
      //of LENGTH equal to the range
      Vector3d visionWithLength = positionEyes.add(look.x * maxRange, look.y * maxRange, look.z * maxRange);
      //ray trayce from eyes, along the vision vec
      BlockRayTraceResult rayTrace = this.world.rayTraceBlocks(new RayTraceContext(positionEyes, visionWithLength, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
      if (this.pos.equals(rayTrace.getPos())) {
        //at least one is enough, stop looping
        return player;
      }
    }
    return null;
  }

  public void tryDumpFakePlayerInvo(final WeakReference<FakePlayer> fakePlayerWeakReference, final boolean includeMainHand) {
    final int start = (includeMainHand) ? 0 : 1;
    final List<ItemStack> toDrop = new ArrayList<>();
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return;
    }
    final List<ItemStack> inventory = fakePlayer.inventory.mainInventory;
    for (int i = start; i < inventory.size(); i++) {
      final ItemStack s = inventory.get(i);
      if (s.isEmpty()) {
        continue;
      }
      toDrop.add(s.copy());
      inventory.set(i, ItemStack.EMPTY);
    }
    UtilItemStack.drop(world, pos.up(), toDrop);
  }

  public WeakReference<FakePlayer> setupBeforeTrigger(final ServerWorld sw, final String name, final UUID uuid) {
    final WeakReference<FakePlayer> fakePlayerWeakReference = UtilFakePlayer.initFakePlayer(sw, uuid, name);
    if (fakePlayerWeakReference == null) {
      ModCyclic.LOGGER.error("Fake player failed to init " + name + " " + uuid);
      return null;
    }
    final FakePlayer fakePlayer = fakePlayerWeakReference.get();
    if (fakePlayer == null) {
      return null;
    }
    //fake player facing the same direction as tile. for throwables
    fakePlayer.setPosition(pos.getX(), pos.getY(), pos.getZ()); //seems to help interact() mob drops like milk
    fakePlayer.rotationYaw = UtilEntity.getYawFromFacing(getCurrentFacing());
    return fakePlayerWeakReference;
  }

  public WeakReference<FakePlayer> setupBeforeTrigger(final ServerWorld sw, final String name) {
    return setupBeforeTrigger(sw, name, UUID.randomUUID());
  }

  public void setLitProperty(final boolean lit) {
    if (world == null) {
      return;
    }
    final BlockState blockState = getBlockState();
    if (!blockState.hasProperty(BlockBase.LIT)) {
      return;
    }
    final boolean previous = blockState.get(BlockBreaker.LIT);
    if (previous != lit) {
      // 1 will cause a block update.
      // 2 will send the change to clients.
      world.setBlockState(pos, blockState.with(BlockBreaker.LIT, lit), 1 | 2);
    }
  }

  public Direction getCurrentFacing() {
    final BlockState blockState = getBlockState();
    if (blockState.hasProperty(BlockStateProperties.FACING)) {
      return blockState.get(BlockStateProperties.FACING);
    }
    if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      return blockState.get(BlockStateProperties.HORIZONTAL_FACING);
    }
    return null;
  }

  @Override
  public CompoundNBT getUpdateTag() {
    //thanks http://www.minecraftforge.net/forum/index.php?topic=39162.0
    final CompoundNBT syncData = new CompoundNBT();
    this.write(syncData); //this calls writeInternal
    return syncData;
  }

  protected BlockPos getCurrentFacingPos(final int distance) {
    final Direction facing = getCurrentFacing();
    if (facing != null) {
      return pos.offset(facing, distance);
    }
    return pos;
  }

  protected BlockPos getCurrentFacingPos() {
    return getCurrentFacingPos(1);
  }

  @Override
  public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
    read(getBlockState(), pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
  }

  public boolean isPowered() {
    if (world == null) {
      return false;
    }
    return world.isBlockPowered(pos);
  }

  public int getRedstonePower() {
    if (world == null) {
      return 0;
    }
    return world.getRedstonePowerFromNeighbors(pos);
  }

  public boolean requiresRedstone() {
    return this.needsRedstone == 1;
  }

  public IEnergyStorage getEnergyStorage() {
    return getCapability(CapabilityEnergy.ENERGY, null).resolve().orElse(null);
  }

  protected LazyOptional<IEnergyStorage> getAdjacentEnergyStorageOptCap(final Direction side) {
    if (world == null) {
      return LazyOptional.empty();
    }
    return UtilEnergyStorage.getOptCap(world, pos.offset(side), side.getOpposite());
  }

  protected IEnergyStorage getAdjacentEnergyStorage(final Direction side) {
    if (world == null) {
      return null;
    }
    return UtilEnergyStorage.get(world, pos.offset(side), side.getOpposite());
  }

  public IFluidHandler getFluidHandler() {
    return getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().orElse(null);
  }

  protected LazyOptional<IFluidHandler> getAdjacentFluidHandlerOptCap(final Direction side) {
    if (world == null) {
      return LazyOptional.empty();
    }
    return UtilFluidHandler.getOptCap(world, pos.offset(side), side.getOpposite());
  }

  protected IFluidHandler getAdjacentFluidHandler(final Direction side) {
    if (world == null) {
      return null;
    }
    return UtilFluidHandler.get(world, pos.offset(side), side.getOpposite());
  }

  public IItemHandler getItemHandler() {
    return getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().orElse(null);
  }

  protected LazyOptional<IItemHandler> getAdjacentItemHandlerOptCap(final Direction side) {
    if (world == null) {
      return LazyOptional.empty();
    }
    return UtilItemHandler.getOptCap(world, pos.offset(side), side.getOpposite());
  }

  protected IItemHandler getAdjacentItemHandler(final Direction side) {
    if (world == null) {
      return null;
    }
    return UtilItemHandler.get(world, pos.offset(side), side.getOpposite());
  }

  @SuppressWarnings("UnusedReturnValue")
  protected FluidStack getFluidsFromAdjacent(final IFluidHandler output, final Direction side, final int amount) {
    final IFluidHandler input = getAdjacentFluidHandler(side);
    if (input == null) {
      return FluidStack.EMPTY;
    }
    return FluidUtil.tryFluidTransfer(output, input, amount, true);
  }

  @SuppressWarnings("UnusedReturnValue")
  protected FluidStack moveFluidsToAdjacent(final IFluidHandler input, final Direction side, final int amount) {
    final IFluidHandler output = getAdjacentFluidHandler(side);
    if (output == null) {
      return FluidStack.EMPTY;
    }
    return FluidUtil.tryFluidTransfer(output, input, amount, true);
  }

  @SuppressWarnings("UnusedReturnValue")
  public FluidStack moveFluidsToBlockPos(final IFluidHandler input, final BlockPos blockPos, final Direction side, final int amount) {
    if (world == null) {
      return FluidStack.EMPTY;
    }
    final IFluidHandler output = UtilFluidHandler.get(world, blockPos, side.getOpposite());
    if (output == null) {
      return FluidStack.EMPTY;
    }
    return FluidUtil.tryFluidTransfer(output, input, amount, true);
  }

  public int getItemsFromAdjacent(final IItemHandler output, final Direction side, final int amount) {
    if (world == null) {
      return 0;
    }
    final IItemHandler input = getAdjacentItemHandler(side);
    if (input == null) {
      return 0;
    }
    int remainingItemCount = amount;
    for (int slot = 0; slot < input.getSlots(); slot++) {
      remainingItemCount -= UtilItemHandler.moveItems(input, slot, output, remainingItemCount);
      if (remainingItemCount <= 0) {
        break;
      }
    }
    return amount - remainingItemCount;
  }

  public int moveItemsToAdjacent(final IItemHandler input, final Direction side, final int amount) {
    final IItemHandler output = getAdjacentItemHandler(side);
    if (output == null) {
      return 0;
    }
    int remainingAmount = amount;
    for (int slot = 0; slot < input.getSlots(); slot++) {
      remainingAmount -= UtilItemHandler.moveItems(input, slot, output, remainingAmount);
      if (remainingAmount <= 0) {
        break;
      }
    }
    return amount - remainingAmount;
  }

  public int moveItemsToBlockPos(final Direction side, final BlockPos blockPos, final int amount, final IItemHandler input, final int inputSlot) {
    if (world == null) {
      return 0;
    }
    final IItemHandler output = UtilItemHandler.get(world, blockPos, side.getOpposite());
    if (output == null) {
      return 0;
    }
    return UtilItemHandler.moveItems(input, inputSlot, output, amount);
  }

  @SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
  protected int getEnergyFromAdjacent(final IEnergyStorage output, final Direction side, final int amount) {
    final IEnergyStorage input = getAdjacentEnergyStorage(side);
    if (input == null) {
      return 0;
    }
    return UtilEnergyStorage.moveEnergy(input, output, amount);
  }

  @SuppressWarnings("UnusedReturnValue")
  protected int moveEnergyToAdjacent(final IEnergyStorage input, final Direction side, final int amount) {
    final IEnergyStorage output = getAdjacentEnergyStorage(side);
    if (output == null) {
      return 0;
    }
    return UtilEnergyStorage.moveEnergy(input, output, amount);
  }

  @SuppressWarnings("SameParameterValue")
  protected int moveEnergyToBlockPos(final IEnergyStorage input, final BlockPos blockPos, final Direction side, final int amount) {
    if (world == null) {
      return 0;
    }
    final IEnergyStorage output = UtilEnergyStorage.get(world, blockPos, side);
    if (output == null) {
      return 0;
    }
    return UtilEnergyStorage.moveEnergy(input, output, amount);
  }

  public void setReceivedFrom(final Direction side) {}

  public void updateConnection(final Direction side, final EnumConnectType connectType) {}

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    flowing = tag.getInt("flowing");
    needsRedstone = tag.getInt("needsRedstone");
    render = tag.getInt("renderParticles");
    timer = tag.getInt("timer");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("flowing", flowing);
    tag.putInt("needsRedstone", needsRedstone);
    tag.putInt("renderParticles", render);
    tag.putInt("timer", timer);
    return super.write(tag);
  }

  public abstract void setField(int field, int value);

  public abstract int getField(int field);

  public void setNeedsRedstone(int value) {
    this.needsRedstone = value % 2;
  }

  public FluidStack getFluid() {
    return FluidStack.EMPTY;
  }

  public void setFluid(FluidStack fluid) {}

  /************************** IInventory needed for IRecipe **********************************/
  @Deprecated
  @Override
  public int getSizeInventory() {
    return 0;
  }

  @Deprecated
  @Override
  public boolean isEmpty() {
    return true;
  }

  @Deprecated
  @Override
  public ItemStack getStackInSlot(int index) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public ItemStack decrStackSize(int index, int count) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public ItemStack removeStackFromSlot(int index) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {}

  @Deprecated
  @Override
  public boolean isUsableByPlayer(PlayerEntity player) {
    return false;
  }

  @Deprecated
  @Override
  public void clear() {}

  public void setFieldString(int field, String value) {
    //for string field  
  }

  public String getFieldString(int field) {
    //for string field  
    return null;
  }

  public int getEnergy() {
    final IEnergyStorage energyStorage = getCapability(CapabilityEnergy.ENERGY, null).resolve().orElse(null);
    if (energyStorage == null) {
      return 0;
    }
    return energyStorage.getEnergyStored();
  }

  public void setEnergy(final int value) {
    final IEnergyStorage energyStorage = getCapability(CapabilityEnergy.ENERGY, null).resolve().orElse(null);
    if (energyStorage == null) {
      return;
    }

    if (energyStorage instanceof CustomEnergyStorage) {
      ((CustomEnergyStorage) energyStorage).setEnergy(value);
    }
    else {
      int current = energyStorage.getEnergyStored();
      while (current != value) {
        if (current > value) {
          energyStorage.extractEnergy(current - value, false);
        }
        else {
          energyStorage.receiveEnergy(value - current, false);
        }
        current = energyStorage.getEnergyStored();
      }
    }
  }

  //fluid tanks have 'onchanged', energy caps do not
  protected void syncEnergy() {
    if (world == null || world.isRemote) {
      return;
    }

    final IEnergyStorage energyStorage = getCapability(CapabilityEnergy.ENERGY, null).resolve().orElse(null);
    if (energyStorage == null) {
      return;
    }

    final PacketEnergySync packetEnergySync = new PacketEnergySync(pos, energyStorage.getEnergyStored());
    PacketRegistry.sendToAllClients(world, packetEnergySync);
  }

  public void exportEnergyAllSides() {
    for (final Direction exportToSide : UtilDirection.getAllInDifferentOrder()) {
      final IEnergyStorage input = getCapability(CapabilityEnergy.ENERGY, exportToSide).resolve().orElse(null);
      if (input != null) {
        moveEnergyToAdjacent(input, exportToSide, MENERGY / 2);
      }
    }
  }
}
