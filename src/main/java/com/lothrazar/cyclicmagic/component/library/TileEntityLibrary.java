package com.lothrazar.cyclicmagic.component.library;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityLibrary extends TileEntityBaseMachine implements ITickable {
  private static final int HEADER_TIMER = 10;
  private static final String NBT_CLICKED = "lastClicked";
  EnchantStack[] storage = new EnchantStack[QuadrantEnum.values().length];
  QuadrantEnum lastClicked = null;
  private int timer = 0;
  public TileEntityLibrary() {
    super();
    for (int i = 0; i < storage.length; i++) {
      storage[i] = new EnchantStack();
    }
  }
  @Override
  public void update() {
    if (this.timer > 0) {
      this.timer--;
    }
    if (this.timer == 0) {
      this.lastClicked = null;
    }
  }
  public EnchantStack getEnchantStack(QuadrantEnum area) {
    //EnchantStack
    return storage[area.ordinal()];
  }
  public void removeEnchantment(QuadrantEnum area) {
    storage[area.ordinal()].remove();
  }
  public boolean addEnchantment(QuadrantEnum area, Enchantment ench, int level) {
    int index = area.ordinal();
    EnchantStack enchStackCurrent = storage[index];
    if (enchStackCurrent.isEmpty()) {
      enchStackCurrent = new EnchantStack(ench, level);
      storage[index] = enchStackCurrent;
      return true;
    }
    else if (enchStackCurrent.doesMatch(ench, level)) {
      enchStackCurrent.add();
      storage[index] = enchStackCurrent;
      return true;
    }
    else {
      return false;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.timer = tags.getInteger("t");
    if (tags.hasKey(NBT_CLICKED))
      this.lastClicked = QuadrantEnum.values()[tags.getInteger(NBT_CLICKED)];
    for (QuadrantEnum q : QuadrantEnum.values()) {
      EnchantStack s = new EnchantStack();
      s.readFromNBT(tags, q.name());
      storage[q.ordinal()] = s;
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    for (QuadrantEnum q : QuadrantEnum.values()) {
      tags.setTag(q.name(), getEnchantStack(q).writeToNBT());
    }
    if (lastClicked != null) {
      tags.setInteger(NBT_CLICKED, lastClicked.ordinal());
    }
    tags.setInteger("t", timer);
    return super.writeToNBT(tags);
  }
  public void setLastClicked(QuadrantEnum segment) {
    this.timer = HEADER_TIMER * Const.TICKS_PER_SEC;
    this.lastClicked = segment;
  }
  public QuadrantEnum getLastClicked() {
    return this.lastClicked;
  }
}
