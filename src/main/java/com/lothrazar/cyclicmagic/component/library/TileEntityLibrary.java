package com.lothrazar.cyclicmagic.component.library;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityLibrary extends TileEntityBaseMachine {
  EnchantStack[] storage = new EnchantStack[QuadrantEnum.values().length];
  public TileEntityLibrary() {
    super();
    for (int i = 0; i < storage.length; i++) {
      storage[i] = new EnchantStack();
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
    return super.writeToNBT(tags);
  }
}
