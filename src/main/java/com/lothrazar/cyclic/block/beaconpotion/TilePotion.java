package com.lothrazar.cyclic.block.beaconpotion;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.data.EntityFilterType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePotion extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private static final int TICKS_FIRE_PER = 60;
  //so if a potion has a duration of 1 second, use this many ticks
  static final int TICKS_PER_DURATION = 160000;
  private static final int POTION_TICKS = 20 * 20;//cant be too low BC night vision flicker
  //  private static final int MAX_RADIUS = 8;
  static final int MAX = 64000;
  private static final int MAX_RADIUS = 64;
  public static IntValue POWERCONF;
  private int radius = MAX_RADIUS;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  /** Primary potion effect given by this beacon. */
  private List<EffectInstance> effects = new ArrayList<>();
  EntityFilterType entityFilter = EntityFilterType.PLAYERS;

  static enum Fields {
    TIMER, REDSTONE, RANGE, ENTITYTYPE;
  }

  public TilePotion() {
    super(TileRegistry.beacon);
    timer = 0;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (effects.size() == 0) {
      timer = 0;
    }
    IEnergyStorage en = this.energy.orElse(null);
    IItemHandler inv = this.inventory.orElse(null);
    final int repair = POWERCONF.get();
    if (en == null || inv == null
        || en.getEnergyStored() < repair) {
      if (repair > 0)
        return;
    }
    timer--;
    if (timer > 0) {// && timer % 60 == 0
      tryAffectEntities(en, repair);
      return;
    }
    //timer is <=zero, delete all effects
    effects.clear();
    //
    ItemStack s = inv.getStackInSlot(0);
    if (s.isEmpty()) {
      return;
    }
    List<EffectInstance> newEffects = PotionUtils.getEffectsFromStack(s);
    if (newEffects.size() > 0) {
      pullFromItem(inv, newEffects);
    }
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.hasEffect();
      }
    };
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
    this.radius = tag.getInt("radius");
    entityFilter = EntityFilterType.values()[tag.getInt("entityFilter")];
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    if (tag.contains("Effects", 9)) {
      ListNBT listnbt = tag.getList("Effects", 10);
      this.effects.clear();
      for (int i = 0; i < listnbt.size(); ++i) {
        EffectInstance effectinstance = EffectInstance.read(listnbt.getCompound(i));
        if (effectinstance != null) {
          effects.add(effectinstance);
        }
      }
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("radius", radius);
    tag.putInt("entityFilter", entityFilter.ordinal());
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    //    CompoundNBT pots = new CompoundNBT();
    if (!this.effects.isEmpty()) {
      ListNBT listnbt = new ListNBT();
      for (EffectInstance effectinstance : this.effects) {
        listnbt.add(effectinstance.write(new CompoundNBT()));
      }
      tag.put("Effects", listnbt);
    }
    return super.write(tag);
  }

  private void pullFromItem(IItemHandler inv, List<EffectInstance> newEffects) {
    //add new effects
    this.timer = TICKS_PER_DURATION;
    setLitProperty(true);
    //first read all potins
    int maxDur = 0;
    for (EffectInstance eff : newEffects) {
      if (this.isPotionValid(eff)) { //cannot set the duration time so we must copy it
        effects.add(new EffectInstance(eff.getPotion(), POTION_TICKS, eff.getAmplifier(), true, false));
        maxDur = Math.max(eff.getDuration(), maxDur);
      }
    }
    inv.extractItem(0, 1, false);
  }

  private void tryAffectEntities(IEnergyStorage en, final int repair) {
    if (timer % TICKS_FIRE_PER == 0
        && effects.size() > 0
        && en.getEnergyStored() >= repair) {
      int affected = affectEntities();
      if (affected > 0)
        en.extractEnergy(repair, false);
    }
  }

  private int affectEntities() {
    boolean showParticles = false;//(this.entityType == EntityType.PLAYERS);
    //    EnumCreatureType creatureType = this.getCreatureType();
    int affecdted = 0;
    List<? extends LivingEntity> list = this.entityFilter.getEntities(world, pos, radius);
    for (LivingEntity entity : list) {
      if (entity == null) {
        continue;
      }
      for (EffectInstance eff : this.effects) {
        affecdted++;
        if (entity.isPotionActive(eff.getPotion())) {
          //important to use combine for thing effects that apply attributes such as health
          entity.getActivePotionEffect(eff.getPotion()).combine(eff);
        }
        else {
          entity.addPotionEffect(new EffectInstance(eff.getPotion(), POTION_TICKS, eff.getAmplifier(), true, showParticles));
        }
      }
    }
    return affecdted;
  }

  /**
   * non-harmful, non-instant
   */
  private boolean isPotionValid(EffectInstance eff) {
    return eff.getPotion().isBeneficial() && !eff.getPotion().isInstant();
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case ENTITYTYPE:
        return this.entityFilter.ordinal();
      case RANGE:
        return this.radius;
    }
    return -1;
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
      case ENTITYTYPE:
        value = value % EntityFilterType.values().length;
        //        if (value >= EntityFilterType.values().length)
        //          value = 0;
        //        if (value < 0)
        //          value = EntityFilterType.values().length - 1;
        this.entityFilter = EntityFilterType.values()[value];
      break;
      case RANGE:
        if (value > MAX_RADIUS)
          radius = MAX_RADIUS;
        else
          this.radius = Math.min(value, MAX_RADIUS);
      break;
    }
  }

  public List<String> getPotionDisplay() {
    List<String> list = new ArrayList<>();
    for (EffectInstance eff : this.effects) {
      list.add(eff.getEffectName());
    }
    return list;
  }

  public String getTimerDisplay() {
    if (this.effects.size() == 0) {
      return "cyclic.gui.empty";
    }
    return this.getTimerSeconds() + " seconds";
  }

  private int getTimerSeconds() {
    return timer / 20;
  }
}
