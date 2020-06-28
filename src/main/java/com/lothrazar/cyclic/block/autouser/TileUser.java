package com.lothrazar.cyclic.block.autouser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilFakePlayer;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileUser extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static final int MAX = 640000;

  public static enum Fields {
    REDSTONE, TIMER;
  }

  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int timerDelay = 20;

  public TileUser() {
    super(BlockRegistry.Tiles.user);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (!(world instanceof ServerWorld)) {
      return;
    }
    if (timer > 0) {
      timer--;
      return;
    }
    //timer is zero so trigger
    timer = timerDelay;
    setupBeforeTrigger((ServerWorld) world);
    //k is zero
    try {
      tryEquipItem();
      ActionResultType result = this.rightClickBlock(this.pos.offset(this.getCurrentFacing()));
      ModCyclic.LOGGER.info("user result " + result);
      if (result == ActionResultType.SUCCESS) {
        // DRAIN POWER
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Fake player failed to init ", e);
    }
    tryDumpFakePlayerInvo(false);
    //    for (int i = 0; i < ATTEMPTS_PERTICK; i++) {
    //      BlockPos target = UtilWorld.getRandomPos(world.rand, getPos(), RADIUS);
    //      if (this.tryHarvestSingle(target)) {
    //        IEnergyStorage cap = this.energy.orElse(null);
    //        cap.extractEnergy(ENERGY_COST, true);
    //        break;
    //      }
    //    }
  }

  private void tryDumpFakePlayerInvo(boolean includeMainHand) {
    int start = (includeMainHand) ? 0 : 1;//main hand is 1
    ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
    for (int i = start; i < fakePlayer.get().inventory.mainInventory.size(); i++) {
      ItemStack s = fakePlayer.get().inventory.mainInventory.get(i);
      if (s.isEmpty() == false) {
        toDrop.add(s.copy());
        fakePlayer.get().inventory.mainInventory.set(i, ItemStack.EMPTY);
      }
    }
    UtilItemStack.drop(world, this.pos.up(), toDrop);
  }

  private ActionResultType rightClickBlock(BlockPos targetPos) throws Exception {
    //do i have enough power?
    //if not return fail
    ModCyclic.LOGGER.info("interact START " + targetPos);
    Hand hand = Hand.MAIN_HAND;
    BlockRayTraceResult blockraytraceresult = new BlockRayTraceResult(fakePlayer.get().getLookVec(), fakePlayer.get().getAdjustedHorizontalFacing(), targetPos, false);
    //processRightClick
    return fakePlayer.get().interactionManager.func_219441_a(fakePlayer.get(), world,
        fakePlayer.get().getHeldItem(hand), hand, blockraytraceresult);
  }

  private void setupBeforeTrigger(ServerWorld sw) {
    if (fakePlayer == null) {
      this.uuid = UUID.randomUUID();
      fakePlayer = UtilFakePlayer.initFakePlayer(sw, this.uuid, "block_user");
      if (fakePlayer == null) {
        ModCyclic.LOGGER.error("Fake player failed to init ");
        return;
      }
      //fake player facing the same direction as tile. for throwables
      fakePlayer.get().setPosition(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());//seems to help interact() mob drops like milk
      fakePlayer.get().rotationYaw = UtilEntity.getYawFromFacing(this.getCurrentFacing());
    }
  }

  private void tryEquipItem() {
    inventory.ifPresent(inv -> {
      ItemStack maybeTool = inv.getStackInSlot(0);
      if (!maybeTool.isEmpty()) {
        if (maybeTool.getCount() <= 0) {
          maybeTool = ItemStack.EMPTY;
        }
      }
      if (!maybeTool.equals(fakePlayer.get().getHeldItem(Hand.MAIN_HAND))) {
        fakePlayer.get().setHeldItem(Hand.MAIN_HAND, maybeTool);
      }
    });
  }

  private Direction getCurrentFacing() {
    return this.world.getBlockState(pos).get(BlockStateProperties.FACING);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        setNeedsRedstone(value);
      break;
      case TIMER:
        this.timer = value;
      break;
      default:
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.getNeedsRedstone();
      case TIMER:
        return timer;
      default:
      break;
    }
    return 0;
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX / 4);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
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
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerUser(i, world, pos, playerInventory, playerEntity);
  }
}
