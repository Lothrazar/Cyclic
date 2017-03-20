package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityXpPylon extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  private static final int XP_PER_SPEWORB = 10;
  private static final int XP_PER_BOTTLE = 11; // On impact with any non-liquid block it will drop experience orbs worth 3–11 experience points. 
  public static final int TIMER_FULL = 18;
  public static final int MAX_EXP_HELD = 1000;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private static final String NBT_EXP = "particles";
  private static final String NBT_MODE = "pushpull";
  private final static int RANGE = 2;
  private static final int[] SLOTS_EXTRACT = new int[] { SLOT_OUTPUT };
  private static final int[] SLOTS_INSERT = new int[] { SLOT_INPUT };
  public static enum Fields {
    TIMER, EXP, MODE, REDSTONE;//MIGHT remove redstone eh
  }
  public static enum Mode {
    COLLECT, SPEW;
  }
  private int timer = 0;
  private int needsRedstone = 1;
  private int mode = 0;//else pull. 0 as default
  private int currentExp = 0;// 0 as default
//  private ItemStack[] inv;
  public TileEntityXpPylon() {
    super(2);
  }
  @Override
  public void update() {
    if (this.mode == Mode.COLLECT.ordinal()) {
      updateCollection();
      updateBottle();
    }
    else if (this.mode == Mode.SPEW.ordinal()) {
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
    List<EntityXPOrb> orbs = getWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(this.getPos().up()).expandXyz(RANGE));
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
    tags.setInteger(NBT_REDST, this.needsRedstone);
    tags.setInteger(NBT_EXP, this.currentExp);
    tags.setInteger(NBT_MODE, this.mode);
//    NBTTagList itemList = new NBTTagList();
//    for (int i = 0; i < inv.length; i++) {
//      ItemStack stack = inv[i];
//      if (stack != null) {
//        NBTTagCompound tag = new NBTTagCompound();
//        tag.setByte(NBT_SLOT, (byte) i);
//        stack.writeToNBT(tag);
//        itemList.appendTag(tag);
//      }
//    }
//    tags.setTag(NBT_INV, itemList);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    needsRedstone = tags.getInteger(NBT_REDST);
    currentExp = tags.getInteger(NBT_EXP);
    mode = tags.getInteger(NBT_MODE);
//    NBTTagList tagList = tags.getTagList(NBT_INV, 10);
//    for (int i = 0; i < tagList.tagCount(); i++) {
//      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
//      byte slot = tag.getByte(NBT_SLOT);
//      if (slot >= 0 && slot < inv.length) {
//        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
//      }
//    }
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    this.setField(Fields.REDSTONE.ordinal(), val % 2);
  }
  private void setExp(int value) {
    currentExp = value;
  }
  private void setMode(int value) {
    if (value >= Mode.values().length) {
      value = 0;
    }
    mode = value;
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
          return this.mode;
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
      }
    }
  }
//  //COPY PASTA invo stuff. this got refactored in 1.11
//  @Override
//  public int getSizeInventory() {
//    return inv.length;
//  }
//  @Override
//  public ItemStack getStackInSlot(int index) {
//    return inv[index];
//  }
//  @Override
//  public ItemStack decrStackSize(int index, int count) {
//    ItemStack stack = getStackInSlot(index);
//    if (stack != null) {
//      if (stack.stackSize <= count) {
//        setInventorySlotContents(index, null);
//      }
//      else {
//        stack = stack.splitStack(count);
//        if (stack.stackSize == 0) {
//          setInventorySlotContents(index, null);
//        }
//      }
//    }
//    return stack;
//  }
//  @Override
//  public ItemStack removeStackFromSlot(int index) {
//    ItemStack stack = getStackInSlot(index);
//    if (stack != null) {
//      setInventorySlotContents(index, null);
//    }
//    return stack;
//  }
//  @Override
//  public void setInventorySlotContents(int index, ItemStack stack) {
//    inv[index] = stack;
//    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
//      stack.stackSize = getInventoryStackLimit();
//    }
//  }
}
