package com.lothrazar.cyclic.block.user;

import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileUser extends TileBlockEntityCyclic implements MenuProvider {

  public static IntValue POWERCONF;

  static enum Fields {
    REDSTONE, TIMER, TIMERDEL, RENDER;
  }

  ItemStackHandler inventory = new ItemStackHandler(1);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int timerDelay = 20;
  static final int MAX = 640000;

  public TileUser(BlockPos pos, BlockState state) {
    super(TileRegistry.user, pos, state);
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
    if (level.isClientSide || !(level instanceof ServerLevel)) {
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
      fakePlayer = setupBeforeTrigger((ServerLevel) level, "user", uuid);
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
      TileBlockEntityCyclic.tryEquipItem(inventoryCap, fakePlayer, 0, InteractionHand.MAIN_HAND);
      //start of SUPERHACK
      ResourceLocation registryItem = fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).getItem().getRegistryName();
      if (registryItem.getNamespace().equalsIgnoreCase("mysticalagriculture")
          && registryItem.getPath().contains("watering_can") &&
          fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).getTag() != null) {
        //        boolean water = fakePlayer.get().getHeldItem(Hand.MAIN_HAND).getTag().getBoolean("Water");
        ModCyclic.LOGGER.info(registryItem + " id hack ");
        //hack around mysttical ag id throttling   fail system 
        //when they fill water, id is set. uses id and gametime 
        //to reject actions to 'throttle'. but fakeplayer confuses this
        fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).getTag().putString("ID", UUID.randomUUID().toString());
        //after this hack. they still return type FAIL
        //but the plants grow and the watering DOES happen
        //        https://github.com/BlakeBr0/MysticalAgriculture/blob/f60de3510c694082acf5ff63299f119ab4a9d9a9/src/main/java/com/blakebr0/mysticalagriculture/item/WateringCanItem.java#L144
        //so successful hack
      }
      //end of SUPERHACK
      BlockPos target = this.worldPosition.relative(this.getCurrentFacing());
      TileBlockEntityCyclic.rightClickBlock(fakePlayer, level, target, InteractionHand.MAIN_HAND, null);
      // ModCyclic.LOGGER.info(result + " user resut " + target + "; held = " + fakePlayer.get().getHeldItem(Hand.MAIN_HAND));
      TileBlockEntityCyclic.syncEquippedItem(inventoryCap, fakePlayer, 0, InteractionHand.MAIN_HAND);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("User action item error", e);
    }
    tryDumpFakePlayerInvo(fakePlayer, false);
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
  public void load(CompoundTag tag) {
    timerDelay = tag.getInt("delay");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("uuid")) {
      uuid = tag.getUUID("uuid");
    }
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.putInt("delay", timerDelay);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    if (uuid != null) {
      tag.putUUID("uuid", uuid);
    }
    return super.save(tag);
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerUser(i, level, worldPosition, playerInventory, playerEntity);
  }
}
