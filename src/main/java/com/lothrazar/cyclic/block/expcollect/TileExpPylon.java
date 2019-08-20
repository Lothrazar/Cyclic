package com.lothrazar.cyclic.block.expcollect;

import java.util.List;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.util.TileEntityBase;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileExpPylon extends TileEntityBase implements ITickableTileEntity {

  private static final int RADIUS = 8;
  private static final int MAX = 64 * 64;
  private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
  private int storedXp;

  public TileExpPylon() {
    super(CyclicRegistry.experience_pylontile);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(2);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return handler.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    setStoredXp(tag.getInt("storedXp"));
    CompoundNBT invTag = tag.getCompound("inv");
    handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("storedXp", getStoredXp());
    handler.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world.isRemote) {
      return;
    }
    List<ExperienceOrbEntity> list = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, new AxisAlignedBB(
        pos.getX() - RADIUS, pos.getY() - 1, pos.getZ() - RADIUS,
        pos.getX() + RADIUS, pos.getY() + 2, pos.getZ() + RADIUS), (entity) -> {
          return entity.isAlive() && entity.getXpValue() > 0;//entity != null && entity.getHorizontalFacing() == facing;
        });
    if (list.size() > 0) {
      ExperienceOrbEntity myOrb = list.get(world.rand.nextInt(list.size()));
      int addMe = myOrb.getXpValue();
      if (getStoredXp() + addMe <= MAX) {
        myOrb.xpValue = 0;
        // myOrb.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        myOrb.remove();
        setStoredXp(getStoredXp() + addMe);
      }
    }
  }

  public int getStoredXp() {
    return storedXp;
  }

  public boolean drainStoredXp(int drainMe) {
    if (drainMe > this.getStoredXp()) {
      return false;
    }
    this.setStoredXp(this.getStoredXp() - drainMe);
    return true;
  }

  public void setStoredXp(int storedXp) {
    this.storedXp = storedXp;
  }
}
