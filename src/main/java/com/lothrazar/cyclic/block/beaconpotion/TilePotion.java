package com.lothrazar.cyclic.block.beaconpotion;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePotion extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int MAX_POTION = 16000;
  private static final int POTION_TICKS = 20 * 20;//cant be too low BC night vision flicker
  //  private static final int MAX_RADIUS = 8;
  int radius = 9;
  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  /** Primary potion effect given by this beacon. */
  @Nullable
  private List<EffectInstance> effects;

  public TilePotion() {
    super(TileRegistry.beacon);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerPotion(i, world, pos, playerInventory, playerEntity);
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
  public void read(BlockState bs, CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
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
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setAnimation(false);
      return;
    }
    setAnimation(true);
    timer--;
    if (timer > 0) {
      return;
    }
    IEnergyStorage en = this.energy.orElse(null);
    IItemHandler inv = this.inventory.orElse(null);
    if (en == null || inv == null) {
      return;
    }
    ItemStack s = inv.getStackInSlot(0);
    List<EffectInstance> newEffects = PotionUtils.getEffectsFromStack(s);
    if (newEffects.size() > 0) {
      //
      effects = new ArrayList<EffectInstance>();
      //first read all potins
      for (EffectInstance eff : newEffects) {
        if (this.isPotionValid(eff))
          //cannot set the duration time so we must copy it
          effects.add(new EffectInstance(eff.getPotion(), POTION_TICKS, eff.getAmplifier(), true, false));
      }
      //then refil progress bar
      this.timer = MAX_POTION;
      //then consume the item, unless disabled
      //        if (doesConsumePotions) {
      this.setInventorySlotContents(0, ItemStack.EMPTY);
      //        }
    }
    addEffectsToEntities();
  }

  private void addEffectsToEntities() {
    if (this.effects == null || this.effects.size() == 0) {
      return;
    }
    int x = this.pos.getX();
    int y = this.pos.getY();
    int z = this.pos.getZ();
    int theRadius = ((int) Math.pow(2, this.radius));
    AxisAlignedBB axisalignedbb = (new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)).grow(theRadius).expand(0.0D, this.world.getHeight(), 0.0D);
    //get players, or non players, or both. but players extend living base too.
    //    boolean skipPlayers = false;//(this.entityType == EntityType.NONPLAYER);
    boolean showParticles = false;//(this.entityType == EntityType.PLAYERS);
    //    EnumCreatureType creatureType = this.getCreatureType();
    List<PlayerEntity> list = new ArrayList<PlayerEntity>();
    //    if (this.entityType == EntityType.PLAYERS) {
    list.addAll(this.world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb));
    //    }
    //    else { // we apply other filters later
    //      list.addAll(this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb));
    //    }
    for (PlayerEntity entity : list) {
      for (EffectInstance eff : this.effects) {
        if (entity.getActivePotionEffect(eff.getPotion()) != null) {
          //important to use combine for thing effects that apply attributes such as health
          entity.getActivePotionEffect(eff.getPotion()).combine(eff);
        }
        else {
          entity.addPotionEffect(new EffectInstance(eff.getPotion(), POTION_TICKS, eff.getAmplifier(), true, showParticles));
        }
      }
    }
  }

  /**
   * non-harmful, non-instant
   */
  private boolean isPotionValid(EffectInstance eff) {
    return eff.getPotion().isBeneficial() && !eff.getPotion().isInstant();
  }

  @Override
  public void setField(int field, int value) {}
}
