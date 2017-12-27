package com.lothrazar.cyclicmagic.component.library;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLibrary extends TileEntity {
  private EnchantStack[] storage = new EnchantStack[BlockLibrary.Quadrant.values().length];
  public TileEntityLibrary() {
    super();
    for (int i = 0; i < storage.length; i++) {
      storage[i] = new EnchantStack();
    }
  }
  public boolean addEnchantment(BlockLibrary.Quadrant area, Enchantment ench, int level) {
    if(ench.getMaxLevel() != level){
      return false;
    }
    int index = area.ordinal();
    EnchantStack enchStackCurrent = storage[index];
    if (enchStackCurrent.count == 0) {
      enchStackCurrent = new EnchantStack(ench, level);
      return true;
    }
    else if (enchStackCurrent.doesMatch(ench, level)) {
      enchStackCurrent.add();
      return true;
    }
    else {
      return false;
    }
  }
  /**
   * One enchantment instance is an enchant combined with its level and we have a number of those
   * 
   * @author Sam
   *
   */
  public static class EnchantStack {
    private int count = 0;
    private Enchantment ench = null;
    private int level = 0;
    public EnchantStack() {}
    public EnchantStack(Enchantment e, int lvl) {
      ench = e;
      level = lvl;
      count = 1;
    }
    public boolean doesMatch(Enchantment e, int lvl) {
      return ench.equals(e) && level == lvl;
    }
    public void add() {
      count++;
    }
    public void remove() {
      count--;
      if (count <= 0) {
        ench = null;
        level = 0;
      }
    }
  }
}
