package com.lothrazar.cyclic.block;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.collect.Lists;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.FakePlayerUtil;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileBlockEntityCyclic extends BlockEntity implements Container {

  public static final String NBTINV = "inv";
  public static final String NBTFLUID = "fluid";
  public static final String NBTENERGY = "energy";
  public static final int MENERGY = 64 * 1000;
  protected int flowing = 1;
  protected int needsRedstone = 1;
  protected int render = 0; // default to do not render
  protected int timer = 0;

  public TileBlockEntityCyclic(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
    super(tileEntityTypeIn, pos, state);
  }

  public int getTimer() {
    return timer;
  }

  protected Player getLookingPlayer(int maxRange, boolean mustCrouch) {
    List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(
        this.worldPosition.getX() - maxRange, this.worldPosition.getY() - maxRange, this.worldPosition.getZ() - maxRange, this.worldPosition.getX() + maxRange, this.worldPosition.getY() + maxRange, this.worldPosition.getZ() + maxRange));
    for (Player player : players) {
      if (mustCrouch && !player.isCrouching()) {
        continue; //check the next one
      }
      //am i looking
      Vec3 positionEyes = player.getEyePosition(1F);
      Vec3 look = player.getViewVector(1F);
      //take the player eye position. draw a vector from the eyes, in the direction they are looking
      //of LENGTH equal to the range
      Vec3 visionWithLength = positionEyes.add(look.x * maxRange, look.y * maxRange, look.z * maxRange);
      //ray trayce from eyes, along the vision vec
      BlockHitResult rayTrace = this.level.clip(new ClipContext(positionEyes, visionWithLength, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
      if (this.worldPosition.equals(rayTrace.getBlockPos())) {
        //at least one is enough, stop looping
        return player;
      }
    }
    return null;
  }

  // TODO: this could use a refactor
  public void tryDumpFakePlayerInvo(WeakReference<FakePlayer> fp, ItemStackHandler out, boolean dropItemsOnGround) {
    if (out == null) {
      return;
    }
    int start = 1;
    ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
    for (int i = start; i < fp.get().getInventory().items.size(); i++) {
      ItemStack fpItem = fp.get().getInventory().items.get(i);
      if (fpItem.isEmpty()) {
        continue;
      }
      if (fpItem == fp.get().getMainHandItem()) {
        continue;
      }
      for (int j = 0; j < out.getSlots(); j++) {
        fpItem = out.insertItem(j, fpItem, false);
      }
      if (dropItemsOnGround) {
        toDrop.add(fpItem);
      }
      else {
        fp.get().getInventory().items.set(i, fpItem);
      }
    }
    if (dropItemsOnGround) {
      ItemStackUtil.drop(this.level, this.worldPosition.above(), toDrop);
    }
  }

  public static void tryEquipItem(ItemStack item, WeakReference<FakePlayer> fp, InteractionHand hand) {
    if (fp == null) {
      return;
    }
    fp.get().setItemInHand(hand, item);
  }

  public static void syncEquippedItem(ItemStackHandler inv, WeakReference<FakePlayer> fp, int slot, InteractionHand hand) {
    if (fp == null) {
      return;
    }
    inv.setStackInSlot(slot, ItemStack.EMPTY);
    //    inv.extractItem(slot, 64, false); //delete and overwrite
    inv.insertItem(slot, fp.get().getItemInHand(hand), false);
  }

  public static void tryEquipItem(LazyOptional<IItemHandler> i, WeakReference<FakePlayer> fp, int slot, InteractionHand hand) {
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
      if (!maybeTool.equals(fp.get().getItemInHand(hand))) {
        fp.get().setItemInHand(hand, maybeTool);
      }
    });
  }

  public static InteractionResult interactUseOnBlock(WeakReference<FakePlayer> fakePlayer,
      Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception {
    if (fakePlayer == null) {
      return InteractionResult.FAIL;
    }
    Direction placementOn = (facing == null) ? fakePlayer.get().getMotionDirection() : facing;
    BlockHitResult blockraytraceresult = new BlockHitResult(
        fakePlayer.get().getLookAngle(), placementOn,
        targetPos, true);
    //processRightClick
    ItemStack itemInHand = fakePlayer.get().getItemInHand(hand);
    InteractionResult result = fakePlayer.get().gameMode.useItemOn(fakePlayer.get(), world, itemInHand, hand, blockraytraceresult);
    // ModCyclic.LOGGER.info(targetPos + " gameMode.useItemOn() result = " + result + "  itemInHand = " + itemInHand);
    //it becomes CONSUME result 1 bucket. then later i guess it doesnt save, and then its water_bucket again
    return result;
  }

  /**
   * SOURCE https://github.com/Lothrazar/Cyclic/pull/1994 @metalshark
   */
  public static InteractionResult playerAttackBreakBlock(WeakReference<FakePlayer> fakePlayer,
      Level world, BlockPos targetPos, InteractionHand hand, Direction facing) throws Exception {
    if (fakePlayer == null) {
      return InteractionResult.FAIL;
    }
    try {
      fakePlayer.get().gameMode.handleBlockBreakAction(targetPos, ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
          facing, world.getMaxBuildHeight(), 0); // 0 == getSequence?
      ModCyclic.LOGGER.info("handle handleBlockBreakAction rightclick i guess");
      return InteractionResult.SUCCESS;
    }
    catch (Exception e) {
      return InteractionResult.FAIL;
    }
  }

  public static boolean tryHarvestBlock(WeakReference<FakePlayer> fakePlayer, Level world, BlockPos targetPos) {
    if (fakePlayer == null) {
      return false;
    }
    return fakePlayer.get().gameMode.destroyBlock(targetPos);
  }

  public WeakReference<FakePlayer> setupBeforeTrigger(ServerLevel sw, String name) {
    WeakReference<FakePlayer> fakePlayer = FakePlayerUtil.initFakePlayer(sw, name);
    if (fakePlayer == null) {
      ModCyclic.LOGGER.error("Fake player failed to init " + name);
      return null;
    }
    //fake player facing the same direction as tile. for throwables
    fakePlayer.get().setPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()); //seems to help interact() mob drops like milk
    fakePlayer.get().setYRot(EntityUtil.getYawFromFacing(this.getCurrentFacing()));
    return fakePlayer;
  }

  public void setLitProperty(boolean lit) {
    BlockState st = this.getBlockState();
    if (!st.hasProperty(BlockCyclic.LIT)) {
      return;
    }
    boolean previous = st.getValue(BlockBreaker.LIT);
    if (previous != lit) {
      this.level.setBlockAndUpdate(worldPosition, st.setValue(BlockBreaker.LIT, lit));
    }
  }

  public Direction getCurrentFacing() {
    if (this.getBlockState().hasProperty(BlockStateProperties.FACING)) {
      return this.getBlockState().getValue(BlockStateProperties.FACING);
    }
    if (this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }
    return null;
  }

  protected BlockPos getCurrentFacingPos(int distance) {
    Direction f = this.getCurrentFacing();
    if (f != null) {
      return this.worldPosition.relative(f, distance);
    }
    return this.worldPosition;
  }

  protected BlockPos getCurrentFacingPos() {
    return getCurrentFacingPos(1);
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag syncData = super.getUpdateTag();
    this.saveAdditional(syncData);
    return syncData;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    this.load(pkt.getTag());
    super.onDataPacket(net, pkt);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public boolean isPowered() {
    return this.getLevel().hasNeighborSignal(this.getBlockPos());
  }

  public int getRedstonePower() {
    return this.getLevel().getBestNeighborSignal(this.getBlockPos());
  }

  public boolean requiresRedstone() {
    return this.needsRedstone == 1;
  }

  protected void moveFluidsDimensional(BlockPosDim loc, int toFlow, IFluidHandler tank) {
    Direction myFacingDir = loc.getSide();
    final Direction themFacingMe = myFacingDir.getOpposite();
    // moveFluidsInternal is just util tryFillPositionFromTank
    FluidHelpers.tryFillPositionFromTank(loc.getServerLevel(level.getServer()), loc.getPos(), themFacingMe, tank, toFlow);
  }

  protected void moveFluids(Direction myFacingDir, BlockPos posTarget, int toFlow, IFluidHandler tank) {
    if (tank == null || tank.getFluidInTank(0).isEmpty()) {
      return;
    }
    final Direction themFacingMe = myFacingDir.getOpposite();
    FluidHelpers.tryFillPositionFromTank(level, posTarget, themFacingMe, tank, toFlow);
  }

  public void tryExtract(IItemHandler myself, Direction extractSide, int qty, ItemStackHandler nullableFilter) {
    if (myself == null || extractSide == null || !myself.getStackInSlot(0).isEmpty()) {
      return;
    }
    BlockPos posTarget = worldPosition.relative(extractSide);
    BlockEntity tile = level.getBlockEntity(posTarget);
    if (tile != null) {
      IItemHandler itemHandlerFrom = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, extractSide.getOpposite()).orElse(null);
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

  public boolean moveItemToCompost(Direction exportToSide, ItemStackHandler inventorySelf) {
    BlockState bsTarget = this.level.getBlockState(worldPosition.relative(exportToSide));
    if (bsTarget.getBlock() instanceof ComposterBlock com
        && bsTarget.getValue(ComposterBlock.LEVEL) < ComposterBlock.MAX_LEVEL) {
      //its not a BlockEntity, its a Worldly
      Container compostContainer = ((WorldlyContainerHolder) bsTarget.getBlock()).getContainer(bsTarget, level, worldPosition.relative(exportToSide));
      for (int i = 0; i < compostContainer.getContainerSize(); i++) {
        ItemStack fromHopper = inventorySelf.extractItem(0, 1, true);
        if (!fromHopper.isEmpty()
            && compostContainer.canPlaceItem(i, fromHopper)
            && ComposterBlock.COMPOSTABLES.containsKey(fromHopper.getItem())) {
          //yes isvalid and allowed
          compostContainer.setItem(i, fromHopper);
          compostContainer.setChanged();
          inventorySelf.extractItem(0, 1, false);
          return true;
        }
      }
    }
    return false;
  }
  //
  //
  //

  public boolean moveItems(Direction myFacingDir, int max, IItemHandler handlerHere) {
    return moveItems(myFacingDir, this.worldPosition.relative(myFacingDir), max, handlerHere, 0);
  }

  public boolean moveItemsDimensional(BlockPosDim loc, int max, IItemHandler handlerHere, int theslot) {
    Direction myFacingDir = loc.getSide();
    final Direction themFacingMe = myFacingDir.getOpposite();
    ServerLevel serverWorld = loc.getServerLevel(level.getServer());
    return moveItemsInternal(max, handlerHere, theslot, themFacingMe, serverWorld.getBlockEntity(loc.getPos()));
  }

  public boolean moveItems(Direction myFacingDir, BlockPos posTarget, int max, IItemHandler handlerHere, int theslot) {
    if (max <= 0 || this.level.isClientSide()) {
      return false;
    }
    //first get the original ItemStack as creating new ones is expensive
    final Direction themFacingMe = myFacingDir.getOpposite();
    final BlockEntity tileTarget = level.getBlockEntity(posTarget);
    return moveItemsInternal(max, handlerHere, theslot, themFacingMe, tileTarget);
  }

  private static boolean moveItemsInternal(int max, IItemHandler handlerHere, int theslot, final Direction themFacingMe, final BlockEntity tileTarget) {
    if (max <= 0 || tileTarget == null || handlerHere == null) {
      return false;
    }
    final ItemStack originalItemStack = handlerHere.getStackInSlot(theslot);
    if (originalItemStack.isEmpty()) {
      return false;
    }
    // IItemHandler handlerOutput = tileTarget.getCapability(ForgeCapabilities.ITEM_HANDLER, themFacingMe).orElse(null);
    final IItemHandler handlerOutput = tileTarget.getCapability(ForgeCapabilities.ITEM_HANDLER, themFacingMe).orElse(null);
    if (handlerOutput == null) {
      return false;
    }
    //first simulate 
    ItemStack drain = handlerHere.extractItem(theslot, max, true);
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
  //
  //
  //
  //
  //
  //

  protected boolean moveEnergy(Direction myFacingDir, int quantity) {
    return moveEnergy(myFacingDir, this.worldPosition.relative(myFacingDir), quantity);
  }

  protected boolean moveEnergyDimensional(final BlockPosDim loc, final int quantity) {
    //validation pre-move
    if (quantity <= 0) {
      return false;
    }
    if (this.level.isClientSide) {
      return false; //important to not desync cables 
    }
    Direction myFacingDir = loc.getSide();
    final IEnergyStorage handlerHere = this.getCapability(ForgeCapabilities.ENERGY, myFacingDir).orElse(null);
    ServerLevel serverWorld = loc.getServerLevel(level.getServer());
    final BlockEntity tileTarget = serverWorld.getBlockEntity(loc.getPos());
    final Direction themFacingMe = myFacingDir.getOpposite();
    return moveEnergyInternal(quantity, handlerHere, themFacingMe, tileTarget);
  }

  //assums posTarget is in the same dimension as this.world
  protected boolean moveEnergy(final Direction myFacingDir, final BlockPos posTarget, final int quantity) {
    //validation pre-move
    if (quantity <= 0) {
      return false;
    }
    if (this.level.isClientSide) {
      return false; //important to not desync cables 
    }
    final IEnergyStorage handlerHere = this.getCapability(ForgeCapabilities.ENERGY, myFacingDir).orElse(null);
    final Direction themFacingMe = myFacingDir.getOpposite();
    final BlockEntity tileTarget = level.getBlockEntity(posTarget);
    return moveEnergyInternal(quantity, handlerHere, themFacingMe, tileTarget);
  }

  private static boolean moveEnergyInternal(final int quantity, final IEnergyStorage handlerHere, final Direction themFacingMe, final BlockEntity tileTarget) {
    if (handlerHere == null) {
      return false;
    }
    if (tileTarget == null) {
      return false;
    }
    final IEnergyStorage handlerOutput = tileTarget.getCapability(ForgeCapabilities.ENERGY, themFacingMe).orElse(null);
    if (handlerOutput == null) {
      return false;
    }
    final int capacity = handlerOutput.getMaxEnergyStored() - handlerOutput.getEnergyStored();
    if (capacity <= 0) {
      return false;
    }
    //validation is done
    //next, simulate
    final int drain = handlerHere.extractEnergy(Math.min(quantity, capacity), true);
    if (drain <= 0) {
      return false;
    }
    //now push it into output, but find out what was ACTUALLY taken
    final int filled = handlerOutput.receiveEnergy(drain, false);
    if (filled <= 0) {
      return false;
    }
    //now actually drain that much from here
    final int drained = handlerHere.extractEnergy(filled, false);
    //sanity check
    if (drained != filled) {
      ModCyclic.LOGGER.error("Imbalance moving energy, extracted " + drained + " received " + filled);
    }
    if (tileTarget instanceof TileCableEnergy) {
      // not so compatible with other fluid systems. it will do i guess
      TileCableEnergy cable = (TileCableEnergy) tileTarget;
      cable.updateIncomingEnergyFace(themFacingMe);
    }
    return true;
  }

  @Override
  public void load(CompoundTag tag) {
    flowing = tag.getInt("flowing");
    needsRedstone = tag.getInt("needsRedstone");
    render = tag.getInt("renderParticles");
    timer = tag.getInt("timer");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("flowing", flowing);
    tag.putInt("needsRedstone", needsRedstone);
    tag.putInt("renderParticles", render);
    tag.putInt("timer", timer);
    super.saveAdditional(tag);
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
  public int getContainerSize() {
    return 0;
  }

  @Deprecated
  @Override
  public boolean isEmpty() {
    return true;
  }

  @Deprecated
  @Override
  public ItemStack getItem(int index) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public ItemStack removeItem(int index, int count) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public void setItem(int index, ItemStack stack) {}

  @Deprecated
  @Override
  public boolean stillValid(Player player) {
    return false;
  }

  @Deprecated
  @Override
  public void clearContent() {}

  public void setFieldString(int field, String value) {
    //for string field  
  }

  public String getFieldString(int field) {
    //for string field  
    return null;
  }

  public int getEnergy() {
    return this.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  public void setEnergy(int value) {
    IEnergyStorage energ = this.getCapability(ForgeCapabilities.ENERGY).orElse(null);
    if (energ != null && energ instanceof CustomEnergyStorage) {
      ((CustomEnergyStorage) energ).setEnergy(value);
    }
  }

  //fluid tanks have 'onchanged', energy caps do not
  protected void syncEnergy() {
    if (level.isClientSide == false && level.getGameTime() % 20 == 0) { //if serverside then 
      IEnergyStorage energ = this.getCapability(ForgeCapabilities.ENERGY).orElse(null);
      if (energ != null) {
        PacketRegistry.sendToAllClients(this.getLevel(), new PacketEnergySync(this.getBlockPos(), energ.getEnergyStored()));
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

  /**
   * beam render tick
   * 
   * @param level
   * @param pos
   * @param beamStuff
   */
  public static void updateBeam(Level level, BlockPos pos, com.lothrazar.cyclic.block.beaconpotion.BeamStuff beamStuff) {
    BlockPos blockpos;
    if (beamStuff.lastCheckY < pos.getY()) {
      blockpos = pos;
      beamStuff.checkingBeamSections = Lists.newArrayList();
      beamStuff.lastCheckY = pos.getY() - 1;
    }
    else {
      blockpos = new BlockPos(pos.getX(), beamStuff.lastCheckY + 1, pos.getZ());
    }
    BeaconBlockEntity.BeaconBeamSection beaconblockentity$beaconbeamsection = beamStuff.checkingBeamSections.isEmpty() ? null : beamStuff.checkingBeamSections.get(beamStuff.checkingBeamSections.size() - 1);
    int surfaceHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
    for (int yLoop = 0; yLoop < 10 && blockpos.getY() <= surfaceHeight; ++yLoop) {
      BlockState blockstate = level.getBlockState(blockpos);
      // important: start one up OR give your beacon block an override to getBeaconColorMultiplier
      float[] colorMult = blockstate.getBeaconColorMultiplier(level, blockpos, pos);
      if (colorMult != null) {
        if (beamStuff.checkingBeamSections.size() <= 1) {
          beaconblockentity$beaconbeamsection = new BeaconBlockEntity.BeaconBeamSection(colorMult);
          beamStuff.checkingBeamSections.add(beaconblockentity$beaconbeamsection);
        }
        else if (beaconblockentity$beaconbeamsection != null) {
          float[] col = beaconblockentity$beaconbeamsection.getColor();
          if (Arrays.equals(colorMult, col)) {
            beaconblockentity$beaconbeamsection.increaseHeight();
          }
          else {
            beaconblockentity$beaconbeamsection = new BeaconBlockEntity.BeaconBeamSection(new float[] { (col[0] + colorMult[0]) / 2.0F, (col[1] + colorMult[1]) / 2.0F, (col[2] + colorMult[2]) / 2.0F });
            beamStuff.checkingBeamSections.add(beaconblockentity$beaconbeamsection);
          }
        }
      }
      else {
        //        .println("     null color so check bedrock from state=" + blockstate);
        if (beaconblockentity$beaconbeamsection == null || blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK)) {
          beamStuff.checkingBeamSections.clear();
          //cancel does work but shoots thru sht. prevents us stopping at day zero
          //.out.print("CANCELLED why reset to surface height here " + lastCheckY + " becomes " + surfaceHeight);
          beamStuff.lastCheckY = surfaceHeight;
          break;
        }
        if (beaconblockentity$beaconbeamsection != null)
          beaconblockentity$beaconbeamsection.increaseHeight();
      }
      blockpos = blockpos.above();
      ++beamStuff.lastCheckY;
      //.out.println("     move up=" + lastCheckY);
    }
    if (level.getGameTime() % 80L == 0L) {
      if (!beamStuff.beamSections.isEmpty()) {
        //        applyEffects(p_155108_, p_155109_, this.levels, this.primaryPower, this.secondaryPower);
        SoundUtil.playSound(level, pos, SoundEvents.BEACON_AMBIENT);
      }
    }
    if (beamStuff.lastCheckY >= surfaceHeight) {
      beamStuff.lastCheckY = level.getMinBuildHeight() - 1;
      beamStuff.beamSections = beamStuff.checkingBeamSections;
    }
  }
}
