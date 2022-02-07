package com.lothrazar.cyclic.block.user;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.lang.ref.WeakReference;
import java.util.UUID;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileUser extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  public static IntValue POWERCONF;

  static enum Fields {
    REDSTONE, TIMER, TIMERDEL, RENDER, LEFTHAND;
  }

  ItemStackHandler inventory = new ItemStackHandler(1);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int timerDelay = 20;
  static final int MAX = 640000;
  private boolean useLeftHand = false;

  public TileUser() {
    super(TileRegistry.user);
    this.needsRedstone = 1;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (world.isRemote || !(world instanceof ServerWorld)) {
      return;
    }
    if (timer > 0) {
      timer--;
      return;
    }
    //timer is zero so trigger
    timer = timerDelay;
    if (fakePlayer == null) {
      if (uuid == null) {
        uuid = UUID.randomUUID();
      }
      fakePlayer = setupBeforeTrigger((ServerWorld) world, "user", uuid);
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
      TileEntityBase.tryEquipItem(inventoryCap, fakePlayer, 0, Hand.MAIN_HAND);
      //start of SUPERHACK
      ResourceLocation registryItem = fakePlayer.get().getHeldItem(Hand.MAIN_HAND).getItem().getRegistryName();
      if (registryItem.getNamespace().equalsIgnoreCase("mysticalagriculture")
          && registryItem.getPath().contains("watering_can") &&
          fakePlayer.get().getHeldItem(Hand.MAIN_HAND).getTag() != null) {
        //        boolean water = fakePlayer.get().getHeldItem(Hand.MAIN_HAND).getTag().getBoolean("Water");
        ModCyclic.LOGGER.info(registryItem + " id hack ");
        //hack around mysttical ag id throttling   fail system 
        //when they fill water, id is set. uses id and gametime 
        //to reject actions to 'throttle'. but fakeplayer confuses this
        fakePlayer.get().getHeldItem(Hand.MAIN_HAND).getTag().putString("ID", UUID.randomUUID().toString());
        //after this hack. they still return type FAIL
        //but the plants grow and the watering DOES happen
        //        https://github.com/BlakeBr0/MysticalAgriculture/blob/f60de3510c694082acf5ff63299f119ab4a9d9a9/src/main/java/com/blakebr0/mysticalagriculture/item/WateringCanItem.java#L144
        //so successful hack
      }
      //end of SUPERHACK
      BlockPos target = this.pos.offset(this.getCurrentFacing());
      if (useLeftHand) {
        TileEntityBase.leftClickBlock(fakePlayer, target, null);
      }
      else {
        TileEntityBase.rightClickBlock(fakePlayer, world, target, Hand.MAIN_HAND, null);
      }
      // ModCyclic.LOGGER.info(result + " user resut " + target + "; held = " + fakePlayer.get().getHeldItem(Hand.MAIN_HAND));
      TileEntityBase.syncEquippedItem(inventoryCap, fakePlayer, 0, Hand.MAIN_HAND);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("User action item error", e);
    }
    tryDumpFakePlayerInvo(fakePlayer, false);
  }

  public boolean isUsingLeftHand() {
    return useLeftHand;
  }

  public void setUseLeftHand(final boolean useLeftHand) {
    this.useLeftHand = useLeftHand;
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
        this.render = value % 2;
      break;
      case LEFTHAND:
        this.useLeftHand = value == 1;
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
        return this.timerDelay;
      case RENDER:
        return render;
      case LEFTHAND:
        return this.isUsingLeftHand() ? 1 : 0;
    }
    return 0;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    timerDelay = tag.getInt("delay");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("uuid")) {
      uuid = tag.getUniqueId("uuid");
    }
    useLeftHand = tag.getBoolean("uselefthand");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("delay", timerDelay);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    if (uuid != null) {
      tag.putUniqueId("uuid", uuid);
    }
    tag.putBoolean("uselefthand", useLeftHand);
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerUser(i, world, pos, playerInventory, playerEntity);
  }
}
