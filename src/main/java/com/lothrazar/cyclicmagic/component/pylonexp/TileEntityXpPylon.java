package com.lothrazar.cyclicmagic.component.pylonexp;
import java.util.List;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityXpPylon extends TileEntityBaseMachineInvo implements ITickable {
  private static final int XP_PER_SPEWORB = 10;
  private static final int XP_PER_BOTTLE = 11; // On impact with any non-liquid block it will drop experience orbs worth 3–11 experience points. 
  public static final int TIMER_FULL = 18;
  public static final int MAX_EXP_HELD = 20000;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_COLLECT = "collect";
  private static final String NBT_SPRAY = "spray";
  private static final String NBT_BOTTLE = "bottle";
  private static final String NBT_EXP = "particles";
  public final static int RADIUS = 5;
  private static final int[] SLOTS_EXTRACT = new int[] { SLOT_OUTPUT };
  private static final int[] SLOTS_INSERT = new int[] { SLOT_INPUT };
  public static enum Fields {
    TIMER, EXP, COLLECT, SPRAY, BOTTLE;//MIGHT remove redstone eh
  }
  private int timer = 0;
  private int collect = 1;
  private int bottle = 0;
  private int spray = 0;
  private int currentExp = 0;
  public TileEntityXpPylon() {
    super(2);
  }
  @Override
  public void update() {
    if (this.collect == 1) {
      updateCollection();
    }
    if (this.bottle == 1) {
      updateBottle();
    }
    if (this.spray == 1) {
      updateSpew();
    }
  }
  private void updateSpew() {
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      int amtToSpew = Math.min(XP_PER_SPEWORB, this.currentExp); //to catch 1 or 2 remainder left
      if (amtToSpew > 0 && tryDecrExp(amtToSpew)) {
        if (this.getWorld().isRemote == false) {
          EntityXPOrb orb = new EntityXPOrb(this.getWorld());
          orb.setPositionAndUpdate(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
          orb.xpValue = amtToSpew;
          this.getWorld().spawnEntity(orb);
          spewOrb(orb);
        }
      }
    }
  }
  private void updateBottle() {
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      if (outputSlotHasRoom() && inputSlotHasSome() && tryDecrExp(XP_PER_BOTTLE)) {
        outputSlotIncrement();
        inputSlotDecrement();
      }
    }
  }
  private void updateCollection() {
    List<EntityXPOrb> orbs = getWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(this.getPos().up()).expandXyz(RADIUS));
    if (orbs != null) {
      //no timer just EAT
      for (EntityXPOrb orb : orbs) {
        if (orb.isDead == false && this.tryIncrExp(orb.getXpValue())) {
          getWorld().removeEntity(orb);//calls     orb.setDead(); for me
        }
        else {//is full
          spewOrb(orb);
        }
      }
    }
  }
  private void outputSlotIncrement() {
    ItemStack fullOnes = this.getStackInSlot(SLOT_OUTPUT);
    if (UtilItemStack.isEmpty(fullOnes)) {
      fullOnes = new ItemStack(Items.EXPERIENCE_BOTTLE);
    }
    else {
      fullOnes.grow(1);
    }
    this.setInventorySlotContents(SLOT_OUTPUT, fullOnes);
  }
  private boolean outputSlotHasRoom() {
    ItemStack fullOnes = this.getStackInSlot(SLOT_OUTPUT);
    return UtilItemStack.isEmpty(fullOnes) || (fullOnes.getCount() < 64);
  }
  private boolean inputSlotHasSome() {
    ItemStack emptyOnes = this.getStackInSlot(SLOT_INPUT);
    return !UtilItemStack.isEmpty(emptyOnes) && (emptyOnes.getCount() > 0);
  }
  private void inputSlotDecrement() {
    ItemStack fullOnes = this.getStackInSlot(SLOT_INPUT);
    fullOnes.shrink(1);
    if (fullOnes.getCount() == 0) {
      fullOnes = ItemStack.EMPTY;
    }
    this.setInventorySlotContents(SLOT_INPUT, fullOnes);
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.DOWN) {
      return SLOTS_EXTRACT;
    }
    else {
      return SLOTS_INSERT;
    }
  }
  private boolean tryDecrExp(int xpValue) {
    if (this.currentExp - xpValue < 0) { return false; }
    this.currentExp -= xpValue;
    return true;
  }
  private boolean tryIncrExp(int xpValue) {
    if (this.currentExp + xpValue > MAX_EXP_HELD) { return false; }
    this.currentExp += xpValue;
    return true;
  }
  private void spewOrb(EntityXPOrb orb) {
    orb.addVelocity(Math.random() / 1000, 0.01, Math.random() / 1000);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_COLLECT, this.collect);
    tags.setInteger(NBT_EXP, this.currentExp);
    tags.setInteger(NBT_SPRAY, this.spray);
    tags.setInteger(NBT_BOTTLE, this.bottle);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    collect = tags.getInteger(NBT_COLLECT);
    currentExp = tags.getInteger(NBT_EXP);
    spray = tags.getInteger(NBT_SPRAY);
    bottle = tags.getInteger(NBT_BOTTLE);
  }
  private void setExp(int value) {
    currentExp = value;
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case EXP:
        return this.currentExp;
      case BOTTLE:
        return bottle;
      case COLLECT:
        return collect;
      case SPRAY:
        return spray;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case EXP:
        this.setExp(value);
      break;
      case BOTTLE:
        this.bottle = value % 2;
      break;
      case COLLECT:
        this.collect = value % 2;
      break;
      case SPRAY:
        this.spray = value % 2;
      break;
    }
  }
}
