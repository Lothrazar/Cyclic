package com.lothrazar.cyclic.block.expcollect;

import java.util.List;
import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.FluidHelpersUtil;
import com.lothrazar.library.util.PlayerUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.minecraft.core.Direction;

public class TileExpPylon extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, COLLECT;
  }


  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
  public static final int EXP_PER_BOTTLE = 11;
  public static final int CAPACITY = 64000 * FluidType.BUCKET_VOLUME;
  public static ModConfigSpec.IntValue RADIUS;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  private int collect = 1;


  public TileExpPylon(BlockPos pos, BlockState state) {
    super(TileRegistry.EXPERIENCE_PYLON.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileExpPylon e) {
    e.tick();
  }

  public void tick() {
    if (!level.isClientSide()) {
      //ignore on/off state, for player standing on top collecting exp
      collectPlayerExperience();
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    //if turned on, collect from the world
    if(this.collect != 0) {
      collectLocalExperience();
    }
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> FluidHelpersUtil.matches(p.getFluid(), DataTags.EXPERIENCE);
  }


  @Override
  public void loadAdditional(ValueInput input) {
    tank.deserialize(input.childOrEmpty(NBTFLUID));
    int legacy = input.getIntOr("storedXp", 0);
    if (legacy > 0) {
      tank.setFluid(new FluidStack(FluidXpJuiceHolder.STILL.get(), legacy * FLUID_PER_EXP));
    }
    this.collect = input.getIntOr("collect", 1);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    tank.serialize(output.child(NBTFLUID));
    output.putInt("storedXp", getStoredXp());
    output.putInt("collect", this.collect);
    super.saveAdditional(output);
  }

  private void collectPlayerExperience() {
    List<Player> players = level.getEntitiesOfClass(Player.class,
        new AABB(this.getBlockPos().above()));
    for (Player p : players) {
      double myTotal = PlayerUtil.getExpTotal(p);
      if (p.isCrouching() && myTotal > 0) {
        //go
        int addMeXp = 1;
        //now wait.
        //depending what level the player is, increase how much we pull per tick 
        if (p.experienceLevel > 300) {
          addMeXp = 1800;
        }
        if (p.experienceLevel > 100) {
          addMeXp = 900;
        }
        else if (p.experienceLevel > 50) {
          addMeXp = 200;
        }
        else if (p.experienceLevel > 30) {
          addMeXp = 50;
        }
        else if (p.experienceLevel > 5) {
          addMeXp = 10;
        }
        else {
          addMeXp = 1;
        }
        //
        int addMeFluid = addMeXp * FLUID_PER_EXP;
        //at level 100+ this is way too slow
        if (tank.getFluidAmount() + addMeFluid <= tank.getCapacity()) {
          p.giveExperiencePoints(-1 * addMeXp);
          tank.fill(new FluidStack(FluidXpJuiceHolder.STILL.get(), addMeFluid), IFluidHandler.FluidAction.EXECUTE);
          SoundUtil.playSound(p, SoundEvents.EXPERIENCE_ORB_PICKUP);
          this.setChanged();
        }
      }
    }
  }

  private void collectLocalExperience() {
    final int radius = RADIUS.get();
    List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, new AABB(
        worldPosition.getX() - radius, worldPosition.getY() - 1, worldPosition.getZ() - radius,
        worldPosition.getX() + radius, worldPosition.getY() + 2, worldPosition.getZ() + radius), (entity) -> {
          return entity.isAlive() && entity.getValue() > 0;
          //entity != null && entity.getHorizontalFacing() == facing;
        });
    if (list.size() > 0) {
      ExperienceOrb myOrb = list.get(level.getRandom().nextInt(list.size()));
      int addMeXp = myOrb.getValue();
      if (getStoredXp() + addMeXp <= tank.getCapacity()) {
        myOrb.value = 0;
        // myOrb.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        myOrb.remove(Entity.RemovalReason.DISCARDED);
        int addMeFluid = addMeXp * FLUID_PER_EXP;
        tank.fill(new FluidStack(FluidXpJuiceHolder.STILL.get(), addMeFluid), IFluidHandler.FluidAction.EXECUTE);
      }
    }
  }

  /**
   * for server->client sync. so technically client only
   */
  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  public int getStoredXp() {
    return tank.getFluidAmount() / FLUID_PER_EXP;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case COLLECT:
        this.collect = value % 2;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case COLLECT:;
        return this.collect;
    }
    return 0;
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerExpPylon(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.EXPERIENCE_PYLON.get().getName();
  }
}
