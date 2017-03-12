package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityXpPylon extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
//  private static final int MIN_RANGE = 1;
  public static final int TIMER_FULL = 30;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private static final String NBT_EXP = "particles";
  private static final String NBT_MODE = "pushpull";
  private static final String NBT_RANGE = "range";
  public static final int MAX_EXP_HELD = 50;
//  private static final int MAX_RANGE = 32;
  public static enum Fields {
    TIMER, REDSTONE, EXP, MODE;
  }
  public static enum Mode{
    PULL, SPEW;//pull in from world, or spew into world
  }
  private int timer;
  private int needsRedstone = 1;
  private int pushIfZero = 0;//else pull. 0 as default
  private int currentExp = 0;// 0 as default
//  private int speedBase = 13;//divide by 100 for real speed. bigger=faster
  private int range = 2;
  @Override
  public void update() {
//    if (this.isRunning() == false) {
//      this.timer = 0;
//      return;
//    }
//    this.timer--;
    List<EntityXPOrb> orbs = getWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(this.getPos().up()).expandXyz(range));
    if(orbs == null ){return;    }
    //TODO: timer?
    for (EntityXPOrb orb : orbs) {
      if(orb.getXpValue() + this.currentExp <= MAX_EXP_HELD){
        this.currentExp += orb.getXpValue();
//        System.out.println(this.currentExp);
        this.getWorld().removeEntity(orb);
        
        
      }
      else{//is full
//        System.out.println("launch");
        orb.addVelocity(Math.random()/100, 0.09, Math.random()/100);
      }
    }
  }
  

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setInteger(NBT_EXP, this.currentExp);
    tags.setInteger(NBT_MODE, this.pushIfZero);
    tags.setInteger(NBT_RANGE, this.range);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    needsRedstone = tags.getInteger(NBT_REDST);
    this.currentExp = tags.getInteger(NBT_EXP);
    this.pushIfZero = tags.getInteger(NBT_MODE);
    this.range = tags.getInteger(NBT_RANGE);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    this.setField(Fields.REDSTONE.ordinal(), val % 2);
  }
  private void setExp(int value) {
    this.currentExp = value;
  }
  private void setMode(int value) {
    this.pushIfZero = value % 2;
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          return timer;
        case REDSTONE:
          return this.needsRedstone;
        case EXP:
          return this.currentExp;
        case MODE:
          return this.pushIfZero;
//        case RANGE:
//          return this.range;
      }
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          this.timer = value;
        break;
        case REDSTONE:
          this.needsRedstone = value;
        break;
        case EXP:
          this.setExp(value);
        break;
        case MODE:
          this.setMode(value);
        break;
//        case RANGE:
//          this.setRange(value);
//        break;
      }
    }
  }
}
