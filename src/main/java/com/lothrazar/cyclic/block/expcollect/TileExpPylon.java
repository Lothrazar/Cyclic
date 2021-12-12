package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilPlayer;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileExpPylon extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE;
  }

  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
  public static final int DRAIN_PLAYER_EXP = 20;
  public static final int EXP_PER_BOTTLE = 11;
  private static final int RADIUS = 16;
  public static final int CAPACITY = 64000 * FluidAttributes.BUCKET_VOLUME;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  private LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> tank);

  public TileExpPylon() {
    super(TileRegistry.experience_pylontile);
    this.needsRedstone = 0;
  }

  @Override
  public void tick() {
    if (world == null) {
      return;
    }
    if (!world.isRemote) {
      //ignore on/off state, for player standing on top collecting exp
      collectPlayerExperience();
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    //if turned on, collect from the world
    collectLocalExperience();
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> p.getFluid().isIn(DataTags.EXPERIENCE);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    fluidHandlerLazyOptional.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    int legacy = tag.getInt("storedXp");
    if (legacy > 0) {
      tank.setFluid(new FluidStack(FluidXpJuiceHolder.STILL.get(), legacy * FLUID_PER_EXP));
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.putInt("storedXp", getStoredXp());
    return super.write(tag);
  }

  private void collectPlayerExperience() {
    List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class,
        new AxisAlignedBB(this.getPos().up()));
    for (PlayerEntity p : players) {
      double myTotal = UtilPlayer.getExpTotal(p);
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
          tank.fill(new FluidStack(FluidXpJuiceHolder.STILL.get(), addMeFluid), FluidAction.EXECUTE);
          //  ModCyclic.LOGGER.info("tank.getFluidAmount() = " + tank.getFluidAmount());
          UtilSound.playSound(p, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
          this.markDirty();
        }
      }
    }
  }

  private void collectLocalExperience() {
    List<ExperienceOrbEntity> list = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, new AxisAlignedBB(
        pos.getX() - RADIUS, pos.getY() - 1, pos.getZ() - RADIUS,
        pos.getX() + RADIUS, pos.getY() + 2, pos.getZ() + RADIUS), (entity) -> {
          return entity.isAlive() && entity.getXpValue() > 0;
          //entity != null && entity.getHorizontalFacing() == facing;
        });
    if (list.size() > 0) {
      ExperienceOrbEntity myOrb = list.get(world.rand.nextInt(list.size()));
      int addMeXp = myOrb.getXpValue();
      if (getStoredXp() + addMeXp <= tank.getCapacity()) {
        myOrb.xpValue = 0;
        // myOrb.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        myOrb.remove();
        int addMeFluid = addMeXp * FLUID_PER_EXP;
        tank.fill(new FluidStack(FluidXpJuiceHolder.STILL.get(), addMeFluid), FluidAction.EXECUTE);
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
    if (Fields.values()[field] == Fields.REDSTONE) {
      this.needsRedstone = value % 2;
    }
  }

  @Override
  public int getField(int field) {
    if (Fields.values()[field] == Fields.REDSTONE) {
      return this.needsRedstone;
    }
    return 0;
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerExpPylon(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }
}
