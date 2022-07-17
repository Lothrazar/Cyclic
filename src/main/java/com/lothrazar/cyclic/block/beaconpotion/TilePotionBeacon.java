package com.lothrazar.cyclic.block.beaconpotion;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.data.EntityFilterType;
import com.lothrazar.cyclic.item.datacard.EntityDataCard;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePotionBeacon extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    TIMER, REDSTONE, RANGE, ENTITYTYPE;
  }

  static final int MAX = 64000;
  private static final int TICKS_FIRE_PER = 60;
  //so if a potion has a duration of 1 second, use this many ticks
  static final int TICKS_PER_DURATION = 160000;
  private static final int POTION_TICKS = 20 * 20; //cant be too low BC night vision flicker
  private static final int MAX_RADIUS = 64;
  private int radius = MAX_RADIUS;
  public static IntValue POWERCONF;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.ENTITY_DATA.get();
    }
  };
  /**
   * Primary potion effect given by this beacon.
   */
  private List<MobEffectInstance> effects = new ArrayList<>();
  EntityFilterType entityFilter = EntityFilterType.PLAYERS;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      List<MobEffectInstance> newEffects = PotionUtils.getMobEffects(stack);
      return newEffects.size() > 0;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private BeamStuff beamStuff = new BeamStuff();

  public TilePotionBeacon(BlockPos pos, BlockState state) {
    super(TileRegistry.BEACON.get(), pos, state);
    timer = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePotionBeacon e) {
    e.tick(level, blockPos, blockState);
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TilePotionBeacon e) {
    e.tick(level, blockPos, blockState);
  }

  //  @Override
  public void tick(Level level, BlockPos pos, BlockState p_155110_) {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (effects.size() == 0) {
      timer = 0;
    }
    int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && (cost > 0)) {
      return;
    }
    energy.extractEnergy(cost, false);
    timer--;
    if (timer > 0) {
      setLitProperty(true);
      tryAffectEntities(cost);
    }
    else {
      //timer is <=zero, delete all effects
      effects.clear();
      ItemStack s = inventory.getStackInSlot(0);
      if (!s.isEmpty()) {
        List<MobEffectInstance> newEffects = PotionUtils.getMobEffects(s);
        if (newEffects.size() > 0) {
          pullFromItem(newEffects);
        }
      }
      return;
    } //end of timer/potion checks 
    updateBeam(level, pos, beamStuff);
  }

  @Override
  public void setLevel(Level p_155091_) {
    super.setLevel(p_155091_);
    beamStuff.lastCheckY = p_155091_.getMinBuildHeight() - 1;
  }

  public List<BeaconBlockEntity.BeaconBeamSection> getBeamSections() {
    return beamStuff.beamSections;
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPotion(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    filter.deserializeNBT(tag.getCompound("filter"));
    this.radius = tag.getInt("radius");
    entityFilter = EntityFilterType.values()[tag.getInt("entityFilter")];
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("Effects", 9)) {
      ListTag listnbt = tag.getList("Effects", 10);
      this.effects.clear();
      for (int i = 0; i < listnbt.size(); ++i) {
        MobEffectInstance effectinstance = MobEffectInstance.load(listnbt.getCompound(i));
        if (effectinstance != null) {
          effects.add(effectinstance);
        }
      }
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("radius", radius);
    tag.putInt("entityFilter", entityFilter.ordinal());
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    //
    if (!this.effects.isEmpty()) {
      ListTag listnbt = new ListTag();
      for (MobEffectInstance effectinstance : this.effects) {
        listnbt.add(effectinstance.save(new CompoundTag()));
      }
      tag.put("Effects", listnbt);
    }
    super.saveAdditional(tag);
  }

  private void pullFromItem(List<MobEffectInstance> newEffects) {
    //add new effects
    this.timer = TICKS_PER_DURATION;
    setLitProperty(true);
    //first read all potins
    int maxDur = 0;
    for (MobEffectInstance eff : newEffects) {
      if (this.isPotionValid(eff)) { //cannot set the duration time so we must copy it
        effects.add(new MobEffectInstance(eff.getEffect(), POTION_TICKS, eff.getAmplifier(), true, false));
        maxDur = Math.max(eff.getDuration(), maxDur);
      }
    }
    inventory.extractItem(0, 1, false);
  }

  private void tryAffectEntities(final int repair) {
    if (timer % TICKS_FIRE_PER == 0
        && effects.size() > 0
        && energy.getEnergyStored() >= repair) {
      int affected = affectEntities();
      if (affected > 0) {
        energy.extractEnergy(repair, false);
      }
    }
  }

  private int affectEntities() {
    boolean showParticles = false;
    int affecdted = 0;
    List<? extends LivingEntity> list = this.entityFilter.getEntities(level, worldPosition, radius);
    for (LivingEntity entity : list) {
      if (entity == null) {
        continue;
      }
      if (EntityDataCard.hasEntity(filter.getStackInSlot(0))
          && !EntityDataCard.matchesEntity(entity, filter.getStackInSlot(0))) {
        //     .println("p!otion skip ent " + entity);
        continue;
      }
      for (MobEffectInstance eff : this.effects) {
        affecdted++;
        if (entity.hasEffect(eff.getEffect())) {
          //important to use combine for thing effects that apply attributes such as health
          entity.getEffect(eff.getEffect()).update(eff);
        }
        else {
          entity.addEffect(new MobEffectInstance(eff.getEffect(), POTION_TICKS, eff.getAmplifier(), true, showParticles));
        }
      }
    }
    return affecdted;
  }

  /**
   * non-harmful, non-instant
   */
  private boolean isPotionValid(MobEffectInstance eff) {
    return eff.getEffect().isBeneficial() && !eff.getEffect().isInstantenous();
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
        this.entityFilter = EntityFilterType.values()[value];
      break;
      case RANGE:
        if (value > MAX_RADIUS) {
          radius = MAX_RADIUS;
        }
        else {
          this.radius = Math.min(value, MAX_RADIUS);
        }
      break;
    }
  }

  public List<String> getPotionDisplay() {
    List<String> list = new ArrayList<>();
    for (MobEffectInstance eff : this.effects) {
      list.add(eff.getDescriptionId());
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
