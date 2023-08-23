package com.lothrazar.cyclic.block.user;

import java.lang.ref.WeakReference;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.CustomEnergyStorage;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileUser extends TileBlockEntityCyclic implements MenuProvider {

  public static IntValue POWERCONF;
  static final int MAX = 640000;

  static enum Fields {
    REDSTONE, TIMER, TIMERDEL, RENDER, INTERACTTYPE, ENTITIES;
  }

  ItemStackHandler userSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlots = new ItemStackHandler(4);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  //  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> userSlots);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(userSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private WeakReference<FakePlayer> fakePlayer;
  private int timerDelay = 20;
  boolean doHitBreak = false; // was useLeftHand
  boolean entities = false;

  public TileUser(BlockPos pos, BlockState state) {
    super(TileRegistry.USER.get(), pos, state);
    this.needsRedstone = 1;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileUser e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileUser e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (level.isClientSide) { //  || !(level instanceof ServerLevel)
      return;
    }
    if (timer > 0) {
      timer--;
      return;
    }
    //timer is zero so trigger
    timer = timerDelay;
    if (fakePlayer == null) {
      fakePlayer = setupBeforeTrigger((ServerLevel) level, "user");
    }
    final int repair = POWERCONF.get();
    if (repair > 0) {
      //we need to pay a cost
      if (energy.getEnergyStored() < repair) {
        //not enough cost
        return;
      }
      //i dont care if result is SUCCESS or FAIL. still drain power every time. 
      //user can turn off with redstone if they want to save power
      energy.extractEnergy(repair, false);
    }
    try {
      // Added to address the broken server side FakePlayer cooldowns we they
      // are not getting decremented causing any item with one to not function correctly.
      var cooldowns = fakePlayer.get().getCooldowns();
      TileBlockEntityCyclic.tryEquipItem(inventoryCap, fakePlayer, 0, InteractionHand.MAIN_HAND);
      var item = fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).getItem();
      var oldItem = item.asItem();
      if (cooldowns.isOnCooldown(item)) {
        cooldowns.removeCooldown(item);
      }
      BlockPos target = this.worldPosition.relative(this.getCurrentFacing());
      if (entities) {
        //do entities
        this.interactEntities(target);
      }
      else {
        //not entities so blocks
        if (doHitBreak) {
          TileBlockEntityCyclic.playerAttackBreakBlock(fakePlayer, level, target, InteractionHand.MAIN_HAND, this.getCurrentFacing());
        }
        else {
          TileBlockEntityCyclic.interactUseOnBlock(fakePlayer, level, target, InteractionHand.MAIN_HAND, null);
        }
      }
      boolean mainhandChanged = oldItem != userSlots.getStackInSlot(0).getItem();
      if (mainhandChanged) {
        this.depositOutputMainhand();
      }
      TileBlockEntityCyclic.syncEquippedItem(this.userSlots, fakePlayer, 0, InteractionHand.MAIN_HAND);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("User action item error", e);
    }
    tryDumpFakePlayerInvo(fakePlayer, this.outputSlots, false);
  }

  private void depositOutputMainhand() {
    var usedItem = fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND);
    for (int slotId = 0; slotId < outputSlots.getSlots(); slotId++) {
      if (!usedItem.isEmpty()) {
        //        usedItem = outputSlots.insertItem(slotId, usedItem.copy(), false);
        if (outputSlots.insertItem(slotId, usedItem.copy(), true).isEmpty()) {
          usedItem = outputSlots.insertItem(slotId, usedItem.copy(), false);
          //          userSlots.setStackInSlot(0, usedItem);
          TileBlockEntityCyclic.tryEquipItem(usedItem, fakePlayer, InteractionHand.MAIN_HAND);
        }
      }
    }
  }

  private void interactEntities(BlockPos target) {
    AABB ab = getEntityRange(target);
    this.level.getEntities(fakePlayer.get(), ab, EntitySelector.NO_SPECTATORS).forEach((entityFound) -> {
      //      ModCyclic.LOGGER.info(worldPosition + "| ??   " + fakePlayer.get().getMainHandItem());
      if (doHitBreak) {
        fakePlayer.get().attack(entityFound);
        //        ModCyclic.LOGGER.info(worldPosition + "| interactEntities ATTACK  " + e);
      }
      else { // interact 
        InteractionResult res = fakePlayer.get().interactOn(entityFound, InteractionHand.MAIN_HAND);
        if (res.consumesAction()) {
          ModCyclic.LOGGER.info(worldPosition + "| entity consume result detected " + res);
        }
      }
    });
  }

  private AABB getEntityRange(BlockPos target) {
    final int r = 1; // TODO radius controls in GUI
    AABB ab = new AABB(target.getX() + r, target.getY(), target.getZ() + r,
        target.getX() - r, target.getY() + 1, target.getZ() - r);
    return ab;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        this.timer = value;
      break;
      case TIMERDEL:
        this.timerDelay = value;
      break;
      case RENDER:
        this.render = value % PreviewOutlineType.values().length;
      break;
      case INTERACTTYPE: // was LEFTHAND
        this.doHitBreak = value == 1;
      break;
      case ENTITIES:
        this.entities = value == 1;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return timer;
      case TIMERDEL:
        return timerDelay;
      case RENDER:
        return render;
      case INTERACTTYPE:
        return doHitBreak ? 1 : 0;
      case ENTITIES:
        return entities ? 1 : 0;
    }
    return 0;
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    timerDelay = tag.getInt("delay");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    userSlots.deserializeNBT(tag.getCompound(NBTINV));
    doHitBreak = tag.getBoolean("doBreakBlock");
    entities = tag.getBoolean("entities");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("delay", timerDelay);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, userSlots.serializeNBT());
    tag.putBoolean("doBreakBlock", doHitBreak);
    tag.putBoolean("entities", entities);
    super.saveAdditional(tag);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.USER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerUser(i, level, worldPosition, playerInventory, playerEntity);
  }
}
