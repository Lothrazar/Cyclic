package com.lothrazar.cyclicmagic.component.magnet;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityMagnet extends TileEntityBaseMachine implements ITickable {
  private int timer;
  private static final String NBT_TIMER = "Timer";
  public static int TIMER_FULL = 100;
  public static int ITEM_VRADIUS = 2;
  public static int ITEM_HRADIUS = 16;
  public TileEntityMagnet() {
    this.timer = TIMER_FULL;
  }
  @Override
  public ITextComponent getDisplayName() {
    return null;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tagCompound);
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  public boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());
  }
  @Override
  public void update() {
    if (this.isPowered()) {
      return;
    }
    boolean trigger = false;
    timer -= this.getSpeed();
    if (timer <= 0) {
      timer = TIMER_FULL;
      trigger = true;
    }
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 0.7;
    double z = this.getPos().getZ() + 0.5;
    if (trigger) {
      UtilEntity.moveEntityItemsInRegion(this.getWorld(), x, y, z, ITEM_HRADIUS, ITEM_VRADIUS, true);
      timer = TIMER_FULL;//harvest worked!
    }
  }
  private int getSpeed() {
    return 1;
  }
}
