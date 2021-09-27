package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilFakePlayer;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityBase extends TileEntity implements IInventory {

  public static final String NBTINV = "inv";
  public static final String NBTFLUID = "fluid";
  public static final String NBTENERGY = "energy";
  public static final int MENERGY = 64 * 1000;
  protected int flowing = 1;
  protected int needsRedstone = 1;
  protected int render = 0; // default to do not render
  protected int timer;

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public int getTimer() {
    return timer;
  }

  protected PlayerEntity getLookingPlayer(int maxRange, boolean mustCrouch) {
    List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(
        this.pos.getX() - maxRange, this.pos.getY() - maxRange, this.pos.getZ() - maxRange, this.pos.getX() + maxRange, this.pos.getY() + maxRange, this.pos.getZ() + maxRange));
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

  public void tryDumpFakePlayerInvo(WeakReference<FakePlayer> fp, boolean includeMainHand) {
    int start = (includeMainHand) ? 0 : 1;
    ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
    for (int i = start; i < fp.get().inventory.mainInventory.size(); i++) {
      ItemStack s = fp.get().inventory.mainInventory.get(i);
      if (s.isEmpty() == false) {
        toDrop.add(s.copy());
        fp.get().inventory.mainInventory.set(i, ItemStack.EMPTY);
      }
    }
    UtilItemStack.drop(this.world, this.pos.up(), toDrop);
  }

  public static void tryEquipItem(ItemStack item, WeakReference<FakePlayer> fp, Hand hand) {
    if (fp == null) {
      return;
    }
    fp.get().setHeldItem(hand, item);
  }

  public static void syncEquippedItem(LazyOptional<IItemHandler> i, WeakReference<FakePlayer> fp, int slot, Hand hand) {
    if (fp == null) {
      return;
    }
    i.ifPresent(inv -> {
      inv.extractItem(slot, 64, false); //delete and overwrite
      inv.insertItem(slot, fp.get().getHeldItem(hand), false);
    });
  }

  public static void tryEquipItem(LazyOptional<IItemHandler> i, WeakReference<FakePlayer> fp, int slot, Hand hand) {
    if (fp == null) {
      return;
    }
    i.ifPresent(inv -> {
      ItemStack maybeTool = inv.getStackInSlot(0);
      if (!maybeTool.isEmpty()) {
        if (maybeTool.getCount() <= 0) {
          maybeTool = ItemStack.EMPTY;
        }
      }
      if (!maybeTool.equals(fp.get().getHeldItem(hand))) {
        fp.get().setHeldItem(hand, maybeTool);
      }
    });
  }

  public static ActionResultType rightClickBlock(WeakReference<FakePlayer> fakePlayer,
      World world, BlockPos targetPos, Hand hand, Direction facing) throws Exception {
    if (fakePlayer == null) {
      return ActionResultType.FAIL;
    }
    Direction placementOn = (facing == null) ? fakePlayer.get().getAdjustedHorizontalFacing() : facing;
    BlockRayTraceResult blockraytraceresult = new BlockRayTraceResult(
        fakePlayer.get().getLookVec(), placementOn,
        targetPos, true);
    //processRightClick
    ActionResultType result = fakePlayer.get().interactionManager.func_219441_a(fakePlayer.get(), world,
        fakePlayer.get().getHeldItem(hand), hand, blockraytraceresult);
    //it becomes CONSUME result 1 bucket. then later i guess it doesnt save, and then its water_bucket again
    return result;
  }

  public static boolean tryHarvestBlock(WeakReference<FakePlayer> fakePlayer, World world, BlockPos targetPos) {
    if (fakePlayer == null) {
      return false;
    }
    return fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
  }

  public WeakReference<FakePlayer> setupBeforeTrigger(ServerWorld sw, String name, UUID uuid) {
    WeakReference<FakePlayer> fakePlayer = UtilFakePlayer.initFakePlayer(sw, uuid, name);
    if (fakePlayer == null) {
      ModCyclic.LOGGER.error("Fake player failed to init " + name + " " + uuid);
      return null;
    }
    //fake player facing the same direction as tile. for throwables
    fakePlayer.get().setPosition(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()); //seems to help interact() mob drops like milk
    fakePlayer.get().rotationYaw = UtilEntity.getYawFromFacing(this.getCurrentFacing());
    return fakePlayer;
  }

  public WeakReference<FakePlayer> setupBeforeTrigger(ServerWorld sw, String name) {
    return setupBeforeTrigger(sw, name, UUID.randomUUID());
  }

  public void setLitProperty(boolean lit) {
    BlockState st = this.getBlockState();
    if (!st.hasProperty(BlockBase.LIT)) {
      return;
    }
    boolean previous = st.get(BlockBreaker.LIT);
    if (previous != lit) {
      this.world.setBlockState(pos, st.with(BlockBreaker.LIT, lit));
    }
  }

  public Direction getCurrentFacing() {
    if (this.getBlockState().hasProperty(BlockStateProperties.FACING)) {
      return this.getBlockState().get(BlockStateProperties.FACING);
    }
    if (this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      return this.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
    }
    return null;
  }

  @Override
  public CompoundNBT getUpdateTag() {
    //thanks http://www.minecraftforge.net/forum/index.php?topic=39162.0
    CompoundNBT syncData = new CompoundNBT();
    this.write(syncData); //this calls writeInternal
    return syncData;
  }

  protected BlockPos getCurrentFacingPos(int distance) {
    Direction f = this.getCurrentFacing();
    if (f != null) {
      return this.pos.offset(f, distance);
    }
    return this.pos;
  }

  protected BlockPos getCurrentFacingPos() {
    return getCurrentFacingPos(1);
  }

  @Override
  public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
    this.read(this.getBlockState(), pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, 1, getUpdateTag());
  }

  public boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());
  }

  public boolean requiresRedstone() {
    return this.needsRedstone == 1;
  }

  public void moveFluids(Direction myFacingDir, BlockPos posTarget, int toFlow, IFluidHandler tank) {
    // posTarget = pos.offset(myFacingDir);
    if (tank == null || tank.getFluidInTank(0).getAmount() <= 0) {
      return;
    }
    Direction themFacingMe = myFacingDir.getOpposite();
    UtilFluid.tryFillPositionFromTank(world, posTarget, themFacingMe, tank, toFlow);
  }

  public void tryExtract(IItemHandler myself, Direction extractSide, int qty, ItemStackHandler nullableFilter) {
    if (extractSide == null) {
      return;
    }
    if (extractSide == null || !myself.getStackInSlot(0).isEmpty()) {
      return;
    }
    BlockPos posTarget = pos.offset(extractSide);
    TileEntity tile = world.getTileEntity(posTarget);
    if (tile != null) {
      IItemHandler itemHandlerFrom = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, extractSide.getOpposite()).orElse(null);
      //
      ItemStack itemTarget;
      if (itemHandlerFrom != null) {
        //ok go
        for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
          itemTarget = itemHandlerFrom.extractItem(i, qty, true);
          if (itemTarget.isEmpty()) {
            continue;
          }
          // and then pull 
          if (nullableFilter != null &&
              !FilterCardItem.filterAllowsExtract(nullableFilter.getStackInSlot(0), itemTarget)) {
            continue;
          }
          itemTarget = itemHandlerFrom.extractItem(i, qty, false);
          ItemStack result = myself.insertItem(0, itemTarget.copy(), false);
          itemTarget.setCount(result.getCount());
          return;
        }
      }
    }
  }

  public boolean moveItems(Direction myFacingDir, int max, IItemHandler handlerHere) {
    return moveItems(myFacingDir, pos.offset(myFacingDir), max, handlerHere, 0);
  }

  public boolean moveItems(Direction myFacingDir, BlockPos posTarget, int max, IItemHandler handlerHere, int theslot) {
    if (this.world.isRemote()) {
      return false;
    }
    if (handlerHere == null) {
      return false;
    }
    Direction themFacingMe = myFacingDir.getOpposite();
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null) {
      return false;
    }
    IItemHandler handlerOutput = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, themFacingMe).orElse(null);
    if (handlerOutput == null) {
      return false;
    }
    if (handlerHere != null && handlerOutput != null) {
      //first simulate 
      ItemStack drain = handlerHere.extractItem(theslot, max, true); // handlerHere.getStackInSlot(theslot).copy();
      int sizeStarted = drain.getCount();
      if (!drain.isEmpty()) {
        //now push it into output, but find out what was ACTUALLY taken
        for (int slot = 0; slot < handlerOutput.getSlots(); slot++) {
          drain = handlerOutput.insertItem(slot, drain, false);
          if (drain.isEmpty()) {
            break; //done draining
          }
        }
      }
      int sizeAfter = sizeStarted - drain.getCount();
      if (sizeAfter > 0) {
        handlerHere.extractItem(theslot, sizeAfter, false);
      }
      return sizeAfter > 0;
    }
    return false;
  }

  protected boolean moveEnergy(Direction myFacingDir, int quantity) {
    return moveEnergy(myFacingDir, pos.offset(myFacingDir), quantity);
  }

  protected boolean moveEnergy(Direction myFacingDir, BlockPos posTarget, int quantity) {
    if (this.world.isRemote) {
      return false; //important to not desync cables
    }
    IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, myFacingDir).orElse(null);
    if (handlerHere == null || handlerHere.getEnergyStored() == 0) {
      return false;
    }
    Direction themFacingMe = myFacingDir.getOpposite();
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null) {
      return false;
    }
    IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, themFacingMe).orElse(null);
    if (handlerOutput == null) {
      return false;
    }
    if (handlerHere != null && handlerOutput != null
        && handlerHere.canExtract() && handlerOutput.canReceive()) {
      //first simulate
      int drain = handlerHere.extractEnergy(quantity, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerOutput.receiveEnergy(drain, false);
        //now actually drain that much from here
        handlerHere.extractEnergy(filled, false);
        if (filled > 0 && tileTarget instanceof TileCableEnergy) {
          // not so compatible with other fluid systems. itl do i guess
          TileCableEnergy cable = (TileCableEnergy) tileTarget;
          cable.updateIncomingEnergyFace(themFacingMe);
        }
        return filled > 0;
      }
    }
    return false;
  }

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
    return this.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  public void setEnergy(int value) {
    IEnergyStorage energ = this.getCapability(CapabilityEnergy.ENERGY).orElse(null);
    if (energ != null && energ instanceof CustomEnergyStorage) {
      ((CustomEnergyStorage) energ).setEnergy(value);
    }
  }

  //fluid tanks have 'onchanged', energy caps do not
  protected void syncEnergy() {
    if (world.isRemote == false && world.getGameTime() % 20 == 0) { //if serverside then 
      IEnergyStorage energ = this.getCapability(CapabilityEnergy.ENERGY).orElse(null);
      if (energ != null) {
        PacketRegistry.sendToAllClients(this.getWorld(), new PacketEnergySync(this.getPos(), energ.getEnergyStored()));
      }
    }
  }

  public void exportEnergyAllSides() {
    List<Integer> rawList = IntStream.rangeClosed(0, 5).boxed().collect(Collectors.toList());
    Collections.shuffle(rawList);
    for (Integer i : rawList) {
      Direction exportToSide = Direction.values()[i];
      moveEnergy(exportToSide, MENERGY / 2);
    }
  }
}
